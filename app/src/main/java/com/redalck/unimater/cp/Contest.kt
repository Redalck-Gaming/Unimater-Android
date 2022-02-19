package com.redalck.unimater.cp

data class Contest(
    val duration: String,
    val end_time: String?,
    val in_24_hours: String?,
    val name: String?,
    val site: String,
    val start_time: String?,
    val status: String,
    val url: String,
)