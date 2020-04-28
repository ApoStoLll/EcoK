package com.missclick.eco

data class User(val username : String, val name : String, val score : String, val imageName : String,
                val followers : MutableList<String>? = null, val following : MutableList<String>? = null)
