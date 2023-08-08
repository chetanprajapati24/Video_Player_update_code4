package com.chirag.videoplayer

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.chirag.videoplayer.databinding.ActivityMainBinding
import java.io.File
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    //companion object is static object use
    companion object{
        var videoList: ArrayList<Video> = ArrayList()
        var foldersList: ArrayList<Folders> = ArrayList()
    }

    private val permission = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        android.Manifest.permission.READ_MEDIA_VIDEO
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // for Navigation Drawer
        toggle= ActionBarDrawerToggle(this,binding.root, R.string.open, R.string.close)
    binding.root.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if ( requestpermission()){
            foldersList =ArrayList()
            videoList=getAllVideos()
            //call set fragment function
            setfragment(Videofragment())
        }

        //call set fragment function
        setfragment(Videofragment())



       binding.bottomNav.setOnItemSelectedListener {

       //    Toast.makeText(this@MainActivity, "success", Toast.LENGTH_SHORT).show()
           when (it.itemId){
               R.id.videoview -> setfragment(Videofragment())
               R.id.folderview -> setfragment(folderfragment())
           }

           return@setOnItemSelectedListener true
       }
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId){
                R.id.themesNav -> Toast.makeText(this@MainActivity, "Themes", Toast.LENGTH_SHORT).show()
                R.id.sortOrderNav ->Toast.makeText(this@MainActivity, "Sort", Toast.LENGTH_SHORT).show()
                R.id.aboutNav -> Toast.makeText(this@MainActivity, "About", Toast.LENGTH_SHORT).show()
                R.id.exitNav -> exitProcess(1)
            }

            return@setNavigationItemSelectedListener true
        }
    }



    private fun setfragment(fragment: Fragment)
    {
        val transaction =supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentFL , fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()

    }
    private fun requestpermission():Boolean
    {
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(permission) ,13)
            return false
        }
        return true
    }

    @SuppressLint("NewApi")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode==13) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this@MainActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
                    foldersList = ArrayList()
                    videoList =getAllVideos()
                    setfragment(Videofragment())

                }
                else
                    ActivityCompat.requestPermissions(this, arrayOf(permission) ,13)
            }
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }




    @SuppressLint("Recycle", "Range", "SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAllVideos(): ArrayList<Video>{
      val tempList =ArrayList<Video>()
      val tempFolderList =ArrayList<String>()
        val projection = arrayOf(MediaStore.Video.Media.TITLE , MediaStore.Video.Media.SIZE,MediaStore.Video.Media._ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME , MediaStore.Video.Media.DATA,MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DURATION,MediaStore.Video.Media.BUCKET_ID)

        val cursor=this.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
        )
        if(cursor != null)
            if (cursor.moveToNext())
                do {


                    try {
                        val titleC= cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                        val idC= cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                        val folderC= cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                        val folderIdC= cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID))
                        val sizeC= cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                        val pathC= cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                        val durationC= cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)).toLong()


                        val file = File(pathC)
                        val artUriC= Uri.fromFile(file)
                        val video= Video(title = titleC, id = idC, folderName = folderC, duration = durationC, size = sizeC, path = pathC, artUri = artUriC)
                        if (file.exists()) tempList.add(video)


                        // for folder adding
                        if(!tempFolderList.contains(folderC)) {
                            tempFolderList.add(folderC)
                            foldersList.add(Folders(id = folderIdC, folderName = folderC))
                        }


                    }catch (_:Exception){}
                }while (cursor.moveToNext())
                cursor?.close()

        return tempList

    }
    }

