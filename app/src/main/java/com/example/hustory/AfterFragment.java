package com.example.hustory;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.fragment.app.ListFragment;

import java.util.ArrayList;

public class AfterFragment extends ListFragment {
    private ArrayList<AfterData> datas;
    private ArrayAdapter adapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        datas = new ArrayList<AfterData>();

        datas.add(new AfterData("홍길동", "02.18(목)", "온라인", "ZOOM", "연봉협상에대하여"));
        adapter = new AfterDataAdapter(getActivity(), R.layout.activity_after_data, datas);
        setListAdapter(adapter);
    }
}
