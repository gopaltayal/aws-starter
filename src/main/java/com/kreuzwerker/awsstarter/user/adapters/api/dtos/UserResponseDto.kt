package com.kreuzwerker.awsstarter.user.adapters.api.dtos

import com.kreuzwerker.awsstarter.user.domain.models.User
import java.time.LocalDate

data class UserResponseDto(
    var userId: String, var lastName: String, var firstName: String, var dateOfBirth: LocalDate
) {
    companion object {
        fun of(user: User): UserResponseDto {
            return UserResponseDto(user.userId, user.lastName, user.firstName, user.dateOfBirth)
        }
    }
}