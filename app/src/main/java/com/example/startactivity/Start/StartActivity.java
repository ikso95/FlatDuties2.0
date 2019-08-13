package com.example.startactivity.Start;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.startactivity.Common.Common;
import com.example.startactivity.Main.MainActivity;
import com.example.startactivity.Models.User;
import com.example.startactivity.R;
import com.example.startactivity.Register_Login_activity;


public class StartActivity extends AppCompatActivity {

    private Button sign_in;
    private Button sign_up;
    private Button next;
    private ViewPager viewPager;

    private SharedPreferences preferences;
    private int uzytkownikID,grupaID;
    private String mail, nick;


    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;

    private TextView[] mDots;

    private Button nextButton;
    private Button backButton;

    private int mCurrentPage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        //isUserLoggedIn();


        mSlideViewPager = (ViewPager)findViewById(R.id.slideViewPager_start);
        mDotLayout = (LinearLayout) findViewById(R.id.dots_layout_start);

        sliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);



        nextButton = (Button)findViewById(R.id.next_button_start);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentPage==mDots.length-1)
                {
                    //go to registration and login activity
                    Intent intent = new Intent(StartActivity.this, Register_Login_activity.class);
                    startActivity(intent);

                }
                else
                {
                    mCurrentPage++;
                    mSlideViewPager.setCurrentItem(getItem(1));
                }
            }
        });

        backButton = (Button) findViewById(R.id.back_button_start);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(getItem(-1));
                mCurrentPage--;
            }
        });
    }



    @SuppressLint("ResourceAsColor")
    public void addDotsIndicator(int position){

        //liczba kropek/ekranów
        mDots = new TextView[3];
        mDotLayout.removeAllViews();


        for(int i=0; i<mDots.length; i++){

            mDots[i]=new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;")); //mozna zastapic "." ale trzeba odpowiednio powiekrzyc
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(R.color.colorTransparentWhite);

            mDotLayout.addView(mDots[i]);
        }

        if(mDots.length>0)
        {
            mDots[position].setTextColor(R.color.colorWhite);
            mDots[position].setTextSize(45);

        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            mCurrentPage = i;
            if(i==0)
            {
                nextButton.setEnabled(true);
                backButton.setEnabled(false);
                backButton.setVisibility(View.INVISIBLE);
                nextButton.setText(R.string.Next);
                backButton.setText("");

            }
            else if(i== mDots.length - 1) {
                nextButton.setEnabled(true);
                backButton.setEnabled(true);
                backButton.setVisibility(View.VISIBLE);
                nextButton.setText(R.string.finish);
                backButton.setText(R.string.back);
            }
            else
            {
                nextButton.setEnabled(true);
                backButton.setEnabled(true);
                backButton.setVisibility(View.VISIBLE);
                nextButton.setText(R.string.Next);
                backButton.setText(R.string.back);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private int getItem(int i) {
        return mSlideViewPager.getCurrentItem() + i;
    }


    private void isUserLoggedIn() {
        preferences = getSharedPreferences("Login", MODE_PRIVATE);
        uzytkownikID = preferences.getInt("UserID",0);
        mail=preferences.getString("Email","");
        nick=preferences.getString("Nick","");
        grupaID=preferences.getInt("GroupID",0);

        User u = new User(uzytkownikID,mail, nick, grupaID);
        Common.currentUser=u;

        if(uzytkownikID!=0) //jeżeli zostały odczytane poprawne dane, wartosc 0 jest domyślnie ustawiana, w bazie nie ma takiego rekordu
        {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(StartActivity.this, "Signed in", Toast.LENGTH_SHORT).show();
        }

    }
}
