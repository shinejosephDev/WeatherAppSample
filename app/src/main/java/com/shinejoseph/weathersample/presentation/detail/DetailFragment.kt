package com.shinejoseph.weathersample.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.shinejoseph.weathersample.R
import com.shinejoseph.weathersample.data.local.AppPrefs
import com.shinejoseph.weathersample.databinding.FragmentDetailBinding
import com.shinejoseph.weathersample.presentation.home.WeatherState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()

    val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title.text = args.name

        viewModel.getWeather(args.lat.toDouble(), args.lng.toDouble())

        binding.ivBack.setOnClickListener { view.findNavController().navigateUp() }

        var isExist = false
        viewModel.isExist.observe(viewLifecycleOwner, Observer {
            isExist = it
            if (it)
                binding.ivFavorite.setImageResource(R.drawable.ic_fav_select)
            else
                binding.ivFavorite.setImageResource(R.drawable.ic_fav_unselect)
        }
        )

        viewModel.isExist(args.name)

        binding.ivFavorite.setOnClickListener {
            if (!isExist) {
                viewModel.insertCity(
                    args.name,
                    args.lat,
                    args.lng)
                binding.ivFavorite.setImageResource(R.drawable.ic_fav_select)
            } else {
                binding.ivFavorite.setImageResource(R.drawable.ic_fav_unselect)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val weatherForecastAdapter =
            WeatherForecastAdapter(layoutInflater, AppPrefs(requireContext()).getUnits())
        binding.recyclerView.adapter = weatherForecastAdapter

        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { value: WeatherState ->
                when (value.isLoading) {
                }
                when (value.weather) {
                    value.weather -> weatherForecastAdapter.addAll(value.weather)
                }
                when (value.error.isNotEmpty()) {
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}