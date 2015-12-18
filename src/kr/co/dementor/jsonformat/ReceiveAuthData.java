package kr.co.dementor.jsonformat;

public class ReceiveAuthData
{
	public Data data = null;
	
	public String code = null;
	
	public class Data
	{
		public String authCode = null;
		public String sessionID = null;
		public String compid = null;
		public String ssoSid = null;
		public String logInType = null;
		public String userid = null;	
	}
	
}
