package com.example.buswayapp.api


data class LoginResponse(
    val user_inf: UserData,
    val error: String,
)

data class UserData(
    val user_id: Int,
    val first_name: String,
    val last_name: String,
    val email: String,
    val login: String,
)

data class RegistrResponse(
    val user_id: Int,
    val error: String
)

data class Bus_info(
    val brand: String,
    val total_seats: Int,
    val license_plate: String
)
data class ScheduleInfo(
    val pk: Int,
    val origin: String,
    val destination: String,
    val departure_time: String,
    val arrival_time: String,
    val available_seats: Int,
    val date: String,
    val price: Int,
    val duration: String,
    val distance: Int,
    val bus: Bus_info,
)

data class ScheduleResponse (
    val schedule: List<ScheduleInfo>
)

data class Seat (
    val seat_number: Int,
    val is_occupied: Boolean,
    val message: String
)

data class Bus_seats(
    val seats: List<Seat>
)

data class CreateTicketResponse (
    val message: String
)

data class Route_inf(
    val origin: String,
    val destination: String,
    val departure_time: String,
    val arrival_time: String,
    val duration: String,
    val distance: Int,
)

data class Sel_route(
    val schedule_id: Int,
    val route: Route_inf,
    val ticket_status: String,
    val sel_seat: String
)
