package kr.co.dementor.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
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
import kr.co.dementor.common.DementorError;
import kr.co.dementor.common.LogTrace;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
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

	private String errorCode = null;
	private String errorExtra = null;
	private String errorMessage = null;

	abstract void main();

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

		public void onError(int errorCode, String extra, String message);
	}

	HashMap<String, Bitmap> getFileFromServer(String serverPath)
	{
		HttpGet httpget = new HttpGet(serverPath);

		HttpClient httpclient = getHttpClient();

		if (httpclient == null)
		{
			LogTrace.e("httpclient create fail");
			return null;
		}

		HttpResponse response = null;
		try
		{
			response = httpclient.execute(httpget);
		}
		catch (ClientProtocolException e)
		{
			completeListener.onError(DementorError.ERROR_CONNECTION_CLIENT_PROTOCOL, e.getMessage(), "ClientProtocolException");
		}
		catch (SocketTimeoutException e)
		{
			completeListener.onError(DementorError.ERROR_CONNECTION_SOCKET_TIMEOUT, e.getMessage(), "SocketTimeoutException");
		}
		catch (ConnectTimeoutException e)
		{
			completeListener.onError(DementorError.ERROR_CONNECTION_CONNECT_TIMEOUT, e.getMessage(), "ConnectTimeoutException");
		}
		catch (NoHttpResponseException e)
		{
			completeListener.onError(DementorError.ERROR_CONNECTION_NO_HTTP_RESPONSE, e.getMessage(), "NoHttpResponseException");
		}
		catch (MalformedURLException e)
		{
			completeListener.onError(DementorError.ERROR_CONNECTION_MALFORMED_URL, e.getMessage(), "MalformedURLException");
		}
		catch (IOException e)
		{
			completeListener.onError(DementorError.ERROR_CONNECTION_UNKNOWN, e.getMessage(), "IOException");
		}
		if (response == null)
		{
			LogTrace.e("No response");
			return null;
		}

		int statusCode = response.getStatusLine().getStatusCode();

		if (hasError(statusCode))
		{
			completeListener.onError(DementorError.ERROR_STATUS_CODE_ERROR, "statusCode : " + statusCode, " plz check statusCode");
			LogTrace.e("Status code : " + statusCode);
			return null;
		}

		HttpEntity entity = response.getEntity();

		File file = null;

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try
		{
			file = File.createTempFile("prefix", "extension");
			LogTrace.d("Create temp file   path : " + file.getPath() + " , name : " + file.getName());
		}
		catch (IOException e)
		{
			completeListener.onError(DementorError.ERROR_CREATE_FILE, e.getMessage(), "IOException");
		}

		if (file == null)
		{
			LogTrace.e("create file error");
			return null;
		}

		try
		{
			bis = new BufferedInputStream(entity.getContent());
		}
		catch (IllegalStateException e)
		{
			completeListener.onError(DementorError.ERROR_CONTENTS_HAS_BEEN_CONSUMED, e.getMessage(), "IllegalStateException");
		}
		catch (IOException e)
		{
			completeListener.onError(DementorError.ERROR_GET_CONTENTS_DATA_FAIL, e.getMessage(), "IOException");
		}
		if (bis == null)
		{
			LogTrace.e("File inputStream error");
			return null;
		}
		try
		{
			bos = new BufferedOutputStream(new FileOutputStream(file));

			int inByte = 0;

			while ((inByte = bis.read()) != -1)
			{
				bos.write(inByte);
			}

			bis.close();
			bos.close();

		}
		catch (FileNotFoundException e)
		{
			completeListener.onError(DementorError.ERROR_FILE_NOT_FOUND, e.getMessage(), "FileNotFoundException");
			LogTrace.e("File not Found");
			return null;
		}
		catch (IOException e)
		{
			completeListener.onError(DementorError.ERROR_FILE_WRITE_FAIL, e.getMessage(), "IOException");
			LogTrace.e("File not Found");
			return null;
		}

		return unZip(file);
	}

	String getDataFromServer(String action)
	{
		String originData = makeJsonData();

		String url = Defines.DEFAULT_HOST + "/" + action;
		LogTrace.d("connect to " + url);

		String encodeData = encodeData(originData);

		if (encodeData == null)
		{
			LogTrace.e("Encode Fail");
			return null;
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
			LogTrace.e("Decode Fail");
			return null;
		}

		return receiveData;
	}

	private class DementorJsonTask extends AsyncTask<String, Void, Void>
	{
		@Override
		protected Void doInBackground(String... params)
		{
			main();

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
		catch (java.net.SocketException e)
		{
			e.printStackTrace();
		}
		catch (java.net.SocketTimeoutException e)
		{
			e.printStackTrace();
		}
		catch (java.net.ProtocolException e)
		{
			e.printStackTrace();
		}
		catch (org.apache.http.NoHttpResponseException e)
		{
			e.printStackTrace();
		}
		catch (org.apache.http.conn.ConnectTimeoutException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		int statusCode = HttpStatus.SC_OK;
		if (response != null)
		{
			statusCode = response.getStatusLine().getStatusCode();
		}

		if (hasError(statusCode) == true)
		{
			return null;
		}

		return response;
	}

	private boolean hasError(int statusCode)
	{
		return statusCode == HttpStatus.SC_OK ? false : true;
	}

	private String encodeData(String jsonData)
	{
		String result = null;
		try
		{
			result = AES256Cipher.AES_Encode(jsonData);
		}
		catch (InvalidKeyException e)
		{
			completeListener.onError(DementorError.ERROR_ENCODING_INVALID_KEY, e.getMessage(), "InvalidKeyException");
		}
		catch (UnsupportedEncodingException e)
		{
			completeListener.onError(DementorError.ERROR_ENCODING_UNSUPPORTED_ENCODING, e.getMessage(), "UnsupportedEncodingException");
		}
		catch (NoSuchAlgorithmException e)
		{
			completeListener.onError(DementorError.ERROR_ENCODING_NO_SUCH_ALGORITHM, e.getMessage(), "NoSuchAlgorithmException");
		}
		catch (NoSuchPaddingException e)
		{
			completeListener.onError(DementorError.ERROR_ENCODING_NO_SUCH_PADDING, e.getMessage(), "NoSuchPaddingException");
		}
		catch (InvalidAlgorithmParameterException e)
		{
			completeListener.onError(DementorError.ERROR_ENCODING_INVALID_ALGORITHM_PARAM, e.getMessage(), "InvalidAlgorithmParameterException");
		}
		catch (IllegalBlockSizeException e)
		{
			completeListener.onError(DementorError.ERROR_ENCODING_ILLEGAL_BLOCK_SIZE, e.getMessage(), "IllegalBlockSizeException");
		}
		catch (BadPaddingException e)
		{
			completeListener.onError(DementorError.ERROR_ENCODING_BAD_PADDING, e.getMessage(), "BadPaddingException");
		}
		if (result == null)
		{
			completeListener.onError(DementorError.ERROR_ENCODING_UNKONWN, null, "encoding result is null");
		}

		return result;
	}

	public String decodeData(String encodeData)
	{
		String result = null;
		try
		{
			result = AES256Cipher.AES_Decode(encodeData);
		}
		catch (InvalidKeyException e)
		{
			completeListener.onError(DementorError.ERROR_DECODING_INVALID_KEY, e.getMessage(), "InvalidKeyException");
		}
		catch (UnsupportedEncodingException e)
		{
			completeListener.onError(DementorError.ERROR_DECODING_UNSUPPORTED_ENCODING, e.getMessage(), "UnsupportedEncodingException");
		}
		catch (NoSuchAlgorithmException e)
		{
			completeListener.onError(DementorError.ERROR_DECODING_NO_SUCH_ALGORITHM, e.getMessage(), "NoSuchAlgorithmException");
		}
		catch (NoSuchPaddingException e)
		{
			completeListener.onError(DementorError.ERROR_DECODING_NO_SUCH_PADDING, e.getMessage(), "NoSuchPaddingException");
		}
		catch (InvalidAlgorithmParameterException e)
		{
			completeListener.onError(DementorError.ERROR_DECODING_INVALID_ALGORITHM_PARAM, e.getMessage(), "InvalidAlgorithmParameterException");
		}
		catch (IllegalBlockSizeException e)
		{
			completeListener.onError(DementorError.ERROR_DECODING_ILLEGAL_BLOCK_SIZE, e.getMessage(), "IllegalBlockSizeException");
		}
		catch (BadPaddingException e)
		{
			completeListener.onError(DementorError.ERROR_DECODING_BAD_PADDING, e.getMessage(), "BadPaddingException");
		}
		if (result == null)
		{
			completeListener.onError(DementorError.ERROR_DECODING_UNKONWN, null, "encoding result is null");
		}
		return result;
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
			completeListener.onError(DementorError.ERROR_KEYSTORE_INSTANSE, e.getMessage(), "KeyStoreException getInstanse Fail");
		}

		try
		{
			trustStore.load(null, null);
		}
		catch (NoSuchAlgorithmException e)
		{
			completeListener.onError(DementorError.ERROR_KEYSTORE_NO_SUCH_ALGORITHM, e.getMessage(), "NoSuchAlgorithmException");
		}
		catch (CertificateException e)
		{
			completeListener.onError(DementorError.ERROR_KEYSTORE_CERTIFICATION, e.getMessage(), "CertificateException");
		}
		catch (IOException e)
		{
			completeListener.onError(DementorError.ERROR_KEYSTORE_UNKNOWN, e.getMessage(), "IOException");
		}

		HttpParams params = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(params, Defines.DEFAULT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, Defines.DEFAULT_TIMEOUT);
		HttpConnectionParams.setSocketBufferSize(params, 8192);

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
				completeListener.onError(DementorError.ERROR_KEYSTORE_KEYMANAGEMENT, e.getMessage(),
						"KeyManagementException..KeyIDConflict or KeyAuthorizationFailure ..");
			}
			catch (UnrecoverableKeyException e)
			{
				completeListener.onError(DementorError.ERROR_KEYSTORE_UNRECOVERABLE_KEY, e.getMessage(), "UnrecoverableKeyException");
			}
			catch (NoSuchAlgorithmException e)
			{
				completeListener.onError(DementorError.ERROR_KEYSTORE_NO_SUCH_ALGORITHM, e.getMessage(), "NoSuchAlgorithmException");
			}
			catch (KeyStoreException e)
			{
				completeListener.onError(DementorError.ERROR_KEYSTORE_UNKNOWN, e.getMessage(), "KeyStoreException");
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
			completeListener.onError(DementorError.ERROR_UNZIP_FAIL, e.getMessage(), "UnZIP fail");
		}
		finally
		{
			if (zin != null) try
			{
				zin.close();
			}
			catch (IOException e)
			{
				completeListener.onError(DementorError.ERROR_UNZIP_FAIL, e.getMessage(), "File close Error");
			}
			// 임시파일 삭제
			if (file != null && file.exists()) file.delete();
		}

		return map;
	}
}
