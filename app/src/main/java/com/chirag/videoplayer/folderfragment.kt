package com.chirag.videoplayer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chirag.videoplayer.databinding.FragmentFolderfragmentBinding


class folderfragment : Fragment() {
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_folderfragment, container, false)
        val binding = FragmentFolderfragmentBinding.bind(view)


        binding.FoldersrvFA.setHasFixedSize(true)
        binding.FoldersrvFA.setItemViewCacheSize(10)
        binding.FoldersrvFA.layoutManager = LinearLayoutManager(requireContext())
        binding.FoldersrvFA.adapter = FolderAdapter(requireContext(), MainActivity.foldersList)
        binding.totalfolder.text = "Total Folders: ${MainActivity.foldersList.size}"
        return view

    }
}