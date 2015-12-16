package kr.co.dementor.provider;

import kr.co.dementor.common.Defines;

public class ConnectInfo
{
	private String host = null, userID = null, deviceID = null;
	private String path = null;
	private int connectionTimeout = -1;

	public ConnectInfo()
	{
		this.host = Defines.DEFAULT_HOST;

		this.setConnectionTimeout(Defines.DEFAULT_TIMEOUT);
	}

	/**
	 * @param host
	 * @param userID
	 * @param deviceID
	 */
	public ConnectInfo(String host, String userID, String deviceID)
	{
		this.host = host;
		this.userID = userID;
		this.deviceID = deviceID;

		this.setConnectionTimeout(Defines.DEFAULT_TIMEOUT);
	}

	/**
	 * host주소를 반환한다.
	 * 
	 * @return the host
	 */
	public String getHost()
	{
		return host;
	}

	/**
	 * 주소를 포트와 함께 입력한다. 설정하지 않을 경우 기본값으로 등록이 된다. ex) "http://118.33.90.76:9101"
	 * 
	 * @param host
	 */
	public void setHost(String host)
	{
		this.host = host;
	}

	/**
	 * userId를 반환한다. ex) "01011112233"
	 * 
	 * @return the userID
	 */
	public String getUserID()
	{
		return userID;
	}

	/**
	 * userId를 입력한다. 기본값이 없으므로 반드시 입력한다.(필수) ex) "01011112233"
	 * 
	 * @param userID
	 */
	public void setUserID(String userID)
	{
		this.userID = userID;
	}

	/**
	 * device를 반환한다.
	 * 
	 * @return the deviceID
	 */
	public String getDeviceID()
	{
		return deviceID;
	}

	/**
	 * DeviceId를 입력한다.
	 * 
	 * @param deviceID
	 */
	public void setDeviceID(String deviceID)
	{
		this.deviceID = deviceID;
	}

	/**
	 * 서버 path를 반환한다. ex) "/dmtd/client/mClient.do"
	 * 
	 * @return the path
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * 서버 path ex) "/dmtd/client/mClient.do"
	 * 
	 * @param path
	 */
	public void setPath(String path)
	{
		this.path = path;
	}

	/**
	 * Connection timeout을 설정한다. 기본값은 10초로 설정되어 있다.
	 * 
	 * @param millisecond
	 */
	private void setConnectionTimeout(int millisecond)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * @return the connectionTimeout 단위 millisecond
	 */
	public int getConnectionTimeout()
	{
		return connectionTimeout;
	}

}
