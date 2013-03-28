package com.iflytek.mscdemo;

import com.iflytek.mscdemo.R;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * 识别的设置界面，主要包括对采样率参数的设置
 * @author iFlytek
 * @since 20120823
 *
 */
public class IsrPreferenceActivity extends PreferenceActivity implements
		OnPreferenceChangeListener {
	/**
	 * 入口函数，初始化参数列表信息.
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PreferenceManager preferenceManager = getPreferenceManager();
		preferenceManager.setSharedPreferencesName(getPackageName());
		preferenceManager.setSharedPreferencesMode(MODE_PRIVATE);
 
		addPreferencesFromResource(R.xml.preference_isr);

		/*
		 * ListPreference engineListPreference = (ListPreference)
		 * findPreference(getString(R.string.preference_key_isr_engine));
		 * engineListPreference.setOnPreferenceChangeListener(this);
		 * engineListPreference.setSummary(engineListPreference.getEntry());
		 */

		ListPreference rateListPreference = (ListPreference) findPreference(getString(R.string.preference_key_isr_rate));
		rateListPreference.setOnPreferenceChangeListener(this);
		rateListPreference.setSummary(rateListPreference.getEntry());
	}

	/**
	 * OnPreferenceChangeListener的接口，选中值被修改之后调用.
	 */
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference instanceof ListPreference) {
			ListPreference listPreference = (ListPreference) preference;

			CharSequence[] entries = listPreference.getEntries();
			int index = listPreference.findIndexOfValue((String) newValue);

			listPreference.setSummary(entries[index]);
		}
		return true;
	}
}
