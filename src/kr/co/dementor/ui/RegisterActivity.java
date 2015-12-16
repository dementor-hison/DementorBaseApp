package kr.co.dementor.ui;

import kr.co.dementor.common.Defines.RegistStatus;
import kr.co.dementor.common.LogTrace;
import kr.co.dementor.common.ResourceLoader;
import kr.co.dementor.net.AbstractClient;
import kr.co.dementor.net.RegisterIconsDownClient;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

public class RegisterActivity extends FragmentActivity
{
	private RegistStatus currentStatus = RegistStatus.SELECTED_NONE;
	private TopView topView = null;
	private HintPopup hintPopup = null;
	private Handler handler = new Handler();
	private ViewPager vpRegistCategory;
	private ViewPager vpRegistCategoryItemPager;
	private AbstractClient mClient;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(ResourceLoader.getResourseIdByName("register_activity", "layout", getApplicationContext()));

		initView();
		
		mClient = new RegisterIconsDownClient();
		mClient.start();
	}

	private void initView()
	{
		topView = (TopView) ResourceLoader.getViewByName("registTopView", "id", this);
		topView.setHelpButtonVisible(true);
		topView.setRefreshButtonVisible(true);
		topView.setConfirmButtonVisible(false);

		hintPopup = (HintPopup) ResourceLoader.getViewByName("hintPopup", "id", this);
		hintPopup.setVisibility(View.GONE);

		vpRegistCategory = (ViewPager)ResourceLoader.getViewByName("vpRegistCategory", "id", this);
		vpRegistCategory.addOnPageChangeListener(onPageChangeListener);
		
		vpRegistCategoryItemPager = (ViewPager)ResourceLoader.getViewByName("vpRegistCategoryItemPager", "id", this);
		vpRegistCategoryItemPager.addOnPageChangeListener(onPageChangeListener);
		
		
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}	

	
	OnPageChangeListener onPageChangeListener = new OnPageChangeListener()
	{	
		@Override
		public void onPageSelected(int arg0){}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2){}
		
		@Override
		public void onPageScrollStateChanged(int index)
		{
			vpRegistCategory.setCurrentItem(index, true);
			vpRegistCategoryItemPager.setCurrentItem(index, true);
		}
	};
	
	
	private Runnable runHideHintPopup = new Runnable()
	{
		@Override
		public void run()
		{
			hintPopup.setVisibility(View.GONE);
		}
	};

	private Runnable runShowHintPopup = new Runnable()
	{
		@Override
		public void run()
		{
			handler.removeCallbacks(runHideHintPopup);

			hintPopup.setVisibility(View.VISIBLE);

			handler.postDelayed(runHideHintPopup, 1000);
		}
	};

	public void onTopClick(View v)
	{
		String resName = ResourceLoader.getResourseNameById(v.getId(), getApplicationContext());
		
		if(resName.equals("flTopBack"))
		{
			
		}
		if(resName.equals("ibTopHelp"))
		{
			
		}
		if(resName.equals("ibTopConfirm"))
		{
			
		}
		if(resName.equals("ibTopRefresh"))
		{
			
		}
	}
	
	public void onClickShowHint(View v)
	{
		handler.post(runShowHintPopup);
	}
}
