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
            start = true
        }

        btnRojo.setOnClickListener {
            var encRojo = GlobalScope.launch(Dispatchers.Main) {
                btnRojo.setBackgroundResource(R.color.rojo_encendido)
                delay(300L)
                btnRojo.setBackgroundResource(R.color.rojo_apagado)
            }
            if (start) {
                if (arraySentencia.size == arrayColores.size-1) {
                    arraySentencia.add(1)
                    if (arraySentencia == arrayColores) {
                        colorAcierto(btnRojo, btnVerde, btnAmarillo, btnAzul)
                    } else {
                        colorError(btnRojo, btnVerde, btnAmarillo, btnAzul)
                    }
                } else if (arraySentencia.size < arrayColores.size) {
                    arraySentencia.add(1)
                }
            }
        }
        btnVerde.setOnClickListener {
            var encVerde = GlobalScope.launch(Dispatchers.Main) {
                btnVerde.setBackgroundResource(R.color.verde_encendido)
                delay(300L)
                btnVerde.setBackgroundResource(R.color.verde_apagado)
            }
            if (start) {
                if (arraySentencia.size == arrayColores.size-1) {
                    arraySentencia.add(2)
                    if (arraySentencia == arrayColores) {
                        colorAcierto(btnRojo, btnVerde, btnAmarillo, btnAzul)
                    } else {
                        colorError(btnRojo, btnVerde, btnAmarillo, btnAzul)
                    }
                } else if (arraySentencia.size < arrayColores.size) {
                    arraySentencia.add(2)
                }
            }
        }
        btnAmarillo.setOnClickListener {
            var encAmarillo = GlobalScope.launch(Dispatchers.Main) {
                btnAmarillo.setBackgroundResource(R.color.amarillo_encendido)
                delay(300L)
                btnAmarillo.setBackgroundResource(R.color.amarillo_apagado)
            }
            if (start) {
                if (arraySentencia.size == arrayColores.size-1) {
                    arraySentencia.add(3)
                    if (arraySentencia == arrayColores) {
                        colorAcierto(btnRojo, btnVerde, btnAmarillo, btnAzul)
                    } else {
                        colorError(btnRojo, btnVerde, btnAmarillo, btnAzul)
                    }
                } else if (arraySentencia.size < arrayColores.size) {
                    arraySentencia.add(3)
                }
            }
        }
        btnAzul.setOnClickListener {
            var encAzul = GlobalScope.launch(Dispatchers.Main) {
                btnAzul.setBackgroundResource(R.color.azul_encendido)
                delay(300L)
                btnAzul.setBackgroundResource(R.color.azul_apagado)
            }
            if (start) {
                if (arraySentencia.size == arrayColores.size-1) {
                    arraySentencia.add(4)
                    if (arraySentencia == arrayColores) {
                        colorAcierto(btnRojo, btnVerde, btnAmarillo, btnAzul)
                    } else {
                        colorError(btnRojo, btnVerde, btnAmarillo, btnAzul)
                    }
                } else if (arraySentencia.size < arrayColores.size) {
                    arraySentencia.add(4)
                }
            }
        }

    }

    private fun colorAcierto(rojoBtn : Button, verdeBtn : Button, amarilloBtn : Button, azulBtn : Button) {
        val acierto = GlobalScope.launch(Dispatchers.Main) {
            rojoBtn.setBackgroundResource(R.color.rojo_encendido)
            verdeBtn.setBackgroundResource(R.color.verde_encendido)
            amarilloBtn.setBackgroundResource(R.color.amarillo_encendido)
            azulBtn.setBackgroundResource(R.color.azul_encendido)
            delay(300L)
            rojoBtn.setBackgroundResource(R.color.rojo_apagado)
            verdeBtn.setBackgroundResource(R.color.verde_apagado)
            amarilloBtn.setBackgroundResource(R.color.amarillo_apagado)
            azulBtn.setBackgroundResource(R.color.azul_apagado)

            cambiarColor(rojoBtn, verdeBtn, amarilloBtn, azulBtn)
        }
    }

    private fun colorError(rojoBtn : Button, verdeBtn : Button, amarilloBtn : Button, azulBtn : Button) {
        val error = GlobalScope.launch(Dispatchers.Main) {
            rojoBtn.setBackgroundResource(R.color.rojo_encendido)
            verdeBtn.setBackgroundResource(R.color.verde_encendido)
            amarilloBtn.setBackgroundResource(R.color.amarillo_encendido)
            azulBtn.setBackgroundResource(R.color.azul_encendido)
            delay(100L)
            rojoBtn.setBackgroundResource(R.color.rojo_apagado)
            verdeBtn.setBackgroundResource(R.color.verde_apagado)
            amarilloBtn.setBackgroundResource(R.color.amarillo_apagado)
            azulBtn.setBackgroundResource(R.color.azul_apagado)
            delay(100L)
            rojoBtn.setBackgroundResource(R.color.rojo_encendido)
            verdeBtn.setBackgroundResource(R.color.verde_encendido)
            amarilloBtn.setBackgroundResource(R.color.amarillo_encendido)
            azulBtn.setBackgroundResource(R.color.azul_encendido)
        }
    }

    private fun cambiarColor(rojoBtn : Button, verdeBtn : Button, amarilloBtn : Button, azulBtn : Button) {
        arraySentencia = ArrayList()

        nuevoColor()

        val encender = GlobalScope.launch(Dispatchers.Main) {
            delay(200L)
            for (i in 0 until arrayColores.size) {
                delay(400L)
                when (arrayColores[i]) {
                    1 -> {
                        rojoBtn.setBackgroundResource(R.color.rojo_encendido)
                        delay(400L)
                        rojoBtn.setBackgroundResource(R.color.rojo_apagado)
                    }
                    2 -> {
                        verdeBtn.setBackgroundResource(R.color.verde_encendido)
                        delay(400L)
                        verdeBtn.setBackgroundResource(R.color.verde_apagado)
                    }
                    3 -> {
                        amarilloBtn.setBackgroundResource(R.color.amarillo_encendido)
                        delay(400L)
                        amarilloBtn.setBackgroundResource(R.color.amarillo_apagado)
                    }
                    else -> {
                        azulBtn.setBackgroundResource(R.color.azul_encendido)
                        delay(400L)
                        azulBtn.setBackgroundResource(R.color.azul_apagado)
                    }
                }
            }
        }
    }
    private fun nuevoColor() {
        var nuevoColor = 0
        when (genColor()) {
            R.color.rojo_encendido ->  nuevoColor = 1
            R.color.verde_encendido -> nuevoColor = 2
            R.color.amarillo_encendido -> nuevoColor = 3
            R.color.azul_encendido -> nuevoColor = 4
        }
        arrayColores.add(nuevoColor)
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