package com.iflytek.mscdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * 转写配置页面.
 * @author iFlytek
 * @since 20120822
 */
public class IatPreferenceActivity extends PreferenceActivity implements
		OnPreferenceChangeListener {

	//地图搜索关键字定义
	public static final String ENGINE_POI = "poi";

	//下拉列表数据源对象定义.
	private JSONArray mDataSource;
	//省份列表对象.
	private ListPreference mProvinceListPreference;
	//城市列表对象.
	private ListPreference mCityListPreference;
	
	/**
	 * 设置页面入口函数.
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PreferenceManager preferenceManager = getPreferenceManager();
		preferenceManager.setSharedPreferencesName(getPackageName());
		preferenceManager.setSharedPreferencesMode(MODE_PRIVATE);

		addPreferencesFromResource(R.xml.preference_iat);

		//转写引擎列表.
		ListPreference engineListPreference = (ListPreference)
			findPreference(getString(R
			.string.preference_key_iat_engine));
		engineListPreference.setOnPreferenceChangeListener(this);
		engineListPreference.setSummary(engineListPreference
			.getEntry());

		//采样率参数列表.
		ListPreference rateListPreference = (ListPreference)
			findPreference(getString(R.string
			.preference_key_iat_rate));
		rateListPreference.setOnPreferenceChangeListener(this);
		rateListPreference.setSummary(rateListPreference.getEntry());

		mProvinceListPreference = (ListPreference)
			findPreference(getString(R.string
			.preference_key_poi_province));
		mProvinceListPreference.setOnPreferenceChangeListener(this);

		mCityListPreference = (ListPreference)
			findPreference(getString(R
			.string.preference_key_poi_city));
		mCityListPreference.setOnPreferenceChangeListener(this);

		if (ENGINE_POI.equals(engineListPreference.getValue())) {
			mProvinceListPreference.setEnabled(true);
			mCityListPreference.setEnabled(true);
		}

		fillCities(mProvinceListPreference.getValue());

		mProvinceListPreference.setSummary(
				mProvinceListPreference.getEntry());
		mCityListPreference.setSummary(mCityListPreference.getEntry());
	}

	/**
	 * OnPreferenceChangeListener回调接口，当设置参数列表
	 * 选中值修改之后被调用.
	 * @param preference
	 * @param newValue
	 */
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference instanceof ListPreference) {
			//获取当前的弹出列表UI对象
			ListPreference listPreference = (ListPreference) preference;
			
			CharSequence[] entries = listPreference.getEntries();
			//获取当前点击的是列表的第几个元素.
			int index = listPreference.findIndexOfValue((String) newValue);

			listPreference.setSummary(entries[index]);
		}
		if (getString(R.string.preference_key_iat_engine).equals(
				preference.getKey())) {
			if (ENGINE_POI.equals((String) newValue)) {
				mProvinceListPreference.setEnabled(true);
				mCityListPreference.setEnabled(true);
			} else {
				mProvinceListPreference.setEnabled(false);
				mCityListPreference.setEnabled(false);
			}
		} else if (getString(R.string.preference_key_poi_province)
				.equals(preference.getKey())) {
			fillCities((String) newValue);
			mCityListPreference.setValueIndex(0);
			//设置城市列表
			mCityListPreference.setSummary(mCityListPreference.getEntry());
		}
		return true;
	}

	/**
	 * 从文件中读取信息.
	 * @param fileName
	 * @return 文件内容(String类型)
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private String readStringFromFile(String fileName)
			throws UnsupportedEncodingException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				getAssets().open(fileName), "UTF-16"));
		StringBuilder builder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		return builder.toString();
	}

	/**
	 * 获取数据源.
	 * @return
	 */
	private JSONArray getDataSource() {
		if (null == mDataSource) {
			try {
				String in = readStringFromFile("provinces");
				mDataSource = ((JSONObject) new JSONTokener(in).nextValue())
						.getJSONArray("provinces");
				int length = mDataSource.length();
				String[] provinces = new String[length];
				for (int i = 0; i < length; i++) {
					JSONObject province = (JSONObject) mDataSource.get(i);
					provinces[i] = province.getString("name");
				}
				mProvinceListPreference.setEntries(provinces);
				mProvinceListPreference.setEntryValues(provinces);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return mDataSource;
	}

	/**
	 * 初始化省份对应的城市列表.
	 * @param newProvince
	 */
	private void fillCities(String newProvince) {
		JSONArray dataSource = getDataSource();
		int index = mProvinceListPreference.findIndexOfValue(newProvince);
		JSONObject province;
		try {
			province = (JSONObject) dataSource.get(index);
			JSONArray cityDataSource = province.getJSONArray("cities");
			int length = cityDataSource.length();
			String[] cities = new String[length];
			for (int i = 0; i < length; i++) {
				cities[i] = (String) cityDataSource.get(i);
			}
			mCityListPreference.setEntries(cities);
			mCityListPreference.setEntryValues(cities);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
