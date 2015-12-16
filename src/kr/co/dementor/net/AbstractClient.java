package kr.co.dementor.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import kr.co.dementor.common.Defines;
import kr.co.dementor.common.LogTrace;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

abstract public class AbstractClient
{
	private OnReceiveDementorListener receiveDementorListener = null;
	private OnDementorErrorListener dementorErrorListener = null;
	private DementorJsonTask jsonDataTask = null;

	/**
	 * @return JsonString
	 */
	abstract void OnReceiveJsonString(String result);

	abstract String makeJsonData();

	public void start()
	{
		jsonDataTask = new DementorJsonTask();
		jsonDataTask.execute(makeJsonData(), Defines.RequestAction.ACTION_LIST.getUrl());
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
		public void onRecieveDementor(AbstractClient client, JSONObject response, String err, String errorInfo);
	}

	public static interface OnDementorErrorListener
	{
		public void onRecieveDementor(AbstractClient client, JSONObject response, String err, String errorInfo);
	}

	private class DementorJsonTask extends AsyncTask<String, Void, Void>
	{
		@Override
		protected Void doInBackground(String... params)
		{
			String originData = params[0];
			String action = params[1];

			String url = Defines.DEFAULT_HOST + "/" + action;
			LogTrace.d("connect to " + url);

			String encodeData = encodeData(originData);

			HttpResponse response = sendPost(url, encodeData);

			HttpEntity resEntity = response.getEntity();

			String receivedEncodeData = null;
			try
			{
				receivedEncodeData = EntityUtils.toString(resEntity);
				LogTrace.d("receivedEncodeData : " + receivedEncodeData);
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			String receiveData = decodeData(receivedEncodeData);
			LogTrace.d("receiveData : " + receiveData);

			OnReceiveJsonString(receiveData);
			// String extractData = extractJsonData(receiveData);
			// LogTrace.d("extractData : " + receiveData);

			return null;
		}
	}

	private HttpResponse sendPost(String url, String encodeData)
	{
		ArrayList<NameValuePair> jsonDatas = new ArrayList<NameValuePair>();
		jsonDatas.add(new BasicNameValuePair("data", encodeData));

		HttpClient httpclient = getHttpClient();

		HttpPost post = new HttpPost(url);

		UrlEncodedFormEntity ent = null;
		try
		{
			ent = new UrlEncodedFormEntity(jsonDatas, HTTP.UTF_8);
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		post.setEntity(ent);

		HttpResponse response = null;

		try
		{
			response = httpclient.execute(post);
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		int statusCode = response.getStatusLine().getStatusCode();
		
		if(hasError(statusCode) == true)
		{
			
			return null;
		}
		
		return response;
	}

	private boolean hasError(int statusCode)
	{
		switch (statusCode)
		{
		case HttpStatus.SC_HTTP_VERSION_NOT_SUPPORTED:
			
			break;

		default:
			break;
		}
		return false;
	}

	private String encodeData(String jsonData)
	{
		String sha = null;

		try
		{
			sha = AES256Cipher.AES_Encode(jsonData);
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
		LogTrace.d(sha);

		return sha;
	}

	public String decodeData(String encodeData)
	{
		String decodeData = null;
		try
		{
			decodeData = AES256Cipher.AES_Decode(encodeData);
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

		return decodeData;
	}

	HttpClient getHttpClient()
	{
		KeyStore trustStore = null;
		try
		{
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		}
		catch (KeyStoreException e)
		{
			e.printStackTrace();
		}
		try
		{
			trustStore.load(null, null);
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch (CertificateException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		HttpParams params = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(params, 15 * 1000);
		HttpConnectionParams.setSoTimeout(params, 15 * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		// HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		// HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

		SchemeRegistry registry = new SchemeRegistry();
		if (Defines.DEFAULT_HOST.startsWith("https"))
		{
			SSLSocketFactory sf = null;
			try
			{
				sf = new SFSSLSocketFactory(trustStore);
			}
			catch (KeyManagementException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			}
			catch (KeyStoreException e)
			{
				e.printStackTrace();
			}
			catch (UnrecoverableKeyException e)
			{
				e.printStackTrace();
			}

			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			registry.register(new Scheme("https", sf, Defines.DEFAULT_PORT));
		}
		else
		{
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), Defines.DEFAULT_PORT));
		}

		ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

		return new DefaultHttpClient(ccm, params);
	}
}
