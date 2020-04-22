package com.missclick.eco

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.missclick.eco.main.profile.Profile

class UpdateService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }
}
