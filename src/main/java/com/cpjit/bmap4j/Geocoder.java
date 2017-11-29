/*
 * Copyright 2011-2017 CPJIT Group.
 * 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.cpjit.bmap4j;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;

/**
 * 地理编码器。
 * 
 * @since 1.0.0
 * @author yonghuan
 */
public class Geocoder {

	private final static String OUTPUT_JSON = "json";
	private String ak;
	private String sk;
	
	
	public Geocoder(String ak, String sk) {
		this.ak = ak;
		this.sk = sk;
	}

	/**
	 * 地理编码。
	 */
	public LngLat addressToLocation(String address) throws Bmap4jException {
		// http://api.map.baidu.com/geocoder/v2/?address=北京市海淀区上地十街10号&output=json&ak=您的ak&callback=showLocation
		// //GET请求
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("address", address);
		params.put("ret_coordtype", "bd09ll");
		params.put("output", OUTPUT_JSON);
		params.put("ak", ak);
		JSONReader reader = null;
		try {
			HttpClient client = HttpClientBuilder.create().build();
			StringBuilder url = new StringBuilder("http://api.map.baidu.com/geocoder/v2/?")
										.append(toQueryString(params))
										.append("&sn=").append(generateSn(params));
			HttpUriRequest request = new HttpGet(url.toString());
			HttpResponse response = client.execute(request);
			InputStream is = response.getEntity().getContent();
			reader = new JSONReader(new InputStreamReader(is, "UTF-8"));
		} catch (Exception e) {
			if(reader != null) {
				reader.close();
			}
			throw new Bmap4jException("地理编码出错", e);
		} 
		JSONObject json = (JSONObject) reader.readObject();
		int status = json.getIntValue("status");
		if(status != 0) {
			String msg = json.getString("message");
			if(msg == null) {
				msg = "地理编码出错";
			}
			reader.close();
			throw new Bmap4jException(msg+", status="+status);
		}
		JSONObject result = json.getJSONObject("result");
		LngLat  location = result!=null ? result.getObject("location", LngLat.class) : null;
		reader.close();
		return location;
	}

	/**
	 * 逆地理编码。
	 */
	public Address locationToAddress(LngLat location) throws Bmap4jException {
		// http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location=39.934,116.329&output=json&pois=1&ak=您的ak
		Map<String, String> params = new LinkedHashMap<String, String>();
		String loc =  location.getLat() + "," + location.getLng();
		params.put("location", loc);
		params.put("coordtype", "bd09ll");
		params.put("ret_coordtype", "bd09ll");
		params.put("extensions_poi", "null");
		params.put("output", OUTPUT_JSON);
		params.put("ak", ak);
		
		JSONReader reader = null;
		try {
			HttpClient client = HttpClientBuilder.create().build();
			StringBuilder url = new StringBuilder("http://api.map.baidu.com/geocoder/v2/?")
										.append(toQueryString(params))
										.append("&sn=").append(generateSn(params));
			HttpUriRequest request = new HttpGet(url.toString());
			HttpResponse response = client.execute(request);
			InputStream is = response.getEntity().getContent();
			reader = new JSONReader(new InputStreamReader(is, "UTF-8"));
		} catch (Exception e) {
			if(reader != null) {
				reader.close();
			}
			throw new Bmap4jException("地理编码出错", e);
		} 
		JSONObject json = (JSONObject) reader.readObject();
		int status = json.getIntValue("status");
		if(status != 0) {
			String msg = json.getString("message");
			if(msg == null) {
				msg = "地理编码出错";
			}
			reader.close();
			throw new Bmap4jException(msg+", status="+status);
		}
		JSONObject result = json.getJSONObject("result");
		
		Address address = result!=null?result.getObject("addressComponent", Address.class) : null;
		reader.close();
		return address;
	}

	private String generateSn(Map<String, String> params) throws UnsupportedEncodingException {
		// 调用下面的toQueryString方法，对LinkedHashMap内所有value作utf8编码，拼接返回结果address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourak
		String paramsStr = toQueryString(params);
		// 对paramsStr前面拼接上/geocoder/v2/?，后面直接拼接yoursk得到/geocoder/v2/?address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourakyoursk
		String wholeStr = new String("/geocoder/v2/?" + paramsStr + sk);
		// 对上面wholeStr再作utf8编码
		String tempStr = URLEncoder.encode(wholeStr, "UTF-8");
		// 调用下面的MD5方法得到最后的sn签名7de5a22212ffaa9e326444c75a58f9a0
		return MD5(tempStr);
	}
	
	// 对Map内所有value作utf8编码，拼接返回结果
	private  String toQueryString(Map<?, ?> data) throws UnsupportedEncodingException {
		StringBuffer queryString = new StringBuffer();
		for (java.util.Map.Entry<?, ?> pair : data.entrySet()) {
			queryString.append(pair.getKey() + "=");
			queryString.append(URLEncoder.encode((String) pair.getValue(), "UTF-8") + "&");
		}
		if (queryString.length() > 0) {
			queryString.deleteCharAt(queryString.length() - 1);
		}
		return queryString.toString();
	}

	// 来自stackoverflow的MD5计算方法，调用了MessageDigest库函数，并把byte数组结果转换成16进制
	private String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}

}
