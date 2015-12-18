package kr.co.dementor.net;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.http.client.ClientProtocolException;

import kr.co.dementor.common.Defines;
import kr.co.dementor.common.DementorUtil;
import kr.co.dementor.common.LogTrace;
import kr.co.dementor.jsonformat.ReceiveRegisterImage;
import kr.co.dementor.jsonformat.ReceiveRegisterImage.CategoryData;
import kr.co.dementor.jsonformat.RequestRegisterImage;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RegisterIconsDownClient extends AbstractClient
{
	private RequestRegisterImage requestParam = null;
	private ReceiveRegisterImage receiveCategoryInfo = null;

	@Override
	String makeJsonData()
	{
		// test
		requestParam = new RequestRegisterImage();
		requestParam.appid = "appid";
		requestParam.cmd = "mlist";
		requestParam.devtype = "1";
		requestParam.userid = "test";

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

	public void setRequestData(RequestRegisterImage data)
	{
		requestParam = data;
	}

	@Override
	void main() throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, KeyManagementException, UnrecoverableKeyException,
			ClientProtocolException, KeyStoreException, CertificateException, DementorException
	{
		String result = getDataFromServer(Defines.RequestAction.ACTION_LIST.getUrl());

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		receiveCategoryInfo = gson.fromJson(result, ReceiveRegisterImage.class);

		DementorUtil.classToLog(receiveCategoryInfo);

		if (receiveCategoryInfo.data == null)
		{
			LogTrace.e("Data was not Ready");
			return;
		}

		// 카테고리 ZIP 다운로드
		String categoryZipUrl = receiveCategoryInfo.data.categoryzip;

		HashMap<String, Bitmap> categoryMap = getFileFromServer(categoryZipUrl);

		receiveCategoryInfo.setCategoryBitmap(categoryMap);

		// 아이콘 ZIP 다운로드
		for (int categoryIndex = 0; categoryIndex < receiveCategoryInfo.data.category.size(); categoryIndex++)
		{
			CategoryData categoryData = receiveCategoryInfo.data.category.get(categoryIndex);

			String iconZipUrl = categoryData.iconzip;

			HashMap<String, Bitmap> imageMap = getFileFromServer(iconZipUrl);

			receiveCategoryInfo.setImageBitmap(imageMap, categoryData.categoryid);
		}
	}

}
