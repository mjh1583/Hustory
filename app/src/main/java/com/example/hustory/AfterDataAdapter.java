package com.example.hustory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AfterDataAdapter extends ArrayAdapter<AfterData> {
    // Member Variable
    private Context context;
    private int layoutResId;
    private ArrayList<AfterData> dataList;

    // Constructor Method
    public AfterDataAdapter(@NonNull Context context, int resource, @NonNull ArrayList<AfterData> objects) {
        super(context, resource, objects);

        this.context = context;
        this.layoutResId = resource;
        this.dataList = objects;

    }

    // Override Method

    @Override
    public int getCount() {
        return dataList.size();
    }


    // Nullable -> null 일 수도 있다. NonNull -> 절때 NULL이면 안된다.
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Data => XML Layout 넣어서 보이고 사용할 수 있도록 객체 생성
        // (1) item Layout xml => java 객체 변환
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResId, null);

            AfterDataHolder holder = new AfterDataHolder(convertView);
            convertView.setTag(holder);
        }

//        // (2) item layout's view 객체 가져오기 => ItemDataHolder 클래스에서 진행
//
//        TextView nameTXT = convertView.findViewById(R.id.nameTXT);
//        TextView phoneTXT = convertView.findViewById(R.id.phoneTXT);
//        TextView addrTXT = convertView.findViewById(R.id.addrTXT);
//        ImageView iconIMG = convertView.findViewById(R.id.iconIMG);

        AfterDataHolder holder = (AfterDataHolder) convertView.getTag();

        ImageView iconIMG = holder.iconIMG;
        TextView profesorName = holder.profesorName;
        TextView contentsTXT = holder.contentsTXT;
        TextView beforeDateTXT = holder.beforeDateTXT;
        TextView beforeTXT = holder.beforeTXT;
        TextView beforePlaceTXT = holder.beforePlaceTXT;




        // (3) Data 준비
        AfterData item = dataList.get(position);

        // ($) Layout <--> Data
        profesorName.setText(item.getProfesor());
        contentsTXT.setText(item.getContents());
        beforeDateTXT.setText(item.getDate());
        beforeTXT.setText(item.getOnOff());
        beforePlaceTXT.setText(item.getPlace());
        iconIMG.setImageResource(R.drawable.avatar);


        // Image 크기 동일하게
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.avatar);
        bitmap = bitmap.createScaledBitmap(bitmap, 100, 100, true);
        iconIMG.setImageBitmap(bitmap);


        return convertView;
    }
}