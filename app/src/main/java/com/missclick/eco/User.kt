package com.missclick.eco

import android.media.Image

class User(username : String,name : String, score : String   /*, image : Image*/ , followers : String, following : String){
    var username : String
    var name : String
    var score : String
    var followers : String
    var following : String
    init{
        this.username = username
        this.name = name
        this.score = score
        this.followers = followers
        this.following = following

    }
}