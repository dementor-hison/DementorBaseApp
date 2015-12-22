package kr.co.dementor.jsonformat;

import java.util.ArrayList;

import kr.co.dementor.imagedata.IconData;

public class ReceiveAuthImageInfo
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

}
