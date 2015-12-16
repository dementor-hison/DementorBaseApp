package kr.co.dementor.ui;

import kr.co.dementor.common.DementorUtil;
import kr.co.dementor.common.ResourceLoader;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class AuthActivity extends FragmentActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(ResourceLoader.getResourseIdByName("auth_activity", "layout", getApplicationContext()));

		int currentVersion = DementorUtil.getApplcationVersion(getApplicationContext());

		int savedVersion = (Integer) DementorUtil.loadPreferance(getApplicationContext(), "versionCode", -1);

		if (savedVersion == -1 || currentVersion > savedVersion)
		{
			DementorUtil.clearCache(getApplicationContext());

			DementorUtil.savePreferance(getApplicationContext(), "versionCode", currentVersion);
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}
}
