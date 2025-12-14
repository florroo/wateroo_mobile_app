package com.example.wateroo_app

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class HydrationReminderWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val sharedPreferences = applicationContext.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

        val isNotificationEnabled = sharedPreferences.getBoolean("notifications_enabled", true)
        val isFlashEnabled = sharedPreferences.getBoolean("flash_enabled", true)
        val isVibrationEnabled = sharedPreferences.getBoolean("vibration_enabled", true)

        if (isNotificationEnabled) {
            sendNotification()
        }

        if (isVibrationEnabled) {
            triggerVibration()
        }

        if (isFlashEnabled) {
            turnOnFlashlight()
        }

        return Result.success()
    }

    private fun sendNotification() {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = 1
        val channelId = "hydration_reminder_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Hydration Reminders", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Pamiętaj o nawodnieniu!")
            .setContentText("Czas na picie wody. Pamiętaj, aby dbać o swoje zdrowie!")
            .setSmallIcon(R.drawable.bubbles)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun triggerVibration() {
        val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(500)
        }
    }

    private fun turnOnFlashlight() {
        val cameraManager = applicationContext.getSystemService(Context.CAMERA_SERVICE) as android.hardware.camera2.CameraManager
        val cameraId = cameraManager.cameraIdList[0]

        val sosPattern = listOf(
            500L, 500L,
            500L, 500L,
            500L, 500L,
        )

        Thread {
            try {
                for (i in sosPattern.indices step 2) {
                    cameraManager.setTorchMode(cameraId, true)
                    Thread.sleep(sosPattern[i])

                    cameraManager.setTorchMode(cameraId, false)
                    Thread.sleep(sosPattern[i + 1])
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

}
