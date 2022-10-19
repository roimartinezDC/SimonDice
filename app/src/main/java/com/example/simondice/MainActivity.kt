package com.example.simondice

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.room.Room
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

@OptIn(DelicateCoroutinesApi::class)
class MainActivity : AppCompatActivity() {

    private var arrayColores = ArrayList<Int>()
    private var arraySentencia = ArrayList<Int>()
    private var start = false
    private var tiempoTrans = 400L
    private val clickdelay = 250L
    private var marcador = 0
    private var record = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        //no se puede instanciar la bd en el hilo principal, por lo que se hace en una corrutine
        val roomCorrutine = GlobalScope.launch(Dispatchers.Main) {
            //instancia de la bd
            val room: RecordDB = Room
                .databaseBuilder(applicationContext,
                    RecordDB::class.java, "records")
                .build()
            //establecemos variable local de record, a puntuación record en la BD
            record = room.recordDao().getAll()[0].puntuacion
        }
        roomCorrutine.start()

        val btnRojo : Button = findViewById(R.id.button_red)
        val btnVerde : Button = findViewById(R.id.button_green)
        val btnAmarillo : Button = findViewById(R.id.button_yellow)
        val btnAzul : Button = findViewById(R.id.button_blue)
        val btnStart : Button = findViewById(R.id.btn_start)
        val textMarcador : TextView = findViewById(R.id.marcador)

        btnStart.setOnClickListener { view ->
            view.visibility = View.INVISIBLE
            btnRojo.setBackgroundResource(R.drawable.hex_rojo_apagado)
            btnVerde.setBackgroundResource(R.drawable.hex_verde_apagado)
            btnAmarillo.setBackgroundResource(R.drawable.hex_amarillo_apagado)
            btnAzul.setBackgroundResource(R.drawable.hex_azul_apagado)
            cambiarColor(btnRojo, btnVerde, btnAmarillo, btnAzul)
            textMarcador.text = marcador.toString()
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
                arraySentencia.add(index)
                if (arraySentencia[arraySentencia.size-1] != arrayColores[arraySentencia.size-1]) {
                    colorError(brojo, bverde, bamarillo, bazul)
                } else {
                    val enc = GlobalScope.launch(Dispatchers.Main) {
                        btn.setBackgroundResource(colorEnc)
                        delay(clickdelay)
                        btn.setBackgroundResource(colorApg)
                    }
                    enc.start()
                }
            }
        }

    }

    private fun colorAcierto(rojoBtn : Button, verdeBtn : Button, amarilloBtn : Button, azulBtn : Button) {

        if (tiempoTrans > 160L) {
            tiempoTrans -= 10L
        }
        val acierto = GlobalScope.launch(Dispatchers.Main) {
            start = false
            rojoBtn.setBackgroundResource(R.drawable.hex_acierto)
            verdeBtn.setBackgroundResource(R.drawable.hex_acierto)
            amarilloBtn.setBackgroundResource(R.drawable.hex_acierto)
            azulBtn.setBackgroundResource(R.drawable.hex_acierto)
            delay(clickdelay)
            rojoBtn.setBackgroundResource(R.drawable.hex_rojo_apagado)
            verdeBtn.setBackgroundResource(R.drawable.hex_verde_apagado)
            amarilloBtn.setBackgroundResource(R.drawable.hex_amarillo_apagado)
            azulBtn.setBackgroundResource(R.drawable.hex_azul_apagado)

            cambiarColor(rojoBtn, verdeBtn, amarilloBtn, azulBtn)
        }
        acierto.start()
        start = true
        marcador++
        //validacion de nuevo record
        if (marcador > record) {
            Toast.makeText(applicationContext, "¡Has establecido un nuevo récord! : $marcador", Toast.LENGTH_SHORT).show()
            val roomCorrutine = GlobalScope.launch(Dispatchers.Main) {
                val room: RecordDB = Room
                    .databaseBuilder(applicationContext,
                        RecordDB::class.java, "records")
                    .build()
                room.recordDao().update(Record(1, marcador))
            }
            roomCorrutine.start()
        }
        val textMarcador : TextView = findViewById(R.id.marcador)
        textMarcador.text = marcador.toString()
    }


    @SuppressLint("SetTextI18n")
    private fun colorError(rojoBtn : Button, verdeBtn : Button, amarilloBtn : Button, azulBtn : Button) {
        start = false
        val error = GlobalScope.launch(Dispatchers.Main) {
            rojoBtn.setBackgroundResource(R.drawable.hex_fallo)
            verdeBtn.setBackgroundResource(R.drawable.hex_fallo)
            amarilloBtn.setBackgroundResource(R.drawable.hex_fallo)
            azulBtn.setBackgroundResource(R.drawable.hex_fallo)
            delay(100L)
            rojoBtn.setBackgroundResource(R.drawable.hex_rojo_apagado)
            verdeBtn.setBackgroundResource(R.drawable.hex_verde_apagado)
            amarilloBtn.setBackgroundResource(R.drawable.hex_amarillo_apagado)
            azulBtn.setBackgroundResource(R.drawable.hex_azul_apagado)
            delay(100L)
            rojoBtn.setBackgroundResource(R.drawable.hex_fallo)
            verdeBtn.setBackgroundResource(R.drawable.hex_fallo)
            amarilloBtn.setBackgroundResource(R.drawable.hex_fallo)
            azulBtn.setBackgroundResource(R.drawable.hex_fallo)
        }
        error.start()

        val btnStart : Button = findViewById(R.id.btn_start)
        btnStart.setText(R.string.play_again)
        btnStart.visibility = View.VISIBLE
        val recordMsg : TextView = findViewById(R.id.recordMsg)
        recordMsg.text = "Récord: $record"

        btnStart.setOnClickListener {
            rojoBtn.setBackgroundResource(R.drawable.hex_rojo_apagado)
            verdeBtn.setBackgroundResource(R.drawable.hex_verde_apagado)
            amarilloBtn.setBackgroundResource(R.drawable.hex_amarillo_apagado)
            azulBtn.setBackgroundResource(R.drawable.hex_azul_apagado)
            btnStart.visibility = View.INVISIBLE
            arrayColores = ArrayList()
            arraySentencia = ArrayList()
            tiempoTrans = 400L
            marcador = 0
            val textMarcador : TextView = findViewById(R.id.marcador)
            textMarcador.text = marcador.toString()
            cambiarColor(rojoBtn, verdeBtn, amarilloBtn, azulBtn)
            recordMsg.text = ""
        }
    }

    private fun cambiarColor(rojoBtn : Button, verdeBtn : Button, amarilloBtn : Button, azulBtn : Button) {
        arraySentencia = ArrayList()
        nuevoColor()
        val encender = GlobalScope.launch(Dispatchers.Main) {
            start = false
            for (i in 0 until arrayColores.size) {
                delay(tiempoTrans)
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