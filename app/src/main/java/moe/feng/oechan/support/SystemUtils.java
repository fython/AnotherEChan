package moe.feng.oechan.support;

import java.lang.reflect.Method;

public class SystemUtils {

	public static String getSystemProperties(String key) {
		try {
			Class c = Class.forName("android.os.SystemProperties");
			Method m = c.getDeclaredMethod("get", String.class);
			m.setAccessible(true);
			return (String) m.invoke(null, key);
		} catch (Throwable e) {
			return "";
		}
	}

}
