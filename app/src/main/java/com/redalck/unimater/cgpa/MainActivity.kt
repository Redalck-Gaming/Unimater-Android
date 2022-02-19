package com.redalck.unimater.cgpa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.redalck.unimater.R
import com.redalck.unimater.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private val sgpaList = ArrayList<EditText?>()
    private val creditList = ArrayList<EditText?>()
    private val pre_name_sgpa = arrayOf(
        "sgpa1",
        "sgpa2",
        "sgpa3",
        "sgpa4",
        "sgpa5",
        "sgpa6",
        "sgpa7",
        "sgpa8"
    )
    private val pre_name_credit = arrayOf(
        "credit1",
        "credit2",
        "credit3",
        "credit4",
        "credit5",
        "credit6",
        "credit7",
        "credit8"
    )
    private val s_sgpa = arrayOfNulls<String>(8)
    private val s_credit = arrayOfNulls<String>(8)
    private val sgpa = FloatArray(10)
    private val credit = FloatArray(8)
    private var test_sgpa: String? = null
    private var test_credit: String? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE)

        sgpaList.add(binding.editSgpa1)
        sgpaList.add(binding.editSgpa2)
        sgpaList.add(binding.editSgpa3)
        sgpaList.add(binding.editSgpa4)
        sgpaList.add(binding.editSgpa5)
        sgpaList.add(binding.editSgpa6)
        sgpaList.add(binding.editSgpa7)
        sgpaList.add(binding.editSgpa8)

        creditList.add(binding.editCredit1)
        creditList.add(binding.editCredit2)
        creditList.add(binding.editCredit3)
        creditList.add(binding.editCredit4)
        creditList.add(binding.editCredit5)
        creditList.add(binding.editCredit6)
        creditList.add(binding.editCredit7)
        creditList.add(binding.editCredit8)
        val nOfSem = 8

        for (i in 0 until nOfSem) {
            sgpaList[i]!!.setText(prefs.getString(pre_name_sgpa[i], ""))
            creditList[i]!!.setText(prefs.getString(pre_name_credit[i], ""))
        }
        binding.btnCalculate.setOnClickListener {
            val editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit()
            var total_credit = 0f
            var mult = 0f
            val result: Float
            for (i in 0 until nOfSem) {
                test_sgpa = sgpaList[i]!!.text.toString()
                Log.d("list$i", "onClick: $test_sgpa")
                if (test_sgpa!!.isEmpty() || test_sgpa == ".") {
                    s_sgpa[i] = "0"
                    sgpaList[i]!!.setText("")
                } else {
                    s_sgpa[i] = test_sgpa
                }
                test_credit = creditList[i]!!.text.toString()
                if (test_credit!!.isEmpty() || test_credit == ".") {
                    s_credit[i] = "0"
                    creditList[i]!!.setText("")
                } else {
                    s_credit[i] = test_credit
                }
                Log.d("list$i", "onClick: after $test_sgpa")
                sgpa[i] = s_sgpa[i]!!.toFloat()
                credit[i] = s_credit[i]!!.toFloat()
                mult += credit[i] * sgpa[i]
                total_credit += credit[i]
                editor.putString(pre_name_sgpa[i], s_sgpa[i]) // Saving String
                editor.putString(pre_name_credit[i], s_credit[i]) // Saving String
                editor.apply()
            }
            if (total_credit != 0f) {
                result = mult / total_credit
                sgpa[nOfSem] = result
                sgpa[nOfSem + 1] = total_credit
                val b = Bundle()
                b.putFloatArray("sgpa", sgpa)
                val i = Intent(applicationContext, GraphActivity::class.java)
                i.putExtras(b)
                startActivity(i)
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
            }
            else {
                Toast.makeText(applicationContext, "Please add credit", Toast.LENGTH_LONG).show()
            }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_clearSgpa_field -> {
                    var i = 0
                    while (i < nOfSem) {
                        sgpaList[i]!!.setText("")
                        i++
                    }
                }
                R.id.action_clearCredit_field -> {
                    var i = 0
                    while (i < nOfSem) {
                        creditList[i]!!.setText("")
                        i++
                    }
                }
                R.id.action_clearAll_field -> {
                    var i = 0
                    while (i < nOfSem) {
                        sgpaList[i]!!.setText("")
                        creditList[i]!!.setText("")
                        i++
                    }
                }
            }
            false
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    companion object {
        private const val MY_PREFS_NAME = "MyPrefsFile"
    }
}