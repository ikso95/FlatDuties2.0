package com.example.startactivity.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.startactivity.Common.Common;
import com.example.startactivity.Common.VolleySingleton;
import com.example.startactivity.R;

import org.json.JSONObject;

import java.util.List;

public class Activities_Adapter extends RecyclerView.Adapter<Activities_Adapter.ViewHolder> {


    private List<ListItem> listItems ;
    private Context context;


    public Activities_Adapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    public ListItem getItem(int position){
        return listItems.get(position);
    }


    //called when viewHolder is created
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {

            View  v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.duty_item,viewGroup,false);
            return  new ViewHolder(v);

    }

    //called after onCreateViewHolder, bind data to viewHolder
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final ListItem listItem = listItems.get(i);

        viewHolder.textViewNazwa.setText(listItem.getDuty_name());
        viewHolder.textViewDescription.setText(listItem.getDuty_description());


        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteDuty().doInBackground(listItem.getDutyId());
                //postActivityDone(listItem.getDutyId());
                listItems.remove(i);
                notifyItemRemoved(i);           //informuje o usunietym elemencie
                notifyItemRangeChanged(i, listItems.size());

            }
        });

    }



    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewNazwa;
        public TextView textViewDescription;
        public ImageButton button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNazwa = (TextView)itemView.findViewById(R.id.duty_name_textView_duty_item);
            textViewDescription = (TextView)itemView.findViewById(R.id.duty_desc_textView_duty_item);

            //textViewDate = (TextView)itemView.findViewById(R.id.day_textView_duty_item);

            button = (ImageButton)itemView.findViewById(R.id.ok_button_duty_item);
        }
    }



    private void postActivityDone(final int id)
    {
        final ProgressDialog mDialog = new ProgressDialog(context);
        mDialog.setMessage("Please wait...");
        mDialog.show();

        String url = Common.getUrl()+"deleteDuty/"+id;

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


    public class DeleteDuty extends AsyncTask<Integer,Void,Void>
    {
        @Override
        protected Void doInBackground(Integer... integers) {
            postActivityDone(integers[0]);
            return null;
        }
    }



}