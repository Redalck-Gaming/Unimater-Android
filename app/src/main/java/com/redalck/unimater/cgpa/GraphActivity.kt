package com.redalck.unimater.cgpa

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.redalck.unimater.R
import java.text.DecimalFormat
import java.util.*

class GraphActivity : AppCompatActivity() {
    private var labels_8 = arrayOf("1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "CGPA")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val b = this.intent.extras
        val sgpa = b!!.getFloatArray("sgpa")
        val chart = findViewById<View>(R.id.chart) as BarChart
        val result_view = findViewById<TextView>(R.id.result_view)

        val nOfSem = 8
        chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels_8)

        val cgpa = DecimalFormat("0.00").format(sgpa!![nOfSem])
        val total_credit = sgpa[nOfSem + 1].toInt()
        result_view.text = "CGPA : $cgpa\nTotal Credit : $total_credit"
        chart.description.isEnabled = false
        val entries = ArrayList<BarEntry>()
        var Xvalue = 0.toFloat()
        for (i in 0..nOfSem) {
            entries.add(BarEntry(Xvalue, sgpa[i]))
            Xvalue++
        }
        val set = BarDataSet(entries, "Semester")
        //set.setDrawValues(true);
        set.setColors(
            intArrayOf(
                R.color.Cyan,
                R.color.Purple,
                R.color.Orange,
                R.color.Blue,
                R.color.Yellow,
                R.color.Green,
                R.color.Pink,
                R.color.Lime,
                R.color.Magenta,
                R.color.Teal,
                R.color.Lavender,
                R.color.Brown,
                R.color.Red
            ),
            applicationContext
        )
        set.valueFormatter = MyValueFormatter()
        val xAxis = chart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.labelRotationAngle = -45f
        //xAxis.setTextSize(14);
        val left = chart.axisLeft
        val yAxis = chart.axisLeft
        chart.axisRight.isEnabled = false // no right axis
        yAxis.axisMaximum = 10f
        yAxis.axisMinimum = 1.5f

        //chart.setPinchZoom();
        chart.animateY(3000, Easing.EasingOption.EaseOutCubic)
        val data = BarData(set)
        data.barWidth = 0.9f // set custom bar width
        chart.data = data
        chart.setFitBars(true) // make the x-axis fit exactly all bars
        chart.invalidate() // refresh

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.home -> {
                    finish()
                }
            }
            false
        }
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    inner class MyValueFormatter : IValueFormatter {
        private val mFormat: DecimalFormat = DecimalFormat("0.00")
        override fun getFormattedValue(value: Float, entry: Entry, dataSetIndex: Int, viewPortHandler: ViewPortHandler): String {
            // write your logic here
            return mFormat.format(value.toDouble())
        }
    }
}