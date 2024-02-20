package com.example.buswayapp.ui.booking

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.buswayapp.R
import com.example.buswayapp.api.api_resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Seat_reservation : AppCompatActivity() {
    private var sel_seat = 0
    private var selected_number_seat = 0
    private lateinit var BusesContainer: LinearLayout
    private lateinit var free_seats: TextView
    private lateinit var number: TextView
    private lateinit var brand_bus: TextView
    private lateinit var Bus_sel_seat_button: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_reservation)

        val available_seats = intent.getStringExtra("free_seats")
        val brand = intent.getStringExtra("brand")
        val license_plate = intent.getStringExtra("license_plate")
        val shedule_id = intent.getStringExtra("schedule_id")
        val price = intent.getStringExtra("price")

        brand_bus = findViewById(R.id.brand_bus)
        number = findViewById(R.id.number)
        free_seats = findViewById(R.id.free_seats)
        BusesContainer = findViewById(R.id.BusesContainer)
        Bus_sel_seat_button = findViewById(R.id.Bus_sel_seat)

        free_seats.text = " Свободных мест - ${available_seats.toString()}"
        number.text = " Номер автобуса - ${license_plate.toString()}"
        brand_bus.text = " Марка авотбуса - ${brand.toString()}"



        GlobalScope.launch(Dispatchers.Main) {
            try {
                val data = api_resource()
                val result = data.get_bus_seats(
                    brand.toString(),
                    license_plate.toString())
                if (result.isNotEmpty()) {
                    for (seat in result) {
                        Log.e("666", "${seat.seat_number}  ${seat.is_occupied} ${seat.message}")

                        val seatView = TextView(this@Seat_reservation)
                        if (seat.is_occupied) {
                            seatView.text = "Место ${seat.seat_number} - занято"
                            seatView.setTextColor(Color.RED) // Место занято
                            seatView.textSize = 20F
                            seatView.setPadding(100,10,10,50)
                        } else {
                            seatView.text = "Место ${seat.seat_number} - свободно"
                            seatView.setTextColor(Color.GREEN) // Место свободно
                            seatView.textSize = 20F
                            seatView.setPadding(100,10,10,50)
                        }
                        seatView.setOnClickListener {
                            if (sel_seat == 1) {
                                val toast = Toast.makeText(this@Seat_reservation, "Можно выбрать только одно место", Toast.LENGTH_SHORT)
                                toast.show()
                                seatView.setTextColor(Color.GREEN)
                                sel_seat -= 1
                            } else if (!seat.is_occupied) {
                                seatView.setTextColor(Color.RED)
                                selected_number_seat = seat.seat_number
                                sel_seat += 1
                            } else {
                                val toast = Toast.makeText(this@Seat_reservation, "Место занято", Toast.LENGTH_SHORT)
                                toast.show()
                            }
                        }

                        Bus_sel_seat_button.setOnClickListener {
                            if (sel_seat == 1) {
                                intent = Intent(this@Seat_reservation, Create_Ticket::class.java)
                                intent.putExtra("selected_seat", selected_number_seat.toString())
                                intent.putExtra("schedule_id", shedule_id.toString())

                                intent.putExtra("price", price)
                                startActivity(intent)
                                finish()
                            } else {
                                val toast = Toast.makeText(this@Seat_reservation, "Выберите место", Toast.LENGTH_SHORT)
                                toast.show()
                            }
                        }

                        // Добавляем место в контейнер
                        BusesContainer.addView(seatView)
                    }
                } else {
                    Log.e("Seat_reservation", "Response failed - result is empty")
                }
            } catch (e: Exception) {
                Log.e("Seat_reservation", "Error during response", e)
                e.printStackTrace()
            }
        }
    }


    //private fun create_bus_seats()
}