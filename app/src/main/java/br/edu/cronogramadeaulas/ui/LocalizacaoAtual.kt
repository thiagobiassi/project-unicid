package br.edu.cronogramadeaulas.ui

import android.location.Location
import android.location.LocationListener
import android.os.Bundle


class LocalizacaoAtual : LocationListener {

    companion object {
        var latitude = 0.0
        var longitude = 0.0

    }

    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude
    }

    override fun onProviderDisabled(provider: String) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
}
