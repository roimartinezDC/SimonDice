package com.example.simondice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val btnStart : Button = findViewById(R.id.btn_start)

    }
    override fun onResume() {
        super.onResume()
        val btnRojo : Button = findViewById(R.id.button_red)
        val btnVerde : Button = findViewById(R.id.button_green)
        val btnAmarillo : Button = findViewById(R.id.button_yellow)
        val btnAzul : Button = findViewById(R.id.button_blue)

    }

    private fun genColor(): Int {
        val color = when (Random().nextInt(4) + 1) {
            1 -> R.color.rojo_encendido
            2 -> R.color.verde_encendido
            3 -> R.color.amarillo_encendido
            else -> R.color.azul_encendido
        }
        return color
    }

}