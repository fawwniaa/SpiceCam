package com.trobeyco.spicecamv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.trobeyco.spicecamv2.Adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    ViewPager mslideViewPager;
    ImageView arrow_left, arrow_right, get_started;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        mslideViewPager = findViewById(R.id.slideViewPager);
        arrow_left = findViewById(R.id.arrow_left);
        arrow_right = findViewById(R.id.arrow_right);
        get_started = findViewById(R.id.get_started);

        // Set up ViewPager
        viewPagerAdapter = new ViewPagerAdapter(this);
        mslideViewPager.setAdapter(viewPagerAdapter);
        mslideViewPager.addOnPageChangeListener(viewListener);



        // Set up click listeners
        arrow_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getitem(-1) >= 0) {
                    mslideViewPager.setCurrentItem(getitem(-1), true);
                }
            }
        });

        arrow_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getitem(1) < 3) {
                    mslideViewPager.setCurrentItem(getitem(1), true);
                } else {
                    // handle else case
                }
            }
        });

        get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MainScreen.class);
                startActivity(i);
                finish();
            }
        });
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position > 0) {
                arrow_left.setVisibility(View.VISIBLE);
            } else {
                arrow_left.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private int getitem(int i) {
        return mslideViewPager.getCurrentItem() + i;
    }
}
