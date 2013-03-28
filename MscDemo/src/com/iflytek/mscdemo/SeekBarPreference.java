/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iflytek.mscdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.SeekBar;

/**
 * A {@link Preference} that allows for string input.
 * <p>
 * It is a subclass of {@link DialogPreference} and shows the {@link SeekBar} in
 * a dialog. This {@link SeekBar} can be modified either programmatically via
 * {@link #getSeekBar()}, or through XML by setting any SeekBar attributes on
 * the SeekBarPreference.
 * <p>
 * This preference will store a int into the SharedPreferences.
 */
public class SeekBarPreference extends DialogPreference {

	//拖动条对象.
	private SeekBar mSeekBar;
	
	//对应的view.
	private View mContentView;

	//进度值.
	private int mProgress;

	/**
	 * SeekBarPreference构造函数.
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		setDialogLayoutResource(R.layout.preference_dialog_seekbar);
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContentView = inflater.inflate(R.layout.preference_seekbar, null);
		mSeekBar = (SeekBar) mContentView.findViewById(R.id.seekbar);
	}
	
	/**
	 * 构造函数.
	 * @param context
	 * @param attrs
	 */
	public SeekBarPreference(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * 构造函数
	 * @param context
	 */
	public SeekBarPreference(Context context) {
		this(context, null);
	}

	/**
	 * Saves the int value to the {@link SharedPreferences}.
	 * 
	 * @param progress
	 */
	public void setProgress(int progress) {
		final boolean wasBlocking = shouldDisableDependents();

		mProgress = progress;

		persistInt(progress);

		final boolean isBlocking = shouldDisableDependents();
		if (isBlocking != wasBlocking) {
			notifyDependencyChange(isBlocking);
		}
	}

	/**
	 * Gets the int value from the {@link SharedPreferences}.
	 * 
	 * @return The current preference value.
	 */
	public int getProgress() {
		return mProgress;
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);

		mSeekBar.setProgress(getProgress());

		ViewParent oldParent = mContentView.getParent();
		if (oldParent != view) {
			if (oldParent != null) {
				((ViewGroup) oldParent).removeView(mContentView);
			}
			onAddSeekBarToDialogView(view, mContentView);
		}
	}

	/**
	 * Adds the SeekBar widget of this preference to the dialog's view.
	 * 
	 * @param dialogView
	 *            The dialog view.
	 */
	protected void onAddSeekBarToDialogView(View dialogView, View view) {
		ViewGroup container = (ViewGroup) dialogView.findViewById(R.id.seekbar_container);
		if (container != null) {
			container.addView(view, ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		//if (positiveResult) {
			int value = mSeekBar.getProgress();
			if (callChangeListener(value)) {
				setProgress(value);
			}
		//}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getString(index);
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		int value = 0;
		if (defaultValue instanceof Integer) {
			value = ((Integer) defaultValue).intValue();
		} else if (defaultValue instanceof String) {
			value = Integer.valueOf((String) defaultValue);
		}
		setProgress(restoreValue ? getPersistedInt(mProgress) : value);
	}

	public SeekBar getSeekBar() {
		return mSeekBar;
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		final Parcelable superState = super.onSaveInstanceState();
		if (isPersistent()) {
			// No need to save instance state since it's persistent
			return superState;
		}

		final SavedState myState = new SavedState(superState);
		myState.progress = getProgress();
		return myState;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state == null || !state.getClass().equals(SavedState.class)) {
			// Didn't save state for us in onSaveInstanceState
			super.onRestoreInstanceState(state);
			return;
		}

		SavedState myState = (SavedState) state;
		super.onRestoreInstanceState(myState.getSuperState());
		setProgress(myState.progress);
	}

	private static class SavedState extends BaseSavedState {
		int progress;

		public SavedState(Parcel source) {
			super(source);
			progress = source.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(progress);
		}

		public SavedState(Parcelable superState) {
			super(superState);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

}
