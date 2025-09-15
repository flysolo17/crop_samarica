package com.jmballangca.cropsamarica.domain.utils

import com.jmballangca.cropsamarica.data.models.weather.Location


fun Location.getLocation() : String {
    return "${this.name}, ${this.region}"
}