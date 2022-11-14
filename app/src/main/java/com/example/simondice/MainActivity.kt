package com.example.simondice

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.room.Room
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

@OptIn(DelicateCoroutinesApi::class)
class MainActivity : AppCompatActivity() {

    private var arrayColores = ArrayList<Int>()
    private var arraySentencia = ArrayList<Int>()
    private var start = false
    private var encenderColores : Job? = null
    private var tiempoTrans = 400L
    private val clickdelay = 250L
    //private var marcador = 0
    private var record = 0
    private var firstClick = false
    private var sonido : MediaPlayer? = null
    //instancia de la ViewModel
    private val miModelo by viewModels<MyViewModel>()


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

            //Fragmento de codigo para poder modificar el record de la base de datos
            //Debe estar siempre comentado
            //room.recordDao().update(Record(1, 0))

            //establecemos variable local de record, a puntuación record en la BD
            //try-catch para crear la BD en caso de que sea la primera vez que se ejecuta la app en un dispostivo
            try {
                record = room.recordDao().getAll()[0].puntuacion
            } catch(ex : IndexOutOfBoundsException) {
                room.recordDao().insert(listOf(Record(1, 0)))
                record = room.recordDao().getAll()[0].puntuacion
            }

        }
        roomCorrutine.start()

        val btnRojo : Button = findViewById(R.id.button_red)
        val btnVerde : Button = findViewById(R.id.button_green)
        val btnAmarillo : Button = findViewById(R.id.button_yellow)
        val btnAzul : Button = findViewById(R.id.button_blue)
        val btnStart : Button = findViewById(R.id.btn_start)
        val textMarcador : TextView = findViewById(R.id.marcador)

        btnStart.setOnClickListener { view ->
            val delayCorrutine = GlobalScope.launch(Dispatchers.Main) {
                delay(320L)
                firstClick = true
                view.visibility = View.INVISIBLE
                btnRojo.setBackgroundResource(R.drawable.hex_rojo_apagado)
                btnVerde.setBackgroundResource(R.drawable.hex_verde_apagado)
                btnAmarillo.setBackgroundResource(R.drawable.hex_amarillo_apagado)
                btnAzul.setBackgroundResource(R.drawable.hex_azul_apagado)
                cambiarColor(btnRojo, btnVerde, btnAmarillo, btnAzul, true)

                //el texto del marcador ahora se contabiliza por el ViewModel
                textMarcador.text = miModelo.ronda.value.toString()
                //textMarcador.text = marcador.toString()

                textMarcador.setBackgroundResource(R.drawable.contador_background)
                textMarcador.setPadding(0,60,0,0)
            }
            delayCorrutine.start()
        }

        btnRojo.setOnClickListener {
            comprobar(btnRojo, R.drawable.hex_rojo_encendido, R.drawable.hex_rojo_apagado, R.raw.red, 1, btnRojo, btnVerde, btnAmarillo, btnAzul)
        }
        btnVerde.setOnClickListener {
            comprobar(btnVerde, R.drawable.hex_verde_encendido, R.drawable.hex_verde_apagado, R.raw.green, 2, btnRojo, btnVerde, btnAmarillo, btnAzul)
        }
        btnAmarillo.setOnClickListener {
            comprobar(btnAmarillo, R.drawable.hex_amarillo_encendido, R.drawable.hex_amarillo_apagado, R.raw.yellow, 3, btnRojo, btnVerde, btnAmarillo, btnAzul)
        }
        btnAzul.setOnClickListener {
            comprobar(btnAzul, R.drawable.hex_azul_encendido, R.drawable.hex_azul_apagado, R.raw.blue, 4, btnRojo, btnVerde, btnAmarillo, btnAzul)
        }

        miModelo.ronda.observe(
            this,
            androidx.lifecycle.Observer(
                fun (_: Int) {
                    if (miModelo.ronda.value != 0)
                        textMarcador.text = miModelo.ronda.value.toString()
                }
            )
        )
    }

    private fun comprobar(btn : Button, colorEnc : Int, colorApg : Int, rutaSonido : Int, index : Int, brojo : Button, bverde : Button, bamarillo : Button, bazul : Button) {
        if (start) {
            if (arraySentencia.size == arrayColores.size-1) {
                arraySentencia.add(index)
                if (arraySentencia == arrayColores) {
                    val enc = GlobalScope.launch(Dispatchers.Main) {
                        btn.setBackgroundResource(colorEnc)
                        val enc2 = GlobalScope.launch(Dispatchers.Main) {
                            sonido = MediaPlayer.create(this@MainActivity, rutaSonido)
                            sonido?.start()
                            delay(clickdelay)
                            btn.setBackgroundResource(colorApg)
                        }
                        enc2.join()
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
                        val enc2 = GlobalScope.launch(Dispatchers.Main) {
                            sonido = MediaPlayer.create(this@MainActivity, rutaSonido)
                            sonido?.start()
                            delay(clickdelay)
                            btn.setBackgroundResource(colorApg)
                        }
                        enc2.join()
                    }
                    enc.start()
                }
            }
        }

    }

    private fun colorAcierto(rojoBtn : Button, verdeBtn : Button, amarilloBtn : Button, azulBtn : Button) {
        if (tiempoTrans > 160L) {
            tiempoTrans -= 20L
        }
        val acierto = GlobalScope.launch(Dispatchers.Main) {
            start = false
            delay(600L)
            cambiarColor(rojoBtn, verdeBtn, amarilloBtn, azulBtn, true)
        }
        acierto.start()

        start = true

        //sumamos uno a la instancia miModelo
        miModelo.sumarRonda()

        //validacion de nuevo record
        //aquí cambié el valor de marcador por miModelo
        if ((miModelo.ronda.value.toString()).toInt() > record) {
            record = (miModelo.ronda.value.toString()).toInt()
            Toast.makeText(applicationContext, "¡Has establecido un nuevo récord!", Toast.LENGTH_SHORT).show()
            val roomCorrutine = GlobalScope.launch(Dispatchers.Main) {
                val room: RecordDB = Room
                    .databaseBuilder(applicationContext,
                        RecordDB::class.java, "records")
                    .build()
                room.recordDao().update(Record(1, (miModelo.ronda.value.toString()).toInt()))
            }
            roomCorrutine.start()
        }

        val textMarcador : TextView = findViewById(R.id.marcador)
        //se comenta esta línea porque la actualización del marcador ya se recoge en el Observer
        //textMarcador.text = miModelo.ronda.value.toString()

        if ((miModelo.ronda.value.toString()).toInt() >= 10) {
            textMarcador.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.resources.getDimension(R.dimen.marcador_small))
            textMarcador.setPadding(0, 77, 0, 0)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun colorError(rojoBtn : Button, verdeBtn : Button, amarilloBtn : Button, azulBtn : Button) {
        start = false
        firstClick = false

        val error = GlobalScope.launch(Dispatchers.Main) {
            rojoBtn.setBackgroundResource(R.drawable.hex_fallo)
            verdeBtn.setBackgroundResource(R.drawable.hex_fallo)
            amarilloBtn.setBackgroundResource(R.drawable.hex_fallo)
            azulBtn.setBackgroundResource(R.drawable.hex_fallo)
            val error2 = GlobalScope.launch(Dispatchers.Main) {
                val mp = MediaPlayer.create(this@MainActivity, R.raw.error1)
                mp.start()
                delay(100L)
                rojoBtn.setBackgroundResource(R.drawable.hex_background)
                verdeBtn.setBackgroundResource(R.drawable.hex_background)
                amarilloBtn.setBackgroundResource(R.drawable.hex_background)
                azulBtn.setBackgroundResource(R.drawable.hex_background)
                val error3 = GlobalScope.launch(Dispatchers.Main) {
                    delay(100L)
                    rojoBtn.setBackgroundResource(R.drawable.hex_fallo)
                    verdeBtn.setBackgroundResource(R.drawable.hex_fallo)
                    amarilloBtn.setBackgroundResource(R.drawable.hex_fallo)
                    azulBtn.setBackgroundResource(R.drawable.hex_fallo)
                    val error4 = GlobalScope.launch(Dispatchers.Main) {
                        val mp2 = MediaPlayer.create(this@MainActivity, R.raw.error2)
                        mp2.start()
                    }
                    error4.join()
                }
                error3.join()
            }
            error2.join()
        }
        error.start()

        val btnStart : Button = findViewById(R.id.btn_start)
        btnStart.setText(R.string.play_again)
        btnStart.visibility = View.VISIBLE
        //funcion empleada para modificar el constraint bottom de la vista, en este caso el botonStart
        btnStart.updateLayoutParams<ConstraintLayout.LayoutParams> {
            topMargin += 290
        }

        val recordMsg : TextView = findViewById(R.id.recordMsg)
        recordMsg.text = "Récord: $record"

        val textMarcador : TextView = findViewById(R.id.marcador)

        btnStart.setOnClickListener {
            val delayCorrutine = GlobalScope.launch(Dispatchers.Main) {
                delay(240L)
                rojoBtn.setBackgroundResource(R.drawable.hex_rojo_apagado)
                verdeBtn.setBackgroundResource(R.drawable.hex_verde_apagado)
                amarilloBtn.setBackgroundResource(R.drawable.hex_amarillo_apagado)
                azulBtn.setBackgroundResource(R.drawable.hex_azul_apagado)
                btnStart.visibility = View.INVISIBLE
                btnStart.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    topMargin -= 290
                }
                arrayColores = ArrayList()
                arraySentencia = ArrayList()
                tiempoTrans = 400L

                //establecer la variable valor de la intancia a 0
                miModelo.resetRonda()
                //marcador = 0

                textMarcador.text = miModelo.ronda.value.toString()
                cambiarColor(rojoBtn, verdeBtn, amarilloBtn, azulBtn, true)
                recordMsg.text = ""
                firstClick = true
            }
            delayCorrutine.start()
        }
    }

    private fun cambiarColor(rojoBtn : Button, verdeBtn : Button, amarilloBtn : Button, azulBtn : Button, nuevo : Boolean) {
        arraySentencia = ArrayList()
        if (nuevo) {
            nuevoColor()
        }
        encenderColores = GlobalScope.launch(Dispatchers.Main) {
            start = false
            for (i in 0 until arrayColores.size) {
                delay(tiempoTrans)
                when (arrayColores[i]) {
                    1 -> {
                        rojoBtn.setBackgroundResource(R.drawable.hex_rojo_encendido)
                        val encender1 = GlobalScope.launch(Dispatchers.Main) {
                            val mp : MediaPlayer? = MediaPlayer.create(this@MainActivity, R.raw.red)
                            mp?.start()
                            delay(tiempoTrans)
                            rojoBtn.setBackgroundResource(R.drawable.hex_rojo_apagado)
                        }
                        encender1.join()
                    }
                    2 -> {
                        verdeBtn.setBackgroundResource(R.drawable.hex_verde_encendido)
                        val encender2  = GlobalScope.launch(Dispatchers.Main) {
                            val mp : MediaPlayer? = MediaPlayer.create(this@MainActivity, R.raw.green)
                            mp?.start()
                            delay(tiempoTrans)
                            verdeBtn.setBackgroundResource(R.drawable.hex_verde_apagado)
                        }
                        encender2.join()
                    }
                    3 -> {
                        amarilloBtn.setBackgroundResource(R.drawable.hex_amarillo_encendido)
                        val encender3  = GlobalScope.launch(Dispatchers.Main) {
                            val mp = MediaPlayer.create(this@MainActivity, R.raw.yellow)
                            mp?.start()
                            delay(tiempoTrans)
                            amarilloBtn.setBackgroundResource(R.drawable.hex_amarillo_apagado)
                        }
                        encender3.join()
                    }
                    else -> {
                        azulBtn.setBackgroundResource(R.drawable.hex_azul_encendido)
                        val encender4  = GlobalScope.launch(Dispatchers.Main) {
                            val mp : MediaPlayer? = MediaPlayer.create(this@MainActivity, R.raw.blue)
                            mp?.start()
                            delay(tiempoTrans)
                            azulBtn.setBackgroundResource(R.drawable.hex_azul_apagado)
                        }
                        encender4.join()
                    }
                }
            }
            start = true
        }
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

    @SuppressLint("SetTextI18n")
    override fun onRestart() {
        super.onRestart()

        if (firstClick) {
            start = false
            firstClick = false
            val btnRojo : Button = findViewById(R.id.button_red)
            val btnVerde : Button = findViewById(R.id.button_green)
            val btnAmarillo : Button = findViewById(R.id.button_yellow)
            val btnAzul : Button = findViewById(R.id.button_blue)

            val constrain : ConstraintLayout = findViewById(R.id.cLayout)
            constrain.setBackgroundResource(R.color.background_standby)

            val btnStart : Button = findViewById(R.id.btn_start)
            btnStart.visibility = View.VISIBLE
            btnStart.text = "REANUDAR"
            btnStart.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topMargin -= 150
            }

            btnStart.setOnClickListener {
                val delayCorrutine = GlobalScope.launch(Dispatchers.Main) {
                    delay(240L)
                    cambiarColor(btnRojo, btnVerde, btnAmarillo, btnAzul, false)
                    btnStart.visibility = View.INVISIBLE
                    btnStart.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        topMargin += 150
                    }
                    constrain.setBackgroundResource(R.color.background)
                    firstClick = true
                    start = true
                }
                delayCorrutine.start()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (encenderColores?.isActive == true) {
            encenderColores!!.cancel()

            /*esto realmente no está sirviendo de nada ahora mismo,
            pero es para dejar la variable al mismo valor de antes de que se iniciase la corrutina,
            ya que en la propia corrutina este valor se reestablece pero al final,
            y si cancelásemos la corrutina a la mitad de la ejecución esto no pasaría.
            Realmente en el onRestart() si que se volvería a reestablecer pero igual el día de mañana
            hago algo con el onStop() y esto me evita futuros problemas
             */
            start = true
        }
    }
}