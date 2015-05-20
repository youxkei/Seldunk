package io.github.youxkei.seldunk

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import com.activeandroid.Model
import rx.android.widget.WidgetObservable
import kotlinx.android.synthetic.activity_change_title.*
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * Created by youxkei on 15/05/04.
 */

class ChangeTitleActivity() : Activity() {
    var preferences: SharedPreferences? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferences = getSharedPreferences("seldunk", Context.MODE_PRIVATE)

        setContentView(R.layout.activity_change_title)

        WidgetObservable.text(editText)
                .debounce(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe { textEvent ->
                    val currentTimeSegmentId = preferences!!.getLong("currentTimeSegmentId", -1L)

                    if (currentTimeSegmentId == -1L) {
                        throw RuntimeException("should be started")
                    }

                    val currentTimeSegment = Model.load<TimeSegment>(javaClass<TimeSegment>(), currentTimeSegmentId)
                    currentTimeSegment.title = textEvent.text().toString()
                    currentTimeSegment.save()

                    val intent = Intent(this, javaClass<MainService>())
                    intent.setAction(MainService.START)
                    startService(intent)
                }
    }

    override fun onResume() {
        super.onResume()

        val currentTimeSegmentId = preferences!!.getLong("currentTimeSegmentId", -1L)

        if (currentTimeSegmentId == -1L) {
            throw RuntimeException("should be started")
        }

        val currentTimeSegment = Model.load<TimeSegment>(javaClass<TimeSegment>(), currentTimeSegmentId)

        editText.setText(currentTimeSegment.title)
    }
}
