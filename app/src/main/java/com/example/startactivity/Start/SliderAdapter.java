package com.example.startactivity.Start;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.startactivity.R;

import org.w3c.dom.Text;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    public SliderAdapter(Context ctx){
        this.context = ctx;
    }

    public int[] slide_images={
            R.drawable.person,
            R.drawable.group2,
            R.drawable.broom,
            R.drawable.cart
    };





    @Override
    public int getCount() {
        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        String[] slide_heading={
                context.getString(R.string.slide_heading_1),
                context.getString(R.string.slide_heading_2),
                context.getString(R.string.slide_heading_3),
                context.getString(R.string.slide_heading_4)
        };

        String[] slide_descs={
                context.getString(R.string.slide_description_1),
                context.getString(R.string.slide_description_2),
                context.getString(R.string.slide_description_3),
                context.getString(R.string.slide_description_4)
        };

        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideImageView = (ImageView)view.findViewById(R.id.imageView2);
        TextView slideHeading = (TextView)view.findViewById(R.id.textViewSlider);
        TextView slideDescription = (TextView)view.findViewById(R.id.textViewSlider2);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_heading[position]);
        slideDescription.setText(slide_descs[position]);


        container.addView(view);

        return  view ;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout)object);

    }
}
