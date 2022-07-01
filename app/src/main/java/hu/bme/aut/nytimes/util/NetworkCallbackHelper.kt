package hu.bme.aut.nytimes.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest

object NetworkCallbackHelper {

    fun registerNetworkCallback(context: Context, networkCallback: ConnectivityManager.NetworkCallback){
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        manager.registerNetworkCallback(request,networkCallback)
    }

    fun unregisterNetworkCallback(context: Context, networkCallback: ConnectivityManager.NetworkCallback){
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        manager.unregisterNetworkCallback(networkCallback)
    }

}