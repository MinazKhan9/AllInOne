package com.example.allinone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.netclanremake.R
import com.example.netclanremake.databinding.FragmentChatBottomSheetBinding
import com.example.netclanremake.databinding.FragmentFilterBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheetFragment : BottomSheetDialogFragment(){
    private lateinit var binding: FragmentFilterBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        binding.backButton.setOnClickListener {
            dismiss()
        }
        binding.applyButton.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    companion object {

    }
}