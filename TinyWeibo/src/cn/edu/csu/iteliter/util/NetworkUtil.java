package cn.edu.csu.iteliter.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

/**
 * network tool class
 * 
 * @author hjw
 */
public class NetworkUtil {
	public final static int NONE = 0; // no network
	public final static int WIFI = 1; // Wi-Fi
	public final static int MOBILE = 2; // 3G,GPRS

	/**
	 * get current network state
	 */
	public static int getNetworkState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// mobile
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return MOBILE;
		}
		// Wifi
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return WIFI;
		}
		return NONE;
	}
}