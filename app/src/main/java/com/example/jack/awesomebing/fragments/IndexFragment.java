package com.example.jack.awesomebing.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jack.awesomebing.MainActivity;
import com.example.jack.awesomebing.R;
import com.example.jack.awesomebing.adapters.BingRvAdapter;
import com.example.jack.awesomebing.beanForGson.DailyInfo;
import com.example.jack.awesomebing.beanForGson.PageInfo;
import com.example.jack.awesomebing.beanForGson.Status;
import com.example.jack.awesomebing.retroService.BingService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndexFragment extends Fragment {
    private BingService service;
    private RecyclerView rv;
    private BingRvAdapter bingRvAdapter;
    private View indexView;
    private SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<DailyInfo> infos = new ArrayList<>();
    private int currentPage = 1;
    private boolean noMore = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrofit用的
        service = ((MainActivity) getActivity()).retrofit.create(BingService.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //这个判断用来解决onCreateView多次调用的问题
        if (indexView == null) {
            //如果indexView为null，说明是第一次调用，正常初始化就行
            //init view
            indexView = inflater.inflate(R.layout.fragment_index, container, false);
            swipeRefreshLayout = (SwipeRefreshLayout) indexView.findViewById(R.id.swipe);
            rv = (RecyclerView) indexView.findViewById(R.id.rv);
            rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            bingRvAdapter = new BingRvAdapter(getActivity(), infos);
            rv.setAdapter(bingRvAdapter);
            loadPage(currentPage++);
            //下拉刷新
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    currentPage = 1;
                    noMore = false;
                    infos.clear();
                    loadPage(currentPage++);
                }
            });
            //上拉加载
            rv.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                        int[] lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                        staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                        int lastVisibleItemPosition = getMax(lastPositions);
                        if (lastVisibleItemPosition + 1 >= staggeredGridLayoutManager.getItemCount()) {
                            loadPage(currentPage++);
                        }
                    }
                }

            });

        } else {
            //如果indexView不为null，说明不是第一次调用，直接返回indexView，方法体中的代码是解决（Java.lang.IllegalStateException: The specified child already has a parent）异常的
            ViewGroup parent = (ViewGroup) indexView.getParent();
            if (parent != null) {
                parent.removeView(indexView);
            }
        }

        return indexView;
    }

    //加载第几页，每页20个
    void loadPage(int p) {
        Call<PageInfo> call = service.getPage(p, 20);
        call.enqueue(new Callback<PageInfo>() {
            @Override
            public void onResponse(Call<PageInfo> call, Response<PageInfo> response) {
                PageInfo pageInfo = response.body();
                Status status = pageInfo.status;
                int code = status.code;
                if (code == 200) {
                    ArrayList<DailyInfo> dailyInfos = pageInfo.data;
                    infos.addAll(dailyInfos);
                    bingRvAdapter.notifyDataSetChanged();
                } else {
                    noMore = true;
                    Toast.makeText(getActivity(), "别再滑了，没有了", Toast.LENGTH_SHORT).show();
                    rv.clearOnScrollListeners();
                }


                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(Call<PageInfo> call, Throwable t) {
                Log.i("TAG", "onFailure=" + t.getMessage());
                Toast.makeText(getActivity(), "出错了", Toast.LENGTH_SHORT).show();
                currentPage--;
            }
        });


    }

    public static int getMax(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }
}
