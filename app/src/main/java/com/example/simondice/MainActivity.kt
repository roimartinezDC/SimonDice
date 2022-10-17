package com.example.simondice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var arrayColores = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val btnRojo : Button = findViewById(R.id.button_red)
        val btnVerde : Button = findViewById(R.id.button_green)
        val btnAmarillo : Button = findViewById(R.id.button_yellow)
        val btnAzul : Button = findViewById(R.id.button_blue)
        val btnStart : Button = findViewById(R.id.btn_start)

        btnStart.setOnClickListener { view ->
            view.visibility = View.INVISIBLE
            arrayColores.add(cambiarColor(btnRojo, btnVerde, btnAmarillo, btnAzul))
        }
    }

    private fun cambiarColor(rojoBtn : Button, verdeBtn : Button, amarilloBtn : Button, azulBtn : Button) : Int {
        var color = 0
        when (genColor()) {
            R.color.rojo_encendido ->  color = 1
            R.color.verde_encendido -> color = 2
            R.color.amarillo_encendido -> color = 3
            R.color.azul_encendido -> color = 4
        }
        when (color) {
            1 -> rojoBtn.setBackgroundResource(R.color.rojo_encendido)
            2 -> verdeBtn.setBackgroundResource(R.color.verde_encendido)
            3 -> amarilloBtn.setBackgroundResource(R.color.amarillo_encendido)
            4 -> azulBtn.setBackgroundResource(R.color.azul_encendido)
        }
        return color
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