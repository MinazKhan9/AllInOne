package com.example.allinone.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allinone.Adapter.GroupAdapter
import com.example.allinone.Model.GroupData
import com.example.netclanremake.R

class GroupFragment : Fragment() {
    private lateinit var adapter: GroupAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<GroupData>

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
        return inflater.inflate(R.layout.fragment_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.gprecyclerview)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = GroupAdapter(dataList)
        recyclerView.adapter = adapter
    }

    private fun dataInitialize(){
        dataList = arrayListOf<GroupData>()
        imageList = arrayOf(
            R.drawable.baseline_photo_camera_24,
            R.drawable.baseline_photo_camera_24,
            R.drawable.baseline_photo_camera_24,
            R.drawable.baseline_photo_camera_24,
            R.drawable.baseline_photo_camera_24,
            R.drawable.baseline_photo_camera_24,
        )

        nameList = arrayOf(
            "John Bajaj", "Justin D'Cruz", "Shahnaz Gill", "Shakshi Singh", "Romi Nishad", "Mini Cutie"
        )
        for (i in imageList.indices){
            val data = GroupData(imageList[i],nameList[i])
            dataList.add(data)
        }
    }

    companion object {

    }
}