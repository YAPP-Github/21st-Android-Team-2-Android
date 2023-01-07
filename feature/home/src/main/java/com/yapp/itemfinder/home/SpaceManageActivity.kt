package com.yapp.itemfinder.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yapp.itemfinder.feature.home.R

class SpaceManageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_space_manage)
    }

    companion object {

        fun newIntent(context: Context) = Intent(context, SpaceManageActivity::class.java)

    }
}
