package com.example.jack.awesomebing.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jack.awesomebing.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View favView = inflater.inflate(R.layout.fragment_fav, container, false);
        return favView;
    }

}
