package com.missclick.eco

import android.graphics.Bitmap
import android.media.Image

data class User(val username : String,val name : String,val score : String   , val image : Bitmap ,
                val followers : String,val following : String)