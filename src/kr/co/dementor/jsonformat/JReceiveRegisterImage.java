package kr.co.dementor.jsonformat;

import java.util.ArrayList;


public class JReceiveRegisterImage
{
	public String message = null;
	
	public Data data = null;
	
	public String code = null;
	
	public class Data
	{
		public String categoryzip = null;
		public ArrayList<CategoryData> category = null;
	}
	
	public class CategoryData
	{
		public String categoryid = null;
		public String categoryname = null;
		public String iconzip = null;
		public ArrayList<IconData> iconitem = null;
	}
	
	public class IconData
	{
		public String iconid = null;
		public String iconname = null;
		public String iconurl = null;
	}
}
