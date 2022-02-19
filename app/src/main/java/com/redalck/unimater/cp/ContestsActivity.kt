package com.redalck.unimater.cp

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.redalck.unimater.R
import com.redalck.unimater.databinding.ActivityCpBinding
import org.json.JSONArray
import org.json.JSONException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ContestsActivity : AppCompatActivity() {

    private var mAdapter: ContestAdapter? = null
    private lateinit var binding: ActivityCpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        binding = ActivityCpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        binding.swipeRefresh.setOnRefreshListener { fetchData() }
        fetchData()

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun fetchData(){
        val requestQueue = Volley.newRequestQueue(applicationContext)
        val apiUrl = "https://kontests.net/api/v1/all"
        val contests =  ArrayList<Contest>()

        //Fetching the API from URL
        val jsonObjectRequest = JsonArrayRequest(Request.Method.GET, apiUrl, null, { response: JSONArray ->
            for (i in 0 until response.length()) {
                try {
                    contests.add(Contest(name = response.getJSONObject(i).getString("name"),
                        url = response.getJSONObject(i).getString("url"),
                        start_time = formatDate(response.getJSONObject(i).getString("start_time")),
                        end_time = formatDate(response.getJSONObject(i).getString("end_time")),
                        duration = formatDuration(response.getJSONObject(i).getString("duration")),
                        in_24_hours = response.getJSONObject(i).getString("in_24_hours"),
                        status = response.getJSONObject(i).getString("status"),
                        site = response.getJSONObject(i).getString("site")
                    ))
                } catch (e: JSONException) {
                    Toast.makeText(this@ContestsActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
            val llm = LinearLayoutManager(this@ContestsActivity)
            llm.orientation = LinearLayoutManager.VERTICAL
            binding.contestList.layoutManager = llm
            binding.contestList.setHasFixedSize(true)
            binding.contestList.setItemViewCacheSize(50)
            mAdapter = ContestAdapter(this@ContestsActivity, contests)
            binding.contestList.adapter = mAdapter
            runAnimation(binding.contestList)

            binding.swipeRefresh.isRefreshing = false
        },
            { error -> error.printStackTrace() })

        requestQueue.add(jsonObjectRequest)
    }

    private fun runAnimation(mRVcontest: RecyclerView?) {
        val context = mRVcontest!!.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_right)
        mRVcontest.layoutAnimation = controller
        mRVcontest.adapter!!.notifyDataSetChanged()
        mRVcontest.scheduleLayoutAnimation()
    }

    fun formatDate(dateString: String): String? {
        if (dateString.length == 24) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            var date: Date? = null
            try {
                date = inputFormat.parse(dateString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return outputFormat.format(date) + " UTC"
        }
        return dateString
    }

    fun formatDuration(duration: String): String {
        var res = ""
        var n = duration.toDouble().toLong()
        val days = (n / (24 * 3600)).toInt()
        n %= (24 * 3600)
        val hour = (n / 3600).toInt()
        n %= 3600
        val minutes = (n / 60).toInt()
        n %= 60
        val seconds = n.toInt()
        if (days != 0) {
            res = "$res$days Days "
        }
        if (hour != 0) {
            res = "$res$hour Hr "
        }
        if (minutes != 0) {
            res = "$res$minutes Min "
        }
        if (seconds != 0) {
            res = "$res$seconds Sec "
        }
        return res
    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }
}