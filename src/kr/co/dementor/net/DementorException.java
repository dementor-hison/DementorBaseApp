package kr.co.dementor.net;

public class DementorException extends Exception
{
	private static final long serialVersionUID = -8521322739045896764L;

	private String errorCode, extraInfo, errorMessage;

	public DementorException(String errorCode, String extraInfo, String errorMessage)
	{
		super("DementorErrorCode : " + errorCode + " , extraInfo : " + extraInfo + " , errorMessage : " + errorMessage);
		this.errorCode = errorCode;
		this.extraInfo = extraInfo;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode()
	{
		return errorCode;
	}

	public String getExtraInfo()
	{
		return extraInfo;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}
}
