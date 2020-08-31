package com.adith.smash.x.covid_19app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

companion object {
    const val EXTRA_COUNTRY= "EXTRA_COUNTRY"
    lateinit var simpandatanegara : String
    lateinit var simpanDataLag : String
}
private val sharedPrefFile = "kotlinsharedpreference"
private lateinit var  sharedPreferences: SharedPreferences
private var dayCases = ArrayList<String>()

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chart_country)


    //untuk menyipan data
    sharedPreferences = this.getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)

    //format angka ditampilkan
    val formatter: NumberFormat = DecimalFormat("#,###")
    val editor: SharedPreferences.Editor = sharedPreferences.edit()

    //DAPATKAN
}
}