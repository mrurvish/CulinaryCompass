package com.example.culinarycompass.Viewmodels

import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.culinarycompass.data.Validator

class LoginViewModel :ViewModel(){
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _error = MutableLiveData<Validator>()
    val error: LiveData<Validator> = _error

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }
     fun isemailvalid() {
        var validator :Validator
       Log.d("email", _email.value.toString())
        validator = if (TextUtils.isEmpty(email.value)) {
            Validator("please enter email", false)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.value.toString().trim()).matches()) {
            Validator("enter valid email address", false)
        }else if(TextUtils.isEmpty(password.value)){
            Validator("please enter password",false)
        } else {
            Validator("", true)
        }
        _error.value=validator


    }


}