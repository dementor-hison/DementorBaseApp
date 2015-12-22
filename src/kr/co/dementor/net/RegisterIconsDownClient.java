package kr.co.dementor.net;

import java.util.HashMap;

import kr.co.dementor.common.Defines;
import kr.co.dementor.common.DementorUtil;
import kr.co.dementor.common.LogTrace;
import kr.co.dementor.imagedata.CategoryData;
import kr.co.dementor.jsonformat.ReceiveRegisterImageInfo;
import kr.co.dementor.jsonformat.RequestRegisterImageInfo;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RegisterIconsDownClient extends AbstractClient
{
	private Context context = null;
	private RequestRegisterImageInfo requestParam = null;
	private ReceiveRegisterImageInfo receiveCategoryInfo = null;
	private ProgressDialog progressDialog = null;

	public RegisterIconsDownClient(Context context)
	{
		this.context = context;
	}
	
	public RegisterIconsDownClient(Context context, RequestRegisterImageInfo data)
	{
		this.context = context;
		this.requestParam = data;
	}
	
	public void setRequestData(RequestRegisterImageInfo data)
	{
		requestParam = data;
	}
	
	@Override
	String makeJsonData()
	{
		if (requestParam == null)
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

	@Override
	void preMain()
	{
		progressDialog  = ProgressDialog.show(context, null, "Data Loading...");
	}
	
	@Override
	void main()
	{
		String result = getDataFromServer(Defines.RequestAction.ACTION_LIST.getUrl());
		
		if(result == null)
		{
			LogTrace.e("getData from Server FAIL");
			return;
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		receiveCategoryInfo = gson.fromJson(result, ReceiveRegisterImageInfo.class);

		DementorUtil.classToLog(receiveCategoryInfo);

		if (receiveCategoryInfo.data == null)
		{
			LogTrace.e("Data was not Ready");
			return;
		}

		// 카테고리 ZIP 다운로드
		String categoryZipUrl = receiveCategoryInfo.data.categoryzip;

		HashMap<String, Bitmap> categoryMap = getFileFromServer(categoryZipUrl);
		
		if(categoryMap == null)
		{
			LogTrace.e("Not Ready category item");
			return;
		}

		receiveCategoryInfo.setCategoryBitmap(categoryMap);

		// 아이콘 ZIP 다운로드
		for (int categoryIndex = 0; categoryIndex < receiveCategoryInfo.data.category.size(); categoryIndex++)
		{
			CategoryData categoryData = receiveCategoryInfo.data.category.get(categoryIndex);

			String iconZipUrl = categoryData.iconzip;

			HashMap<String, Bitmap> imageMap = getFileFromServer(iconZipUrl);
			
			if(imageMap == null)
			{
				LogTrace.e("Not Ready Image item... categoryID : " + categoryData.categoryid);
				return;
			}

			receiveCategoryInfo.setImageBitmap(imageMap, categoryData.categoryid);
		}
	}
	
	@Override
	void postMain()
	{
		completeListener.onComplete(receiveCategoryInfo);
		
		if(progressDialog != null && progressDialog.isShowing())
		{
			progressDialog.dismiss();
		}
		
		receiveCategoryInfo = null;
	}
	

}
