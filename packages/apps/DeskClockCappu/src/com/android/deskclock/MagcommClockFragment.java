/*
* Copyright (C) 2014 MediaTek Inc.
* Modification based on code covered by the mentioned copyright
* and/or permission notice(s).
*/
/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.android.deskclock;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextClock;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.cappu.widget.CareButton;
/**
 * Fragment that shows  the clock (analog or digital), the next alarm info and the world clock.
 */
public class MagcommClockFragment extends DeskClockFragment{

    private final static String TAG = "MagcommClockFragment";
    private TextView mDate ,mWday;
    private CareButton mSetting;
    public MagcommClockFragment() {
    	
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle icicle) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.magcomm_clock_fragment, container, false);
        final DeskClock activity = (DeskClock) getActivity();
        mDate = (TextView) v.findViewById(R.id.date);
        mWday = (TextView) v.findViewById(R.id.wday);
        String mDateStr = DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyyMMMMd");
        SimpleDateFormat mDateFormat = new SimpleDateFormat(mDateStr, Locale.getDefault());
        String mDateString = mDateFormat.format(new Date(System.currentTimeMillis()));
        mDate.setText(mDateString);
        
        mDateStr = DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEE");
        mDateFormat = new SimpleDateFormat(mDateStr, Locale.getDefault());
        mDateString = mDateFormat.format(new Date(System.currentTimeMillis()));
        mWday.setText(mDateString);
        
        mSetting = (CareButton)v.findViewById(R.id.menu_button);
        mSetting.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Intent.ACTION_QUICK_CLOCK));
			}
        	
        });
        
        
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
