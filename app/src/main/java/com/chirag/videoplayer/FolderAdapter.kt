package com.chirag.videoplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chirag.videoplayer.databinding.FolderViewBinding

class FolderAdapter(private val context: Context, private var folderslist: ArrayList<Folders>): RecyclerView.Adapter<FolderAdapter.MyHolder>() {
    class MyHolder(binding: FolderViewBinding): RecyclerView.ViewHolder(binding.root) {

        val folderName=binding.foldernameFV
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {

        return  MyHolder(FolderViewBinding.inflate(LayoutInflater.from(context) , parent , false))
    }
    // is called when views are initializing or attaching to screen
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.folderName.text= folderslist[position].folderName
        holder.root.setOnClickListener {
            val intent=Intent(context,FolderActivity::class.java)
            intent.putExtra("position" ,position)
            ContextCompat.startActivity(context,intent,null)
        }

    }

    override fun getItemCount(): Int {
        return folderslist.size
    }
}
