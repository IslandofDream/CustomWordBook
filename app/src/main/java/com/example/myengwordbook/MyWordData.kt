package com.example.myengwordbook

import java.io.Serializable

data class MyWordData(var id:Int , var word:String, var meaning:String , var checked : Int):Serializable {
}