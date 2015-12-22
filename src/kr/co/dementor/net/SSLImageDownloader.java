package kr.co.dementor.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import kr.co.dementor.common.LogTrace;
import android.content.Context;
import android.net.Uri;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class SSLImageDownloader extends BaseImageDownloader
{
	public static final String TAG = SSLImageDownloader.class.getName();

	public SSLImageDownloader(Context context, int connectTimeout, int readTimeout)
	{
		super(context, connectTimeout, readTimeout);
	}

	@Override
	protected HttpURLConnection createConnection(String url, Object extra) throws IOException
	{
		String encodedUrl = Uri.encode(url, ALLOWED_URI_CHARS);
		URL imageUrl = new URL(encodedUrl);

		HttpURLConnection http = null;

		// 현민 - 보안
		if (Scheme.ofUri(encodedUrl) == Scheme.HTTPS)
		{
			trustAllHosts();
			HttpsURLConnection https = (HttpsURLConnection) imageUrl.openConnection();
			https.setHostnameVerifier(DO_NOT_VERIFY);
			http = https;
			http.connect();
		}
		else
		{
			http = (HttpURLConnection) imageUrl.openConnection();
		}

		http.setConnectTimeout(connectTimeout);
		http.setReadTimeout(readTimeout);

		return http;
	}

	// always verify the host - dont check for certificate
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier()
	{
		@Override
		public boolean verify(String hostname, SSLSession session)
		{
			return true;
		}

	};

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
			LogTrace.e(e.getMessage());
		}
	}

}