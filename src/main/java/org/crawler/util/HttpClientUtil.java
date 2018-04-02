/**
 * 
 */
package org.crawler.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.hadoop.yarn.util.StringHelper;
import org.springframework.util.StringUtils;

import sun.net.www.protocol.https.Handler;

/**
 * @author panye
 * 
 */
public class HttpClientUtil {
	//private static final Logger log = Logger.getLogger(HttpClientUtil.class);
public static void main(String[] args) throws Exception {
	Properties properties = System.getProperties();
	properties.setProperty("http.proxyHost", "127.0.0.1");
	properties.setProperty("http.proxyPort", "1080");
	properties.setProperty("https.proxyHost", "127.0.0.1");
	properties.setProperty("https.proxyPort", "1080");
	String urlString="https://www.facebook.com/Kobe.Thomas/friends?lst=100025261115546%3A602278835%3A1522671544&source_ref=pb_friends_tl";
	System.out.println(doGetHttps(urlString));
}
	/**
	 * http get请求
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String url) throws Exception {
		URL localURL = new URL(url);
		URLConnection connection = localURL.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

		httpURLConnection.setRequestProperty("Accept-Charset", "GBK");
//		httpURLConnection.setRequestProperty("Content-Type",
//				"application/x-www-form-urlencoded");

		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;
		httpURLConnection.setInstanceFollowRedirects(true);
		if (httpURLConnection.getResponseCode() >= 300) {
			String location;
			System.out.println(location=httpURLConnection.getHeaderField("location"));
			httpURLConnection.disconnect();
			return doGet(location);
//			throw new Exception(
//					"HTTP Request is not success, Response code is "
//							+ httpURLConnection.getResponseCode());
		}

		try {
			inputStream = httpURLConnection.getInputStream();
			// inputStreamReader = new InputStreamReader(inputStream);
			//String charset = httpURLConnection.getContentType().split("=")[1];
			inputStreamReader = new InputStreamReader(inputStream,"utf-8");

			reader = new BufferedReader(inputStreamReader);

			while ((tempLine = reader.readLine()) != null) {
				resultBuffer.append(tempLine+"\n");
			}

		} finally {
			if (reader != null) {
				reader.close();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return resultBuffer.toString();
	}

	/**
	 * http post请求
	 * 
	 * @param url
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, String data) throws Exception {
		String parameterData = data;

		URL localURL = new URL(url);
		URLConnection connection = localURL.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

		httpURLConnection.setDoOutput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
		httpURLConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		httpURLConnection.setRequestProperty("Content-Length",
				String.valueOf(parameterData.length()));

		OutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;

		try {
			outputStream = httpURLConnection.getOutputStream();
			outputStreamWriter = new OutputStreamWriter(outputStream, "utf-8");

			outputStreamWriter.write(parameterData.toString());
			outputStreamWriter.flush();

			if (httpURLConnection.getResponseCode() > 200) {
				inputStream = httpURLConnection.getErrorStream();
			}else {
				inputStream = httpURLConnection.getInputStream();
			}

			
			inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			reader = new BufferedReader(inputStreamReader);

			while ((tempLine = reader.readLine()) != null) {
				resultBuffer.append(tempLine);
			}

		} finally {
			if (outputStreamWriter != null) {
				outputStreamWriter.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
			if (reader != null) {
				reader.close();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return resultBuffer.toString();
	}

	/**
	 * https get请求
	 * 
	 * @param url
	 * @param ctype
	 * @param connectTimeout
	 * @param readTimeout
	 * @return
	 * @throws Exception
	 */
	public static String doGetHttps(String url, String ctype,
			int connectTimeout, int readTimeout) throws Exception {
		HttpsURLConnection conn = null;
		String rsp = null;
		try {
			try {
				SSLContext ctx = SSLContext.getInstance("SSL");
				ctx.init(new KeyManager[0],
						new TrustManager[] { new DefaultTrustManager() },
						new SecureRandom());
				SSLContext.setDefault(ctx);

				conn = getConnection(new URL(null, url, new Handler()), "GET",
						ctype);
				conn.setHostnameVerifier(new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				});
				conn.setConnectTimeout(connectTimeout);
				conn.setReadTimeout(readTimeout);
			} catch (Exception e) {
				//log.error("doGet GET_CONNECTOIN_ERROR, URL = " + url, e);
				throw e;
			}
			try {
				rsp = getResponseAsString(conn);
			} catch (IOException e) {
				//log.error("doGet REQUEST_RESPONSE_ERROR, URL = " + url, e);
				throw e;
			}

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return rsp;
	}

