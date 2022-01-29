package com.shinejoseph.weathersample.presentation.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shinejoseph.weathersample.R
import com.shinejoseph.weathersample.data.model.City

class CityAdapter(
    private val layoutInflater: LayoutInflater,
    private val onClick: (name: City) -> Unit
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {
    private var cityList = mutableListOf<City>()

    override fun getItemCount(): Int = cityList.size
    override fun onBindViewHolder(holder: CityViewHolder, position: Int) =
        holder.bind(cityList[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder =
        CityViewHolder(layoutInflater, parent, onClick)

    fun add(city: List<City>) {
        this.cityList.addAll(city)
        notifyDataSetChanged()
    }

    class CityViewHolder(
        layoutInflater: LayoutInflater,
        parentView: ViewGroup,
        private val onClick: (name: City) -> Unit
    ) : RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.item_city, parentView, false)) {
        private val tvName: TextView = itemView.findViewById(R.id.name)

        fun bind(city: City) {
            itemView.setOnClickListener { onClick(city) }
            tvName.text = city.name
        }
    }
}