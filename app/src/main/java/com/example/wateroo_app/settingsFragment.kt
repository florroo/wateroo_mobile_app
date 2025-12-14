package com.example.wateroo_app

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.work.WorkManager

class settingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val sharedPreferences = requireContext().getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val flashSetting = view.findViewById<Switch>(R.id.flashSetting)
        val vibrationSetting = view.findViewById<Switch>(R.id.vibrationSetting)
        val notificationSetting = view.findViewById<Switch>(R.id.notificationSetting)

        flashSetting.isChecked = sharedPreferences.getBoolean("flash_enabled", true)
        vibrationSetting.isChecked = sharedPreferences.getBoolean("vibration_enabled", true)
        notificationSetting.isChecked = sharedPreferences.getBoolean("notifications_enabled", true)

        flashSetting.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("flash_enabled", isChecked).apply()
        }

        vibrationSetting.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("vibration_enabled", isChecked).apply()
        }

        notificationSetting.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("notifications_enabled", isChecked).apply()

            if (isChecked) {
                scheduleHydrationReminder()
            } else {
                cancelHydrationReminder()
            }
        }

        return view
    }

    private fun scheduleHydrationReminder() {
        val hydrationReminderRequest = androidx.work.PeriodicWorkRequest.Builder(
            HydrationReminderWorker::class.java,
            15, java.util.concurrent.TimeUnit.MINUTES
        ).addTag("hydration_reminder").build()

        androidx.work.WorkManager.getInstance(requireContext()).enqueue(hydrationReminderRequest)
    }

    private fun cancelHydrationReminder() {
        WorkManager.getInstance(requireContext()).cancelAllWorkByTag("hydration_reminder")
    }
}
