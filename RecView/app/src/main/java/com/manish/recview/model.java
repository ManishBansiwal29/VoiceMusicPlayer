package com.manish.recview;

import android.widget.ImageView;

public class model {
    ImageView imageView;

    public model(ImageView imageView){
        this.imageView=imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
