package ca.realitywargames.mysterybox

import android.app.Application

class MysteryNightsApp: Application() {
    override fun onCreate() {
        super.onCreate()
//        initializeDependencies()
//        initializeSentry()
        initializeIAP()
    }
}