package com.yusril.githubuser2.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.yusril.githubuser2.R
import com.yusril.githubuser2.util.AlarmReceiver

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {

        private lateinit var sp: SharedPreferences
        private lateinit var alarmReceiver: AlarmReceiver

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
            sp.registerOnSharedPreferenceChangeListener(this)
            alarmReceiver = AlarmReceiver()
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            if (preference?.key.equals("change_language")) changeLanguage()
            return super.onPreferenceTreeClick(preference)
        }

        private fun changeLanguage() {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?,
        ) {
            val isReminder = sharedPreferences?.getBoolean("reminder", false)
            if (isReminder == true) {
                if (activity != null) alarmReceiver.setRepeatingAlarm(this.requireContext())
            } else {
                if (activity != null) alarmReceiver.cancelAlarm(this.requireContext())
            }
        }

    }
}