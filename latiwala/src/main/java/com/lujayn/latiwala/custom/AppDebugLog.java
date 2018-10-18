package com.lujayn.latiwala.custom;
import android.util.Log;
import com.lujayn.latiwala.common.AppConstant;
public class AppDebugLog {
	private static boolean isProductionMode;
	private static boolean isFileLogMode;
	public static void setProductionMode(boolean isProductionMode) {
		AppDebugLog.isProductionMode = isProductionMode;
	}
	public static void setFileLogMode(boolean isFileLogMode) {
		AppDebugLog.isFileLogMode = isFileLogMode;
	}
	public static void println(String message) {
		if (!isProductionMode) {
			Log.e(AppConstant.APP_TAG+" :" ,""+ message);
		}

	}
}
