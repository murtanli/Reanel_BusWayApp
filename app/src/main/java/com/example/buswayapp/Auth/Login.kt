package com.example.buswayapp.Auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.buswayapp.MainActivity
import com.example.buswayapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.example.buswayapp.api.api_resource

class Login : AppCompatActivity() {

    private lateinit var but_reg : TextView
    private lateinit var buttonLogin : TextView
    private lateinit var ErrorText : TextView
    private lateinit var login : EditText
    private lateinit var password : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        but_reg = findViewById(R.id.textView2)
        buttonLogin = findViewById(R.id.buttonLogin)
        ErrorText = findViewById(R.id.textView3)
        login = findViewById(R.id.editTextLogin)
        password = findViewById(R.id.editTextPassword)

        ErrorText.text = ""

        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val log = sharedPreferences.getString("login", "")

        if (log == "true"){
            val intent = Intent(this@Login, MainActivity::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {
            val loginText = login?.text?.toString()
            val passwordText = password?.text?.toString()


            if (loginText.isNullOrBlank() || passwordText.isNullOrBlank()) {
                ErrorText.text = "Введите данные в поля"
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        // Вызываем функцию logIn для выполнения запроса
                        val data = api_resource()
                        val result = data.logIn(login.text.toString(), password.text.toString())

                        if (result != null) {
                            if (result.error != "пароль не верный" || result.error != "Пользователя не существует" ) {
                                // Если успешно авторизованы, выводим сообщение об успешной авторизации и обрабатываем данные
                                Log.d("LoginActivity", "Login successful")
                                //Log.d("LoginActivity", "User ID: ${result.user_data.user_id}")
                                ErrorText.text = result.error

                                val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                try {
                                    editor.putString("user_id", result.user_inf.user_id.toString())
                                    editor.putString("first_name", result.user_inf.first_name)
                                    editor.putString("last_name", result.user_inf.last_name)
                                    editor.putString("email", result.user_inf.email)
                                    editor.putString("login", result.user_inf.login)
                                    editor.putString("login", "true")
                                    editor.apply()
                                } catch(e: Exception) {
                                    editor.putString("user_id", result.user_inf.user_id.toString())
                                    editor.putString("login", "true")
                                    editor.apply()
                                }

                                val intent = Intent(this@Login, MainActivity::class.java)
                                startActivity(intent)
                                //ErrorText.setTextColor(R.color.blue)

                            } else {
                                // Если произошла ошибка, выводим сообщение об ошибке
                                Log.e("LoginActivity", "Login failed")
                                ErrorText.text = result.error
                                val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("login", "false")
                                editor.apply()
                            }
                        } else {
                            // Обработка случая, когда result равен null
                            Log.e("LoginActivity", "Login failed - result is null")
                            ErrorText.text = "Ошибка в процессе авторизации ${result.error}"
                        }
                    } catch (e: Exception) {
                        // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                        Log.e("LoginActivity", "Error during login", e)
                        e.printStackTrace()
                        ErrorText.text = "Ошибка входа: Неправильный пароль или профиль не найден"
                        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("login", "false")
                        editor.apply()
                    }
                }
            }
        }

        but_reg.setOnClickListener {
            val intent = Intent(this, Registr::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {

    }
}