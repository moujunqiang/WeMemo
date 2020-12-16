package com.android.myapplication;

import java.util.ArrayList;
import java.util.List;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.myapplication.bean.GlobalValues;
import com.android.myapplication.receiver.AlarmReceiver;


public class MainActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragments = new ArrayList<Fragment>();//列表里装的是碎片


	/**
	 * 底部四个按钮
	 */
	private LinearLayout mTabBtnWeixin;
	private LinearLayout mTabBtnFrd;
	private LinearLayout mTabBtnAddress;
	private LinearLayout mTabBtnSettings;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        AlarmReceiver alarmReceiver = new AlarmReceiver();
        IntentFilter intentFilter =new IntentFilter();
        intentFilter.addAction(GlobalValues.TIMER_ACTION);
        intentFilter.addAction(GlobalValues.TIMER_ACTION_REPEATING);
        registerReceiver(alarmReceiver,intentFilter);
		initView()  ;



		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())//无名派生类对象，相当于一个构造方法，适配器
		{

			@Override
			public int getCount()
			{
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int arg0)
			{
				return mFragments.get(arg0);
			}//显示的部分，取得列表数组的第几个
		};

		mViewPager.setAdapter(mAdapter);


		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()//无名派生类对象，一定要理解！
		{

			private int currentIndex;

			@Override
			public void onPageSelected(int position)
			{
				resetTabBtn();
				switch (position)
				{
					case 0:
						((ImageButton) mTabBtnWeixin.findViewById(R.id.btn_tab_bottom_weixin))
								.setImageResource(R.drawable.tab_weixin_pressed);
						break;
					case 1:
						((ImageButton) mTabBtnFrd.findViewById(R.id.btn_tab_bottom_friend))
								.setImageResource(R.drawable.tab_find_frd_pressed);
						break;
					case 2:
						((ImageButton) mTabBtnAddress.findViewById(R.id.btn_tab_bottom_contact))
								.setImageResource(R.drawable.tab_address_pressed);
						break;
					case 3:
						((ImageButton) mTabBtnSettings.findViewById(R.id.btn_tab_bottom_setting))
								.setImageResource(R.drawable.tab_settings_pressed);
						break;
				}

				currentIndex = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{

			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{
			}
		});

	}

	protected void resetTabBtn()
	{
		((ImageButton) mTabBtnWeixin.findViewById(R.id.btn_tab_bottom_weixin))
				.setImageResource(R.drawable.tab_weixin_normal);
		((ImageButton) mTabBtnFrd.findViewById(R.id.btn_tab_bottom_friend))
				.setImageResource(R.drawable.tab_find_frd_normal);
		((ImageButton) mTabBtnAddress.findViewById(R.id.btn_tab_bottom_contact))
				.setImageResource(R.drawable.tab_address_normal);
		((ImageButton) mTabBtnSettings.findViewById(R.id.btn_tab_bottom_setting))
				.setImageResource(R.drawable.tab_settings_normal);
	}

	private void initView()
	{

		mTabBtnWeixin = (LinearLayout) findViewById(R.id.id_tab_bottom_weixin);
		mTabBtnFrd = (LinearLayout) findViewById(R.id.id_tab_bottom_friend);
		mTabBtnAddress = (LinearLayout) findViewById(R.id.id_tab_bottom_contact);
		mTabBtnSettings = (LinearLayout) findViewById(R.id.id_tab_bottom_setting);
		mTabBtnWeixin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(0);
			}
		});
		mTabBtnFrd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(1);
			}
		});
		mTabBtnAddress.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(2);
			}
		});
		mTabBtnSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(3);
			}
		});
		MainTab01 tab01 = new MainTab01();
		MainTab02 tab02 = new MainTab02();
		MainTab03 tab03 = new MainTab03();
		MainTab04 tab04 = new MainTab04();
		mFragments.add(tab01);
		mFragments.add(tab02);
		mFragments.add(tab03);
		mFragments.add(tab04);
	}
}
