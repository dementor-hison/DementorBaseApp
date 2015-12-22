package kr.co.dementor.ui;

import kr.co.dementor.adapter.CategoryIconAdapter;
import kr.co.dementor.adapter.CategoryMenuAdapter;
import kr.co.dementor.adapter.CategoryIconAdapter.OnSelectListener;
import kr.co.dementor.common.Defines;
import kr.co.dementor.common.Defines.RegistStatus;
import kr.co.dementor.common.LogTrace;
import kr.co.dementor.common.ResourceLoader;
import kr.co.dementor.imagedata.IconData;
import kr.co.dementor.jsonformat.ReceiveRegisterImageInfo;
import kr.co.dementor.jsonformat.RequestRegisterImageInfo;
import kr.co.dementor.net.AbstractClient;
import kr.co.dementor.net.AbstractClient.OnCompleteListener;
import kr.co.dementor.net.RegisterIconsDownClient;
import kr.co.dementor.provider.UserInfo;
import android.content.Intent;
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
	private AbstractClient client;

	private ReceiveRegisterImageInfo receiveRegisterImageData = null;
	private CategoryMenuAdapter categoryMenuAdapter;
	private CategoryIconAdapter categoryIconAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(ResourceLoader.getResourseIdByName("register_activity", "layout", getApplicationContext()));

		initView();

		Intent intent = getIntent();
		
		UserInfo userInfo = (UserInfo) intent.getSerializableExtra("userInfo");
		
		RequestRegisterImageInfo registParams = makeParam(userInfo);
		
		client = new RegisterIconsDownClient(getApplicationContext(), registParams);
		client.start();
		client.setOnCompleteListener(completeListener);
	}

	private RequestRegisterImageInfo makeParam(UserInfo userInfo)
	{
		RequestRegisterImageInfo requestRegisterImageInfo = new RequestRegisterImageInfo();
//		requestRegisterImageInfo.appid = userInfo.appId;
//		requestRegisterImageInfo.cmd = Defines.RequestCmd.CMD_LIST.getCmd();
//		requestRegisterImageInfo.devtype = Integer.toString(1);
//		requestRegisterImageInfo.userid = userInfo.userId;
		
		//test
		requestRegisterImageInfo.appid = "appid";
		requestRegisterImageInfo.cmd = Defines.RequestCmd.CMD_LIST.getCmd();
		requestRegisterImageInfo.devtype = Integer.toString(1);
		requestRegisterImageInfo.userid = "testUserId";

		return requestRegisterImageInfo;
	}

	private void initView()
	{
		topView = (TopView) ResourceLoader.getViewByName("registTopView", "id", this);
		topView.setHelpButtonVisible(true);
		topView.setRefreshButtonVisible(true);
		topView.setConfirmButtonVisible(false);

		hintPopup = (HintPopup) ResourceLoader.getViewByName("hintPopup", "id", this);
		hintPopup.setVisibility(View.GONE);

		vpRegistCategory = (ViewPager) ResourceLoader.getViewByName("vpRegistCategory", "id", this);
		vpRegistCategory.addOnPageChangeListener(onPageChangeListener);

		vpRegistCategoryItemPager = (ViewPager) ResourceLoader.getViewByName("vpRegistCategoryItemPager", "id", this);
		vpRegistCategoryItemPager.addOnPageChangeListener(onPageChangeListener);
		
		categoryMenuAdapter = new CategoryMenuAdapter(getSupportFragmentManager());
		
		vpRegistCategory.setAdapter(categoryMenuAdapter);
		
		categoryIconAdapter = new CategoryIconAdapter(getSupportFragmentManager());
		categoryIconAdapter.setOnSelectListener(onSelectListener);
		vpRegistCategory.setAdapter(categoryIconAdapter);
		
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	OnPageChangeListener onPageChangeListener = new OnPageChangeListener()
	{
		@Override
		public void onPageSelected(int arg0)
		{
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
		}

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

		if (resName.equals("flTopBack"))
		{

		}
		if (resName.equals("ibTopHelp"))
		{

		}
		if (resName.equals("ibTopConfirm"))
		{

		}
		if (resName.equals("ibTopRefresh"))
		{

		}
	}

	public void onClickShowHint(View v)
	{
		handler.post(runShowHintPopup);
	}

	OnCompleteListener completeListener = new OnCompleteListener()
	{
		@Override
		public void onError(int errorCode, String extra, String message)
		{
			LogTrace.e("ErrorCode : " + errorCode + " , extra : " + extra + " message : " + message);
		}

		@Override
		public void onComplete(Object result)
		{
			receiveRegisterImageData = (ReceiveRegisterImageInfo) result;
			
			categoryMenuAdapter.setCategoryList(receiveRegisterImageData.data.category);
			
			categoryIconAdapter.setIconList(receiveRegisterImageData.data.category);
		}
	};

	OnSelectListener onSelectListener = new OnSelectListener()
	{
		@Override
		public void onSeletedItem(View v, IconData iconData)
		{
			LogTrace.e("iconData.iconid : " + iconData.iconid + " , iconData.iconname : " + iconData.iconname);
		}
	};
}
