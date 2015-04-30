package io.github.youxkei.seldunk

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import java.util.Date

/**
 * Created by youkei on 15/04/24.
 */

Table(name = "TimeSegment") public class TimeSegment() : Model() {
    Column(name = "begin") var begin: Date = Date()
    Column(name = "end") var end: Date = Date()
}
