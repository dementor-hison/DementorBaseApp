package kr.co.dementor.net;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kr.co.dementor.common.DementorUtil;
import kr.co.dementor.common.LogTrace;
import kr.co.dementor.jsonformat.JReceiveRegisterImage;
import kr.co.dementor.jsonformat.JRequestRegisterImage;

public class RegisterIconsDownClient extends AbstractClient
{
	private JRequestRegisterImage requestParam = null;

	@Override
	String makeJsonData()
	{
		//test
		requestParam = new JRequestRegisterImage();
		requestParam.appid = "appid";
		requestParam.cmd = "mlist";
		requestParam.devtype = "1";
		requestParam.userid = "test";
		
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

	@Override
	void OnReceiveJsonString(String result)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		JReceiveRegisterImage receiveCategoryInfo = gson.fromJson(result, JReceiveRegisterImage.class);
		
		//DementorUtil.classToLog(receiveCategoryInfo);
		
		// 아이콘 ZIP 다운로드
		String categoryZipUrl = receiveCategoryInfo.data.categoryzip;
		
		
	}
	
	private class ZipFileDownloadWithExtractTask extends AsyncTask<String, Void, HashMap<String, Bitmap>>
	{
		@Override
		protected HashMap<String, Bitmap> doInBackground(String... params)
		{
			String serverPath = params[0];

			File file = null;

			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;

			try
			{
				HttpGet httpget = new HttpGet(serverPath);

				HttpClient httpclient = getHttpClient();

				HttpResponse response = httpclient.execute(httpget);

				HttpEntity entity = response.getEntity();

				file = File.createTempFile("prefix", "extension");

				bis = new BufferedInputStream(entity.getContent());
				bos = new BufferedOutputStream(new FileOutputStream(file));

				int inByte;
				while ((inByte = bis.read()) != -1)
				{
					bos.write(inByte);
				}
			}
			catch (MalformedURLException e)
			{
				LogTrace.e(e.toString());
				// this.failure("202200", null);
			}
			catch (IOException e)
			{
				LogTrace.e(e.toString());
				// this.failure("202200", null);
			}
			finally
			{
				try
				{
					bis.close();
					bos.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
			return unZip(file);
		}
		
		private HashMap<String, Bitmap> unZip(File file)
		{
			ZipInputStream zin = null;
			HashMap<String, Bitmap> map = new HashMap<String, Bitmap>();

			try
			{
				zin = new ZipInputStream(new FileInputStream(file));

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;

				ZipEntry zipEntry;
				while ((zipEntry = zin.getNextEntry()) != null)
				{
					map.put(zipEntry.getName(), BitmapFactory.decodeStream(zin, null, options));
					zin.closeEntry();
				}
			}
			catch (Exception e)
			{
				LogTrace.e(e.toString());
				// this.failed("202400", null);
			}
			finally
			{
				if (zin != null) try
				{
					zin.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
					// this.failed("202400", null);
				}
				if (file != null && file.exists()) file.delete();
			}

			return map;
		}
	}

}
