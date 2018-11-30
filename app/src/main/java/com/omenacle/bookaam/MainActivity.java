package com.omenacle.bookaam;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    case R.id.nav_tickets:
                        MyTicketsFragment myTicketsFragment = new MyTicketsFragment();
                        setFragment(myTicketsFragment, "ticket");
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case  R.id.nav_history:
                        Toast.makeText(MainActivity.this, R.string.history, Toast.LENGTH_SHORT).show();
                        HistoryFragment historyFragment = new HistoryFragment();
                        setFragment(historyFragment, "history");
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_review:
                        Toast.makeText(MainActivity.this, R.string.reviews, Toast.LENGTH_SHORT).show();
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_contact:
                        Toast.makeText(MainActivity.this, R.string.contact_feedback, Toast.LENGTH_SHORT).show();
                        MessagingFragment messagingFragment = new MessagingFragment();
                        setFragment(messagingFragment, "messaging");
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_share:
                        Toast.makeText(MainActivity.this, R.string.share, Toast.LENGTH_SHORT).show();
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_faq:
                        Toast.makeText(MainActivity.this, R.string.faq, Toast.LENGTH_SHORT).show();
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_about:
                        Toast.makeText(MainActivity.this, R.string.about_us, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_policy:
                        Toast.makeText(MainActivity.this, R.string.refund_policy, Toast.LENGTH_SHORT).show();
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_logout:
                        Toast.makeText(MainActivity.this, R.string.log_out, Toast.LENGTH_SHORT).show();
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;

                }
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

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
                return;
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
}
