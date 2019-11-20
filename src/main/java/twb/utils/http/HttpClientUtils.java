package twb.utils.http;

import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {
	static CloseableHttpClient httpClient = HttpClients.createDefault();

	public static byte[] urlRequest(String endPoint, String requestStr, String sendMethod, String enCode, Map headMap) {
		if (enCode == null || enCode.isEmpty()) {
			enCode = "gbk";
		}
		HttpRequestBase request = null;
		try {

			if (sendMethod != null && sendMethod.length() > 1) {
				String sendMethodName = Character.toUpperCase(sendMethod.charAt(0))
						+ sendMethod.substring(1).toLowerCase();
				String className = "org.apache.http.client.methods.Http" + sendMethodName;
				request = (HttpRequestBase) Class.forName(className).newInstance();
				request.setURI(URI.create(endPoint));
			} else {
				request = new HttpPost(endPoint);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("服务请求类型出错：" + e.getMessage());
		}

		if (headMap != null && !headMap.isEmpty()) {
			Set<Map.Entry<String, String>> set = headMap.entrySet();
			Iterator<Entry<String, String>> it = set.iterator();
			while (it.hasNext()) {
				Entry<String, String> mapitem = it.next();
				if (!StringUtils.isEmpty(mapitem.getValue())) {
					request.addHeader(mapitem.getKey(), mapitem.getValue());
				}
			}
		}

		HttpResponse response = null;

		int timeOut = 60;
		// 设置超时
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeOut * 1000)
				.setConnectTimeout(60 * 1000).setConnectionRequestTimeout(timeOut * 1000)
				.setExpectContinueEnabled(false).build();
		request.setConfig(requestConfig);

		try {

			String contentType = "application/json;charset=" + enCode.toUpperCase();
			if (request instanceof HttpEntityEnclosingRequestBase) {
				HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase = (org.apache.http.client.methods.HttpEntityEnclosingRequestBase) request;
				StringEntity entity = new StringEntity(requestStr, enCode);
				entity.setContentType(contentType);
				httpEntityEnclosingRequestBase.setEntity(entity);
			}

			response = httpClient.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				byte[] result = EntityUtils.toByteArray(response.getEntity());
				return result;
				// HttpEntity httpEntity = response.getEntity();
				// return httpEntity;
			} else {
				throw new RuntimeException("服务请求出错：" + statusCode);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("服务请求出错：" + e.getMessage());
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());// 自动释放连接
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("释放连接失败：" + e.getMessage());
				}
			}
		}
	}

	public static byte[] urlRequest(String endPoint, String requestStr, String sendMethod, String enCode) {
		return urlRequest(endPoint, requestStr, sendMethod, enCode, null);
	}

	public static String urlRequestStr(String endPoint, String requestStr, String sendMethod, String enCode,
			Map headMap) {

		if (enCode == null || enCode.isEmpty()) {
			enCode = "gbk";
		}
		HttpRequestBase request = null;
		try {

			if (sendMethod != null && sendMethod.length() > 1) {
				String sendMethodName = Character.toUpperCase(sendMethod.charAt(0))
						+ sendMethod.substring(1).toLowerCase();
				String className = "org.apache.http.client.methods.Http" + sendMethodName;
				request = (HttpRequestBase) Class.forName(className).newInstance();
				request.setURI(URI.create(endPoint));
			} else {
				request = new HttpPost(endPoint);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("服务请求类型出错：" + e.getMessage());
		}

		if (headMap != null && !headMap.isEmpty()) {
			Set<Map.Entry<String, String>> set = headMap.entrySet();
			Iterator<Entry<String, String>> it = set.iterator();
			while (it.hasNext()) {
				Entry<String, String> mapitem = it.next();
				if (!StringUtils.isEmpty(mapitem.getValue())) {
					request.addHeader(mapitem.getKey(), mapitem.getValue());
				}
			}
		}

		HttpResponse response = null;

		int timeOut = 60;
		// 设置超时
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeOut * 1000)
				.setConnectTimeout(60 * 1000).setConnectionRequestTimeout(timeOut * 1000)
				.setExpectContinueEnabled(false).build();
		request.setConfig(requestConfig);

		try {

			String contentType = "application/json;charset=" + enCode.toUpperCase();
			if (request instanceof HttpEntityEnclosingRequestBase) {
				HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase = (org.apache.http.client.methods.HttpEntityEnclosingRequestBase) request;
				StringEntity entity = new StringEntity(requestStr, enCode);
				entity.setContentType(contentType);
				httpEntityEnclosingRequestBase.setEntity(entity);
			}

			response = httpClient.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			if (200 != statusCode) {
				throw new RuntimeException("服务请求出错：" + statusCode);
			} else {
				byte[] result = EntityUtils.toByteArray(response.getEntity());
				return new String(result, "utf-8");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("服务请求出错：" + e.getMessage());
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());// 自动释放连接
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("释放连接失败：" + e.getMessage());
				}
			}
		}

	}

}
