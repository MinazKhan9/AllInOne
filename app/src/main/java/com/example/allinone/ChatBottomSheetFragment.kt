package com.example.allinone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.netclanremake.databinding.FragmentChatBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChatBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentChatBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBottomSheetBinding.inflate(inflater, container, false)
        binding.backButton.setOnClickListener {
            dismiss()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
    }
}