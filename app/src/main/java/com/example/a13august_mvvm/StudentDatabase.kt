package com.example.a13august_mvvm

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [StudentEntity::class],version = 1)
abstract class StudentDatabase:RoomDatabase() {

    abstract fun getStudentDao():StudentDao

    companion object{
        private var INSTANCE:StudentDatabase?=null

        fun getDatabase(context: Context):StudentDatabase{
            if(INSTANCE==null){
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        StudentDatabase::class.java,
                        "StudentDb"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}