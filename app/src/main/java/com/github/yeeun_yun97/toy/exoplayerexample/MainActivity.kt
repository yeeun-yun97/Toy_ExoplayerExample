package com.github.yeeun_yun97.toy.exoplayerexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.yeeun_yun97.toy.exoplayerexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }







}