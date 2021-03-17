package com.example.hustory.question;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hustory.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        TextView question_name = (TextView) convertView.findViewById(R.id.question_name);
        TextView question_time = (TextView) convertView.findViewById(R.id.question_time);
        TextView answer_count = (TextView) convertView.findViewById(R.id.answer_count);

        QuestionItem listViewItem = listViewItemList.get(position);

        question_name.setText(listViewItem.getQ_title());
        question_content.setText(listViewItem.getQ_content());
        question_time.setText(listViewItem.getQ_time());
        answer_count.setText(String.valueOf(listViewItem.getQ_count()));

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

    public void addItem(String questionStr, String nameStr, String timeStr, int countStr) {
        QuestionItem item = new QuestionItem(nameStr, questionStr, timeStr, countStr);

        item.setQ_title(nameStr);
        item.setQ_content(questionStr);
        item.setQ_time(timeStr);
        item.setQ_count(countStr);

        listViewItemList.add(item);
    }

    public void deleteItem(int position) { listViewItemList.remove(position); }
}