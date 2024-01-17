package com.jagl.playground

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jagl.playground.databinding.ActivityMainBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val controler by lazy { Controler() }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents.isEmpty()) return@registerForActivityResult
        val product = controler.findProduct(result.contents)
        Toast.makeText(this, product?.name ?: "Not found", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnScan.setOnClickListener {
            val scanOptions = ScanOptions().apply {
                setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
                setPrompt("Scan code")
                setCameraId(0)
                setOrientationLocked(false)
                setBeepEnabled(false)
                setCaptureActivity(CaptureActivityPortaint::class.java)
                setBarcodeImageEnabled(false)
            }
            barcodeLauncher.launch(scanOptions)
        }
    }


}