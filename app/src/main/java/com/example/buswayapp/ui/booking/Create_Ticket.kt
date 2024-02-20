package com.example.buswayapp.ui.booking

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.example.buswayapp.Auth.Login
import com.example.buswayapp.MainActivity
import com.example.buswayapp.R
import com.example.buswayapp.api.api_resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Create_Ticket : AppCompatActivity() {

    private lateinit var passanger_fullname: TextView
    private lateinit var Seat_reservation_sel: TextView

    private lateinit var pay_text: TextView

    private lateinit var switch_nal: Switch
    private lateinit var switch_card: Switch
    private lateinit var button_pay: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_ticket)

        passanger_fullname = findViewById(R.id.Passanger_fullname)
        Seat_reservation_sel = findViewById(R.id.Seat_reservation_sel)
        pay_text = findViewById(R.id.pay_text)
        switch_nal = findViewById(R.id.switch_nal)
        switch_card = findViewById(R.id.switch_card)
        button_pay = findViewById(R.id.button_pay)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val sharedPreference = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val first_name = sharedPreference.getString("first_name", "")
        val last_name = sharedPreference.getString("last_name", "")

        val price = intent.getStringExtra("price")
        val available_seats = intent.getStringExtra("available_seats")
        val brand = intent.getStringExtra("brand")
        val license_plate = intent.getStringExtra("license_plate")
        val total_seats = intent.getStringExtra("total_seats")
        val schedule_id = intent.getStringExtra("schedule_id")
        val price_text = price.toString()




        if (!first_name.toString().isNullOrBlank() && !last_name.toString().isNullOrBlank()) {
            passanger_fullname.text = "${first_name}  ${last_name}"
        } else {
            passanger_fullname.text = "Имя или фамилия не заданы"
        }

        if (!price_text.isNullOrBlank()) {
            pay_text.text = "К оплате - ${price_text}"
        }


        val number_seat = intent.getStringExtra("selected_seat")

        if (!number_seat.isNullOrBlank()){
            Seat_reservation_sel.text = number_seat
            Seat_reservation_sel.setTextColor(getColor(R.color.green))
        } else {
            Seat_reservation_sel.setOnClickListener {
                val intent = Intent(this, Seat_reservation::class.java)
                intent.putExtra("free_seats", available_seats)
                intent.putExtra("brand", brand)
                intent.putExtra("license_plate", license_plate)
                intent.putExtra("Seat_reservation_sel", Seat_reservation_sel.toString())
                intent.putExtra("price", price.toString())
                intent.putExtra("schedule_id", schedule_id)
                startActivity(intent)
            }
        }

        button_pay.setOnClickListener {
            if (switch_nal.isChecked && switch_card.isChecked) {
                val toast = Toast.makeText(this, "Выберите только один способ оплаты", Toast.LENGTH_LONG)
                toast.show()
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                        val user_id = sharedPreferences.getString("user_id", "")


                        val data = api_resource()
                        val result = data.create_Ticket(
                            user_id.toString(),
                            schedule_id.toString(),
                            number_seat.toString()
                        )

                        if (result != null) {
                            val intent = Intent(this@Create_Ticket, MainActivity::class.java)
                            startActivity(intent)
                            val toast = Toast.makeText(this@Create_Ticket, result.message, Toast.LENGTH_LONG)
                            toast.show()

                        } else {
                            // Обработка случая, когда result равен null
                            Log.e("LoginActivity", "Login failed - result is null")
                        }
                    } catch (e: Exception) {
                        // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                        Log.e("LoginActivity", "Error during login", e)
                        e.printStackTrace()
                    }
                }
            }
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Проверяем, была ли нажата кнопка назад
        if (item.itemId == android.R.id.home) {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            // Завершаем текущую активность
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {

    }
}