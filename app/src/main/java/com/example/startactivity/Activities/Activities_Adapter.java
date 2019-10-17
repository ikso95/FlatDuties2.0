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

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public class Activities_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<ListItem> listItems ;
    private Context context;


    public Activities_Adapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    public ListItem getItem(int position){
        return listItems.get(position);
    }



    //w zaleznosci od danych zwraca liczbe odpowiednia dla kazdego layoutu
    @Override
    public int getItemViewType(int position) {



        if(listItems.get(position).getDuty_description()!=null)
        {
            return 1;
        }
        else if(listItems.get(position).getMonday()==0 && listItems.get(position).getTuesday()==0 &&
                listItems.get(position).getWednesday()==0 && listItems.get(position).getThursday()==0 &&
                listItems.get(position).getFriday()==0 && listItems.get(position).getSaturday()==0 &&
                listItems.get(position).getSunday()==0 )
        {
            return 3;
         }
        else
        {
            return 2;
        }
    }

    //w zaleznosci jaki layout powinien byc taki wlasnie tworzymy
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =null;
        RecyclerView.ViewHolder viewHolder = null;

        if(viewType==1)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.duty_item4,parent,false);
            viewHolder = new ViewHolder4(view);
        }
        else if (viewType==2)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.duty_item2,parent,false);
            viewHolder= new ViewHolder2(view);
        }
        else if(viewType==3)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.duty_item3,parent,false);
            viewHolder= new ViewHolder3(view);
        }

        return viewHolder;
    }



    //wypelniamy wszystkie dane
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder.getItemViewType()==1)
        {
            ViewHolder4 vaultItemHolder = (ViewHolder4) holder;
            ((ViewHolder4) holder).textViewNazwa.setText(listItems.get(position).getDuty_name());
            ((ViewHolder4) holder).textViewDescription.setText(listItems.get(position).getDuty_description());


            ((ViewHolder4) holder).button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //new DeleteDuty().doInBackground(listItems.get(position).getDutyId());
                    listItems.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,listItems.size());
                }
            });


        }
        else if (holder.getItemViewType() == 2){

            ViewHolder2 vaultItemHolder = (ViewHolder2) holder;
            ((ViewHolder2) holder).textViewNazwa.setText(listItems.get(position).getDuty_name());


            ((ViewHolder2) holder).button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //new DeleteDuty().doInBackground(listItems.get(position).getDutyId());
                    listItems.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,listItems.size());
                }
            });

        }
        else if (holder.getItemViewType() == 3)
        {
            ViewHolder3 vaultItemHolder = (ViewHolder3) holder;
            ((ViewHolder3) holder).textViewNazwa.setText(listItems.get(position).getDuty_name());
            ((ViewHolder3) holder).button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DeleteDuty().doInBackground(listItems.get(position).getDutyId());
                    listItems.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,listItems.size());
                }
            });
        }

    }




    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder{

        public TextView textViewNazwa;
        public TextView textViewDescription;
        public TextView textViewDay;
        public ImageButton button;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);

            textViewNazwa = (TextView)itemView.findViewById(R.id.duty_name_textView_duty_item);
            textViewDescription = (TextView)itemView.findViewById(R.id.duty_desc_textView_duty_item);
            textViewDay = (TextView)itemView.findViewById(R.id.day_textView_duty_item);
            button = (ImageButton)itemView.findViewById(R.id.ok_button_duty_item);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder{

        public TextView textViewNazwa;
        public ImageButton button;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);

            textViewNazwa = (TextView)itemView.findViewById(R.id.duty_name_textView_duty_item2);
            button = (ImageButton)itemView.findViewById(R.id.ok_button_duty_item2);
        }
    }

    public class ViewHolder3 extends RecyclerView.ViewHolder{

        public TextView textViewNazwa;
        public ImageButton button;

        public ViewHolder3(@NonNull View itemView) {
            super(itemView);

            textViewNazwa = (TextView)itemView.findViewById(R.id.duty_name_textView_duty_item3);
            button = (ImageButton)itemView.findViewById(R.id.ok_button_duty_item3);
        }
    }

    public class ViewHolder4 extends RecyclerView.ViewHolder{

        public TextView textViewNazwa;
        public ImageButton button;
        public TextView textViewDescription;


        public ViewHolder4(@NonNull View itemView) {
            super(itemView);

            textViewNazwa = (TextView)itemView.findViewById(R.id.duty_name_textView_duty_item4);
            button = (ImageButton)itemView.findViewById(R.id.ok_button_duty_item4);
            textViewDescription = (TextView)itemView.findViewById(R.id.duty_desc_textView_duty_item4);
        }
    }






    public class DeleteDuty extends AsyncTask<Integer,Void,Void>
    {
        @Override
        protected Void doInBackground(Integer... integers) {
            postActivityDone(integers[0]);
            return null;
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



}
