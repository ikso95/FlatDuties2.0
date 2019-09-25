package com.example.startactivity.Shopping;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.startactivity.Activities.Activity_fragment;
import com.example.startactivity.R;

import java.util.List;


public class Shopping_fragment extends Fragment {

    private FloatingActionButton plus_fab;
    private FloatingActionButton send_fab;
    private EditText item_name_input;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private List shoppingListItems;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.shopping_fragment,container,false);

        plus_fab= (FloatingActionButton)view.findViewById(R.id.new_item_floating_action_button_shopping_fragment);
        send_fab = (FloatingActionButton)view.findViewById(R.id.send_new_shopping_item_button_shopping_fragment);
        item_name_input = (EditText)view.findViewById(R.id.shopping_item_input_editText_shopping_fragment);

        recyclerView = (RecyclerView)view.findViewById(R.id.shopping_recyclerView_shopping_fragment);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy > 5){
                    if(send_fab.getVisibility()==View.VISIBLE)
                    {
                        plus_fab.hide();
                        plus_fab.animate().rotationBy(135.0f).setDuration(500);
                        plus_fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary,null)));
                        plus_fab.animate().translationX(0);
                        send_fab.hide();
                        item_name_input.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        plus_fab.hide();
                    }

                } else if(dy<-5){
                    plus_fab.show();
                }


                //if cant scroll down, list reaches top show our dab
                if(!recyclerView.canScrollVertically(-1))
                {
                    plus_fab.show();
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });


        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_shopping_fragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });


        send_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item_name_input.getText().toString().trim().length()==0)
                    item_name_input.setError("This field cannot be empty");
                else
                {
                    //new Activity_fragment.AddDuty().doInBackground();

                    item_name_input.setText("");
                    item_name_input.setVisibility(View.INVISIBLE);
                    send_fab.hide();


                    plus_fab.animate().rotationBy(135.0f).setDuration(500);
                    plus_fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary,null)));
                    plus_fab.animate().translationX(0);
                }

            }
        });

        plus_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item_name_input.isShown()==false)
                {
                    item_name_input.setVisibility(View.VISIBLE);
                    item_name_input.requestFocus();
                    send_fab.show();

                    plus_fab.animate().rotationBy(-135.0f).setDuration(500);
                    plus_fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent,null)));
                    plus_fab.animate().translationX(-170);

                }
                else {
                    item_name_input.setVisibility(View.INVISIBLE);
                    item_name_input.setText("");
                    item_name_input.setError(null);
                    send_fab.hide();

                    plus_fab.animate().rotationBy(135.0f).setDuration(500);
                    plus_fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary,null)));
                    plus_fab.animate().translationX(0);

                }
            }
        });



        return view;
    }
}
