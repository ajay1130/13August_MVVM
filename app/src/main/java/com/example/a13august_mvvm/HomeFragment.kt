package com.example.a13august_mvvm

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.DialogCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a13august_mvvm.databinding.FragmentHomeBinding
import com.example.a13august_mvvm.databinding.StudentLayoutEditBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment(),OnClickHandler {
    lateinit var homeBinding:FragmentHomeBinding
    lateinit var studentViewModel: StudentViewModel
    lateinit var bottomSheet:BottomSheetDialog
    lateinit var bindingSheet:StudentLayoutEditBinding
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container, false)

        val studentDao = StudentDatabase.getDatabase(this.requireActivity()).getStudentDao()
        val studentRepository = StudentRepository(studentDao)
        studentViewModel = ViewModelProvider(requireActivity(),
            StudentViewModelFactory(studentRepository,requireActivity())).get(StudentViewModel::class.java)

        homeBinding.studentDataModel = studentViewModel

        bottomSheet = BottomSheetDialog(requireContext())
        bindingSheet = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.student_layout_edit,
            null,
            false
        )
        bottomSheet.setContentView(bindingSheet.root)



        studentViewModel.getStudentData().observe(requireActivity(), Observer {

            if(bottomSheet.isShowing){
                bottomSheet.dismiss()
            }

            homeBinding.root.recyclerview_studentdata.apply {
                layoutManager = LinearLayoutManager(requireContext())
                val adpater = StudentAdapter(requireContext(), it as ArrayList<StudentEntity>,
                    this@HomeFragment)
                adapter = adpater
            }
        })

        return homeBinding.root
    }

    override fun onClick(student: StudentEntity) {
        bottomSheet.show()
        studentViewModel.name = student.name
        studentViewModel.course = student.course
        bindingSheet.studentData = studentViewModel
        bindingSheet.sData = student

        bindingSheet.buttonCanceledit.setOnClickListener {
            bottomSheet.dismiss()
        }
    }

    override fun onLongPress(student: StudentEntity) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setTitle("Do you really want to delete ?")
            .setIcon(R.drawable.ic_delete)
            .setMessage("Once click on ok button data will be deleted..")




        dialogBuilder.setPositiveButton("Ok"){dialogInterface,which->
            studentViewModel.delete(student)
        }

        dialogBuilder.setNegativeButton("Cancel"){dialogInterface,which->
            dialogInterface.dismiss()
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }


}