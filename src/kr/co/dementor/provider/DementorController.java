package kr.co.dementor.provider;

import kr.co.dementor.common.Defines;
import kr.co.dementor.common.LogTrace;
import kr.co.dementor.net.SSLImageDownloader;
import kr.co.dementor.ui.AuthActivity;
import kr.co.dementor.ui.RegisterActivity;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class DementorController
{
	static private DementorController sharedDementorController = null;

	private OnDementorStatusListener mListener = null;

	static public DementorController getInstance()
	{
		if (sharedDementorController == null) sharedDementorController = new DementorController();

		return sharedDementorController;
	}

	// Hison add
	public void auth(Context context, UserInfo info, OnDementorStatusListener listener)
	{
		mListener = listener;

		initImageLoader(context);

		Intent intent = new Intent(context, AuthActivity.class);

		context.startActivity(intent);
	}

	public void register(Context context, UserInfo info, boolean reReg, OnDementorStatusListener listener)
	{
		mListener = listener;

		initImageLoader(context);

		if (reReg)
		{
			Intent intent = new Intent(context, AuthActivity.class);
			intent.putExtra("isReset", reReg);
			context.startActivity(intent);
		}
		else
		{
			Intent intent = new Intent(context, RegisterActivity.class);

			intent.putExtra("userInfo", info);

			context.startActivity(intent);
		}
	}

	static public interface OnDementorStatusListener
	{
		public void onAuthComplete(Object result);

		public void onRegistComplete(Object result);

		public void onError(int errorCode, String extra, String errorMessage, DementorConnectRetry retry);

		public void onBack(Activity activity);
	}

	static public interface DementorConnectRetry
	{
		public void retry();
	}

	private void initImageLoader(Context context)
	{
		if (ImageLoader.getInstance().isInited())
		{
			LogTrace.i("Already ImageLoader inited");
			return;
		}
		
		int memoryCacheSize;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR)
		{
			int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
			memoryCacheSize = (memClass / 8) * 1024 * 1024; // 1/8 of app memory
															// limit
		}
		else
		{
			memoryCacheSize = 2 * 1024 * 1024;
		}

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();

		ImageLoaderConfiguration.Builder configBuilder = new ImageLoaderConfiguration.Builder(context);
		configBuilder.imageDownloader(new SSLImageDownloader(context, Defines.DEFAULT_TIMEOUT, Defines.DEFAULT_TIMEOUT));
		configBuilder.threadPriority(Thread.MAX_PRIORITY);
		configBuilder.defaultDisplayImageOptions(defaultOptions);
		configBuilder.diskCacheExtraOptions(100, 100, null);
		configBuilder.diskCache(new UnlimitedDiskCache(context.getCacheDir()));

		configBuilder.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		configBuilder.memoryCache(new LruMemoryCache(memoryCacheSize));
		configBuilder.memoryCacheSize(memoryCacheSize);
		configBuilder.denyCacheImageMultipleSizesInMemory();
		configBuilder.threadPoolSize(25);

		ImageLoaderConfiguration config = configBuilder.build();
		ImageLoader.getInstance().init(config);
	}
}
