package com.jagl.playground

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val countDownFlow = flow<Int>{
        val startingValue = 10
        var currentValue = startingValue
        emit(currentValue)
        while (currentValue > 0){
            delay(1000L)
            currentValue--
            emit(currentValue)
        }
    }

    /**
     * Accepts the given collector and emits values into it.
     */
    fun collectFlow(observers: (Int)->Unit)= viewModelScope.launch{
        countDownFlow.collect{time ->
            observers(time)
        }
    }

    /**
     * Terminal flow operator that collects the given flow with a provided action.
     * The crucial difference from collect is that when the original flow emits a
     * new value then the action block for the previous value is cancelled.
     */
    fun collectLatestFlow(observers: (Int)->Unit)= viewModelScope.launch{
        countDownFlow.collectLatest{time ->
            delay(1500L)
            observers(time)
        }
    }

}