package com.mayumi.conversormoedas.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.mayumi.conversormoedas.R
import com.mayumi.conversormoedas.service.ServiceBuilder
import com.mayumi.conversormoedas.service.WebAPI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import okhttp3.internal.format
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var context: Context
    override val coroutineContext: CoroutineContext =
        Dispatchers.Main + SupervisorJob()

    private var lista_moedas = arrayListOf<String>(
        "CAD", "HKD", "ISK", "PHP", "DKK", "HUF", "CZK",
        "GBP", "RON", "SEK", "IDR", "INR", "BRL", "RUB", "HRK", "JPY", "THB", "CHF", "EUR", "MYR",
        "BGN", "TRY", "CNY", "NOK", "NZD", "ZAR", "USD", "MXN", "SGD", "AUD", "ILS", "KRW", "PLN"
    )
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
            R.layout.custom_spinner,
            lista_moedas
        )

        adapterMoedas.setDropDownViewResource(R.layout.custom_dropdown)
        spinner_1.adapter = adapterMoedas
    }

    private fun setSecondSpinner() {
        adapterMoedas = ArrayAdapter<String>(
            context,
            R.layout.custom_spinner,
            lista_moedas
        )

        adapterMoedas.setDropDownViewResource(R.layout.custom_dropdown)
        spinner_2.adapter = adapterMoedas
    }

    private fun initActions() {

        btn.setOnClickListener {
            var moedaAtual = spinner_1.selectedItem as String
            var moedaConversao = spinner_2.selectedItem as String

            queryConversao(moedaConversao, moedaAtual)
            queryMoeda(moedaAtual, moedaConversao)

        }
    }

    private fun queryMoeda(moedaAtual: String, moedaConversao: String) {
        launch {
            val response = withContext(Dispatchers.IO) {
                val destinationService = ServiceBuilder.buildService(WebAPI::class.java)
                return@withContext destinationService.getMoedas(moedaAtual)
            }
            if (response.isSuccessful) {
                var moedas = response.body()!!
                var valorConversao = moedas.rates.get(moedaConversao)?.toFloat()

                calcular(valorConversao, moedaAtual, moedaConversao)
            } else {
                Toast.makeText(context, "Ocorreu um erro!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun queryConversao(moedaConversao: String, moedaAtual: String) {
        launch {
            val response = withContext(Dispatchers.IO) {
                val destinationService = ServiceBuilder.buildService(WebAPI::class.java)
                return@withContext destinationService.getMoedas(moedaConversao)
            }
            if (response.isSuccessful) {
                var moedas = response.body()!!
                var valorConversao = moedas.rates.get(moedaAtual)?.toFloat()
                var date = moedas.date

                tv_vlr_2.text = format("%.4f", valorConversao!!)
                tv_sigla_2_blc2.text = moedaAtual
                tv_sigla_1_blc2.text = moedaConversao
                tv_date.text = "Data: " + date
            } else {
                Toast.makeText(context, "Ocorreu um erro!", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun calcular(valorConversao: Float?, moedaAtual: String, moedaConversao: String) {
        var valor = et_valor.text.toString()
        var resultado = valorConversao!! * valor.toFloat()

        tv_valor_input.text = valor.toString()
        tv_sigla_1.text = moedaAtual

        tv_valor_resultado.text = format("%.2f", resultado)
        tv_sigla_2.text = moedaConversao


        tv_sigla_1_blc1.text = moedaAtual
        tv_vlr_1.text = format("%.4f", valorConversao)
        tv_sigla_2_blc1.text = moedaConversao


    }
}
