package com.jagl.playground

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val countDownFlow = flow<Int>{
        val startingValue = 5
        var currentValue = startingValue
        emit(currentValue)
        while (currentValue > 0){
            delay(1000L)
            currentValue--
            emit(currentValue)
        }
    }

    /**
     * Simple Flow operators
     */

    fun collectFlow(observers: (Int)->Unit)= viewModelScope.launch{
        countDownFlow
            /**
             * Returns a flow containing only values of the original flow
             * that match the given predicate.
             */
            .filter {time ->
                time % 2 == 0
            }
            /**
             * Returns a flow containing the results of applying the given
             * transform function to each value of the original flow.
             */
            .map { time ->
                time * time
            }
            /**
             * Returns a flow that invokes the given action before
             * each value of the upstream flow is emitted downstream.
             */
            .onEach {time ->
                println(time)
            }
            .collect{time ->
            observers(time)
        }
    }

    fun collectFlow2(observers: (Int)->Unit)= countDownFlow.onEach {time ->
        println("Time in collectFlow2 value: $time")
    }.launchIn(viewModelScope)

    fun collectLatestFlow(observers: (Int)->Unit)= viewModelScope.launch{
        countDownFlow.collectLatest{time ->
            delay(1500L)
            observers(time)
        }
    }

    /**
     * Terminal Flow operators
     */

    fun collectCountFlow()= viewModelScope.launch{
        val count = countDownFlow
            .filter {time ->
                time % 2 == 0
            }
            .map { time ->
                time * time
            }
            .onEach { time ->
                println(time)
            }
            /**
             * Returns the number of elements matching the given predicate.
             */
            .count {
                it % 2 == 0
            }
        println("The count is $count")
    }

    fun collectReduceFlow() = viewModelScope.launch {
        val reduceResult = countDownFlow
            /**
             * Accumulates value starting with the first element and applying
             * operation to current accumulator value and each element.
             * Throws NoSuchElementException if flow was empty.
             */
            .reduce{ accumulator, value ->
                accumulator + value
            }
        println(reduceResult)
    }

    fun collectFoldFlow() = viewModelScope.launch {
        val reduceResult = countDownFlow
            /**
             * Accumulates value starting with initial value and
             * applying operation current accumulator value and each element
             */
            .fold(100){ accumulator, value ->
                accumulator + value
            }
        println(reduceResult)
    }

    /**
     * Flattening Flow operators
     */
    fun collectFlatteningFlow() = viewModelScope.launch {
        val flow1 = flow {
            emit(1)
            delay(500L)
            emit(2)
        }
        /**
         * Transforms elements emitted by the original flow by applying transform,
         * that returns another flow, and then concatenating and flattening these flows.
         * This method is a shortcut for map(transform).flattenConcat().
         */
        flow1.flatMapConcat {value ->
            flow {
                emit(value+1)
                delay(500L)
                emit(value+2)
            }
        }.collect{ value ->
            println(value)
        }
    }

    fun collectFlatteningFlow2() = viewModelScope.launch {
        val flow1 = (1..5).asFlow()
        flow1.flatMapConcat {id ->
            /**
             * Concat do this transform one by one id
             */
            getProductById(id)
        }.collect{ value ->
            println(value)
        }
    }

    private fun getProductById(id: Int): Flow<String> = flow {
        val product = when(id){
            1 -> "Jabon"
            2 -> "Sopa"
            3 -> "Cereal"
            4 -> "Leche"
            5 -> "Avena"
            else -> "N/A"
        }
        emit(product)
    }

    fun collectFlatteningFlow3() = viewModelScope.launch {
        val flow1 = (1..5).asFlow()
        /**
         * Transforms elements emitted by the original flow by applying transform,
         * that returns another flow, and then merging and flattening these flows.
         */
        flow1.flatMapMerge {id ->
            /**
             * Merge do this transform for all ids at onces
             */
            getProductById(id)
        }.collect{ value ->
            println(value)
        }
    }


    fun collectFlatteningFlow4() = viewModelScope.launch {
        val flow1 = (1..5).asFlow()
        /**
         * Returns a flow that switches to a new flow produced by transform function
         * every time the original flow emits a value.
         * When the original flow emits a new value,
         * the previous flow produced by transform block is cancelled.
         */
        flow1.flatMapLatest {id ->
            /**
             * Has the same behavor of collectLatest
             */
            getProductById(id)
        }.collect{ value ->
            println(value)
        }
    }

    /**
     * Buffer, conflate and collectLatest
     */
    fun collectBufferFlow() = viewModelScope.launch {
        val flow1 = flow {
            emit("One")
            delay(1000L)
            emit("Two")
            delay(1000L)
            emit("Three")
        }
        flow1.onEach {
            println("Value is $it")
        }
            /**
             * Buffers flow emissions via channel of a specified
             * capacity and runs collector in a separate coroutine
             */
            .buffer()
            .collect{ value ->
            println(value)
        }
    }

    fun collectConflateFlow() = viewModelScope.launch {
        val flow1 = flow {
            emit("One")
            delay(1000L)
            emit("Two")
            delay(1000L)
            emit("Three")
        }
        flow1.onEach {
            println("Value is $it")
        }
            /**
             * Conflates flow emissions via conflated channel and
             * runs collector in a separate coroutine.
             * The effect of this is that emitter is never suspended due
             * to a slow collector, but collector always gets the most recent value emitted.
             */
            .conflate()
            .collect{ value ->
                println(value)
            }
    }

}