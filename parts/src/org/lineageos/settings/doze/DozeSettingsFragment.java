/*
 * Copyright (C) 2015 The CyanogenMod Project
 *               2017-2018 The LineageOS Project
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

package org.lineageos.settings.doze;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceCategory;
import androidx.preference.SwitchPreferenceCompat;

import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.SettingsBasePreferenceFragment;

import org.lineageos.settings.R;

public class DozeSettingsFragment extends SettingsBasePreferenceFragment
        implements OnPreferenceChangeListener {

    private SwitchPreferenceCompat mWakeOnGesturePreference;
    private SwitchPreferenceCompat mPickUpPreference;
    private SwitchPreferenceCompat mHandwavePreference;
    private SwitchPreferenceCompat mPocketPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.doze_settings, rootKey);

        boolean dozeEnabled = DozeUtils.isDozeEnabled(getActivity());

        mWakeOnGesturePreference = (SwitchPreferenceCompat) findPreference(DozeUtils.WAKE_ON_GESTURE_KEY);
        mWakeOnGesturePreference.setEnabled(dozeEnabled);
        mWakeOnGesturePreference.setOnPreferenceChangeListener(this);

        MainSwitchPreference switchBar = findPreference(DozeUtils.DOZE_ENABLE);
        switchBar.setOnPreferenceChangeListener(this);
        switchBar.setChecked(dozeEnabled);

        PreferenceCategory proximitySensorCategory = (PreferenceCategory) getPreferenceScreen().
                findPreference(DozeUtils.CATEG_PROX_SENSOR);

        mPickUpPreference = (SwitchPreferenceCompat) findPreference(DozeUtils.GESTURE_PICK_UP_KEY);
        mPickUpPreference.setEnabled(dozeEnabled);
        mPickUpPreference.setOnPreferenceChangeListener(this);

        mHandwavePreference = (SwitchPreferenceCompat) findPreference(DozeUtils.GESTURE_HAND_WAVE_KEY);
        mHandwavePreference.setEnabled(dozeEnabled);
        mHandwavePreference.setOnPreferenceChangeListener(this);

        mPocketPreference = (SwitchPreferenceCompat) findPreference(DozeUtils.GESTURE_POCKET_KEY);
        mPocketPreference.setEnabled(dozeEnabled);
        mPocketPreference.setOnPreferenceChangeListener(this);

        // Hide proximity sensor related features if the device doesn't support them
        if (!DozeUtils.getProxCheckBeforePulse(getActivity())) {
            getPreferenceScreen().removePreference(proximitySensorCategory);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean isChecked = (Boolean) newValue;

        if (DozeUtils.DOZE_ENABLE.equals(preference.getKey())) {
            DozeUtils.enableDoze(getActivity(), isChecked);

            mWakeOnGesturePreference.setEnabled(isChecked);
            mPickUpPreference.setEnabled(isChecked);
            mHandwavePreference.setEnabled(isChecked);
            mPocketPreference.setEnabled(isChecked);
        } else {
            DozeUtils.enableGesture(getActivity(), preference.getKey(), isChecked);
        }

        DozeUtils.checkDozeService(getActivity());
        return true;
    }
}
