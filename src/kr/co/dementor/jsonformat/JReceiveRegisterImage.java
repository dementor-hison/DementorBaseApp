package kr.co.dementor.jsonformat;

import java.util.ArrayList;


public class JReceiveRegisterImage
{
	private String message;
	
	private ArrayList<Data> data;
	
	private String code;
	
	public class Data
	{
		private String categoryid;
		private String categoryname;
		private String iconzip;
		private ArrayList<IconData> iconitem;
	}
	
	public class IconData
	{
		private String iconid;
		private String iconname;
	}
}
