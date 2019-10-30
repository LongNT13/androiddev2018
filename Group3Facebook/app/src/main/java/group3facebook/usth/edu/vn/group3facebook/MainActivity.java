package group3facebook.usth.edu.vn.group3facebook;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.FacebookActivity;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import static com.facebook.FacebookSdk.getApplicationContext;


public class MainActivity extends AppCompatActivity {
    //private Bundle bundle = new Bundle();
    public static String profileName="";
    public static String profileImageLink1="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        FacebookSdk.sdkInitialize(getApplicationContext());
        String profileFirstName = returnValueFromBundles(LoginActivity.PROFILE_FIRST_NAME);
        String profileLastName = returnValueFromBundles(LoginActivity.PROFILE_LAST_NAME);
        String profileImageLink = returnValueFromBundles(LoginActivity.PROFILE_IMAGE_URL);

        //bundle.putString("profileFirstName", profileFirstName);
        //bundle.putString("profileLastName", profileLastName);
        //bundle.putString("profileImageLink", profileImageLink);
        profileName=profileFirstName+ " " + profileLastName;
        profileImageLink1=profileImageLink;

        PagerAdapter adapter = new HomeFragmentPagerAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(adapter);

        TabLayout mainTab = (TabLayout) findViewById(R.id.tab);
        mainTab.setupWithViewPager(pager);

        //toolbar
        Toolbar tb = (Toolbar)findViewById(R.id.ToolbarMain);
        setSupportActionBar(tb);
        //get data contains user name and avatar


    }

    public void ProfileMenu(View view){
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
    //Toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.btnLogOut:
                logoutAction();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoutAction() {
        LoginManager.getInstance().logOut();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }
    //End Toolbar

    public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
        private final int PAGE_COUNT = 2;
        private String titles[] = {"News Feeds", "Menu"};

        public HomeFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return new NewsFeedFragment();
                case 1: return new DropdownMenu();
            }
            return new Fragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
    private String returnValueFromBundles(String key){
        Bundle inBundle = getIntent().getExtras();
        return inBundle.get(key).toString();
    }
}

