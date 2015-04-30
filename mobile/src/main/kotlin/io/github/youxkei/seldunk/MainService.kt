package io.github.youxkei.seldunk

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log

/**
 * Created by youkei on 15/04/24.
 */

class MainService(): Service() {
    var notificationManager: NotificationManagerCompat? = null

    override fun onCreate(){
        notificationManager = NotificationManagerCompat.from(this)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null;
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getStringExtra("SeldunkAction")) {
            "start" -> start()
            "switch" -> switch()
            "stop" -> stop()
        }

        return Service.START_STICKY
    }

    fun start() {
        Log.d("Seldunk", "start")
        val intent = Intent(this, javaClass<MainService>())
        intent.putExtra("SeldunkAction", "switch")

        val builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Seldunk")
                .addAction(R.mipmap.ic_launcher, "switch", PendingIntent.getService(this, 0, intent, 0))

        notificationManager!!.notify(0, builder.build())
    }

    fun switch() {
        Log.d("Seldunk", "switch")
    }

    fun stop() {
        Log.d("Seldunk", "stop")
    }
}
