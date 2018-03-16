package com.cn.danceland.myapplication.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.fragment.RecommendFragment;
import com.cn.danceland.myapplication.fragment.RecommendedFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import static com.cn.danceland.myapplication.adapter.TabAdapter.TITLES;

/**
 * Created by shy on 2018/3/12 15:02
 * Email:644563767@qq.com
 * 推荐列表
 */


public class RecommendActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager mViewPager;

    public  String[] mTitleDataList = new String[]{"我的推荐", "推荐我的"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(myViewPagerAdapter);

        initMagicIndicator1();
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                current_page = position;
//                if (position == 0) {
//                    btn_add.setVisibility(View.GONE);
//                } else {
//                    btn_add.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private void initMagicIndicator1() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mTitleDataList == null ? 0 : mTitleDataList.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setText(mTitleDataList[index]);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

//            @Override
//            public IPagerIndicator getIndicator(Context context) {
//                LinePagerIndicator indicator = new LinePagerIndicator(context);
//                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
//                return indicator;
//            }
            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                //  indicator.setColors(Color.parseColor("#40c4ff"));
                indicator.setColors(Color.parseColor("#5e6379"));
                return indicator;
            }

        });
        commonNavigator.setAdjustMode(true);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
        case R.id.iv_back:

        break;
        default:
        break;
        }
    }
    public class MyViewPagerAdapter extends FragmentPagerAdapter {


        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            Bundle bundle = new Bundle();
//            bundle.putString("id", id);
//            bundle.putString("auth",auth);
            if (arg0 == 0) {
                RecommendFragment fragment = new RecommendFragment();

                //  fragment.setArguments(bundle);
                return fragment;
            } else if (arg0 == 1) {
                RecommendedFragment fragment = new RecommendedFragment();
             //   fragment.setArguments(bundle);
                return fragment;
            }
            return null;

        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

    }

}