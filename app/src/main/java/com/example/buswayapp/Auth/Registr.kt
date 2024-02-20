package com.example.buswayapp.Auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.buswayapp.R
import com.example.buswayapp.api.api_resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.log

class Registr : AppCompatActivity() {

    private lateinit var editTextLogin: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextPassword2: EditText
    private lateinit var first_name_edit: EditText
    private lateinit var last_name_edit: EditText
    private lateinit var email_edit: EditText
    private lateinit var textView6: TextView
    private lateinit var buttonLogin: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registr)

        editTextLogin = findViewById(R.id.editTextLogin)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextPassword2 = findViewById(R.id.editTextPassword2)
        first_name_edit = findViewById(R.id.first_name_edit)
        last_name_edit = findViewById(R.id.last_name_edit)
        email_edit = findViewById(R.id.email_edit)
        textView6 = findViewById(R.id.textView6)
        buttonLogin = findViewById(R.id.buttonLogin)

        buttonLogin.setOnClickListener {

            if (!last_name_edit.text.isNullOrEmpty() && !first_name_edit.text.isNullOrEmpty() && !email_edit.text.isNullOrEmpty() && !editTextLogin.text.isNullOrEmpty() && !editTextPassword.text.isNullOrEmpty() && !editTextPassword2.text.isNullOrEmpty() && editTextPassword.text in editTextPassword2.text) {
                val first_name = first_name_edit?.text?.toString()
                val last_name = last_name_edit?.text?.toString()
                val email = email_edit?.text?.toString()
                val loginText = editTextLogin?.text?.toString()
                val passwordText = editTextPassword?.text?.toString()
                GlobalScope.launch(Dispatchers.Main) {
                    try {

                        val data = api_resource()
                        val result = data.Sign_in(
                            first_name.toString(),
                            last_name.toString(),
                            email.toString(),
                            loginText.toString(),
                            passwordText.toString())

                        if (result != null) {
                            val intent = Intent(this@Registr, Login::class.java)
                            startActivity(intent)
                            textView6.text = result.error

                            val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("user_id", result.user_id.toString())
                            editor.putString("first_name", first_name)
                            editor.putString("last_name", last_name)
                            editor.putString("email", email)
                            editor.putString("login", loginText)
                            editor.putString("login", "true")


                            editor.apply()

                        } else {
                            // Обработка случая, когда result равен null
                            Log.e("LoginActivity", "Login failed - result is null")
                            textView6.text = "Ошибка в процессе авторизации $result.message"
                        }
                    } catch (e: Exception) {
                        // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                        Log.e("LoginActivity", "Error during login", e)
                        e.printStackTrace()
                        textView6.text = "Ошибка входа: Неправильный пароль или профиль уже существует"
                    }
                }
            } else {
                textView6.text = "Пустые поля ! либо пороли не совпадают"
            }
        }
    }
}