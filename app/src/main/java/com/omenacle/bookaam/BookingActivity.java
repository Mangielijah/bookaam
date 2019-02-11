package com.omenacle.bookaam;

import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import android.view.Window;
import android.view.WindowManager;

import com.omenacle.bookaam.BookingFragment.ConfirmFragment;
import com.omenacle.bookaam.BookingFragment.LocationFragment;
import com.omenacle.bookaam.BookingFragment.UserInfoFragment;
import com.omenacle.bookaam.DataClasses.Seats;

import static com.omenacle.bookaam.ListAdapters.AgencyListAdapter.AGENCY_KEY;
import static com.omenacle.bookaam.ListAdapters.AgencyListAdapter.AGENCY_NAME;


public class BookingActivity extends AppCompatActivity implements LocationFragment.OnLocationListener, ViewPager.OnPageChangeListener, UserInfoFragment.OnUserInfoListener {

    public static  String PRICE = "PRICE";
    public static  String ROUTE = "ROUTE";

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    String agency_name, agency_key, route, passengerName, passengerNumber, passengerId;
    String travelTime, travelDate;
    long  ticketPrice;
    Seats routeSeats = null;
    //bookaam info
    long bCharge, bNum;
    String bEmail, bPass;
    ConfirmFragment confirmFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
         agency_name = getIntent().getStringExtra(AGENCY_NAME);
         agency_key  = getIntent().getStringExtra(AGENCY_KEY);


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

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentStatePagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.b_viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);

        TabLayout tabLayout = findViewById(R.id.booking_nav_menu);
        // Set the tabs to fill the entire layout.
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(mViewPager);

        for(int i = 0; i < tabLayout.getTabCount(); i++){
            switch (i){
                case 0:
                    setActionBarTitle(getResources().getString(R.string.select_your_location));
                    tabLayout.getTabAt(i).setIcon(R.drawable.location_icon);
                    break;
                case 1:
                    tabLayout.getTabAt(i).setIcon(R.drawable.user_info_icon);
                    break;
                case 2:
                    tabLayout.getTabAt(i).setIcon(R.drawable.confirm_icon);
                    break;
            }
        }
    }


    @Override
    public void onSendLocation(String location, long fare, String travel_time, Seats seats) {
        //Here we have to send the location to the UserInfoFragment
        Log.d("onSendLocation", location);
        this.route = location;
        this.ticketPrice = fare;
        this.travelTime = travel_time;
        routeSeats = seats;
        mViewPager.setCurrentItem(1);
        UserInfoFragment.newInstance(travel_time);
    }

    @Override
    public void onSendUserInfo(String name, String number, String NId, String time, String day, long bookaamNum, long bookaamC, String bookaamE, String bookaamP) {

        passengerName = name;
        passengerNumber = number;
        passengerId = NId;
        travelTime = time;
        travelDate = day;
        bCharge = bookaamC;
        bNum = bookaamNum;
        bEmail = bookaamE;
        bPass = bookaamP;

        mViewPager.setCurrentItem(2);
        confirmFragment.updateConfirmFragment(passengerName, passengerNumber, passengerId, travelTime, travelDate, bCharge, bNum, bEmail, bPass, routeSeats);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("PageSelectedPosition", String.valueOf(position));
        switch (position){
            case 0:
                setActionBarTitle(getResources().getString(R.string.select_your_location));
                break;
            case 1:
                setActionBarTitle(getResources().getString(R.string.enter_info));
                break;
            case 2:
                setActionBarTitle(getResources().getString(R.string.confirm_pay));
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * A {@link FragmentStatePagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    setActionBarTitle(getResources().getString(R.string.select_your_location));
                    fragment = LocationFragment.newInstance(agency_key, agency_name);
                    break;
                case 1:
                    setActionBarTitle(getResources().getString(R.string.enter_info));
                    fragment = new UserInfoFragment();
                    break;
                case 2:
                    setActionBarTitle(getResources().getString(R.string.confirm_pay));
                    fragment = confirmFragment = ConfirmFragment.newInstance(agency_name, agency_key, route, ticketPrice);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

}
