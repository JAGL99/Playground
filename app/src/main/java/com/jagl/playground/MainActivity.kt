package com.jagl.playground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.tv_message)
        val btnClick = findViewById<Button>(R.id.btn_click)

        collectLatestLifecycleFlow(viewModel.stateFlow) { number ->
            textView.text = "Counter $number"
        }

        collectLatestLifecycleFlow(viewModel.combineText) { combinedText ->
            println(combinedText)
        }

        /* Other way
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.stateFlow.collectLatest{number ->

                    textView.text = "Counter $number"
                }
            }
        }
        */



        btnClick.setOnClickListener {
            viewModel.incrementCounter()
        }


    }
}


fun <T> AppCompatActivity.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        /**
         * LifecycleOwner's extension function for Lifecycle.
         * repeatOnLifecycle to allow an easier call to the API
         * from LifecycleOwners such as Activities and Fragments.
         */
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest { collect(it) }
        }
    }
}