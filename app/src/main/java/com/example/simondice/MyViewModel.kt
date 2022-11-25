package com.example.simondice

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MyViewModel(application: Application) : AndroidViewModel(application) {

    val ronda = MutableLiveData<Int>()
    var record = MutableLiveData<Int?>()
    private val tag = "RealTime"
    //private val context = getApplication<Application>().applicationContext
    //private var room : RecordDB? = null
    private var database_record: DatabaseReference

    init {
        ronda.value = 0

        //acceso a la BD Firebase
        database_record = Firebase.database("https://simondice-2022-default-rtdb.firebaseio.com/")
            .getReference("record")
        //definición del listener
        val recordListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                record.value = dataSnapshot.getValue<Int>()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d(tag, "recordListener:OnCancelled", error.toException())
            }
        }
        //se añade el listener a la BD
        database_record.addValueEventListener(recordListener)

        /*
            //acceso a la BD Room
            room = Room
                .databaseBuilder(context,
                    RecordDB::class.java, "records")
                .build()
            val roomCorrutine = GlobalScope.launch(Dispatchers.Main) {
                try {
                    record.value = room!!.recordDao().getPuntuacion()
                } catch(ex : NullPointerException) {
                    room!!.recordDao().crearPuntuacion()
                    record.value = room!!.recordDao().getPuntuacion()
                }
            }
            roomCorrutine.start()
        */
    }

    fun actualizarRecord() {
        record.value = ronda.value
        database_record.setValue(record.value)

        /*
        val updateCorrutine = GlobalScope.launch(Dispatchers.Main) {
            room!!.recordDao().update(Record(1, ronda.value!!))
        }
        updateCorrutine.start()
         */
    }

    fun sumarRonda(){
        ronda.value = ronda.value?.plus(1)
    }
    fun resetRonda() {
        ronda.value = 0
    }
}