package com.mayumi.conversormoedas.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.mayumi.conversormoedas.R
import com.mayumi.conversormoedas.service.ServiceBuilder
import com.mayumi.conversormoedas.service.WebAPI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import okhttp3.internal.format
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var context: Context

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

    private fun initVars() {
        context = this@MainActivity
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
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

            var valorInput = et_valor.text.toString()

            if (valorInput.isEmpty()) {
                valorInput = "0"
            }

            tv_valor_input.text = valorInput.toString()
            viewModel.queryMoeda(moedaAtual, moedaConversao, valorInput)


            tv_sigla_1_blc1.text = moedaAtual
            tv_sigla_2_blc1.text = moedaConversao

            tv_sigla_1_blc2.text = moedaConversao
            tv_sigla_2_blc2.text = moedaAtual

            tv_sigla_1.text = moedaAtual
            tv_sigla_2.text = moedaConversao
        }

        viewModel.date.observe(this, Observer { data ->
            tv_date.text = data
        })

        viewModel.valorAtual.observe(this, Observer { valorAtu ->
            tv_vlr_2.text = format("%.4f", valorAtu)
        })

        viewModel.valorConversao.observe(this, Observer { valorConver ->
            tv_vlr_1.text = format("%.4f", valorConver)
        })

        viewModel.resultado.observe(this, Observer { result ->
            tv_valor_resultado.text = format("%.2f", result)
        })

        viewModel.responseError.observe(this, Observer { erro ->
            Toast.makeText(context, erro, Toast.LENGTH_LONG).show()
        })
    }
}
