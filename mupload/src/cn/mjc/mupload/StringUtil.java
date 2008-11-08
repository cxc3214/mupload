package cn.mjc.mupload;

/**
 * 字符串实用方法类。
 * 
 * @author 马必强
 * 
 */
public class StringUtil {
	/**
	 * 返回一个对象的字符串，多数是处理字符串的
	 */
	public static String trim(Object obj) {
		return obj == null ? "" : String.valueOf(obj).trim();
	}

	/**
	 * 对一字符串数组进行去空格操作
	 */
	public final static String[] trim(String[] aStr) {
		if (aStr == null)
			return null;
		for (int i = 0; i < aStr.length; i++) {
			aStr[i] = trim(aStr[i]);
		}
		return aStr;
	}

	/**
	 * 数字字符串的长整型转换
	 * 
	 * @param str
	 *            数字字符串
	 * @param defaultVal
	 *            默认值
	 * @return 转换后的结果
	 */
	public final static long parseLong(String str, long defaultVal) {
		try {
			return Long.parseLong(str);
		} catch (NumberFormatException ex) {
			return defaultVal;
		}
	}

	/**
	 * 检查指定的字符串数组中是否包含了指定的字符串，不区分大小写.
	 * 
	 * @param source
	 * @param test
	 * @return
	 */
	public final static boolean includeIgnoreCase(String[] source, String test) {
		if (source == null || source.length < 1)
			return false;
		for (String tmp : source) {
			if (tmp == null && test == null)
				return true;
			if (tmp != null && tmp.equalsIgnoreCase(test))
				return true;
		}
		return false;
	}
}
