package com.mayumi.conversormoedas

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mayumi.conversormoedas.service.ServiceBuilder
import com.mayumi.conversormoedas.service.WebAPI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var context: Context
    override val coroutineContext: CoroutineContext =
            Dispatchers.Main + SupervisorJob()

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
    }

    private fun initActions() {
        btn.setOnClickListener {
            launch{
                val response = withContext(Dispatchers.IO){
                    val destinationService = ServiceBuilder.buildService(WebAPI::class.java)
                    return@withContext destinationService.getMoedas()

                }
                if (response.isSuccessful) {
                    var moedas = response.body()!!
                    tv_teste.text = moedas.base
                }
            }
        }
    }
}
