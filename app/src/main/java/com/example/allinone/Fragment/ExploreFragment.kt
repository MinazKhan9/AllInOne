package com.example.allinone.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allinone.Adapter.ExploreAdapter
import com.example.allinone.FilterBottomSheetFragment
import com.example.allinone.Model.Data
import com.example.netclanremake.R
import com.example.netclanremake.databinding.FragmentExploreBinding

class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding
    private lateinit var adapter: ExploreAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<Data>

    lateinit var imageList: Array<Int>
    lateinit var nameList: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        binding.filterBtn.setOnClickListener {
            val bottomSheetDialog = FilterBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager, "Test")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.recyclerview)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = ExploreAdapter(dataList)
        recyclerView.adapter = adapter
    }

    private fun dataInitialize() {
        dataList = arrayListOf<Data>()
        imageList = arrayOf(
            R.drawable.baseline_account_circle_24,
            R.drawable.baseline_account_circle_24,
            R.drawable.baseline_account_circle_24,
            R.drawable.baseline_account_circle_24,
            R.drawable.baseline_account_circle_24,
            R.drawable.baseline_account_circle_24
        )

        nameList = arrayOf(
            "John Bajaj",
            "Justin D'Cruz",
            "Shahnaz Gill",
            "Shakshi Singh",
            "Romi Nishad",
            "Mini Cutie"
        )
        for (i in imageList.indices) {
            val data = Data(imageList[i], nameList[i])
            dataList.add(data)
        }
    }

    companion object {
    }
}