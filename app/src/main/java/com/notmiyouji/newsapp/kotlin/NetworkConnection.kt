package com.notmiyouji.newsapp.kotlin

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData

class NetworkConnection(val context: Context) : LiveData<Boolean>(){
    private var connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
    private lateinit var networkCallback: android.net.ConnectivityManager.NetworkCallback

    override fun onActive() {
        super.onActive()
        updateConnection()
        connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback())
    }


    private fun connectivityManagerCallback(): ConnectivityManager.NetworkCallback {
        networkCallback = object : android.net.ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                postValue(true)
            }

            override fun onLost(network: android.net.Network) {
                postValue(false)
            }
        }
        return networkCallback
    }

    private val networkRevicer = object : android.content.BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateConnection()
        }
    }

    private fun updateConnection() {
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        postValue(capabilities != null && capabilities.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET))
    }
}