package com.example.simondice

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class MyViewModel(application: Application) : AndroidViewModel(application) {

    val ronda = MutableLiveData<Int>()
    var record = MutableLiveData<Int>()
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private var room : RecordDB? = null

    init {
        ronda.value = 0
        room = Room
            .databaseBuilder(context,
                RecordDB::class.java, "records")
            .build()

        val roomCorrutine = GlobalScope.launch(Dispatchers.Main) {
            try {
                record.value = room!!.recordDao().getAll()[0].puntuacion
            } catch(ex : IndexOutOfBoundsException) {
                room!!.recordDao().insert(listOf(Record(1, 0)))
                record.value = room!!.recordDao().getAll()[0].puntuacion
            }
        }
        roomCorrutine.start()
    }

    fun actualizarRecord() {
        record.value = ronda.value
        val updateCorrutine = GlobalScope.launch(Dispatchers.Main) {
            room!!.recordDao().update(Record(1, (ronda.value.toString()).toInt()))
        }
        updateCorrutine.start()
    }
    fun resetRecord() {
        val resetCorrutine = GlobalScope.launch(Dispatchers.Main) {
            room!!.recordDao().update(Record(1, 0))
            record.value = room!!.recordDao().getAll()[0].puntuacion
        }
        resetCorrutine.start()
    }

    fun sumarRonda() {
        ronda.value = ronda.value?.plus(1)
    }
    fun resetRonda() {
        ronda.value = 0
    }
}