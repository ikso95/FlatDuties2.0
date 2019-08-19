package com.example.startactivity.Start;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
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
import com.example.startactivity.SignIn.SignInActivity;
import com.example.startactivity.SignUp.SignUpActivity;


public class StartActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private int uzytkownikID,grupaID;
    private String mail, nick, groupName;


    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;

    private TextView[] mDots;

    private Button nextButton;
    private Button backButton;
    private Button sign_in;
    private Button sign_up;


    private int mCurrentPage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        isUserLoggedIn();


        mSlideViewPager = (ViewPager)findViewById(R.id.slideViewPager_start);
        mDotLayout = (LinearLayout) findViewById(R.id.dots_layout_start);

        sliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);


        //automatyczne czasowe przesuwanie się slajdów co określony okres czasu
        //po przejsciu wszystkich okien wraca spowrotem do pierwszego slajdu
        final Handler refreshHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mCurrentPage++;
                if(mCurrentPage==4)
                {
                    mCurrentPage=0;
                    mSlideViewPager.setCurrentItem(getItem(-4));
                }
                else
                {
                    mSlideViewPager.setCurrentItem(getItem(1));
                }
                refreshHandler.postDelayed(this,  5000);
            }
        };
        refreshHandler.postDelayed(runnable, 5000);



        nextButton = (Button)findViewById(R.id.next_button_slide);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    mCurrentPage++;
                    mSlideViewPager.setCurrentItem(getItem(1));

            }
        });

        backButton = (Button) findViewById(R.id.back_button_slide);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(getItem(-1));
                mCurrentPage--;
            }
        });

        //przejście do ekranu logowania
        sign_in = (Button) findViewById(R.id.sign_in_button_start);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        //przejscie do ekranu rejestracji
        sign_up = (Button) findViewById(R.id.sign_up_button_start);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });



    }



    @SuppressLint("ResourceAsColor")
    public void addDotsIndicator(int position){

        //liczba kropek/ekranów
        mDots = new TextView[4];
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
                nextButton.setVisibility(View.VISIBLE);
            }
            else if(i== mDots.length - 1) {
                nextButton.setEnabled(true);
                backButton.setEnabled(true);
                nextButton.setVisibility(View.INVISIBLE);
            }
            else
            {
                nextButton.setEnabled(true);
                backButton.setEnabled(true);
                backButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
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
        groupName=preferences.getString("GroupName","");



        User u = new User(uzytkownikID,mail, nick, grupaID,groupName);
        Common.currentUser=u;

        if(uzytkownikID!=0) //jeżeli zostały odczytane poprawne dane, wartosc 0 jest domyślnie ustawiana, w bazie nie ma takiego rekordu
        {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(StartActivity.this, "Signed in", Toast.LENGTH_SHORT).show();
        }

    }
}
