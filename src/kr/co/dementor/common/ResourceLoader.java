package kr.co.dementor.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;

public class ResourceLoader
{

	static public int getResourseIdByName(String name, String className, Context ctx)
	{
		return ctx.getResources().getIdentifier(name, className, ctx.getPackageName());
	}
	
	static public String getResourseNameById(int resId, Context ctx)
	{
		return ctx.getResources().getResourceEntryName(resId);
	}

	static public View getViewByName(String name, String className, Object obj)
	{
		View resultView = null;

		if (obj instanceof Activity)
		{
			Activity act = (Activity) obj;
			resultView = act.findViewById(getResourseIdByName(name, className, act));
		}

		if (obj instanceof View)
		{
			View view = (View) obj;
			resultView = view.findViewById(getResourseIdByName(name, className, view.getContext()));
		}

		if (obj instanceof Dialog)
		{
			Dialog dialog = (Dialog) obj;
			resultView = dialog.findViewById(getResourseIdByName(name, className, dialog.getContext()));
		}

		return resultView;
	}

	public static final int[] getResourceDeclareStyleableIntArray(Context context, String name)
	{
		try
		{
			// use reflection to access the resource class
			java.lang.reflect.Field[] fields2 = Class.forName(context.getPackageName() + ".R$styleable").getFields();

			// browse all fields
			for (java.lang.reflect.Field f : fields2)
			{
				// pick matching field
				if (f.getName().equals(name))
				{
					// return as int array
					int[] ret = (int[]) f.get(null);
					return ret;
				}
			}
		}
		catch (Throwable t)
		{
		}

		return null;
	}

	public static int getPixelsFromDP(Context ctx, float dp)
	{
		final float scale = ctx.getResources().getDisplayMetrics().density;
		int px = (int) (dp * scale + 0.5f);
		return px;
	}

	public static void vibrate(Context ctx, long milliseconds)
	{
		Vibrator vib = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}

	public static int getScreenWidth(Context ctx)
	{
		DisplayMetrics display = ctx.getResources().getDisplayMetrics();
		return display.widthPixels;
	}

}
