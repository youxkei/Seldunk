package io.github.youxkei.seldunk

import android.content.Intent
import android.os.Bundle
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

class MainActivity(): ActionBarActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById(R.id.editText) as EditText
        val textView = findViewById(R.id.textView) as TextView

        WidgetObservable.text(editText)
                .debounce(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe{ event ->
                    textView.setText(event.text())

                    val intent = Intent(this, javaClass<MainService>())
                    intent.putExtra("SeldunkAction", event.text().toString())
                    startService(intent)
                }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.getItemId()

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item)
    }
}
