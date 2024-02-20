package com.example.buswayapp.ui.booking

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.buswayapp.Auth.Login
import com.example.buswayapp.MainActivity
import com.example.buswayapp.api.api_resource
import com.example.buswayapp.databinding.FragmentBookingBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingFragment : Fragment() {

    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(BookingViewModel::class.java)

        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as? MainActivity)?.act_bar()

        val departureCityEditText = binding.departureCityEditText
        val arrivalCityEditText = binding.arrivalCityEditText
        val date = binding.setDate
        val searchButton = binding.searchButton


        date.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext())

            datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                // Здесь можно обработать выбранную пользователем дату
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                // Устанавливаем выбранную дату в EditText для даты выезда
                date.setText(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time))

            }

            // Показываем DatePickerDialog
            datePickerDialog.show()
        }

        searchButton.setOnClickListener {
            if (!departureCityEditText.text.isNullOrEmpty() && !arrivalCityEditText.text.isNullOrEmpty() && !date.text.isNullOrEmpty()){
                val intent = Intent(requireContext(), all_bus_routes::class.java)
                intent.putExtra("departureCity", departureCityEditText.text.toString())
                intent.putExtra("arrivalCity", arrivalCityEditText.text.toString())
                intent.putExtra("date", date.text.toString())
                startActivity(intent)
            } else {
                val toast = Toast.makeText(requireContext(), "Введите все поля", Toast.LENGTH_LONG)
                toast.show()
            }
        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
