package com.shiftkey.codingchallenge.availableshiftsbrowser.config

import java.util.*

interface Configuration {
    val shiftsApiUrl: String
    val searchArea: String
    val searchAreaTimeZone: TimeZone
}