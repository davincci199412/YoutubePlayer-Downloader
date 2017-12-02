package org.schabi.goldstar;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.schabi.goldstar.database.DBHelper;
import org.schabi.goldstar.database.Database_Class;
import org.schabi.goldstar.download.DownloadActivity;
import org.schabi.goldstar.fragments.search_fragment.SearchInfoItemFragment;
import org.schabi.goldstar.fragments.search_fragment.other_fragments.Favourite_Fragment;
import org.schabi.goldstar.fragments.search_fragment.other_fragments.Recent_Fragment;
import org.schabi.goldstar.settings.SettingsActivity;
import org.schabi.goldstar.util.PermissionHelper;
import org.schabi.goldstar.util.ThemeHelper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements Favourite_Fragment.OnFragmentInteractionListener , Recent_Fragment.OnFragmentInteractionListener {
    public static int count_video = 0;
    public static Context context;
    public static Database_Class database_class;
    public static int nfragmentstate = 0;
    private SearchInfoItemFragment mainFragment = null;
    private  Recent_Fragment recentlyPlayedFragment = null;
    private  Favourite_Fragment favourites_fragment = null;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private InterstitialAd mInterstitial;
    private int[] tabIcons = {
            R.drawable.ic_action_heart,
            R.drawable.ic_recently_wached,
            R.drawable.ic_search,
            R.drawable.ic_action_playlist
    };
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ThemeHelper.setTheme(this, true);
        setContentView(R.layout.activity_main);
        context = this;
        database_class = new Database_Class(context);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        initViewPager();
        initInterstitial();
        initAdmob();
        initDrawerLayout();
    }

    private void initDrawerLayout() {


        /*NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id)
                {

                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initInterstitial()
    {
        mInterstitial = new InterstitialAd(this);
        mInterstitial.setAdUnitId(getResources().getString(R.string.admob_intertestial_id));
        requestNewInterstitial();
        mInterstitial.setAdListener(new AdListener() {
          @Override
         public void onAdFailedToLoad(int errorCode) {
          }
             @Override
             public void onAdLoaded() {

             }
             @Override
             public void onAdClosed() {
                 requestNewInterstitial();
             }
         }
        );
    }
    private void requestNewInterstitial() {

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitial.loadAd(adRequest);


    }

    private void showInterstitial() {
        if (mInterstitial.isLoaded()) {
            mInterstitial.show();
        }else
        {
            requestNewInterstitial();
        }
    }

    private void initAdmob() {
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("Device ID")
                .build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdOpened() {

            }

            @Override
            public void onAdLeftApplication() {

            }

            @Override
            public void onAdClosed() {

            }
        });
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        mainFragment = new SearchInfoItemFragment();
        recentlyPlayedFragment = new Recent_Fragment();
        favourites_fragment = new Favourite_Fragment();
        adapter.addFragment(favourites_fragment, null);
        adapter.addFragment(recentlyPlayedFragment, null);
        adapter.addFragment(mainFragment, null);
        viewPager.setAdapter(adapter);
    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home: {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpTo(this, intent);
                return true;
            }
            case R.id.action_settings: {
                /*Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);*/
                return true;
            }
            case R.id.action_show_downloads: {
                if (!PermissionHelper.checkStoragePermissions(this)) {
                    return false;
                }

                Intent intent = new Intent(this, DownloadActivity.class);
                startActivity(intent);
                showInterstitial();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
                finish();
        }
    }

    @Override
    protected void onPause() {


            super.onPause();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void showAdmobInerstials() {
        showInterstitial();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
}
