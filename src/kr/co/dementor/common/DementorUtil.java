package kr.co.dementor.common;

import java.io.File;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

public class DementorUtil
{
	public static int getApplcationVersion(Context context)
	{	
		int result = -1;
		
		try
		{
			result = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static Object loadPreferance(@NonNull Context context, @NonNull String key, @NonNull Object defaultValue)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        if(defaultValue instanceof String)
        {
            return preferences.getString(key, (String)defaultValue);
        }
        else if(defaultValue instanceof Boolean)
        {
            return preferences.getBoolean(key, (Boolean) defaultValue);
        }
        else if(defaultValue instanceof Integer)
        {
            return preferences.getInt(key, (Integer) defaultValue);
        }
        /*
        else if(defaultValue instanceof Set)
        {
            return preferences.getStringSet(key, (Set<String>) defaultValue);
        }
        */
        else
        {
            LogTrace.e("Undefine value");
            return null;
        }
    }

    public static void savePreferance(Context context, String key, Object value)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor edit = preferences.edit();

        if(value instanceof String)
        {
            edit.putString(key, (String)value);
        }
        else if(value instanceof Boolean)
        {
            edit.putBoolean(key, (Boolean) value);
        }
        else if(value instanceof Integer)
        {
            edit.putInt(key, (Integer) value);
        }
        /*
        else if(value instanceof Set)
        {
            edit.putStringSet(key, (Set<String>) value);
        }
        */
        else
        {
            LogTrace.e("Undefine value");
        }

        edit.commit();
    }
    
    public static void clearCache(Context context) {
		
		File cache = context.getCacheDir();
		String[] files = cache.list();
		for(String file : files) {
			File data = new File(cache.getPath() + "/" + file);
			data.delete();
		}
	}
}
