package com.example.adni.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application()

    //@Inject lateinit var workFactory: HiltWorkerFactory