package moe.feng.oechan.support;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import moe.feng.oechan.model.BaseMessage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {

	private static OkHttpClient client = new OkHttpClient.Builder()
			.connectTimeout(10, TimeUnit.SECONDS)
			.readTimeout(10, TimeUnit.SECONDS)
			.build();

	private static final String UA_ANOTHERECHAN = "AnotherEChan (Android) 1.x";

	private static final String TAG = HttpUtils.class.getSimpleName();

	public static BaseMessage<String> getString(String url, String ua) {
		BaseMessage<String> result = new BaseMessage<>();

		Request request = new Request.Builder().url(url).addHeader("User-Agent", ua).build();
		try {
			Response response = client.newCall(request).execute();
			result.setCode(response.code());
			result.setData(response.body().string());
			Log.i(TAG, result.getData());
		} catch (IOException e) {
			result.setCode(BaseMessage.CODE_ERROR);
			e.printStackTrace();
		}

		return result;
	}

	public static BaseMessage<String> getString(String url, boolean useLocalUA) {
		return getString(
				url,
				useLocalUA ? System.getProperty("http.agent") : UA_ANOTHERECHAN
		);
	}

}
