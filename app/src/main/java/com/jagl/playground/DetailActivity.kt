package com.jagl.playground

import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.jagl.playground.databinding.ActivityDetailBinding
import android.os.Handler
import androidx.core.view.isVisible

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        Handler(Looper.getMainLooper()).postDelayed({
            showData()
        }, 5000)
    }

    fun showData(){
        binding.viewLoading.isVisible = false
        binding.viewContainer.isVisible = true
    }
}