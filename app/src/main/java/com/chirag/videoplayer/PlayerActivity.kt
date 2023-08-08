package com.chirag.videoplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

import com.chirag.videoplayer.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    lateinit var  binding:ActivityPlayerBinding
    lateinit var playerView :PlayerView
    var player :ExoPlayer? = null
 companion object{
     lateinit var playerList:ArrayList<Video>
     var position: Int = -1
 }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerView =findViewById(R.id.playe_view)
        initializeLayout()



    }
    private  fun  initializeLayout(){
        when(intent.getStringExtra("class")){
            "AllVideos" ->{
                playerList= ArrayList()
                playerList.addAll(MainActivity.videoList)
            }
            "FolderActivity" ->{
                playerList= ArrayList()
                playerList.addAll(FolderActivity.currentFolderVideo)
            }
        }
        createPlayer()
    }

    private fun createPlayer(){
        player =ExoPlayer.Builder(this).build()
        playerView.player=player
        val mediaItem =MediaItem.fromUri(playerList[position].artUri)
        player?.setMediaItem( mediaItem)
        player?.prepare()
        player?.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }
}