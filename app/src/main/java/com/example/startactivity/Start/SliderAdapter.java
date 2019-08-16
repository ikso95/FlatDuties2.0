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

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context ctx){
        this.context = ctx;
    }

    public int[] slide_images={
            R.drawable.ic_mood,
            R.drawable.ic_local_laundry_service,
            R.drawable.ic_local_grocery_store
    };

    public String[] slide_heading={
            "Welcome!",
            "Share duties",
            "Make shopping lists"
    };

    public String[] slide_descs={
            "We are happy that you have downloaded our app. You are only a few steps away from using it!",
            "The application allows you to manage your household duties.",
            "Create shopping lists with your flat mates."
    };



    @Override
    public int getCount() {
        return slide_heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
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
