package io.github.youxkei.seldunk

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.ActionBarActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import com.activeandroid.query.Select
import rx.android.schedulers.AndroidSchedulers
import rx.android.widget.WidgetObservable
import java.util.Date
import java.util.concurrent.TimeUnit

class MainActivity(): Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        for (timeSegment in Select().from(javaClass<TimeSegment>()).execute<TimeSegment>()) {
            Log.d("", timeSegment.begin.toString() + " >> " + timeSegment.title + " >> " + timeSegment.end.toString())
        }

        val intent = Intent(this, javaClass<MainService>())
        intent.setAction(MainService.START)

        startService(intent)

        finish()
    }
}
