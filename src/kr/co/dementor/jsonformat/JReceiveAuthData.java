package kr.co.dementor.jsonformat;

import java.util.ArrayList;


public class JReceiveAuthData
{
	private ArrayList<Data> data;
	
	private String code;
	
	public class Data
	{
		private String authCode;
		private String sessionID;
		private String compid;
		private String ssoSid;
		private String logInType;
		private String userid;	
	}
}
