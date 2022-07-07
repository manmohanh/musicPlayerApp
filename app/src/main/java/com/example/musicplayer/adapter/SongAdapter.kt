package com.example.musicplayer.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.MyMediaPlayer
import com.example.musicplayer.R
import com.example.musicplayer.activity.PlayerActivity
import com.example.musicplayer.model.AudioModel
import java.io.IOException
import java.io.Serializable


class SongAdapter( val context: Context,
                   val list: ArrayList<AudioModel>) :RecyclerView.Adapter<SongAdapter.SongHolder>(){


    class SongHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
         val title = itemView.findViewById<TextView>(R.id.title)
         val artist = itemView.findViewById<TextView>(R.id.artistName)
        val constraintLayout = itemView.findViewById<ConstraintLayout>(R.id.constraintLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {
        val viewHolder = SongHolder(LayoutInflater.from(context).inflate(R.layout.single_row_design,
            parent,false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        val currentItem = list[position]
        holder.title.text = currentItem.title
        holder.artist.text = currentItem.artist

        holder.constraintLayout.setOnClickListener{
            MyMediaPlayer.getInstance().reset()
            MyMediaPlayer.currentIndex = position

            val intent = Intent(context,PlayerActivity::class.java)
            intent.putExtra("list",list as Serializable)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}