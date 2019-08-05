package com.example.startactivity.Start;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.startactivity.R;

import java.net.ConnectException;

public class ImageAdapter extends PagerAdapter {

    private Context mContext;
    //deklaracja obrazów do galerii
    private  int[] mImageIds = new int[] {R.drawable.ic_launcher_background,R.drawable.ic_launcher_foreground};

    ImageAdapter(Context context){
        mContext=context;
    }

    @Override
    public int getCount() {
        return mImageIds.length;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(mImageIds[position]);
        container.addView(imageView,0);
        return imageView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
