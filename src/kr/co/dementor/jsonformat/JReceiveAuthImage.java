package kr.co.dementor.jsonformat;

import java.util.ArrayList;

public class JReceiveAuthImage
{
	public Data DATA = null;
	
	public String code = null;
	
	public class Data
	{
		public String level = null;
		public String servertime = null;
		public String maxfailcnt = null;
		public ArrayList<IconData> icons = null;
	}
	
	public class IconData
	{
		public String iconurl;
	}
}
