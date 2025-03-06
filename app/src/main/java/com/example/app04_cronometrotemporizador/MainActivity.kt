package com.example.app04_cronometrotemporizador

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : ComponentActivity() {
    private lateinit var textViewCronometro: TextView
    private lateinit var buttonIniciarCronometro: Button
    private lateinit var buttonDetenerCronometro: Button
    private lateinit var buttonReiniciarCronometro: Button

    private lateinit var editTextTemporizador: EditText
    private lateinit var textViewTemporizador: TextView
    private lateinit var buttonIniciarTemporizador: Button

    private var tiempoInicioCronometro: Long = 0
    private var corriendoCronometro: Boolean = false

    private var tiempoRestanteTemporizador: Long = 0
    private var corriendoTemporizador: Boolean = false
    private var timerTemporizador: Timer? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewCronometro = findViewById(R.id.textViewCronometro)
        buttonIniciarCronometro = findViewById(R.id.buttonIniciarCronometro)
        buttonDetenerCronometro = findViewById(R.id.buttonDetenerCronometro)
        buttonReiniciarCronometro = findViewById(R.id.buttonReiniciarCronometro)

        editTextTemporizador = findViewById(R.id.editTextTemporizador)
        textViewTemporizador = findViewById(R.id.textViewTemporizador)
        buttonIniciarTemporizador = findViewById(R.id.buttonIniciarTemporizador)

        buttonIniciarCronometro.setOnClickListener { iniciarCronometro() }
        buttonDetenerCronometro.setOnClickListener { detenerCronometro() }
        buttonReiniciarCronometro.setOnClickListener { reiniciarCronometro() }

        buttonIniciarTemporizador.setOnClickListener { iniciarTemporizador() }
    }

    private fun iniciarCronometro() {
        if (!corriendoCronometro) {
            tiempoInicioCronometro = SystemClock.uptimeMillis() -
                    (textViewCronometro.text.toString().split(":").let {
                        it[0].toLong() * 3600 + it[1].toLong() * 60 + it[2].toLong()
                    } * 1000)
            corriendoCronometro = true
            actualizarCronometro()
        }
    }

    private fun detenerCronometro() {
        corriendoCronometro = false
    }

    private fun reiniciarCronometro() {
        corriendoCronometro = false
        tiempoInicioCronometro = 0
        textViewCronometro.text = "00:00:00"
    }

    private fun actualizarCronometro() {
        if (corriendoCronometro) {
            val tiempoActual = SystemClock.uptimeMillis() - tiempoInicioCronometro
            val segundos = (tiempoActual / 1000) % 60
            val minutos = (tiempoActual / (1000 * 60)) % 60
            val horas = (tiempoActual / (1000 * 60 * 60))
            textViewCronometro.text = String.format("%02d:%02d:%02d", horas, minutos, segundos)
            android.os.Handler().postDelayed({ actualizarCronometro() }, 1000)
        }
    }

    private fun iniciarTemporizador() {
        if (!corriendoTemporizador) {
            val duracion = editTextTemporizador.text.toString().toLongOrNull() ?: 0
            tiempoRestanteTemporizador = duracion * 1000
            corriendoTemporizador = true
            actualizarTemporizador()
            timerTemporizador = Timer()
            timerTemporizador?.schedule(0, 1000) {
                tiempoRestanteTemporizador -= 1000
                actualizarTemporizador()
                if (tiempoRestanteTemporizador <= 0) {
                    corriendoTemporizador = false
                    timerTemporizador?.cancel()
                    timerTemporizador = null
                }
            }
        }
    }

    private fun actualizarTemporizador() {
        if (corriendoTemporizador) {
            val segundos = (tiempoRestanteTemporizador / 1000) % 60
            val minutos = (tiempoRestanteTemporizador / (1000 * 60)) % 60
            val horas = (tiempoRestanteTemporizador / (1000 * 60 * 60))
            textViewTemporizador.text = String.format("%02d:%02d:%02d", horas, minutos, segundos)
        } else {
            textViewTemporizador.text = "00:00:00"
        }
    }
}
