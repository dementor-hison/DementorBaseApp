package kr.co.dementor.common;

import java.util.ArrayList;
import java.util.Arrays;

import kr.co.dementorlibrary.R;

public class Defines
{
	// Default Value
	public static final String DEFAULT_HOST = "http://118.33.90.76"; // 디멘터test서버.
	public static final int DEFAULT_PORT = 9102; // 디멘터test서버.
	public static final String DEFAULT_PATH = "dmtd"; // 디멘터test서버.
	public static final int DEFAULT_TIMEOUT = 10 * 1000; // millisecond
	public static final int MAX_KEY_CAPACITY = 4;

	public static final ArrayList<Integer> RES_ID_DEFAULT_HINT_IMAGE = new ArrayList<Integer>(Arrays.asList(R.drawable.hint_rock,
			R.drawable.hint_key01, R.drawable.hint_key02, R.drawable.hint_key03));

	// public static final String DEFAULT_HOST =
	// "http://mbetest.shinhan.com:38300"; // 신한서버.

	public static enum RegistStatus
	{
		SELECTED_NONE(0), SELECTED_LOCK(1), SELECTED_KEY1(2), SELECTED_KEY2(3), SELECTED_KEY3(4);

		private int value;

		private RegistStatus(int value)
		{
			this.value = value;
		}

		public int getValue()
		{
			return value;
		}

		public RegistStatus nextState()
		{
			for (RegistStatus status : RegistStatus.values())
			{
				if (this.getValue() < status.getValue())
				{
					return status;
				}
			}
			return this;
		}
	}

	public static enum AuthStatus
	{
		INSERT_NONE(0), INSERTED_KEY1(1), INSERTED_KEY2(2), INSERTED_KEY3(3);

		private int value;

		private AuthStatus(int value)
		{
			this.value = value;
		}

		public int getValue()
		{
			return value;
		}

		public AuthStatus nextState()
		{
			for (AuthStatus status : AuthStatus.values())
			{
				if (this.getValue() < status.getValue())
				{
					return status;
				}
			}
			return this;
		}
	}

	public enum RequestCmd
	{
		CMD_LIST("mlist"), CMD_SET("mkeyset"), CMD_KEY("mreqauthimgkey"), CMD_AUTH("mreqauth");

		private String cmd;

		private RequestCmd(String cmd)
		{
			this.cmd = cmd;
		}

		public String getCmd()
		{
			return this.cmd;
		}
	}
	
	public enum RequestAction
	{
		ACTION_LIST("mlist.do"), ACTION_KEY_SET("mkeyset.do"), ACTION_AUTH_IMAGE_KEY("mreqauthimgkey.do"), ACTION_AUTH("mreqauth.do");

		private String url;

		private RequestAction(String url)
		{
			this.url = url;
		}

		public String getUrl()
		{
			return this.url;
		}
	}

	public class ImagePosition
	{
		public static final int LOCK = 0;
		public static final int KEY1 = 1;
		public static final int KEY2 = 2;
		public static final int KEY3 = 3;
	}

	public class ArrowImagePosition
	{
		public static final int ARROW1 = 0;
		public static final int ARROW2 = 1;
		public static final int ARROW3 = 2;
	}

}
