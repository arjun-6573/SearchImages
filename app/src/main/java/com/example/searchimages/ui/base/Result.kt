package com.example.searchimages.ui.base

sealed class Result<out T> {
    class Success<T>(val data: T) : Result<T>()
    class Error<T>(val error: T) : Result<T>()
}
