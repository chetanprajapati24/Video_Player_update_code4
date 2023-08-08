package com.chirag.videoplayer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chirag.videoplayer.databinding.FragmentVideofragmentBinding


class Videofragment : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view=inflater.inflate(R.layout.fragment_videofragment, container, false)
        val binding = FragmentVideofragmentBinding.bind(view)



        // set is true. which means our Recyclerview will have  fixed size & save memory for us
        binding.videorv.setHasFixedSize(true)
        //how many items should be in cache memory ,at a time
        binding.videorv.setItemViewCacheSize(10)
        //
        binding.videorv.layoutManager = LinearLayoutManager(requireContext())
        //
        binding.videorv.adapter = VideoAdapter(requireContext() , MainActivity.videoList)
        binding.totalvideo.text = "Total Videos: ${MainActivity.videoList.size}"
        return view
    }


}
