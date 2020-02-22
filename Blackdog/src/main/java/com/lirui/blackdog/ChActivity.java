package com.lirui.blackdog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.SimpleAdapter;

public class ChActivity extends FragmentActivity implements OnClickListener,
		OnPageChangeListener
{
	private List<Map<String, Object>> dataList;
	private SimpleAdapter simp_adapter;
	private ViewPager mViewPager;
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private String[] mTitles = new String[]
	{ "First Fragment !", "Second Fragment !", "Third Fragment !",
			"Fourth Fragment !"};
	private FragmentPagerAdapter mAdapter;
	private TabFragment_l   tabFragment_l;
	private TabFragment_f   tabFragment_f;
	private TabFragment_chat   tabFragment_chat;
	
	private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();
     String useridString;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ch);
		setOverflowButtonAlways();
		getActionBar().setDisplayShowHomeEnabled(false);
		dataList = new ArrayList<Map<String, Object>>();
		initView();
		initDatas();
		mViewPager.setAdapter(mAdapter);
		initEvent();
		Intent intent = getIntent();
		useridString = intent
				.getStringExtra("com.lirui.blackdog.intent.string");
	}
	public String getUseridString(){
		return useridString;
	}
//	public List<Map<String, Object>> getData() {
//		SQLiteDatabase clientdba = openOrCreateDatabase("clientdba.db",MODE_PRIVATE, null);
//		Cursor cs = clientdba.query("clienttb", null, "_id>=?",
//				new String[] { "0" }, null, null, "_id");
//		if (cs != null) {
//			String[] columns = cs.getColumnNames();
//			while (cs.moveToNext()) {
//
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("pic", R.drawable.touxiang);
//				map.put("atext", cs.getString(cs.getColumnIndex(columns[1])));
//				map.put("btext", cs.getString(cs.getColumnIndex(columns[3])));
//				dataList.add(map);
//			}
//			cs.close();
//			clientdba.close();
//		}
//		return dataList;
//	}
	
	
	/**
	 * 初始化所有事件
	 */
	private void initEvent()
	{

		mViewPager.setOnPageChangeListener(this);

	}

	private void initDatas()
	{
		    TabFragment_chat tabFragment_chat=new TabFragment_chat();
		    mTabs.add(tabFragment_chat);
		    TabFragment_l tabFragment_l=new TabFragment_l();
			mTabs.add(tabFragment_l);
			TabFragment_f tabFragment_f=new TabFragment_f();
			mTabs.add(tabFragment_f);
//			TabFragment_me tabFragment_me=new TabFragment_me();
//			mTabs.add(tabFragment_me);
			TabFragment tabFragment = new TabFragment();
			Bundle bundle = new Bundle();
			bundle.putString(TabFragment.TITLE, "不疯魔，不成活");
			tabFragment.setArguments(bundle);
			mTabs.add(tabFragment);
//		for (int i=0;i<=3;i++)  //String title : mTitles
//		{
//			TabFragment tabFragment = new TabFragment();
//			Bundle bundle = new Bundle();
//			if ((i!=1)&&(i!=2) ){
//				String title=mTitles[i];
//				bundle.putString(TabFragment.TITLE, title);
//				tabFragment.setArguments(bundle);
//				mTabs.add(tabFragment);
//			}
//			else {if(i==1){
//				 tabFragment_l=new TabFragment_l();
//					mTabs.add(tabFragment_l);}else{
//						tabFragment_f=new TabFragment_f();
//						mTabs.add(tabFragment_f);
//					}
//			}
//		}

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
		{

			@Override
			public int getCount()
			{
				return mTabs.size();
			}

			@Override
			public Fragment getItem(int position)
			{
				return mTabs.get(position);
			}
		};
	}

	private void initView()
	{
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

		ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
		mTabIndicators.add(one);
		ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
		mTabIndicators.add(two);
		ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
		mTabIndicators.add(three);
		ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
		mTabIndicators.add(four);

		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);

		one.setIconAlpha(1.0f);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void setOverflowButtonAlways()
	{
		try
		{
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKey = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKey.setAccessible(true);
			menuKey.setBoolean(config, false);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 设置menu显示icon
	 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu)
	{

		if (featureId == Window.FEATURE_ACTION_BAR && menu != null)
		{
			if (menu.getClass().getSimpleName().equals("MenuBuilder"))
			{
				try
				{
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public void onClick(View v)
	{
		clickTab(v);

	}

	/**
	 * 点击Tab按钮
	 * 
	 * @param v
	 */
	private void clickTab(View v)
	{
		resetOtherTabs();

		switch (v.getId())
		{
		case R.id.id_indicator_one:
			mTabIndicators.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0, false);
			break;
		case R.id.id_indicator_two:
			mTabIndicators.get(1).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(1, false);
			break;
		case R.id.id_indicator_three:
			mTabIndicators.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(2, false);
			break;
		case R.id.id_indicator_four:
			mTabIndicators.get(3).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(3, false);
			break;
		}
	}

	/**
	 * 重置其他的TabIndicator的颜色
	 */
	private void resetOtherTabs()
	{
		for (int i = 0; i < mTabIndicators.size(); i++)
		{
			mTabIndicators.get(i).setIconAlpha(0);
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels)
	{
		// Log.e("TAG", "position = " + position + " ,positionOffset =  "
		// + positionOffset);
		if (positionOffset > 0)
		{
			ChangeColorIconWithText left = mTabIndicators.get(position);
			ChangeColorIconWithText right = mTabIndicators.get(position + 1);
			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);
		}

	}

	@Override
	public void onPageSelected(int position)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrollStateChanged(int state)
	{
		// TODO Auto-generated method stub

	}

}
