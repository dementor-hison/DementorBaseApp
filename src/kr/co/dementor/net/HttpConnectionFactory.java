package kr.co.dementor.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import kr.co.dementor.common.LogTrace;
import android.os.Build;

public class HttpConnectionFactory
{
	static public HttpURLConnection getHttpURLConnection(URL url) throws IOException
	{
		// 프로요 이전의 안드로이드에서 HttpURLConnection을 사용할 경우 readable input stream에서
		// close()를
		// 호출하면 전체 connection pool에 쓰레기값이 들어갈 수도 있는 현상이 발생하므로, 아래와 같이
		// connection pooling을 off 해주어야한다.
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO)
		{
			System.setProperty("http.keepAlive", "false");
		}

		HttpURLConnection http = null;

		try
		{
			if (url.toURI().getScheme().equalsIgnoreCase("https"))
			{
				trustAllHosts();
				HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
				https.setHostnameVerifier(DO_NOT_VERIFY);
				http = https;
			}
			else
			{
				http = (HttpURLConnection) url.openConnection();
			}
		}
		catch (URISyntaxException e)
		{
			LogTrace.e(e.toString());
		}

		return http;
	}

	/**
	 * Trust every server - dont check for any certificate
	 */
	private static void trustAllHosts()
	{
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
		{
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s)
					throws java.security.cert.CertificateException
			{
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s)
					throws java.security.cert.CertificateException
			{
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers()
			{
				return new java.security.cert.X509Certificate[] {};
			}
		} };

		// Install the all-trusting trust manager
		try
		{
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		}
		catch (Exception e)
		{
			LogTrace.e(e.toString());
		}
	}

	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier()
	{
		@Override
		public boolean verify(String hostname, SSLSession session)
		{
			return true;
		}
	};
}
