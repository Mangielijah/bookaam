package com.omenacle.bookaam;

import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
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

import static com.omenacle.bookaam.ListAdapters.AgencyListAdapter.AGENCY_KEY;
import static com.omenacle.bookaam.ListAdapters.AgencyListAdapter.AGENCY_NAME;


public class BookingActivity extends AppCompatActivity implements LocationFragment.OnLocationListener, ViewPager.OnPageChangeListener, UserInfoFragment.OnUserInfoListener, ConfirmFragment.OnConfirmTicketListener {

    public static  String P_NAME = "P_NAME";
    public static  String P_NUM = "P_NUM";
    public static  String P_ID = "P_ID";
    public static  String PRICE = "PRICE";
    public static  String ROUTE = "ROUTE";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentStatePagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    String agency_name, agency_key, route, passengerName, passengerNumber, passengerId;
    String travelTime, travelDate;
    long  ticketPrice;
    //bookaam info
    long bCharge, bNum;
    String bEmail, bPass;

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
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.b_viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.booking_nav_menu);
        // Set the tabs to fill the entire layout.
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(mViewPager);

        for(int i = 0; i < tabLayout.getTabCount(); i++){
            switch (i){
                case 0:
                    setActionBarTitle("Select your location");
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
    public void onSendLocation(String location, long fare, String travel_time) {
        //Here we have to send the location to the UserInfoFragment
        Log.d("onSendLocation", location);
        this.route = location;
        this.ticketPrice = fare;
        this.travelTime = travel_time;
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
        ConfirmFragment.updateConfirmFragment(passengerName, passengerNumber, passengerId, time, day, bCharge, bNum, bEmail, bPass);
    }

    @Override
    public void onBookTicket(String ticketCode) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("PageSelectedPosition", String.valueOf(position));
        switch (position){
            case 0:
                setActionBarTitle("Select your location");
                break;
            case 1:
                setActionBarTitle("Enter information");
                break;
            case 2:
                setActionBarTitle("Confirm and pay");
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

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    setActionBarTitle("Select your location");
                    fragment = LocationFragment.newInstance(agency_key, agency_name);
                    break;
                case 1:
                    setActionBarTitle("Enter your information");
                    fragment = new UserInfoFragment();
                    break;
                case 2:
                    setActionBarTitle("Confirm and pay");
                    fragment = ConfirmFragment.newInstance(agency_name, agency_key, route, ticketPrice);
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
