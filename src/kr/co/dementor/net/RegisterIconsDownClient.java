package kr.co.dementor.net;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.os.AsyncTask;

import java.util.ArrayList;

import kr.co.dementor.common.LogTrace;
import kr.co.dementor.jsonformat.JReceiveRegisterImage;
import kr.co.dementor.jsonformat.JRequestRegisterImage;
import kr.co.dementor.jsonformat.JRequestRegisterSetting;

public class RegisterIconsDownClient extends AbstractClient
{
	private JRequestRegisterImage requestParam = null;
	@Override
	Object main()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String makeParams()
	{
		//test
		requestParam = new JRequestRegisterImage();
		requestParam.setAppid("appid");
		requestParam.setCmd("mlist");
		requestParam.setDevtype("1");
		requestParam.setUserid("test");
		
		if(requestParam == null)
		{
			LogTrace.e("requestParam not set!!... plz call setRequestData");
			return null;
		}
		
		Gson gson = new Gson();
		
		String jsonParam = gson.toJson(requestParam);
		
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(jsonParam).getAsJsonObject();
		JsonObject addJson = new JsonObject();
		addJson.add("data", json);
		
		LogTrace.d(addJson.toString());
		
		return addJson.toString();
	}

	public void setRequestData(JRequestRegisterImage data)
	{
		requestParam = data;
	}
}
