package com.shinejoseph.weathersample.presentation.favorites

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shinejoseph.weathersample.data.model.City
import com.shinejoseph.weathersample.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.io.File
import java.io.FileWriter


@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModels()
    private val adapter: CityAdapter by lazy { CityAdapter(layoutInflater, ::onClick) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBack.setOnClickListener {
            view.findNavController().navigateUp()
        }

        binding.ivExport.setOnClickListener {
            exportToCSV()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                adapter.add(it)
            }
        }

    }

    private fun exportToCSV() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClick(city: City) {
        val action =
            FavoritesFragmentDirections.actionWeatherFavoritesFragmentToWeatherDetailsFragment(
                city.name.toString(),
                0F,
                0F
            )
        view?.findNavController()?.navigate(action)
    }
}