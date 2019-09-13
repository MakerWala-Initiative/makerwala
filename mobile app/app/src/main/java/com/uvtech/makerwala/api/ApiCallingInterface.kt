package com.uvtech.makerwala.api

interface ApiCallingInterface {

    fun onSuccess(response: String, message: String)

    fun onFailure(errorMessage: String)
}
