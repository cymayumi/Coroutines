package com.mayumi.conversormoedas.ui


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mayumi.conversormoedas.service.ServiceBuilder
import com.mayumi.conversormoedas.service.WebAPI
import kotlinx.coroutines.*


class MainActivityViewModel : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val responseError = MutableLiveData<String>()
    val valorConversao = MutableLiveData<Float>()
    val valorAtual = MutableLiveData<Float>()
    val date = MutableLiveData<String>()
    val resultado = MutableLiveData<Float>()

    init {
        resultado.value = 0f
    }

    fun queryMoeda(moedaAtual: String, moedaConversao: String, num: String) {
        uiScope.launch {
                val destinationService = ServiceBuilder.buildService(WebAPI::class.java)
                val response = destinationService.getMoedas(moedaAtual)

            if (response.isSuccessful) {
                var moedas = response.body()!!
                valorConversao.value = moedas.rates.get(moedaConversao)?.toFloat()

                valorAtual.value = 1/(valorConversao.value.toString().toFloat())
                date.value = moedas.date
                resultado.value = num.toFloat() * valorConversao.value.toString().toFloat()

            } else {
                responseError.value = "Ocorreu um erro!"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}