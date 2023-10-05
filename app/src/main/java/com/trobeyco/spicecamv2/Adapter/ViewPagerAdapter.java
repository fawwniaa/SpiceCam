package com.trobeyco.spicecamv2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.trobeyco.spicecamv2.R;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    int images[] = {
            R.drawable.onboard_one,
            R.drawable.onboard_two,
            R.drawable.onboard_three

    };
    String headings[];
    String desc_onboarding[];


    public ViewPagerAdapter(Context context){
        this.context = context;
        this.headings = new String[]{
                context.getResources().getString(R.string.heading_one),
                context.getResources().getString(R.string.heading_two),
                context.getResources().getString(R.string.heading_three)
        };
        this.desc_onboarding = new String[]{
                context.getResources().getString(R.string.desc_one),
                context.getResources().getString(R.string.desc_two),
                context.getResources().getString(R.string.desc_three)
        };

    }


    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout,container,false);

        ImageView slidetitleimage = (ImageView) view.findViewById(R.id.titleImage);
        TextView slideHeading = (TextView) view.findViewById(R.id.text_title);
        TextView slideDescription = (TextView) view.findViewById(R.id.text_description);

        slidetitleimage.setImageResource(images[position]);
        slideHeading.setText(headings[position]);
        slideDescription.setText(desc_onboarding[position]);

        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);

    }
}
