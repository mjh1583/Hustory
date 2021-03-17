package com.kang.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class QuestionAdapter extends BaseAdapter {
    private ArrayList<QuestionItem> listViewItemList = new ArrayList<QuestionItem>();
    public QuestionAdapter() {}

    @Override
    public int getCount() { return listViewItemList.size(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.question_item, parent, false);
        }

        TextView question_content = (TextView) convertView.findViewById(R.id.question_content);
        TextView question_time = (TextView) convertView.findViewById(R.id.question_time);
        TextView answer_count = (TextView) convertView.findViewById(R.id.answer_count);

        QuestionItem listViewItem = listViewItemList.get(position);

        question_content.setText(listViewItem.getQuestionStr());
        question_time.setText(listViewItem.getTimeStr());
        answer_count.setText(listViewItem.getCountStr());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    public void addItem(String questionStr, String timeStr, String countStr) {
        QuestionItem item = new QuestionItem();

        item.setQuestionStr(questionStr);
        item.setTimeStr(timeStr);
        item.setCountStr(countStr);

        listViewItemList.add(item);
    }

    public void deleteItem(int position) { listViewItemList.remove(position); }
}