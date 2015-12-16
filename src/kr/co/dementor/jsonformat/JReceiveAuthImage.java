package kr.co.dementor.jsonformat;

import java.util.ArrayList;

public class JReceiveAuthImage
{
	private ArrayList<Data> DATA;
	
	private String code;
	
	public class Data
	{
		private String level;
		private String servertime;
		private String maxfailcnt;
		private ArrayList<IconData> icons;
	}
	
	public class IconData
	{
		private String iconurl;
	}
}
