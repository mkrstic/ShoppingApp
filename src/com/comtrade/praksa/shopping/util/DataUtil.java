package com.comtrade.praksa.shopping.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * The Class DataUtil - singleton.
 */
public class DataUtil {

	/** The instance. */
	private static DataUtil instance;

	/**
	 * Gets the single instance of DataUtil.
	 *
	 * @return single instance of DataUtil
	 */
	public static DataUtil getInstance() {
		if (instance == null) {
			instance = new DataUtil();
		}
		return instance;

	}

	/**
	 * Read file to string.
	 *
	 * @param context the context
	 * @param resourceId the resource id
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String readFileToString(Context context, int resourceId)
			throws IOException {
		InputStream is = context.getResources().openRawResource(resourceId);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder strBuilder = new StringBuilder("");
		while (reader.ready()) {
			strBuilder.append(reader.readLine());
		}
		reader.close();
		return strBuilder.toString();
	}
}
