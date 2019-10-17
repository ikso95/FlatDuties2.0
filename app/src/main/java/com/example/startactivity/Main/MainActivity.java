package com.example.startactivity.Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.startactivity.Activities.Activity_fragment;
import com.example.startactivity.Activities.NewCyclicalDuty;
import com.example.startactivity.Add_or_join_group.Create_new_group;
import com.example.startactivity.Add_or_join_group.Join_group;
import com.example.startactivity.Common.Common;
import com.example.startactivity.R;
import com.example.startactivity.Shopping.Shopping_fragment;
import com.example.startactivity.Start.StartActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //tabs and drawer menu
    private SectionsPageAdapter msectionsPageAdapter;
    private ViewPager mViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar settings
        Toolbar toolbar = findViewById(R.id.toolbar_appBarMain);
        setSupportActionBar(toolbar);

        //drawer implementation
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout_main);
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view_main);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);



        //przypisanie nicku i maila w headerze nav bara
        View header = navigationView.getHeaderView(0);
        TextView nick = (TextView)header.findViewById(R.id.userNick_header);
        nick.setText(Common.currentUser.getNick());
        TextView mail = (TextView)header.findViewById(R.id.userEmail_header);
        mail.setText(Common.currentUser.getEmail());
        TextView groupName = (TextView)header.findViewById(R.id.userGroupName_header);
        groupName.setText(Common.currentUser.getGroupName());




        //tabs settings
        msectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewPager_main);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs_TabLayout_appBarMain);
        tabLayout.setupWithViewPager(mViewPager);



    }




    //tabs
    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Activity_fragment(),"Duties");
        adapter.addFragment(new Shopping_fragment(),"Shopping");
        viewPager.setAdapter(adapter);
    }


    //ustawienia menu
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.new_duty_drawer) {
            Intent intent = new Intent(MainActivity.this, NewCyclicalDuty.class);
            startActivity(intent);
        } else if (id == R.id.menage_duties_drawer) {
            //Intent intent = new Intent(MainActivity.this, MenageDuties.class);
            //startActivity(intent);
        } else if (id == R.id.join_group_drawer) {
            Intent intent = new Intent(MainActivity.this, Join_group.class);
            startActivity(intent);
        } else if (id == R.id.create_new_group_drawer) {
            Intent intent = new Intent(MainActivity.this, Create_new_group.class);
            startActivity(intent);

        }else if (id == R.id.nav_settings_drawer) {
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout_drawer) {
            logout();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences preferences = getSharedPreferences("Login", MODE_PRIVATE);
        preferences.edit().clear().commit();
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}
