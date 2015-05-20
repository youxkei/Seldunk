package io.github.youxkei.seldunk

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.activeandroid.Model
import com.activeandroid.query.Select
import java.util.Date

/**
 * Created by youxkei on 15/04/24.
 */

class MainService(): Service() {
    companion object {
        val START = "io.github.youxkei.seldunk.action.START"
        val STOP_AND_START = "io.github.youxkei.seldunk.action.STOP_AND_START"
        val STOP = "io.github.youxkei.seldunk.action.STOP"
    }

    override fun onBind(intent: Intent): IBinder? {
        return null;
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getAction()) {
            START -> start()
            STOP -> stop()
            STOP_AND_START -> {
                val currentTime = Date()
                stop(currentTime)
                start(currentTime)
            }
        }

        return Service.START_STICKY
    }

    fun start(currentTime: Date = Date()) {
        val preferences = getSharedPreferences("seldunk", Context.MODE_PRIVATE)

        val currentTimeSegmentId = preferences.getLong("currentTimeSegmentId", -1L)
        val currentTimeSegment = if (currentTimeSegmentId == -1L) {
            val timeSegment = TimeSegment(currentTime)
            timeSegment.save()

            val preferenceEditor = preferences.edit()
            preferenceEditor.putLong("currentTimeSegmentId", timeSegment.getId())
            preferenceEditor.apply()

            timeSegment
        } else {
            Model.load<TimeSegment>(javaClass<TimeSegment>(), currentTimeSegmentId)
        }


        val changeTitleIntent = Intent(this, javaClass<ChangeTitleActivity>())

        val stopAndStartIntent = Intent(this, javaClass)
        stopAndStartIntent.setAction(STOP_AND_START)

        val stopIntent = Intent(this, javaClass)
        stopIntent.setAction(STOP)

        val builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(currentTimeSegment.title + " - Seldunk")
                .addAction(R.mipmap.ic_launcher, "Change Title", PendingIntent.getActivity(this, 0, changeTitleIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .addAction(R.mipmap.ic_launcher, "Stop And Start", PendingIntent.getService(this, 0, stopAndStartIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .addAction(R.mipmap.ic_launcher, "Stop", PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT))

        val notification = builder.build()
        NotificationManagerCompat.from(this).notify(0, notification)
    }

    fun stop(currentTime: Date = Date()) {
        val preferences = getSharedPreferences("seldunk", Context.MODE_PRIVATE)
        val preferenceEditor = preferences.edit()

        val currentTimeSegmentId = preferences.getLong("currentTimeSegmentId", -1L)

        if (currentTimeSegmentId == -1L) {
            throw RuntimeException("should be started")
        }

        val currentTimeSegment = Model.load<TimeSegment>(javaClass<TimeSegment>(), currentTimeSegmentId)
        currentTimeSegment.end = currentTime
        currentTimeSegment.save()

        preferenceEditor.putLong("currentTimeSegmentId", -1L)
        preferenceEditor.apply()

        NotificationManagerCompat.from(this).cancel(0)
    }
}
