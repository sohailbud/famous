package com.example.android.famous.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sohail on 10/6/15.
 */
public class TabsPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {

    private final List<Fragment> FRAGMENTS = new ArrayList<>();
    private final List<Integer> FRAGMENTS_ICON = new ArrayList<>();
    public AppCompatActivity context;

    public TabsPagerAdapter(FragmentManager fm, AppCompatActivity context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        if (FRAGMENTS != null) return FRAGMENTS.size();
        else return 0;
    }

    @Override
    public Fragment getItem(int position) {
        return FRAGMENTS.get(position);
    }

    public void addFragment(Fragment fragment, int icon) {
        FRAGMENTS.add(fragment);
        FRAGMENTS_ICON.add(icon);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable drawable = context.getResources().getDrawable(FRAGMENTS_ICON.get(position));

        SpannableString sb = new SpannableString(" ");
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }
}
