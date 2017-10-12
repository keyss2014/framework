package cn.keyss.common.utilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

/**
 * <br>
 *
 * @author 李恒
 * @version 1.0
 * @date: 2016/9/22 0022 下午 3:19
 */
public class HttpUtil {
	/**
	 * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public final static String getIpAddress(HttpServletRequest request) throws IOException {
		/* 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址 */

		String ip = request.getHeader("X-Forwarded-For");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");

			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");

			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");

			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();

			}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = (String) ips[index];
				if (!("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;
	}

	public static String getLocalIp() {

		InetAddress addr = null;
		String ip = null;
		try {
			addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress();
		} catch (UnknownHostException e) {
		}
		return ip;
	}

	public static String getQueryString(HttpServletRequest request)
			throws UnsupportedEncodingException, URISyntaxException {

		String encoding = "UTF-8";
		String currentQueryString = request.getQueryString();
		if (currentQueryString == null || currentQueryString.equals("")) {
			return "";
		}

		String rebuiltQueryString = "";
		for (String keyPair : currentQueryString.split("&")) {
			if (rebuiltQueryString.length() > 0) {
				rebuiltQueryString = rebuiltQueryString + "&";
			}

			if (keyPair.contains("=")) {
				String[] params = keyPair.split("=", 2);
				String name = params[0];
				String value = params[1];
				value = URLDecoder.decode(value, encoding);
				value = new URI(null, null, null, value, null).toString().substring(1);
				value = value.replaceAll("&", "%26");
				rebuiltQueryString = rebuiltQueryString + name + "=" + value;
			} else {
				String value = URLDecoder.decode(keyPair, encoding);
				value = new URI(null, null, null, value, null).toString().substring(1);
				rebuiltQueryString = rebuiltQueryString + value;
			}
		}
		return "?" + rebuiltQueryString;
	}
}