package com.ingloriousmind.openweathermapdemo.domain

abstract class Mapper<From, To> {

    abstract fun map(from: From): To

    fun map(list: List<From>?): List<To> {
        return list
            ?.run {
                val result = ArrayList<To>(size)
                mapTo(result) { map(it) }
                result
            }
            ?: emptyList()
    }

}
