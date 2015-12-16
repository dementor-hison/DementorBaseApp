package kr.co.dementor.net;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.http.NameValuePair;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kr.co.dementor.common.Defines;
import kr.co.dementor.common.LogTrace;
import android.os.AsyncTask;

abstract public class AbstractClient
{
	private OnReceiveDementorListener receiveDementorListener = null;
	private DementorTask currentTask = null;

	/**
	 * @return JsonString
	 */
	abstract String makeParams();

	abstract Object main();

	public void start()
	{
		currentTask = new DementorTask();
		currentTask.execute(makeParams(), Defines.RequestAction.ACTION_LIST.getUrl());
	}

	/**
	 * @return the receiveDementorListener
	 */
	public OnReceiveDementorListener getOnReceiveDementorListener()
	{
		return receiveDementorListener;
	}

	/**
	 * @param receiveDementorListener
	 *            the receiveDementorListener to set
	 */
	public void setOnReceiveDementorListener(OnReceiveDementorListener receiveDementorListener)
	{
		this.receiveDementorListener = receiveDementorListener;
	}

	public static interface OnReceiveDementorListener
	{
		public void onRecieveDementor(AbstractClient client, JsonObject response, String err, String errorInfo);
	}

	private class DementorTask extends AsyncTask<String, Integer, JsonObject>
	{
		@Override
		protected JsonObject doInBackground(String... params)
		{
			String path = Defines.DEFAULT_HOST + "/" + params[1];
			LogTrace.d("connect to " + path);
			URL url = null;
			HttpURLConnection conn = null;
			try
			{
				url = new URL(path);
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}

			try
			{
				conn = openConnection(conn, url);

				LogTrace.d(params[0]);

				String encodeData = encodeData(params[0]);

				encodeData = "data=" + encodeData;

				// 새로운 OutputStream에 요청할 OutputStream을 넣는다.
				OutputStream out = conn.getOutputStream();

				// 그리고 write메소드로 메시지로 작성된 파라미터정보를 바이트단위로 "UTF-8"로 인코딩해서 요청한다.
				out.write(encodeData.getBytes("UTF-8"));

				// 그리고 스트림의 버퍼를 비워준다.
				out.flush();

				// 스트림을 닫는다.
				out.close();

				conn.connect();
				
				int responseCode = conn.getResponseCode();
				LogTrace.d("responseCode : " + responseCode);

				if (responseCode == HttpURLConnection.HTTP_OK)
				{
					InputStream is = conn.getInputStream();

					InputStreamReader reader = new InputStreamReader(is, "UTF-8");

					BufferedReader br = new BufferedReader(reader, conn.getContentLength());

					StringBuilder sb = new StringBuilder();

					String buf = null;
					while ((buf = br.readLine()) != null)
					{
						sb.append(buf);
					}

					br.close();

					LogTrace.d("Result : " + sb.toString());
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			return null;
		}

		private String encodeData(String jsonData)
		{
			String SHA = null;

			try
			{
				SHA = AES256Cipher.AES_Encode(jsonData);
			}
			catch (InvalidKeyException e)
			{
				e.printStackTrace();
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchPaddingException e)
			{
				e.printStackTrace();
			}
			catch (InvalidAlgorithmParameterException e)
			{
				e.printStackTrace();
			}
			catch (IllegalBlockSizeException e)
			{
				e.printStackTrace();
			}
			catch (BadPaddingException e)
			{
				e.printStackTrace();
			}
			LogTrace.d(SHA);

			return SHA;
		}

		private HttpURLConnection openConnection(HttpURLConnection conn, URL url)
		{
			try
			{
				// 해당 주소의 페이지로 접속을 하고, 단일 HTTP 접속을 하기위해 캐스트한다.
				conn = (HttpURLConnection) url.openConnection();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			conn.setConnectTimeout(Defines.DEFAULT_TIMEOUT);
			conn.setReadTimeout(Defines.DEFAULT_TIMEOUT);
			try
			{
				// POST방식으로 요청한다.( 기본값은 GET )
				conn.setRequestMethod("POST");
			}
			catch (ProtocolException e)
			{
				e.printStackTrace();
			}
			// 요청 헤더를 정의한다.( 원래 Content-Length값을 넘겨주어야하는데 넘겨주지 않아도 되는것이 이상하다. )
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setUseCaches(false);
			// InputStream으로 서버로 부터 응답 헤더와 메시지를 읽어들이겠다는 옵션을 정의한다.
			conn.setDoInput(true);
			// OutputStream으로 POST 데이터를 넘겨주겠다는 옵션을 정의한다.
			conn.setDoOutput(true);
			
			return conn;
		}

		@Override
		protected void onCancelled()
		{
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(JsonObject result)
		{
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values)
		{
			super.onProgressUpdate(values);
		}

	}

}