	/**
	 * https get
	 * 
	 * @param url
	 * @return
	 */
	public static String doGetHttps(String url) throws Exception {
		HttpsURLConnection conn = null;
		String rsp = null;
		try {
			try {
				SSLContext ctx = SSLContext.getInstance("SSL");
				ctx.init(new KeyManager[0],
						new TrustManager[] { new DefaultTrustManager() },
						new SecureRandom());
				SSLContext.setDefault(ctx);

				conn = getConnection(new URL(null, url, new Handler()), "GET",
						null);
				conn.setHostnameVerifier(new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				});
				// conn.setConnectTimeout(connectTimeout);
				// conn.setReadTimeout(readTimeout);
			} catch (Exception e) {
				//log.error("doGet GET_CONNECTOIN_ERROR, URL = " + url, e);
				throw e;
			}
			try {
				rsp = getResponseAsString(conn);
			} catch (IOException e) {
				//log.error("doGet REQUEST_RESPONSE_ERROR, URL = " + url, e);
				throw e;
			}

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return rsp;
	}

	/**
	 * https post
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @param connectTimeout
	 * @param readTimeout
	 * @param getLocation
	 * @return
	 * @throws Exception
	 */
	public static String doPostHttps(String url, String params, String charset,
	/* int connectTimeout, int readTimeout, */boolean getLocation)
			throws Exception {
		String ctype = "text/html;charset=" + charset;
		byte[] content = {};
		if (params != null) {
			content = params.getBytes(charset);
		}
		return doPostHttps(url, ctype, content, /* connectTimeout, readTimeout, */
				getLocation);
	}

	/**
	 * https post
	 * 
	 * @param url
	 * @param ctype
	 * @param content
	 * @param connectTimeout
	 * @param readTimeout
	 * @param getLocation
	 * @return
	 * @throws Exception
	 */
	public static String doPostHttps(String url, String ctype, byte[] content,
	/* int connectTimeout, int readTimeout, */boolean getLocation)
			throws Exception {
		HttpsURLConnection conn = null;
		OutputStream out = null;
		String rsp = null;
		try {
			try {
				SSLContext ctx = SSLContext.getInstance("SSL");
				ctx.init(new KeyManager[0],
						new TrustManager[] { new DefaultTrustManager() },
						new SecureRandom());
				SSLContext.setDefault(ctx);

				conn = getConnection(new URL(null, url, new Handler()), "POST",
						ctype);
				conn.setRequestMethod("POST");
				conn.setHostnameVerifier(new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				});
				// conn.setConnectTimeout(connectTimeout);
				// conn.setReadTimeout(readTimeout);
			} catch (Exception e) {
				//log.error("doPost GET_CONNECTOIN_ERROR, URL = " + url, e);
				throw e;
			}
			try {
				out = conn.getOutputStream();
				out.write(content);
				out.flush();
				rsp = getResponseAsString(conn);
				if (rsp != null) {
					if (getLocation) {
						String location = conn.getHeaderField("Location");
						if (location != null) {
							return location.substring(
									location.lastIndexOf("/") + 1,
									location.length());
						}
					}
				}
			} catch (IOException e) {
				//log.error("doPost REQUEST_RESPONSE_ERROR, URL = " + url, e);
				throw e;
			}

		} finally {
			if (out != null) {
				out.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}

		return rsp;
	}

	// ========================private=============================
	private static class DefaultTrustManager implements X509TrustManager {
		public void checkClientTrusted(
				java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

	}

	private static javax.net.ssl.HttpsURLConnection getConnection(
			java.net.URL JNurl, String method, String ctype) throws IOException {
		javax.net.ssl.HttpsURLConnection conn = (javax.net.ssl.HttpsURLConnection) JNurl
				.openConnection();
		conn.setRequestMethod(method);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		// conn.setRequestProperty("Accept", "text/plain");
		// conn.setRequestProperty("User-Agent", "stargate");
		// conn.setRequestProperty("Content-Type", ctype);
		return conn;
	}

	protected static String getResponseAsString(HttpsURLConnection conn)
			throws IOException {
		String charset = getResponseCharset(conn.getContentType());
		InputStream es = conn.getErrorStream();
		if (es == null) {
			return getStreamAsString(conn.getInputStream(), charset);
		} else {
			String msg = getStreamAsString(es, charset);
			if (StringUtils.isEmpty(msg)) {
				throw new IOException(conn.getResponseCode() + ":"
						+ conn.getResponseMessage());
			} else {
				throw new IOException(msg);
			}
		}
	}

	private static String getStreamAsString(InputStream stream, String charset)
			throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stream, charset));
			StringWriter writer = new StringWriter();

			char[] chars = new char[256];
			int count = 0;
			while ((count = reader.read(chars)) > 0) {
				writer.write(chars, 0, count);
			}

			return writer.toString();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	private static String getResponseCharset(String ctype) {
		String charset = "utf-8";

		if (!StringUtils.isEmpty(ctype)) {
			String[] params = ctype.split(";");
			for (String param : params) {
				param = param.trim();
				if (param.startsWith("charset")) {
					String[] pair = param.split("=", 2);
					if (pair.length == 2) {
						if (!StringUtils.isEmpty(pair[1])) {
							charset = pair[1].trim();
						}
					}
					break;
				}
			}
		}

		return charset;
	}
	
	public static String serailize(Map<String,Object> map){
		List list = new ArrayList();
		for(Map.Entry<String,Object> entry:map.entrySet()){
			try {
				list.add(entry.getKey()+"="+URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
			}
		}
		return StringHelper.join(list, "&");
	}
}
