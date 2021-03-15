package com.example.hustory;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hustory.R;

public class BeforeDataHolder {
    public ImageView iconIMG;
    public TextView profesorName;
    public TextView contentsTXT;
    public TextView beforeDateTXT;
    public TextView beforeTXT;
    public TextView beforePlaceTXT;
    public Button acceptBTN;


    public BeforeDataHolder(View root) {
        this.iconIMG = root.findViewById(R.id.iconIMG);
        this.profesorName = root.findViewById(R.id.profesorName);
        this.contentsTXT = root.findViewById(R.id.contentsTXT);
        this.beforeDateTXT = root.findViewById(R.id.beforeDateTXT);
        this.beforeTXT = root.findViewById(R.id.beforeTXT);
        this.beforePlaceTXT = root.findViewById(R.id.beforePlaceTXT);
        this.acceptBTN = root.findViewById(R.id.acceptBTN);

    }
}
