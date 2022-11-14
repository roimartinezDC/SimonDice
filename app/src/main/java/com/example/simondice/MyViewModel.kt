package com.example.simondice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {

    val ronda = MutableLiveData<Int>()

    // la variable se inicia al instanciar
    init {
        ronda.value = 0
    }

    fun sumarRonda() {
        ronda.value = ronda.value?.plus(1)
    }

    fun resetRonda() {
        ronda.value = 0
    }
}