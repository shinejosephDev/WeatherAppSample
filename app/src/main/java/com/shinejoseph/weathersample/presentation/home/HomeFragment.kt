package com.shinejoseph.weathersample.presentation.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.shinejoseph.weathersample.BuildConfig
import com.shinejoseph.weathersample.R
import com.shinejoseph.weathersample.common.AlarmReceiver
import com.shinejoseph.weathersample.data.local.AppPrefs
import com.shinejoseph.weathersample.databinding.FragmentHomeBinding
import com.shinejoseph.weathersample.presentation.detail.WeatherForecastAdapter
import com.shinejoseph.weathersample.presentation.isGPSOn
import com.shinejoseph.weathersample.presentation.requestGPSOn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var gpsOnRequest: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private val locationRequest: LocationRequest = LocationRequest.create().apply {
//        interval = 30
//        fastestInterval = 10
//        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
//        maxWaitTime = 60
//    }
//
//    private var locationCallback: LocationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            val locationList = locationResult.locations
//            if (locationList.isNotEmpty()) {
//                //The last location in the list is the newest
//                val location = locationList.last()
//                Toast.makeText(
//                    context,
//                    "Got Location: " + location.toString(),
//                    Toast.LENGTH_LONG
//                )
//                    .show()
//            }
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        gpsOnRequest = getGPSOnRequest()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        checkLocationPermission()

        _binding!!.btnLocation.setOnClickListener {
            checkLocationPermission()
        }

        binding.ivSearch.setOnClickListener {
            if (!Places.isInitialized()) {
                Places.initialize(requireContext(), BuildConfig.GOOGLE_PLACES_API, Locale.US);
            }
            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(requireContext())
            startActivityForResult(intent, 12)

        }

        binding.ivFav.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_weatherHomeFragment_to_weatherFavoritesFragment)
        }

        binding.ivSettings.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_weatherHomeFragment_to_settingsFragment)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12) {
            try {
                val place = Autocomplete.getPlaceFromIntent(data);
                val action =
                    HomeFragmentDirections.actionWeatherHomeFragmentToWeatherDetailsFragment(
                        place.name,
                        place.latLng.latitude.toFloat(),
                        place.latLng.longitude.toFloat()
                    )
                view?.findNavController()?.navigate(action)
            } catch (e: Exception) {
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val weatherForecastAdapter = WeatherForecastAdapter(
            layoutInflater,
            AppPrefs(requireContext()).getUnits()
        )
        binding.recyclerView.adapter = weatherForecastAdapter
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { value: WeatherState ->
                when (value.isLoading) {
                }
                when (value.weather) {
                    value.weather -> {
                        schedulePushNotifications()
                        binding.btnLocation.visibility = View.GONE
                        weatherForecastAdapter.addAll(value.weather)
                    }
                }
                when (value.error.isNotEmpty()) {
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(context)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission()
            }
        } else {
            if (isGPSOn()) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    run {
                        if (location != null) {
                            viewModel.getWeather(location.latitude, location.longitude)
                        }
                    }
                }
            } else
                requestGPSOn(gpsOnRequest)
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (context?.let {
                            ContextCompat.checkSelfPermission(
                                it,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        } == PackageManager.PERMISSION_GRANTED
                    ) {
//                        fusedLocationClient?.requestLocationUpdates(
//                            locationRequest,
//                            locationCallback,
//                            Looper.getMainLooper()
//                        )

                        if (isGPSOn()) {
                            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                                run {
                                    println("loca" + location.toString())
                                }
                            }
                        } else
                            requestGPSOn(gpsOnRequest)

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(context, "permission denied", Toast.LENGTH_LONG).show()

                    // Check if we are in a state where the user has denied the permission and
                    // selected Don't ask again
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", requireContext().packageName, null),
                            ),
                        )
                    }
                }
                return
            }
        }
    }

//    override fun onStop() {
//        super.onStop()
//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            )
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//
//            fusedLocationClient.removeLocationUpdates(locationCallback)
//        }
//    }

    @SuppressLint("MissingPermission")
    private fun getGPSOnRequest() =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    run {
                        println("loca" + location.toString())
                    }
                }
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun schedulePushNotifications() {
        try {
            val alarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager
            val alarmPendingIntent by lazy {
                val intent = Intent(context, AlarmReceiver::class.java).apply {
                    putExtra("WEATHER", viewModel.state.value.weather[0].temp)
                }
                PendingIntent.getBroadcast(context, 0, intent, 0)
            }

            val calendar = GregorianCalendar.getInstance().apply {
                if (get(Calendar.HOUR_OF_DAY) >= HOUR_TO_SHOW_PUSH) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }

                set(Calendar.HOUR_OF_DAY, HOUR_TO_SHOW_PUSH)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                alarmPendingIntent
            )
        } catch (exception: Exception) {

        }
    }


    companion object {
        private const val HOUR_TO_SHOW_PUSH = 6
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 231
    }

}