package com.example.startactivity.Shopping;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.startactivity.Activities.Activities_Adapter;
import com.example.startactivity.Common.Common;
import com.example.startactivity.Common.VolleySingleton;
import com.example.startactivity.R;

import org.json.JSONObject;

import java.util.List;

public class Shopping_Adapter extends RecyclerView.Adapter<Shopping_Adapter.ViewHolder> {


    private List<ShoppingItem> listItems ;
    private Context context;


    public Shopping_Adapter(List<ShoppingItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    public ShoppingItem getItem(int position){
        return listItems.get(position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_item,parent,false);

        return new ViewHolder(view);
    }



    //wypelniamy wszystkie dane
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        final ShoppingItem shoppingItem = listItems.get(position);

        holder.textViewNazwa.setText(shoppingItem.getItem_name());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Shopping_Adapter.DeleteShoppingItem().doInBackground(listItems.get(position).getShoppingID());
                listItems.remove(position);    //i-1?  !!!!!!!!!!!!!!!
                notifyItemRemoved(position);           //informuje o usunietym elemencie !!! WAZNE
            }
        });

    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewNazwa;
        public ImageButton button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNazwa = (TextView)itemView.findViewById(R.id.duty_name_textView_shopping_item);
            button = (ImageButton)itemView.findViewById(R.id.ok_button_shopping_item);
        }
    }





    private void deleteShoppingItem(final int id)
    {
        final ProgressDialog mDialog = new ProgressDialog(context);
        mDialog.setMessage("Please wait...");
        mDialog.show();

        String url = Common.getUrl()+"deleteShoppingItem/"+id;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mDialog.dismiss();
                        error.printStackTrace();
                        Log.d("Error", error.toString());
                    }

                });
        RequestQueue queue =  VolleySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
        queue.add(jsonRequest);
    }


    public class DeleteShoppingItem extends AsyncTask<Integer,Void,Void>
    {
        @Override
        protected Void doInBackground(Integer... integers) {
            deleteShoppingItem(integers[0]);
            return null;
        }
    }



}
