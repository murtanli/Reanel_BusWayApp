package com.example.buswayapp.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.buswayapp.Auth.Login
import com.example.buswayapp.MainActivity
import com.example.buswayapp.R
import com.example.buswayapp.api.api_resource
import com.example.buswayapp.databinding.FragmentProfileBinding
import com.example.buswayapp.ui.booking.Create_Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val RoutesContainer = binding.RoutesContainer
        val Exit_Button = binding.ExitButton

        val sharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "")

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val data = api_resource()
                val result = data.get_routes_user(
                    user_id.toString())
                if (result.isNotEmpty()) {
                    //вызов функции отрисовки блоков
                    for (routeInfo in result) {
                        val block = create_blocks(
                            routeInfo.route.origin,
                            routeInfo.route.destination,
                            routeInfo.route.departure_time,
                            routeInfo.route.arrival_time,
                            routeInfo.route.distance.toString(),
                            routeInfo.ticket_status,
                            routeInfo.sel_seat.toString(),
                            routeInfo.route.duration.toString()
                        )

                        RoutesContainer.addView(block)
                    }
                } else {
                    // Обработка случая, когда список пуст
                    Log.e("BusActivity", "Response failed - result is empty")
                }
            } catch (e: Exception) {
                // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                Log.e("BusActivity", "Error during response", e)
                e.printStackTrace()
            }
        }


        Exit_Button.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("login", "false")
            editor.apply()
            val intent = Intent(requireContext(), Login::class.java)
            startActivity(intent)
        }

        return root
    }

    private fun create_blocks (origin: String, destination: String, departure_time: String, arrival_time: String, distance: String, ticket_Status: String, sel_seat: String, duration: String): LinearLayout{


        val BlockePage = LinearLayout(requireContext())
        val padding_in_layout = 16

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            800
        )
        layoutParams.setMargins(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)

        BlockePage.layoutParams = layoutParams
        BlockePage.orientation = LinearLayout.VERTICAL
        val backgroundDrawable = ContextCompat.getDrawable(requireContext() , R.drawable.rounded_background)
        BlockePage.background = backgroundDrawable

        // блок с верхнем блоком времени и местом прибытием
        val top_block_inf = LinearLayout(requireContext())
        val top_block_layout_inf = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            280
        )
        top_block_layout_inf.setMargins(padding_in_layout, 60, padding_in_layout, padding_in_layout)

        val background_top__block_inf = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_block)
        top_block_inf.layoutParams = top_block_layout_inf
        top_block_inf.orientation = LinearLayout.VERTICAL
        top_block_inf.background = background_top__block_inf

        // верзхний блок с вермеменем и местом прибытием
        val timeblock = LinearLayout(requireContext())
        val block_layut_time = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            100
        )
        block_layut_time.setMargins(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)


        timeblock.layoutParams = block_layut_time
        timeblock.orientation = LinearLayout.HORIZONTAL

        //нижний блок с вермеменем и местом прибытием
        val timeblock2 = LinearLayout(requireContext())
        val block_layut_time2 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            100
        )
        block_layut_time2.setMargins(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)


        timeblock2.layoutParams = block_layut_time
        timeblock2.orientation = LinearLayout.HORIZONTAL



        val bottom_block = LinearLayout(requireContext())
        val bottom_block_layout = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            150
        )
        bottom_block_layout.setMargins(padding_in_layout, 60, padding_in_layout, padding_in_layout)

        bottom_block.layoutParams = bottom_block_layout
        bottom_block.orientation = LinearLayout.HORIZONTAL


        val departure_time_text = TextView(requireContext())
        departure_time_text.text = departure_time
        departure_time_text.setTextAppearance(R.style.PageTitle)
        val padding_title = 10
        departure_time_text.setPadding(padding_title, padding_in_layout, padding_in_layout, padding_in_layout)

        val arrival_time_text = TextView(requireContext())
        arrival_time_text.text = arrival_time
        arrival_time_text.setTextAppearance(R.style.PageTitle)
        arrival_time_text.setPadding(padding_title, padding_in_layout, padding_in_layout, padding_in_layout)

        val origin_text = TextView(requireContext())
        origin_text.text = origin
        origin_text.setTextAppearance(R.style.PageTitle)
        origin_text.setPadding(170, padding_in_layout, padding_in_layout, padding_in_layout)

        val destination_text = TextView(requireContext())
        destination_text.text = destination
        destination_text.setTextAppearance(R.style.PageTitle)
        destination_text.setPadding(170, padding_in_layout, padding_in_layout, padding_in_layout)

        val available_seats_text = TextView(requireContext())
        available_seats_text.text = " Расстояние -  ${distance.toString()}"
        available_seats_text.setTextAppearance(R.style.PageText)
        available_seats_text.setPadding(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)

        val price_text = TextView(requireContext())
        price_text.text = " Место -  ${sel_seat.toString()}"
        price_text.setTextAppearance(R.style.PageText)
        price_text.setPadding(padding_in_layout, padding_in_layout, 400, padding_in_layout)

        val duration_text = TextView(requireContext())
        duration_text.text = " Статус билета -  ${ticket_Status.toString()}"
        duration_text.setTextAppearance(R.style.PageText)
        duration_text.setPadding(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)




        timeblock.addView(departure_time_text)
        timeblock.addView(origin_text)

        timeblock2.addView(arrival_time_text)
        timeblock2.addView(destination_text)

        top_block_inf.addView(timeblock)
        top_block_inf.addView(timeblock2)

        bottom_block.addView(price_text)


        BlockePage.addView(top_block_inf)
        BlockePage.addView(available_seats_text)
        BlockePage.addView(duration_text)
        BlockePage.addView(bottom_block)


        return BlockePage
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}