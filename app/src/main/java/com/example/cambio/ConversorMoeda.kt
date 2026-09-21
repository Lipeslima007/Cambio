package com.example.cambio

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class ConversorMoeda : AppCompatActivity() {

    companion object{
        private const val cotacao = 5.50;
    }

    private val localeBR = Locale("pt", "BR")

    private var realParaDolar = true;

    // lateinit var: só serão inicializadas no setupViews(), depois do setContentView()
    private lateinit var ivBandeiraOrigem: ImageView
    private lateinit var ivBandeiraDestino: ImageView
    private lateinit var btnInverter: Button
    private lateinit var etValor: EditText
    private lateinit var btnCalcular: Button
    private lateinit var btnLimpar: Button
    private lateinit var tvResultado: TextView

    // ---------- CICLO DE VIDA ----------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        setupListeners()
    }

    // ---------- CONFIGURAÇÃO ----------

    // Liga cada atributo ao componente do XML
    private fun setupViews() {
        ivBandeiraOrigem = findViewById(R.id.idBandeiraOrigem)
        ivBandeiraDestino = findViewById(R.id.idBandeiraDestino)
        btnInverter = findViewById(R.id.bntInverter)
        etValor = findViewById(R.id.etValor)
        btnCalcular = findViewById(R.id.bntCalvular)
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

    // ---------- FUNÇÕES UTILITÁRIAS ----------

    // Valida o campo, converte e mostra o resultado
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

        val resultado = converterMoeda(valor)
        val simbolo = if (realParaDolar) "US$" else "R$"

        tvResultado.text = String.format(localeBR, "%s %.2f", simbolo, resultado)
    }

    // Faz a conta de acordo com o sentido atual da conversão
    private fun converterMoeda(valor: Double): Double {
        return if (realParaDolar) {
            valor / cotacao   // R$ -> US$
        } else {
            valor * cotacao   // US$ -> R$
        }
    }

    // Troca o sentido da conversão e as bandeiras de lado
    private fun inverterMoedas() {
        realParaDolar = !realParaDolar

        if (realParaDolar) {
            ivBandeiraOrigem.setImageResource(R.drawable.bandeira_br)
            ivBandeiraDestino.setImageResource(R.drawable.bandeira_eua)
            etValor.hint = "Valor em R$"
        } else {
            ivBandeiraOrigem.setImageResource(R.drawable.bandeira_eua)
            ivBandeiraDestino.setImageResource(R.drawable.bandeira_br)
            etValor.hint = "Valor em US$"
        }

        // O resultado antigo não vale mais para o novo sentido
        tvResultado.text = "Resultado"
    }

    private fun limparCampos() {
        etValor.text.clear()
        tvResultado.text = "Resultado"
        etValor.requestFocus()
    }
}