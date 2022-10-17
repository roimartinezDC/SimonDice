package com.example.simondice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

@OptIn(DelicateCoroutinesApi::class)
class MainActivity : AppCompatActivity() {

    private var arrayColores = ArrayList<Int>()
    private var arraySentencia = ArrayList<Int>()
    private var start = false
    private val tiempoTrans = 400L
    private val clickdelay = 250L

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
            cambiarColor(btnRojo, btnVerde, btnAmarillo, btnAzul)
        }

        btnRojo.setOnClickListener {
            comprobar(btnRojo, R.drawable.hex_rojo_encendido, R.drawable.hex_rojo_apagado, 1, btnRojo, btnVerde, btnAmarillo, btnAzul)
        }
        btnVerde.setOnClickListener {
            comprobar(btnVerde, R.drawable.hex_verde_encendido, R.drawable.hex_verde_apagado, 2, btnRojo, btnVerde, btnAmarillo, btnAzul)
        }
        btnAmarillo.setOnClickListener {
            comprobar(btnAmarillo, R.drawable.hex_amarillo_encendido, R.drawable.hex_amarillo_apagado, 3, btnRojo, btnVerde, btnAmarillo, btnAzul)
        }
        btnAzul.setOnClickListener {
            comprobar(btnAzul, R.drawable.hex_azul_encendido, R.drawable.hex_azul_apagado, 4, btnRojo, btnVerde, btnAmarillo, btnAzul)
        }

    }

    private fun comprobar(btn : Button, colorEnc : Int, colorApg : Int, index : Int, brojo : Button, bverde : Button, bamarillo : Button, bazul : Button) {
        if (start) {
            if (arraySentencia.size == arrayColores.size-1) {
                arraySentencia.add(index)
                if (arraySentencia == arrayColores) {
                    val enc = GlobalScope.launch(Dispatchers.Main) {
                        btn.setBackgroundResource(colorEnc)
                        delay(clickdelay)
                        btn.setBackgroundResource(colorApg)
                    }
                    enc.start()
                    colorAcierto(brojo, bverde, bamarillo, bazul)
                } else {
                    colorError(brojo, bverde, bamarillo, bazul)
                }
            } else if (arraySentencia.size < arrayColores.size) {
                val enc = GlobalScope.launch(Dispatchers.Main) {
                    btn.setBackgroundResource(colorEnc)
                    delay(clickdelay)
                    btn.setBackgroundResource(colorApg)
                }
                enc.start()
                arraySentencia.add(index)
                if (arraySentencia[arraySentencia.size-1] != arrayColores[arraySentencia.size-1]) {
                    colorError(brojo, bverde, bamarillo, bazul)
                }
            }
        }
    }

    private fun colorAcierto(rojoBtn : Button, verdeBtn : Button, amarilloBtn : Button, azulBtn : Button) {
        val acierto = GlobalScope.launch(Dispatchers.Main) {
            rojoBtn.setBackgroundResource(R.drawable.hex_rojo_encendido)
            verdeBtn.setBackgroundResource(R.drawable.hex_verde_encendido)
            amarilloBtn.setBackgroundResource(R.drawable.hex_amarillo_encendido)
            azulBtn.setBackgroundResource(R.drawable.hex_azul_encendido)
            delay(clickdelay)
            rojoBtn.setBackgroundResource(R.drawable.hex_rojo_apagado)
            verdeBtn.setBackgroundResource(R.drawable.hex_verde_apagado)
            amarilloBtn.setBackgroundResource(R.drawable.hex_amarillo_apagado)
            azulBtn.setBackgroundResource(R.drawable.hex_azul_apagado)

            cambiarColor(rojoBtn, verdeBtn, amarilloBtn, azulBtn)
        }
        acierto.start()
    }

    private fun colorError(rojoBtn : Button, verdeBtn : Button, amarilloBtn : Button, azulBtn : Button) {
        start = false
        val error = GlobalScope.launch(Dispatchers.Main) {
            rojoBtn.setBackgroundResource(R.drawable.hex_rojo_encendido)
            verdeBtn.setBackgroundResource(R.drawable.hex_verde_encendido)
            amarilloBtn.setBackgroundResource(R.drawable.hex_amarillo_encendido)
            azulBtn.setBackgroundResource(R.drawable.hex_azul_encendido)
            delay(100L)
            rojoBtn.setBackgroundResource(R.drawable.hex_rojo_apagado)
            verdeBtn.setBackgroundResource(R.drawable.hex_verde_apagado)
            amarilloBtn.setBackgroundResource(R.drawable.hex_amarillo_apagado)
            azulBtn.setBackgroundResource(R.drawable.hex_azul_apagado)
            delay(100L)
            rojoBtn.setBackgroundResource(R.drawable.hex_rojo_encendido)
            verdeBtn.setBackgroundResource(R.drawable.hex_verde_encendido)
            amarilloBtn.setBackgroundResource(R.drawable.hex_amarillo_encendido)
            azulBtn.setBackgroundResource(R.drawable.hex_azul_encendido)
        }
        error.start()

        val btnStart : Button = findViewById(R.id.btn_start)
        btnStart.setText(R.string.play_again)
        btnStart.visibility = View.VISIBLE
        btnStart.setOnClickListener {
            rojoBtn.setBackgroundResource(R.drawable.hex_rojo_apagado)
            verdeBtn.setBackgroundResource(R.drawable.hex_verde_apagado)
            amarilloBtn.setBackgroundResource(R.drawable.hex_amarillo_apagado)
            azulBtn.setBackgroundResource(R.drawable.hex_azul_apagado)
            btnStart.visibility = View.INVISIBLE
            arrayColores = ArrayList<Int>()
            arraySentencia = ArrayList<Int>()
            cambiarColor(rojoBtn, verdeBtn, amarilloBtn, azulBtn)
        }
    }

    private fun cambiarColor(rojoBtn : Button, verdeBtn : Button, amarilloBtn : Button, azulBtn : Button) {
        arraySentencia = ArrayList()

        nuevoColor()

        val encender = GlobalScope.launch(Dispatchers.Main) {
            for (i in 0 until arrayColores.size) {
                delay(450L)
                when (arrayColores[i]) {
                    1 -> {
                        rojoBtn.setBackgroundResource(R.drawable.hex_rojo_encendido)
                        delay(tiempoTrans)
                        rojoBtn.setBackgroundResource(R.drawable.hex_rojo_apagado)
                    }
                    2 -> {
                        verdeBtn.setBackgroundResource(R.drawable.hex_verde_encendido)
                        delay(tiempoTrans)
                        verdeBtn.setBackgroundResource(R.drawable.hex_verde_apagado)
                    }
                    3 -> {
                        amarilloBtn.setBackgroundResource(R.drawable.hex_amarillo_encendido)
                        delay(tiempoTrans)
                        amarilloBtn.setBackgroundResource(R.drawable.hex_amarillo_apagado)
                    }
                    else -> {
                        azulBtn.setBackgroundResource(R.drawable.hex_azul_encendido)
                        delay(tiempoTrans)
                        azulBtn.setBackgroundResource(R.drawable.hex_azul_apagado)
                    }
                }
            }
            start = true
        }
        encender.start()
    }
    private fun nuevoColor() {
        var nuevoColor = 0
        when (genColor()) {
            R.drawable.hex_rojo_encendido ->  nuevoColor = 1
            R.drawable.hex_verde_encendido -> nuevoColor = 2
            R.drawable.hex_amarillo_encendido -> nuevoColor = 3
            R.drawable.hex_azul_encendido -> nuevoColor = 4
        }
        arrayColores.add(nuevoColor)
    }
    private fun genColor(): Int {
        val color = when (Random().nextInt(4) + 1) {
            1 -> R.drawable.hex_rojo_encendido
            2 -> R.drawable.hex_verde_encendido
            3 -> R.drawable.hex_amarillo_encendido
            else -> R.drawable.hex_azul_encendido
        }
        return color
    }

}