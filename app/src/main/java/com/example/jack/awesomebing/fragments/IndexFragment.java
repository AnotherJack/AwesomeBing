package com.example.jack.awesomebing.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jack.awesomebing.MainActivity;
import com.example.jack.awesomebing.R;
import com.example.jack.awesomebing.adapters.BingRvAdapter;
import com.example.jack.awesomebing.beanForGson.DailyInfo;
import com.example.jack.awesomebing.beanForGson.PageInfo;
import com.example.jack.awesomebing.retroService.BingService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndexFragment extends Fragment {
    private BingService service;
    private RecyclerView rv;
    private BingRvAdapter bingRvAdapter;
    ArrayList<DailyInfo> infos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //retrofit用的
        service = ((MainActivity) getActivity()).retrofit.create(BingService.class);
        //init view
        View indexView = inflater.inflate(R.layout.fragment_index, container, false);
        rv = (RecyclerView) indexView.findViewById(R.id.rv);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        bingRvAdapter = new BingRvAdapter(getActivity(),infos);
        rv.setAdapter(bingRvAdapter);
        loadPage(1);

        return indexView;
    }

    //加载第几页，每页20个
    void loadPage(int p){
        Call<PageInfo> call = service.getPage(p,20);
        call.enqueue(new Callback<PageInfo>() {
            @Override
            public void onResponse(Call<PageInfo> call, Response<PageInfo> response) {
                PageInfo pageInfo = response.body();
                ArrayList<DailyInfo> dailyInfos = pageInfo.data;
                infos.addAll(dailyInfos);
                bingRvAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<PageInfo> call, Throwable t) {
                Toast.makeText(getActivity(),"出错了",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
