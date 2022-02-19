package com.redalck.unimater

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.redalck.unimater.cgpa.MainActivity
import com.redalck.unimater.cp.ContestsActivity
import com.redalck.unimater.databinding.ActivityEssentialsBinding

class EssentialsDTU : AppCompatActivity() {

    private lateinit var binding: ActivityEssentialsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        binding = ActivityEssentialsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.rmButton.setOnClickListener {
            val intent = Intent(applicationContext, MainDTU::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.githubButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/akshaaatt/UnimateR"))
            startActivity(browserIntent)
        }
        binding.cv.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.contestsButton.setOnClickListener {
            val intent = Intent(applicationContext, ContestsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }
}