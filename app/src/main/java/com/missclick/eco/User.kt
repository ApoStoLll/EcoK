package com.missclick.eco

import android.media.Image

data class User(val username : String,val name : String,val score : String   /*, image : Image*/ ,
                val followers : String,val following : String)