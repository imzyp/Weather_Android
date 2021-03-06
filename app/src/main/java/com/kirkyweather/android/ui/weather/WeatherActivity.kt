package com.kirkyweather.android.ui.weather

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kirkyweather.android.R
import com.kirkyweather.android.databinding.ActivityPlaceBinding
import com.kirkyweather.android.databinding.ActivityWeatherBinding
import com.kirkyweather.android.logic.model.Place
import com.kirkyweather.android.logic.model.Weather
import com.kirkyweather.android.logic.model.getSky
import com.kirkyweather.android.ui.place.PlaceActivity
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(WeatherViewModel::class.java) }
    private lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT

        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 菜单栏的功能
        binding.placeName.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search_iem -> {
                    val intent = Intent(this, PlaceActivity::class.java)
                    intent.putExtra("clean", "true")
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }


        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }

        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            binding.swipeRefresh.isRefreshing = false
        })

        // 刷新设置
        binding.swipeRefresh.setColorSchemeResources(R.color.colorSecondary)
        binding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }

        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
    }

    // 刷新数据
    private fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        binding.swipeRefresh.isRefreshing = true
    }

    // 展示数据
    private fun showWeatherInfo(weather: Weather) {
        binding.placeName.title = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily

        // 填充now.xml布局中的数据
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        binding.now.currentTemp.text = currentTempText
        binding.now.currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        binding.now.currentAQI.text = currentPM25Text
        binding.weatherLayout.setBackgroundResource(getSky(realtime.skycon).bg)

        // 填充forecast.xml布局中的数据
        binding.forecast.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item, binding.forecast.forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            binding.forecast.forecastLayout.addView(view)
        }
        // 填充life_index.xml布局中的数据
        val lifeIndex = daily.lifeIndex
        binding.lifeIndex.coldRiskText.text = lifeIndex.coldRisk[0].desc
        binding.lifeIndex.dressingText.text = lifeIndex.dressing[0].desc
        binding.lifeIndex.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        binding.lifeIndex.carWashingText.text = lifeIndex.carWashing[0].desc
        binding.weatherLayout.visibility = View.VISIBLE
    }

    // 刷新数据

}