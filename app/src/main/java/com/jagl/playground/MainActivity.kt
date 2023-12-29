package com.jagl.playground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModel = MainViewModel()
        val textView = findViewById<TextView>(R.id.tv_message)
        viewModel.collectFlow {
            textView.text = "Time is $it"
        }
        viewModel.collectLatestFlow {
            println("Time is $it")
        }

    }
}