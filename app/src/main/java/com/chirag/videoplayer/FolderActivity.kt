package com.chirag.videoplayer

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.chirag.videoplayer.databinding.ActivityFodlerBinding
import java.io.File

class FolderActivity : AppCompatActivity() {

    companion object {
        lateinit var currentFolderVideo : ArrayList<Video>
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fodler)
        val binding = ActivityFodlerBinding.inflate(layoutInflater)

        setContentView(binding.root)



        val position= intent.getIntExtra("position",0)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title =MainActivity.foldersList[position].folderName
        currentFolderVideo =getAllVideos(MainActivity.foldersList[position].id)
        binding.videorvFA.setHasFixedSize(true)
        binding.videorvFA.setItemViewCacheSize(10)
        binding.videorvFA.layoutManager = LinearLayoutManager(this@FolderActivity)
        binding.videorvFA.adapter = VideoAdapter (this@FolderActivity, currentFolderVideo, isFolder = true)
        binding.totalvideoFA.text = "Total Videos: ${currentFolderVideo.size}"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true

    }
    @SuppressLint("NewApi", "Range")
    private fun getAllVideos(folderId: String): ArrayList<Video>{
        val tempList =ArrayList<Video>()
       val selection =MediaStore.Video.Media.BUCKET_ID + " like? "
        val projection = arrayOf(
            MediaStore.Video.Media.TITLE , MediaStore.Video.Media.SIZE, MediaStore.Video.Media._ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME , MediaStore.Video.Media.DATA, MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DURATION, MediaStore.Video.Media.BUCKET_ID)

        val cursor=this.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, selection, arrayOf(folderId),
        MediaStore.Video.Media.DATE_ADDED + " DESC")

        if(cursor != null)
            if (cursor.moveToNext())
                do {
                    val titleC= cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                    val idC= cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                    val folderC= cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))

                    val sizeC= cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                    val pathC= cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                    val durationC= cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)).toLong()


                    try {
                        val file = File(pathC)
                        val artUriC= Uri.fromFile(file)
                        val video= Video(title = titleC, id = idC, folderName = folderC, duration = durationC, size = sizeC, path = pathC, artUri = artUriC)
                        if (file.exists()) tempList.add(video)





                    }catch (_:Exception){}
                }while (cursor.moveToNext())
        cursor?.close()

        return tempList

    }

    }

