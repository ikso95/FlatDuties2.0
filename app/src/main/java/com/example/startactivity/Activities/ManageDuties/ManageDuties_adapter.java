package com.example.startactivity.Activities.ManageDuties;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.startactivity.Activities.ListItem;
import com.example.startactivity.Activities.NewCyclicalDuty;
import com.example.startactivity.Common.Common;
import com.example.startactivity.Common.VolleySingleton;
import com.example.startactivity.R;
import com.example.startactivity.Shopping.ShoppingItem;
import com.example.startactivity.Shopping.Shopping_Adapter;
import com.sun.mail.imap.IMAPBodyPart;

import org.json.JSONObject;

import java.util.List;

public class ManageDuties_adapter extends RecyclerView.Adapter<ManageDuties_adapter.ViewHolder> {
    private List<ListItem> listItems ;
    private Context context;


    public ManageDuties_adapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    public ListItem getItem(int position){
        return listItems.get(position);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.manage_duties_item,viewGroup,false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        final ListItem listItem = listItems.get(i);

        viewHolder.textViewNazwa.setText(listItem.getDuty_name());

        viewHolder.deleteDutyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ManageDuties_adapter.DeleteCyclicalDuty().doInBackground(listItems.get(i).getDutyId());
                listItems.remove(i);    //i-1?  !!!!!!!!!!!!!!!
                notifyItemRemoved(i);           //informuje o usunietym elemencie !!! WAZNE
            }
        });

        viewHolder.editDutyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edycja cyklicznego duty
                Intent intent = new Intent(context, EditCyclicalDuty.class);

                intent.putExtra("dutyID",       listItem.getDutyId());
                intent.putExtra("dutyName",     listItem.getDuty_name());
                intent.putExtra("dutyDesc",     listItem.getDuty_description());
                intent.putExtra("dutyForUserID",listItem.getForUserID());
                intent.putExtra("dutyMonday",   listItem.getMonday());
                intent.putExtra("dutyTuesday",  listItem.getTuesday());
                intent.putExtra("dutyWednesday",listItem.getWednesday());
                intent.putExtra("dutyThursday", listItem.getThursday());
                intent.putExtra("dutyFriday",   listItem.getFriday());
                intent.putExtra("dutySaturday", listItem.getSaturday());
                intent.putExtra("dutySunday",   listItem.getSunday());

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewNazwa;
        public ImageButton deleteDutyButton;
        public ImageButton editDutyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNazwa = (TextView)itemView.findViewById(R.id.duty_name_textView_manage_duties);
            deleteDutyButton = (ImageButton)itemView.findViewById(R.id.cancel_button_manage_duties);
            editDutyButton = (ImageButton)itemView.findViewById(R.id.edit_button_manage_duties);
        }
    }

    private void deleteCyclicalDuty(final int id)
    {
        String url = Common.getUrl()+"deleteDuty/"+id;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        Log.d("Error", error.toString());
                    }

                });
        RequestQueue queue =  VolleySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
        queue.add(jsonRequest);
    }


    public class DeleteCyclicalDuty extends AsyncTask<Integer,Void,Void>
    {
        @Override
        protected Void doInBackground(Integer... integers) {
            deleteCyclicalDuty(integers[0]);
            return null;
        }
    }




}
