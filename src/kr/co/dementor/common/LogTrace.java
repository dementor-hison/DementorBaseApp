package kr.co.dementor.common;

import android.util.Log;

public class LogTrace
{
	private final static String TAG = "Dementor";
	private static boolean isEnabled = true;
	private static StackTraceElement callerElement = null;
	private static String format = "[%s %s] %s - %s";

	public static void enable(boolean isEnable)
	{
		isEnabled = isEnable;
	}

	public static void e(String strMsg)
	{
		if (isEnabled)
		{
			Exception e = new Exception();
			callerElement = e.getStackTrace()[1];
			Log.e(TAG, String.format(format, callerElement.getFileName(), callerElement.getLineNumber(), callerElement.getMethodName(), strMsg));
		}
	}

	public static void w(String strMsg)
	{
		if (isEnabled)
		{
			Exception e = new Exception();
			callerElement = e.getStackTrace()[1];
			Log.w(TAG, String.format(format, callerElement.getFileName(), callerElement.getLineNumber(), callerElement.getMethodName(), strMsg));
		}
	}

	public static void i(String strMsg)
	{
		if (isEnabled)
		{
			Exception e = new Exception();
			callerElement = e.getStackTrace()[1];
			Log.i(TAG, String.format(format, callerElement.getFileName(), callerElement.getLineNumber(), callerElement.getMethodName(), strMsg));
		}
	}

	public static void d(String strMsg)
	{
		if (isEnabled)
		{
			Exception e = new Exception();
			callerElement = e.getStackTrace()[1];
			Log.d(TAG, String.format(format, callerElement.getFileName(), callerElement.getLineNumber(), callerElement.getMethodName(), strMsg));
		}
	}

	public static void v(String strMsg)
	{
		if (isEnabled)
		{
			Exception e = new Exception();
			callerElement = e.getStackTrace()[1];
			Log.v(TAG, String.format(format, callerElement.getFileName(), callerElement.getLineNumber(), strMsg));
		}
	}
}
