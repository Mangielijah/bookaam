package com.omenacle.bookaam;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.omenacle.bookaam.Account.LoginActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    boolean doubleBackToExitPressedOnce = false;
    TextView emailTextView;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {
            Log.d("OnStartuser", user.getEmail());
            updateUI(user);
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null){
            emailTextView.setText(user.getEmail());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Kill background processes
        killBackgroundProcesses();

        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawerLayout);
        Window window = getWindow();
        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }

        HomeFragment homeFragment = new HomeFragment();

        setFragment(homeFragment, "home");

        NavigationView mNavigationView = findViewById(R.id.nav_view);
        //Get navigation header
        View headerView = mNavigationView.getHeaderView(0);
        emailTextView = headerView.findViewById(R.id.nav_header_text);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_book:
                        BookTicketFragment bookTicketFragment = new BookTicketFragment();
                        setFragment(bookTicketFragment, "book");
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_vip:
                        BookVIPTicketFragment bookVIPTicketFragment = new BookVIPTicketFragment();
                        setFragment(bookVIPTicketFragment, "book");
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_tickets:
                        MyTicketsFragment myTicketsFragment = new MyTicketsFragment();
                        setFragment(myTicketsFragment, "ticket");
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case  R.id.nav_history:
                        HistoryFragment historyFragment = new HistoryFragment();
                        setFragment(historyFragment, "history");
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_demo:
                        PrefManager prefManager = new PrefManager(MainActivity.this);
                        prefManager.setFirstTimeLaunch(true);
                        startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                        break;
                    case R.id.nav_review:
                        openUrl("https://play.google.com/store/apps/details?id=com.omenacle.bookaam");
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_share:
                        share();
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_contact:
                        startActivity(new Intent(MainActivity.this, ContactActivity.class));
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_about:
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_logout:
                        mAuth.signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                        return true;

                }
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

    }

    private void openUrl(String url){
        if(isNetworkAvailable()){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
        else{
            AlertDialog.Builder bd = new AlertDialog.Builder(MainActivity.this);
            bd.setMessage(getString(R.string.require_internet));
            bd.create().show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
            if (doubleBackToExitPressedOnce) {
                finish();
            }else{

                doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please press BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() { doubleBackToExitPressedOnce=false;
                    }
                }, 3000);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFragment(android.support.v4.app.Fragment fragment, String tag){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_panel, fragment).addToBackStack(tag);
        fragmentTransaction.commit();
    }

    private void share(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.text_to_share));
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    private void killBackgroundProcesses()
    {
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = getPackageManager();

        //get a list of installed apps.
        packages = pm.getInstalledApplications(0);

        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ApplicationInfo packageInfo : packages) {
            if((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM)==1){
                if(!packageInfo.packageName.equals("com.omenacle.bookaam")){
                    Log.d("PackageName", packageInfo.packageName);
                    mActivityManager.killBackgroundProcesses(packageInfo.packageName);
                }
            }
        }


        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }
}
