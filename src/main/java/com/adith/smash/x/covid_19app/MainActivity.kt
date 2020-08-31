package com.adith.smash.x.covid_19app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.sequences.sequence

class MainActivity : AppCompatActivity() {

    private var progressBar: ProgressBar? = null

    //utk mengurutkan list negara
    private val ascending = true

    companion object{
        //untuk memanggil adapter
        lateinit var adapters: CountryAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progress_bar)

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapters.filter.filter(newText)
                return false
            }
        })

        swipe_refresh.setOnRefreshListener {
            getCountry()
            swipe_refresh.isRefreshing = false
        }

        getCountry()
        initializeView()
    }



    private fun initializeView(){
        sequence.setOnClickListener{
            sequenceWitoutInternet(ascending)
            ascending != ascending
        }
    }

    private fun sequenceWitoutInternet(asc: Boolean) {
        rv_country.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            if (asc){
                (layoutManager as LinearLayoutManager).reverseLayout = true
                (layoutManager as LinearLayoutManager).stackFromEnd = true
                Toast.makeText(this@MainActivity, "Z-A", Toast.LENGTH_SHORT).show()
            }else{
                (layoutManager as LinearLayoutManager).reverseLayout = true
                (layoutManager as LinearLayoutManager).stackFromEnd = true
                Toast.makeText(this@MainActivity, "A-Z", Toast.LENGTH_SHORT).show()
            }
            adapter = adapters
        }
    }

    private fun getCountry() {
        val api = retrofit.create(ApiService::class.java)
        api.getAllNegara().enqueue(object : Callback<AllNegara>{

            override fun onResponse(call: Call<AllNegara>, response: Response<AllNegara>) {
                if(response.isSuccessful){
                    val getListDataCorona = response.body()!!.Global
                    val formatter : NumberFormat = DecimalFormat("#,###")
                    txt_confirmed_globe.text = formatter.format(getListDataCorona?.TotalConfirmed?.toDouble())
                    txt_death_globe.text = formatter.format(getListDataCorona?.TotalDeaths?.toDouble())
                    txt_recovered_globa.text = formatter.format(getListDataCorona?.TotalRecovered?.toDouble())
                    rv_country.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        progressBar?.visibility = View.GONE
                        adapters = CountryAdapter(response.body()!!.Countries as ArrayList<Negara>)
                        {negara ->  itemClicked(negara)}

                        adapter = adapters
                    }
                }else{
                    progressBar?.visibility = View.GONE
                    errorLoading(this@MainActivity)

                }
            }

            override fun onFailure(call: Call<AllNegara>, t: Throwable) {
                progressBar?.visibility = View.GONE
                errorLoading(this@MainActivity)
            }

        })

    }

    private fun itemClicked(negara: Negara) {
        val moveWithData = Intent(this@MainActivity, ChartCountryActivity::class.java)
        moveWithData.putExtra(ChartCountryActivity.EXTRA_COUNTRY, negara)
        startActivity(moveWithData)
    }

    private fun errorLoading(context: Context) {
        val builder = AlertDialog.Builder(context)
        with(builder){
            setTitle("Negara Error")
            setCancelable(false)
            setPositiveButton("REFRESH"){_,_->
                super.onRestart()
                val refresh = Intent(this@MainActivity, MainActivity::class.java)
                startActivity(refresh)
                finish()
            }

            setNegativeButton("EXIT"){_,_ ->
                finish()
            }
            create()
            show()
            show()
        }
    }

}