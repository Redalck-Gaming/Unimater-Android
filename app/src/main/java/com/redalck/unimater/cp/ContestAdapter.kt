package com.redalck.unimater.cp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.redalck.unimater.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class ContestAdapter(private val context: Context, private val data: List<Contest>) : RecyclerView.Adapter<ContestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.container_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = data[position]
        holder.compename.text = list.name
        when {
            list.site.equals("codechef", ignoreCase = true) -> {
                Glide.with(context).load(R.drawable.codechef).into(holder.imageView)
            }
            list.site.equals("hackerearth", ignoreCase = true) -> {
                Glide.with(context).load(R.drawable.hackerearth_logo).into(holder.imageView)
            }
            list.site.equals("hackerrank", ignoreCase = true) -> {
                Glide.with(context).load(R.drawable.hackerrank_logo).into(holder.imageView)
            }
            list.site.equals("topcoder", ignoreCase = true) -> {
                Glide.with(context).load(R.drawable.topcoder).into(holder.imageView)
            }
            list.site.equals("codeforces", ignoreCase = true) -> {
                Glide.with(context).load(R.drawable.codeforces).into(holder.imageView)
            }
            list.site.equals("leetcode", ignoreCase = true) -> {
                Glide.with(context).load(R.drawable.leetcode).into(holder.imageView)
            }
            list.site.equals("csacademy", ignoreCase = true) -> {
                Glide.with(context).load(R.drawable.csacademy).into(holder.imageView)
            }
            list.site.equals("atcoder", ignoreCase = true) -> {
                Glide.with(context).load(R.drawable.atcoder).into(holder.imageView)
            }
            list.site.equals("kaggle", ignoreCase = true) -> {
                Glide.with(context).load(R.drawable.kaggle).into(holder.imageView)
            }
            list.site.equals("other", ignoreCase = true) -> {
                Glide.with(context).load(R.drawable.engineer).into(holder.imageView)
            }
        }
        holder.startdate.text = list.start_time
        holder.endDate.text = list.end_time
        holder.duration.text = list.duration
        holder.compelink.setOnClickListener {
            val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(list.url))
                        context.startActivity(browserIntent)
                    }
                    DialogInterface.BUTTON_NEGATIVE -> Toast.makeText(
                        context,
                        "\u2639",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            MaterialAlertDialogBuilder(context,R.style.MatDialog).setMessage("Do you want to head to the link?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNeutralButton("No", dialogClickListener).show()
        }
        holder.sharebutton.setOnClickListener {
            val myIntent = Intent(Intent.ACTION_SEND)
            myIntent.type = "text/plain"
            val shareBody = list.name
            val shareSub = """
                Hey, check out this coding contest:${list.name}
                Link:${list.url}
                ${list.start_time}
                """.trimIndent()
            myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody)
            myIntent.putExtra(Intent.EXTRA_TEXT, shareSub)
            context.startActivity(Intent.createChooser(myIntent, "Share Contest"))
        }
        holder.compenoti.setOnClickListener {
            var hours = 0
            var year = 0
            var month = 0
            var minutes = 0
            var date = 0
            try {
                val map = HashMap<String?, Int>()
                map["Jan"] = 0
                map["Feb"] = 1
                map["Mar"] = 2
                map["Apr"] = 3
                map["May"] = 4
                map["June"] = 5
                map["July"] = 6
                map["Aug"] = 7
                map["Sep"] = 8
                map["Oct"] = 9
                map["Nov"] = 10
                map["Dec"] = 11
                var s = list.start_time
                s = s!!.replace(':', ' ')
                val s1 = s.split(" ").toTypedArray()
                val l = s1.size - 3
                val s2 = arrayOfNulls<String>(l)
                var c = 0
                for (i in s1.indices) {
                    if (i <= 2) continue else s2[c++] = s1[i]
                }
                for (i in s2.indices) {
                    when (i) {
                        1 -> {
                            month = map[s2[1]]!!
                            //System.out.println(month);
                        }
                        0 -> {
                            date = s2[0]!!.toInt()
                            //System.out.println(date);
                        }
                        2 -> {
                            year = s2[2]!!.toInt()
                            //System.out.println(year);
                        }
                        3 -> {
                            hours = s2[3]!!.toInt()
                            //System.out.println(hours);
                        }
                        4 -> {
                            minutes = s2[4]!!.toInt()
                            //System.out.println(minutes);
                        }
                    } //till here
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val calIntent = Intent(Intent.ACTION_INSERT)
            calIntent.data = CalendarContract.Events.CONTENT_URI
            calIntent.putExtra(CalendarContract.Events.TITLE, list.name)
            val startTime = Calendar.getInstance()
            startTime[year, month, date, hours] = minutes
            calIntent.putExtra(
                CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                startTime.timeInMillis
            )
            context.startActivity(calIntent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var compename: TextView = itemView.findViewById<View>(R.id.compename) as TextView
        var startdate: TextView = itemView.findViewById<View>(R.id.startDate) as TextView
        var endDate: TextView = itemView.findViewById<View>(R.id.endDate) as TextView
        var duration: TextView = itemView.findViewById<View>(R.id.duration) as TextView
        var compelink: ImageView = itemView.findViewById<View>(R.id.compelink) as ImageView
        var sharebutton: ImageView = itemView.findViewById<View>(R.id.sharebutton) as ImageView
        var imageView: ImageView = itemView.findViewById(R.id.compelogo)
        var compenoti: ImageView = itemView.findViewById<View>(R.id.compenoti) as ImageView
    }
}