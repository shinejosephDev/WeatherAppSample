package com.shinejoseph.weathersample.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.shinejoseph.weathersample.data.local.AppPrefs
import com.shinejoseph.weathersample.databinding.FragmentSettingsBinding
import com.shinejoseph.weathersample.presentation.home.HomeViewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.ivBack.setOnClickListener { view.findNavController().navigateUp() }

        val appPrefs = AppPrefs(requireContext())
        binding.switch1.isChecked = !appPrefs.getUnits().equals(METRIC)
        binding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                appPrefs.saveUnit(IMPERIAL)
            } else {
                appPrefs.saveUnit(METRIC)
            }
        }

        return view
    }


    companion object {
        const val METRIC = "metric"
        const val IMPERIAL = "imperial"
    }

}