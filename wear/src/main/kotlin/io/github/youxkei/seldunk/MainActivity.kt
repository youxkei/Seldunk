package io.github.youxkei.seldunk

import android.app.Activity
import android.os.Bundle
import android.support.wearable.view.WatchViewStub
import android.widget.TextView

/**
 * Created by youkei on 15/03/30.
 */

class MainActivity(): Activity() {
    private var textView: TextView? = null

    override fun onCreate(savedInstanceBundle: Bundle?) {
        super.onCreate(savedInstanceBundle)
        setContentView(R.layout.activity_main)

        val stub = findViewById(R.id.watch_view_stub) as WatchViewStub

        stub.setOnLayoutInflatedListener { stub ->
            textView = stub.findViewById(R.id.text) as TextView
        }
    }
}
