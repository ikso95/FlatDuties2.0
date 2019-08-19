package com.example.startactivity.Add_or_join_group;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.startactivity.R;

public class First_use_add_or_join_group extends AppCompatActivity {

    private Button join;
    private Button create;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_use_add_or_join_group);

        join = (Button)findViewById(R.id.join_group_button_first);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(First_use_add_or_join_group.this, Join_group.class);
                startActivity(intent);
            }
        });

        create = (Button)findViewById(R.id.create_group_button_first);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(First_use_add_or_join_group.this, Create_new_group.class);
                startActivity(intent);
            }
        });



    }
}
