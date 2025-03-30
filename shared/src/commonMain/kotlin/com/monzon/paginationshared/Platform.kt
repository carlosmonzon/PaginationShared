package com.monzon.paginationshared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform