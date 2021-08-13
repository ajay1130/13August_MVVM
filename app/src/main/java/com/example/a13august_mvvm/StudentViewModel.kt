package com.example.a13august_mvvm

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class StudentViewModel(val studentRepository: StudentRepository,
                       @SuppressLint("StaticFieldLeak") val context:Context):ViewModel() {
    var name:String?=null
    var course:String?=null
    fun getStudentData():LiveData<List<StudentEntity>>{
        return studentRepository.getStudentData()
    }

    fun insert(student: StudentEntity){
        viewModelScope.launch(IO) {
            studentRepository.insert(student)
        }
    }

    fun insert(){
        if(name!=null && course!=null){
            if(name!!.isNotEmpty() && course!!.isNotEmpty()) {
                viewModelScope.launch(IO) {
                    studentRepository.insert(StudentEntity(0, name!!, course!!))
                }
            }
            else{
                Toast.makeText(context,"Name and course should not be null",Toast.LENGTH_LONG).show()
                Log.d("StudentViewModel","data is null $name and $course")
            }
        }
        else{
            Toast.makeText(context,"Name and course should not be null",Toast.LENGTH_LONG).show()
            Log.d("StudentViewModel","data is null $name and $course")
        }
    }


    fun update(student: StudentEntity){
        if(name!=null && course!=null){
            if(name!!.isNotEmpty() && course!!.isNotEmpty()) {
                val jobUpdate = viewModelScope.launch(IO) {
                    studentRepository.update(StudentEntity(student.id, name!!, course!!))
                }
                runBlocking {
                    jobUpdate.join()
                }

                Toast.makeText(context,"Updated successfully...",Toast.LENGTH_LONG).show()

            }
            else{
                Toast.makeText(context,"Name and course should not be null",Toast.LENGTH_LONG).show()
                Log.d("StudentViewModel","data is null $name and $course")
            }
        }
        else{
            Toast.makeText(context,"Name and course should not be null",Toast.LENGTH_LONG).show()
            Log.d("StudentViewModel","data is null $name and $course")
        }
    }


    fun delete(student: StudentEntity){
        val jobDelete = viewModelScope.launch(IO) {
            studentRepository.delete(student)
        }
        runBlocking {
            jobDelete.join()
        }

        Toast.makeText(context,"Deleted successfully...",Toast.LENGTH_LONG).show()

    }



}