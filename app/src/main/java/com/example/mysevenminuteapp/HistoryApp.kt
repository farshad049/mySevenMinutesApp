package com.example.mysevenminuteapp

import android.app.Application

class HistoryApp:Application() {
    val db by lazy { HistoryDatabase.getInstance(this) }
}