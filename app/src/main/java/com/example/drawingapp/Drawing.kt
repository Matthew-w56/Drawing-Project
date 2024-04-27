package com.example.drawingapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drawings")
data class Drawing(var fileName: String) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}