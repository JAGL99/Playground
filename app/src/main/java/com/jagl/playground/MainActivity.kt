package com.jagl.playground

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jagl.playground.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.cvCard.setOnClickListener {
            it as MaterialCardView
            println("Card is cliked")
        }

        binding.cvCard.setOnLongClickListener {
            it as MaterialCardView
            println("Card is check")
            it.isChecked = !it.isChecked
            true
        }

        binding.btnShowDialog.setOnClickListener {
            showDialog(baseContext)
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_1 -> {
                    println("Respond to navigation item 1 click")
                    true
                }

                R.id.item_2 -> {
                    println("Respond to navigation item 2 click")
                    true
                }

                else -> false
            }
        }

        binding.bottomNavigation.setOnItemReselectedListener { item ->
            when(item.itemId) {
                R.id.item_1 -> {
                    println("Respond to navigation item 1 reselection")
                }
                R.id.item_2 -> {
                    println("Respond to navigation item 2 reselection")
                }
            }
        }

        val textWhatcher = object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

        }
        binding.searchBar.textView.addTextChangedListener(textWhatcher)
    }
}

fun showDialog(context: Context) {
    MaterialAlertDialogBuilder(context, R.style.Theme_Playground)
        .show()
}