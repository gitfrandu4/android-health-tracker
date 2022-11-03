package com.example.android_health_tracker.models.records

import com.example.android_health_tracker.views.home.DocumentNames

class Water (
    val amount: Double? = null,
    val date: Long? = null,
    val category: String? = DocumentNames.WATER
)