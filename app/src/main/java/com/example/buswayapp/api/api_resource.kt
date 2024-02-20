package com.example.buswayapp.api
import android.util.Log
import com.example.buswayapp.api.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class api_resource {
    suspend fun logIn(login: String, password: String): LoginResponse {
        val apiUrl = "http://5.63.159.179:8100/login/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"  // Используйте POST вместо GET
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                // Создаем JSON-строку с логином и паролем
                val jsonInputString = "{\"login\":\"$login\",\"password\":\"$password\"}"

                // Отправляем JSON в тело запроса
                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                gson.fromJson(response.toString(), LoginResponse::class.java)
            } catch (e: Exception) {
                Log.e("LoginError", "Error fetching or parsing login data ", e)
                throw e
            }
        }
    }

    suspend fun Sign_in(first_name:String, last_name:String, email: String, login: String, password: String): RegistrResponse {
        val apiUrl = "http://5.63.159.179:8100/register/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"  // Используйте POST вместо GET
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                // Создаем JSON-строку с логином и паролем
                val jsonInputString = "{\"first_name\":\"$first_name\",\"last_name\":\"$last_name\",\"email\":\"$email\",\"login\":\"$login\",\"password\":\"$password\"}"

                // Отправляем JSON в тело запроса
                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                gson.fromJson(response.toString(), RegistrResponse::class.java)
            } catch (e: Exception) {
                Log.e("LoginError", "Error fetching or parsing login data ", e)
                throw e
            }
        }
    }

    suspend fun get_schedule_info(departure_date: String, origin: String, destination: String): List<ScheduleInfo> {
        val apiUrl = "http://5.63.159.179:8100/schedule-info/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonInputString = "{\"departure_date\":\"$departure_date\",\"origin\":\"$origin\",\"destination\":\"$destination\"}"

                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                val scheduleArray = gson.fromJson(response.toString(), Array<ScheduleInfo>::class.java)
                scheduleArray.toList()

            } catch (e: Exception) {
                // Обработка ошибок
                throw e
            }
        }
    }

    suspend fun get_bus_seats(brand: String, license_plate: String): List<Seat> {
        val apiUrl = "http://5.63.159.179:8100/bus-seats/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true
                Log.e("666", "$brand $license_plate")
                val jsonInputString = "{\"brand\":\"$brand\",\"license_plate\":\"$license_plate\"}"

                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                val scheduleArray = gson.fromJson(response.toString(), Array<Seat>::class.java)
                scheduleArray.toList()

            } catch (e: Exception) {
                // Обработка ошибок
                throw e
            }
        }
    }

    suspend fun create_Ticket(user_id:String, schedule_id:String, seat_nubmer: String): CreateTicketResponse {
        val apiUrl = "http://5.63.159.179:8100/create-ticket/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"  // Используйте POST вместо GET
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true
                // Создаем JSON-строку с логином и паролем
                val jsonInputString = "{\"user_id\":\"$user_id\",\"schedule_id\":\"$schedule_id\",\"seat_number\":\"$seat_nubmer\"}"

                // Отправляем JSON в тело запроса
                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                gson.fromJson(response.toString(), CreateTicketResponse::class.java)
            } catch (e: Exception) {
                Log.e("LoginError", "Error fetching or parsing data ", e)
                throw e
            }
        }
    }

    suspend fun get_routes_user(user_id: String): List<Sel_route> {
        val apiUrl = "http://5.63.159.179:8100/selected-tickets/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonInputString = "{\"user_id\":\"$user_id\"}"

                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                val scheduleArray = gson.fromJson(response.toString(), Array<Sel_route>::class.java)
                scheduleArray.toList()

            } catch (e: Exception) {
                // Обработка ошибок
                throw e
            }
        }
    }
}