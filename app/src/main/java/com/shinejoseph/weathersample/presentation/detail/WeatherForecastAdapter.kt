package com.shinejoseph.weathersample.presentation.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shinejoseph.weathersample.R
import com.shinejoseph.weathersample.data.local.AppPrefs
import com.shinejoseph.weathersample.domain.model.Weather

class WeatherForecastAdapter(
    private val layoutInflater: LayoutInflater,
    private val units: String,
) : RecyclerView.Adapter<WeatherForecastAdapter.ViewHolder>() {

    private var weatherList = mutableListOf<Weather>()

    class ViewHolder(
        layoutInflater: LayoutInflater,
        parentView: ViewGroup,
    ) : RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.item_weather, parentView, false)) {
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvTemp: TextView = itemView.findViewById(R.id.tvTemp)
        private val tvHumidity: TextView = itemView.findViewById(R.id.tvHumidity)
        private val tvWind: TextView = itemView.findViewById(R.id.tvWind)

        fun bind(weather: Weather, units: String?) {
            tvDate.text = weather.date
            if (units.equals(METRIC))
                tvTemp.text = "" + weather.temp+"°C"
            else
                tvTemp.text = "" + weather.temp+"°F"

            tvHumidity.text = "Humidity = " + weather.humidity+"%"
            tvWind.text = "Wind Speed = " + weather.wind_speed+"km/h"

            Glide.with(image).load(
                "http://openweathermap.org/img/w/" + weather.icon + ".png"
            ).into(image)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(weatherList[position],  units)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(layoutInflater, parent)

    fun addAll(city: List<Weather>) {
        this.weatherList.addAll(city)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = weatherList.size

    companion object {
        const val METRIC = "metric"
    }
}