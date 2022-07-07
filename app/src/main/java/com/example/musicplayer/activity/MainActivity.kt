package com.example.musicplayer.activity

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.adapter.SongAdapter
import com.example.musicplayer.model.AudioModel
import java.io.File

class MainActivity : AppCompatActivity() {

    private val songList = ArrayList<AudioModel>()
    lateinit var recyclerView:RecyclerView
    lateinit var adapter:SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerSongList)
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

        recyclerView.addItemDecoration(DividerItemDecoration(this,
            ( recyclerView.layoutManager as LinearLayoutManager).orientation)
            )

        if (!checkPermission()){
            requestPermission()
            return
        }
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION
        )

        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,selection,null,null)
        if (cursor != null) {
            while (cursor.moveToNext()){
                val songData = AudioModel(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
                )
                if (File(songData.path).exists()){
                    songList.add(songData)
                }
                if (songList.size == 0){
                    Toast.makeText(this@MainActivity,"Songs Not Found",Toast.LENGTH_SHORT).show()
                }else{
                    adapter = SongAdapter(this@MainActivity,songList)
                    recyclerView.adapter = adapter
                }
            }
        }
    }
    private fun checkPermission():Boolean{
        val result = ContextCompat.checkSelfPermission(this@MainActivity,
            Manifest.permission.READ_EXTERNAL_STORAGE)
        if (result == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }
    private fun requestPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(this@MainActivity,"Permission required",Toast.LENGTH_SHORT).show()
        }
        ActivityCompat.requestPermissions(this@MainActivity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),123)
    }
}