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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;

abstract public class AbstractClient
{
	OnCompleteListener completeListener = null;

	private DementorJsonTask jsonDataTask = null;

	abstract void main() throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, KeyManagementException, UnrecoverableKeyException,
			ClientProtocolException, KeyStoreException, CertificateException, DementorException;

	abstract String makeJsonData();

	public void start()
	{
		jsonDataTask = new DementorJsonTask();

		if (Build.VERSION.SDK_INT >= 11)
		{
			jsonDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, makeJsonData(), Defines.RequestAction.ACTION_LIST.getUrl());
		}
		else
		{
			jsonDataTask.execute(makeJsonData(), Defines.RequestAction.ACTION_LIST.getUrl());
		}
	}

	/**
	 * @return the receiveDementorListener
	 */
	public OnCompleteListener getOnCompleteListener()
	{
		return completeListener;
	}

	/**
	 * @param receiveDementorListener
	 *            the receiveDementorListener to set
	 */
	public void setOnCompleteListener(OnCompleteListener completeListener)
	{
		this.completeListener = completeListener;
	}

	public static interface OnCompleteListener
	{
		public void onComplete(Object result);

		public void onError(String errorCode, String extra, String message);
	}

	HashMap<String, Bitmap> getFileFromServer(String serverPath) throws KeyManagementException, UnrecoverableKeyException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException
	{
		File file = null;

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try
		{
			HttpGet httpget = new HttpGet(serverPath);

			HttpClient httpclient = getHttpClient();

			HttpResponse response = httpclient.execute(httpget);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK)
			{
				LogTrace.w("ERROR connect!!! status code: " + statusCode);
			}

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

	String getDataFromServer(String action) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, KeyManagementException,
			UnrecoverableKeyException, ClientProtocolException, KeyStoreException, CertificateException, DementorException
	{
		String originData = makeJsonData();

		String url = Defines.DEFAULT_HOST + "/" + action;
		LogTrace.d("connect to " + url);

		String encodeData = encodeData(originData);

		if (encodeData == null)
		{
			throw new DementorException("11111", null, "Encoding Fail");
		}
		
		HttpResponse response = sendPost(url, encodeData);

		HttpEntity resEntity = response.getEntity();

		String receivedEncodeData = null;

		try
		{
			receivedEncodeData = EntityUtils.toString(resEntity);
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
		
		if (receiveData == null)
		{
			throw new DementorException("22222", null, "Decoding Fail");
		}

		return receiveData;
	}

	private class DementorJsonTask extends AsyncTask<String, Void, Void>
	{
		@Override
		protected Void doInBackground(String... params)
		{
			try
			{
				main();
			}
			// encode, decodeException START
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
			// encode, decodeException END

			catch (KeyManagementException e)
			{
				e.printStackTrace();
			}
			catch (UnrecoverableKeyException e)
			{
				e.printStackTrace();
			}
			catch (ClientProtocolException e)
			{
				e.printStackTrace();
			}
			catch (KeyStoreException e)
			{
				e.printStackTrace();
			}
			catch (CertificateException e)
			{
				e.printStackTrace();
			}
			catch (DementorException e)
			{
				e.printStackTrace();
				LogTrace.e("Error code : " + e.getErrorCode() + " , extra : " + e.getExtraInfo() + " , message : " + e.getMessage());
			}

			return null;
		}
	}

	private HttpResponse sendPost(String url, String encodeData) throws UnsupportedEncodingException, ClientProtocolException, DementorException,
			KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException
	{
		ArrayList<NameValuePair> jsonDatas = new ArrayList<NameValuePair>();
		jsonDatas.add(new BasicNameValuePair("data", encodeData));

		HttpClient httpclient = getHttpClient();

		HttpPost post = new HttpPost(url);

		UrlEncodedFormEntity ent = null;

		ent = new UrlEncodedFormEntity(jsonDatas, HTTP.UTF_8);

		post.setEntity(ent);

		HttpResponse response = null;

		try
		{
			response = httpclient.execute(post);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new DementorException("200000", "http status code : " + null, "IOException");
		}
		
		int statusCode = HttpStatus.SC_OK;
		if(response != null)
		{
			statusCode = response.getStatusLine().getStatusCode();	
		}

		if (hasError(statusCode) == true)
		{
			throw new DementorException("200000", "http status code : " + statusCode, "errormessage");
		}

		return response;
	}

	// TODO :
	private boolean hasError(int statusCode)
	{
		return statusCode == HttpStatus.SC_OK ? false : true;
	}

	private String encodeData(String jsonData) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
		return AES256Cipher.AES_Encode(jsonData);
	}

	public String decodeData(String encodeData) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
		return AES256Cipher.AES_Decode(encodeData);
	}

	HttpClient getHttpClient() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, KeyManagementException,
			UnrecoverableKeyException
	{
		KeyStore trustStore = null;

		trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

		try
		{
			trustStore.load(null, null);
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

			sf = new SFSSLSocketFactory(trustStore);

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
			}
			// 임시파일 삭제
			if (file != null && file.exists()) file.delete();
		}

		return map;
	}
}
