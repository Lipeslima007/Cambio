package com.example.cambio

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class ConversorMoedaActivity : AppCompatActivity() {


    //constantes de classe
    companion object {
        private const val COTACAO_DOLAR = 5.50
        private const val COTACAO_EURO = 6.00
    }

    private val localeBR: Locale = Locale.forLanguageTag("pt-BR")

    // var: mudam durante a execução
    private var realParaEstrangeira = true
    private var usarEuro = false

    // lateinit var: só recebem valor no setupViews(), depois do setContentView()
    private lateinit var ivBandeiraOrigem: ImageView
    private lateinit var ivBandeiraDestino: ImageView
    private lateinit var btnInverter: Button
    private lateinit var rgMoeda: RadioGroup
    private lateinit var etValor: EditText
    private lateinit var btnCalcular: Button
    private lateinit var btnLimpar: Button
    private lateinit var tvResultado: TextView

    // life cycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.conversor_moeda)


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

    // config

    // Liga cada atributo ao componente do XML
    private fun setupViews() {
        ivBandeiraOrigem = findViewById(R.id.ivBandeiraOrigem)
        ivBandeiraDestino = findViewById(R.id.ivBandeiraDestino)
        btnInverter = findViewById(R.id.btnInverter)
        rgMoeda = findViewById(R.id.rgMoeda)
        etValor = findViewById(R.id.etValor)
        btnCalcular = findViewById(R.id.btnCalcular)
        btnLimpar = findViewById(R.id.btnLimpar)
        tvResultado = findViewById(R.id.tvResultado)
    }

    //define o que acontece em cada clique
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

        //troca entre Dólar e Euro
        rgMoeda.setOnCheckedChangeListener { _, checkedId ->
            usarEuro = (checkedId == R.id.rbEuro)
            atualizarBandeiras()
            exibirResultado(0.0)
        }
    }

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

    private fun cotacaoEstrangeira(): Double {
        return if (usarEuro) COTACAO_EURO else COTACAO_DOLAR
    }

    // Símbolo da moeda estrangeira escolhida no RadioGroup
    private fun simboloEstrangeira(): String {
        return if (usarEuro) "€" else "US$"
    }

    // Faz a conta de acordo com o sentido atual da conversão
    private fun converterMoeda(valor: Double): Double {
        return if (realParaEstrangeira) {
            valor / cotacaoEstrangeira()   // R$ -> US$ ou €
        } else {
            valor * cotacaoEstrangeira()   // US$ ou € -> R$
        }
    }

    private fun exibirResultado(valor: Double) {
        val simbolo = if (realParaEstrangeira) simboloEstrangeira() else "R$"
        tvResultado.text = String.format(localeBR, "Resultado: %s %.2f", simbolo, valor)
    }

    // Coloca as bandeiras nos lados certos, de acordo com o sentido e a moeda escolhida
    private fun atualizarBandeiras() {
        val bandeiraEstrangeira = if (usarEuro) R.drawable.bandeira_ue else R.drawable.bandeira_eua

        if (realParaEstrangeira) {
            ivBandeiraOrigem.setImageResource(R.drawable.bandeira_br)
            ivBandeiraDestino.setImageResource(bandeiraEstrangeira)
        } else {
            ivBandeiraOrigem.setImageResource(bandeiraEstrangeira)
            ivBandeiraDestino.setImageResource(R.drawable.bandeira_br)
        }
    }


    private fun inverterMoedas() {
        realParaEstrangeira = !realParaEstrangeira
        atualizarBandeiras()

        // O resultado antigo não vale mais para o novo sentido
        exibirResultado(0.0)
    }

    private fun limparCampos() {
        etValor.text.clear()
        exibirResultado(0.0)
        etValor.requestFocus()
    }
}