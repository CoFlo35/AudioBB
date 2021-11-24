package edu.temple.audiobb

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import edu.temple.audlibplayer.PlayerService
import java.io.FileDescriptor
import java.io.PrintWriter

class AudioBook : Service() {

    var paused = false
    var shouldExit = false
    lateinit var playerHandler:Handler






    override fun onBind(intent: Intent): IBinder {
        return PlayerBinder()
    }

    inner class PlayerBinder: Binder() {


        fun setHandler(handler:Handler){
            playerHandler = handler
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        shouldExit = true
        paused = false
    }

    override fun dump(fd: FileDescriptor, fout: PrintWriter, args: Array<out String>?) {
        TODO("Not yet implemented")
    }

}