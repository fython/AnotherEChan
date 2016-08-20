package moe.feng.oechan.support;

import com.google.gson.Gson;

public final class GsonUtils {

	private static Gson gson = new Gson();

	public static <T> T fromJson(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}

	public static String toJson(Object object) {
		return gson.toJson(object);
	}

}
