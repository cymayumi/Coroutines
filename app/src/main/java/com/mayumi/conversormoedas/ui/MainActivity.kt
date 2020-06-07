package com.mayumi.conversormoedas.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.mayumi.conversormoedas.R
import com.mayumi.conversormoedas.service.ServiceBuilder
import com.mayumi.conversormoedas.service.WebAPI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var context: Context
    override val coroutineContext: CoroutineContext =
        Dispatchers.Main + SupervisorJob()

    private var lista_moedas = arrayListOf<String>("CAD", "HKD", "ISK", "PHP", "DKK", "HUF", "CZK",
        "GBP", "RON", "SEK", "IDR", "INR", "BRL", "RUB", "HRK", "JPY", "THB", "CHF", "EUR", "MYR",
        "BGN", "TRY", "CNY", "NOK", "NZD", "ZAR", "USD", "MXN", "SGD", "AUD", "ILS", "KRW", "PLN")
    private lateinit var adapterMoedas: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initVars()
        initActions()

    }

    override fun onDestroy() {
        coroutineContext[Job]!!.cancel()
        super.onDestroy()
    }

    private fun initVars() {
        context = this@MainActivity
        setFirstSpinner()
        setSecondSpinner()
    }

    private fun setFirstSpinner() {
        adapterMoedas = ArrayAdapter<String>(
            context,
            android.R.layout.simple_spinner_item,
            lista_moedas
        )

        adapterMoedas.setDropDownViewResource(android.R.layout.simple_list_item_1)
        spinner_1.adapter = adapterMoedas
    }

    private fun setSecondSpinner() {
        adapterMoedas = ArrayAdapter<String>(
            context,
            android.R.layout.simple_spinner_item,
            lista_moedas
        )

        adapterMoedas.setDropDownViewResource(android.R.layout.simple_list_item_1)
        spinner_2.adapter = adapterMoedas
    }

    private fun initActions() {
        btn.setOnClickListener {
            var moedaAtual = spinner_1.selectedItem as String
            var moedaConverter = spinner_2.selectedItem as String
            queryMoeda(moedaAtual)

        }
    }

    private fun queryMoeda(moedaAtual: String) {
        openValores(moedaAtual)
    }

    private fun openValores(item : String) {

        launch {
            val response = withContext(Dispatchers.IO) {
                val destinationService = ServiceBuilder.buildService(WebAPI::class.java)
                return@withContext destinationService.getMoedas(item)

            }
            if (response.isSuccessful) {
                var moedas = response.body()!!
                tv_teste.text = moedas.rates.toString()
            } else {
                Toast.makeText(context, "Ocorreu um erro!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
