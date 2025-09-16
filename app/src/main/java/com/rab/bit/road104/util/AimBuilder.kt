package com.rab.bit.road104.util

import androidx.core.net.toUri

class AimBuilder {

    private var countryCode = ""

    fun setCountryCode(inputCountryCode: String) = apply { this.countryCode = inputCountryCode }

    fun build(baseUrl: String): String {
        val sb = kotlin.text.StringBuilder()
        baseUrl.forEach { sb.append(it) }
        val urlBuilder = sb.toString().toUri()
            .buildUpon()

        urlBuilder.appendQueryParameter(COUNTRY_CODE, countryCode)

        return urlBuilder.build().toString()
    }

    companion object {
        private const val COUNTRY_CODE = "geosim"
    }
}