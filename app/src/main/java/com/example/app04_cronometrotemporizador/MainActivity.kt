package com.example.app04_cronometrotemporizador
//Miembros del equipo:
//Coronel Meza Sergio Daniel
//Sanchez Cruz Sergio Antonio

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
    /**
     * TextView para mostrar el tiempo transcurrido del cronómetro.
     */
    private lateinit var textViewCronometro: TextView

    /**
     * Botón para iniciar el cronómetro.
     */
    private lateinit var buttonIniciarCronometro: Button

    /**
     * Botón para detener el cronómetro.
     */
    private lateinit var buttonDetenerCronometro: Button

    /**
     * Botón para reiniciar el cronómetro.
     */
    private lateinit var buttonReiniciarCronometro: Button

    /**
     * EditText para ingresar la duración del temporizador.
     */
    private lateinit var editTextTemporizador: EditText

    /**
     * TextView para mostrar el tiempo restante del temporizador.
     */
    private lateinit var textViewTemporizador: TextView

    /**
     * Botón para iniciar el temporizador.
     */
    private lateinit var buttonIniciarTemporizador: Button

    /**
     * Tiempo de inicio del cronómetro en milisegundos.
     */
    private var tiempoInicioCronometro: Long = 0

    /**
     * Indica si el cronómetro está corriendo.
     */
    private var corriendoCronometro: Boolean = false

    /**
     * Tiempo restante del temporizador en milisegundos.
     */
    private var tiempoRestanteTemporizador: Long = 0

    /**
     * Indica si el temporizador está corriendo.
     */
    private var corriendoTemporizador: Boolean = false

    /**
     * Instancia de Timer para el temporizador.
     */
    private var timerTemporizador: Timer? = null

    /**
     * Método llamado al crear la actividad.
     * Inicializa las vistas y configura los listeners de los botones.
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa las vistas del cronómetro.
        textViewCronometro = findViewById(R.id.textViewCronometro)
        buttonIniciarCronometro = findViewById(R.id.buttonIniciarCronometro)
        buttonDetenerCronometro = findViewById(R.id.buttonDetenerCronometro)
        buttonReiniciarCronometro = findViewById(R.id.buttonReiniciarCronometro)

        // Inicializa las vistas del temporizador.
        editTextTemporizador = findViewById(R.id.editTextTemporizador)
        textViewTemporizador = findViewById(R.id.textViewTemporizador)
        buttonIniciarTemporizador = findViewById(R.id.buttonIniciarTemporizador)

        // Configura los listeners de los botones del cronómetro.
        buttonIniciarCronometro.setOnClickListener { iniciarCronometro() }
        buttonDetenerCronometro.setOnClickListener { detenerCronometro() }
        buttonReiniciarCronometro.setOnClickListener { reiniciarCronometro() }

        // Configura el listener del botón del temporizador.
        buttonIniciarTemporizador.setOnClickListener { iniciarTemporizador() }
    }

    /**
     * Inicia el cronómetro.
     */
    private fun iniciarCronometro() {
        if (!corriendoCronometro) {
            // Calcula el tiempo de inicio basado en el tiempo mostrado actualmente.
            tiempoInicioCronometro = SystemClock.uptimeMillis() -
                    (textViewCronometro.text.toString().split(":").let {
                        it[0].toLong() * 3600 + it[1].toLong() * 60 + it[2].toLong()
                    } * 1000)
            corriendoCronometro = true
            actualizarCronometro()
        }
    }

    /**
     * Detiene el cronómetro.
     */
    private fun detenerCronometro() {
        corriendoCronometro = false
    }

    /**
     * Reinicia el cronómetro.
     */
    private fun reiniciarCronometro() {
        corriendoCronometro = false
        tiempoInicioCronometro = 0
        textViewCronometro.text = "00:00:00"
    }

    /**
     * Actualiza el TextView del cronómetro con el tiempo transcurrido.
     */
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

    /**
     * Inicia el temporizador.
     */
    private fun iniciarTemporizador() {
        if (!corriendoTemporizador) {
            // Obtiene la duración del temporizador del EditText.
            val duracion = editTextTemporizador.text.toString().toLongOrNull() ?: 0
            tiempoRestanteTemporizador = duracion * 1000
            corriendoTemporizador = true
            actualizarTemporizador()
            // Crea un Timer para actualizar el tiempo restante cada segundo.
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

    /**
     * Actualiza el TextView del temporizador con el tiempo restante.
     */
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
