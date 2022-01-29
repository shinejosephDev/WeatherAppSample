package com.shinejoseph.weathersample.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shinejoseph.weathersample.R
import com.shinejoseph.weathersample.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var _binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }
}