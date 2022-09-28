package gq.glowman554.crawler.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpClient
{
	public static String get(String _url, Map<String, String> headers) throws IOException
	{
		OkHttpClient client = new OkHttpClient();

		var req = new Request.Builder();

		req.url(_url);

		req.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

		for (String key : headers.keySet())
		{
			req.addHeader(key, headers.get(key));
		}

		Response res = client.newCall(req.build()).execute();

		assert res.body() != null;
		return res.body().string();

	}

	public static String get(String _url) throws IOException
	{
		return get(_url, new HashMap<>());
	}
}
