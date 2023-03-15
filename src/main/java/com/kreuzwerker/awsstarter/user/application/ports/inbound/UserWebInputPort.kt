package com.kreuzwerker.awsstarter.user.application.ports.inbound

import com.kreuzwerker.awsstarter.user.adapters.api.dtos.UserRequestDto
import com.kreuzwerker.awsstarter.user.adapters.api.dtos.UserResponseDto

interface UserWebInputPort {
    fun createUser(userRequestDto: UserRequestDto): UserResponseDto
    fun getUser(id: String): UserResponseDto
    fun getAllUsers(): List<UserResponseDto>
    fun updateUser(id: String, userRequestDto: UserRequestDto): UserResponseDto
    fun deleteUser(id: String): UserResponseDto
}