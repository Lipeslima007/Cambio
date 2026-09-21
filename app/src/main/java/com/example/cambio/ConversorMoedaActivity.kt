package com.example.cambio

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class ConversorMoedaActivity : AppCompatActivity() {

    companion object {
        private const val COTACAO_DOLAR = 5.50
    }


    // val: nunca muda depois de criado
    private val localeBR: Locale = Locale.forLanguageTag("pt-BR")

    // var: muda durante a execução (true = R$ -> US$ | false = US$ -> R$)
    private var realParaDolar = true

    // lateinit var: só recebem valor no setupViews(), depois do setContentView()
    private lateinit var ivBandeiraOrigem: ImageView
    private lateinit var ivBandeiraDestino: ImageView
    private lateinit var btnInverter: Button
    private lateinit var etValor: EditText
    private lateinit var btnCalcular: Button
    private lateinit var btnLimpar: Button
    private lateinit var tvResultado: TextView

    // life cycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.conversor_moeda)

        // Afasta o conteúdo da barra de status, da barra de navegação e do teclado
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val barras = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            )
            v.setPadding(barras.left, barras.top, barras.right, barras.bottom)
            insets
        }

        setupViews()
        setupListeners()
    }

    //config

    // Liga cada atributo ao componente do XML
    private fun setupViews() {
        ivBandeiraOrigem = findViewById(R.id.ivBandeiraOrigem)
        ivBandeiraDestino = findViewById(R.id.ivBandeiraDestino)
        btnInverter = findViewById(R.id.btnInverter)
        etValor = findViewById(R.id.etValor)
        btnCalcular = findViewById(R.id.btnCalcular)
        btnLimpar = findViewById(R.id.btnLimpar)
        tvResultado = findViewById(R.id.tvResultado)
    }

    // Define o que acontece em cada clique
    private fun setupListeners() {
        btnInverter.setOnClickListener {
            inverterMoedas()
        }

        btnCalcular.setOnClickListener {
            calcular()
        }

        btnLimpar.setOnClickListener {
            limparCampos()
        }
    }

    // funções

    private fun calcular() {
        val texto = etValor.text.toString().trim()

        if (texto.isEmpty()) {
            Toast.makeText(this, "Digite um valor para converter", Toast.LENGTH_SHORT).show()
            return
        }

        // Aceita "10.5" e "10,5"
        val valor = texto.replace(",", ".").toDoubleOrNull()

        if (valor == null || valor < 0) {
            Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show()
            return
        }

        exibirResultado(converterMoeda(valor))
    }


    private fun exibirResultado(valor: Double) {
        val simbolo = if (realParaDolar) "US$" else "R$"
        tvResultado.text = String.format(localeBR, "Resultado: %s %.2f", simbolo, valor)
    }


    private fun converterMoeda(valor: Double): Double {
        return if (realParaDolar) {
            valor / COTACAO_DOLAR   // R$ -> US$
        } else {
            valor * COTACAO_DOLAR   // US$ -> R$
        }
    }

    private fun inverterMoedas() {
        realParaDolar = !realParaDolar

        if (realParaDolar) {
            ivBandeiraOrigem.setImageResource(R.drawable.bandeira_br)
            ivBandeiraDestino.setImageResource(R.drawable.bandeira_eua)
        } else {
            ivBandeiraOrigem.setImageResource(R.drawable.bandeira_eua)
            ivBandeiraDestino.setImageResource(R.drawable.bandeira_br)
        }

        exibirResultado(0.0)
    }

    private fun limparCampos() {
        etValor.text.clear()
        exibirResultado(0.0)
        etValor.requestFocus()
    }
}