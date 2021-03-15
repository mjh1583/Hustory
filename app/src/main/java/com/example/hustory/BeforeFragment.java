package com.example.hustory;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import java.util.ArrayList;

public class BeforeFragment extends ListFragment {
    private ArrayList<BeforeData> datas;
    private BeforeDataAdapter adapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        datas = new ArrayList<BeforeData>();

        datas.add(new BeforeData("홍길동", "02.18(목)", "온라인", "ZOOM", "수락대기", "연봉협상에대하여"));
        adapter = new BeforeDataAdapter(getActivity(), R.layout.activity_before_data, datas);
        setListAdapter(adapter);
    }
}
