package com.example.startactivity.Activities;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;


import com.example.startactivity.Models.User;
import com.example.startactivity.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by SQ-OGBE PC on 06/08/2017.
 */

public class Adapter_user extends RecyclerView.Adapter<Adapter_user.ViewHolder> {

    private List<User> users = new ArrayList<>();
    SparseBooleanArray itemStateArray= new SparseBooleanArray();
    Adapter_user() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForItem = R.layout.user_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForItem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (users == null) {
            return 0;
        }
        return users.size();
    }

    void loadItems(List<User> users) {
        this.users=users;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CheckedTextView mCheckedTextView;

        ViewHolder(View itemView) {
            super(itemView);
            mCheckedTextView = (CheckedTextView) itemView.findViewById(R.id.user_checkedTextView);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            // use the sparse boolean array to check
            if (!itemStateArray.get(position, false)) {
                mCheckedTextView.setChecked(false);}
            else {
                mCheckedTextView.setChecked(true);
            }
            mCheckedTextView.setText(users.get(position).getNick());

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            if (!itemStateArray.get(adapterPosition, false)) {
                users.get(adapterPosition).setChecked(true);//
                mCheckedTextView.setChecked(true);
                itemStateArray.put(adapterPosition, true);
            }
            else  {
                users.get(adapterPosition).setChecked(true);//
                mCheckedTextView.setChecked(false);
                itemStateArray.put(adapterPosition, false);
            }
        }

    }
}