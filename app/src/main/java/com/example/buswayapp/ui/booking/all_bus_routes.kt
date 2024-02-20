package com.example.buswayapp.ui.booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.buswayapp.R
import com.example.buswayapp.api.api_resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class all_bus_routes : AppCompatActivity() {

    private lateinit var BusesContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_bus_routes)


        val departureCity = intent.getStringExtra("departureCity")
        val arrivalCity = intent.getStringExtra("arrivalCity")
        val date = intent.getStringExtra("date")
        BusesContainer = findViewById(R.id.BusesContainer)

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val data = api_resource()
                val result = data.get_schedule_info(
                    date.toString(),
                    departureCity.toString(),
                    arrivalCity.toString())
                if (result.isNotEmpty()) {
                    //вызов функции отрисовки блоков
                    supportActionBar?.title = "Рейсы ${departureCity} -> ${arrivalCity}"
                    for (scheduleInfo in result) {
                        val res_block = createBusSchedulePage(
                            scheduleInfo.pk,
                            scheduleInfo.origin,
                            scheduleInfo.destination,
                            scheduleInfo.departure_time,
                            scheduleInfo.arrival_time,
                            scheduleInfo.available_seats,
                            scheduleInfo.date,
                            scheduleInfo.price,
                            scheduleInfo.duration,
                            scheduleInfo.bus.brand,
                            scheduleInfo.bus.license_plate,
                            scheduleInfo.distance,
                            scheduleInfo.bus.total_seats
                        )
                        BusesContainer.addView(res_block)
                    }
                } else {
                    // Обработка случая, когда список пуст
                    Log.e("BusActivity", "Response failed - result is empty")

                    val error = createBusEpty()
                    BusesContainer.addView(error)
                }
            } catch (e: Exception) {
                // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                Log.e("BusActivity", "Error during response", e)
                e.printStackTrace()
            }
        }

    }

    private fun createBusEpty(): LinearLayout{
        //общий блок
        val schedulePage = LinearLayout(this)
        val padding_in_layout = 16

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            200
        )
        layoutParams.setMargins(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)

        schedulePage.layoutParams = layoutParams
        schedulePage.orientation = LinearLayout.VERTICAL
        val backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.rounded_background)
        schedulePage.background = backgroundDrawable

        val error_text = TextView(this)
        error_text.text = "Рейсов по данным параметрам нет"
        error_text.setTextAppearance(R.style.PageTitle)
        error_text.setPadding(100, padding_in_layout, padding_in_layout, padding_in_layout)

        schedulePage.addView(error_text)

        return schedulePage
    }

    private fun createBusSchedulePage(schedule_id: Int, origin: String, destination: String, departure_time: String, arrival_time:String, available_seats: Int, date: String, price: Int, duration: String, brand: String, license_plate: String, distance: Int, total_seats: Int): LinearLayout{
        //общий блок
        val schedulePage = LinearLayout(this)
        val padding_in_layout = 16

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            800
        )
        layoutParams.setMargins(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)

        schedulePage.layoutParams = layoutParams
        schedulePage.orientation = LinearLayout.VERTICAL
        val backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.rounded_background)
        schedulePage.background = backgroundDrawable

        // блок с верхнем блоком времени и местом прибытием
        val top_block_inf = LinearLayout(this)
        val top_block_layout_inf = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            280
        )
        top_block_layout_inf.setMargins(padding_in_layout, 60, padding_in_layout, padding_in_layout)

        val background_top__block_inf = ContextCompat.getDrawable(this, R.drawable.rounded_block)
        top_block_inf.layoutParams = top_block_layout_inf
        top_block_inf.orientation = LinearLayout.VERTICAL
        top_block_inf.background = background_top__block_inf

        // верзхний блок с вермеменем и местом прибытием
        val timeblock = LinearLayout(this)
        val block_layut_time = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            100
        )
        block_layut_time.setMargins(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)


        timeblock.layoutParams = block_layut_time
        timeblock.orientation = LinearLayout.HORIZONTAL

        //нижний блок с вермеменем и местом прибытием
        val timeblock2 = LinearLayout(this)
        val block_layut_time2 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            100
        )
        block_layut_time2.setMargins(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)


        timeblock2.layoutParams = block_layut_time
        timeblock2.orientation = LinearLayout.HORIZONTAL



        val bottom_block = LinearLayout(this)
        val bottom_block_layout = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            150
        )
        bottom_block_layout.setMargins(padding_in_layout, 60, padding_in_layout, padding_in_layout)

        bottom_block.layoutParams = bottom_block_layout
        bottom_block.orientation = LinearLayout.HORIZONTAL


        val departure_time_text = TextView(this)
        departure_time_text.text = departure_time
        departure_time_text.setTextAppearance(R.style.PageTitle)
        val padding_title = 10
        departure_time_text.setPadding(padding_title, padding_in_layout, padding_in_layout, padding_in_layout)

        val arrival_time_text = TextView(this)
        arrival_time_text.text = arrival_time
        arrival_time_text.setTextAppearance(R.style.PageTitle)
        arrival_time_text.setPadding(padding_title, padding_in_layout, padding_in_layout, padding_in_layout)

        val origin_text = TextView(this)
        origin_text.text = origin
        origin_text.setTextAppearance(R.style.PageTitle)
        origin_text.setPadding(170, padding_in_layout, padding_in_layout, padding_in_layout)

        val destination_text = TextView(this)
        destination_text.text = destination
        destination_text.setTextAppearance(R.style.PageTitle)
        destination_text.setPadding(170, padding_in_layout, padding_in_layout, padding_in_layout)

        val available_seats_text = TextView(this)
        available_seats_text.text = " свободных мест -  ${available_seats.toString()}"
        available_seats_text.setTextAppearance(R.style.PageText)
        available_seats_text.setPadding(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)

        val price_text = TextView(this)
        price_text.text = " Цена -  ${price.toString()}"
        price_text.setTextAppearance(R.style.PageText)
        price_text.setPadding(padding_in_layout, padding_in_layout, 400, padding_in_layout)

        val duration_text = TextView(this)
        duration_text.text = " Время пути -  ${duration.toString()}"
        duration_text.setTextAppearance(R.style.PageText)
        duration_text.setPadding(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)

        val button_buy_ticket = Button(this)
        button_buy_ticket.text = "Купить"
        button_buy_ticket.setPadding(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)

        button_buy_ticket.setOnClickListener {
            it.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(300)
                .withEndAction {
                    it.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .start()
                }
                .start()

            val intent = Intent(this, Create_Ticket::class.java)
            intent.putExtra("origin", origin)
            intent.putExtra("destination", destination)
            intent.putExtra("distance", distance.toString())
            intent.putExtra("price", price.toString())
            intent.putExtra("available_seats", available_seats.toString())
            intent.putExtra("departure_time", departure_time)
            intent.putExtra("arrival_time", arrival_time)
            intent.putExtra("duration", duration)
            intent.putExtra("brand", brand)
            intent.putExtra("license_plate", license_plate)
            intent.putExtra("total_seats", total_seats.toString())
            intent.putExtra("schedule_id", schedule_id.toString())
            startActivity(intent)
        }



        timeblock.addView(departure_time_text)
        timeblock.addView(origin_text)

        timeblock2.addView(arrival_time_text)
        timeblock2.addView(destination_text)

        top_block_inf.addView(timeblock)
        top_block_inf.addView(timeblock2)

        bottom_block.addView(price_text)
        bottom_block.addView(button_buy_ticket)


        schedulePage.addView(top_block_inf)
        schedulePage.addView(available_seats_text)
        schedulePage.addView(duration_text)
        schedulePage.addView(bottom_block)


        return schedulePage
    }
}