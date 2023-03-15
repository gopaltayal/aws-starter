package com.kreuzwerker.awsstarter.user.domain.models

import com.kreuzwerker.awsstarter.user.adapters.api.dtos.UserRequestDto
import com.kreuzwerker.awsstarter.user.adapters.db.entity.UserJpaEntity
import java.time.LocalDate
import java.util.*

data class User(var userId: String, var lastName: String, var firstName: String, var dateOfBirth: LocalDate) {
    companion object {
        fun of(userJpaEntity: UserJpaEntity): User {
            return User(
                userJpaEntity.userId,
                userJpaEntity.lastName,
                userJpaEntity.firstName,
                userJpaEntity.dateOfBirth
            )
        }

        fun of(userRequestDto: UserRequestDto): User {
            return User(
                "",
                userRequestDto.lastName,
                userRequestDto.firstName,
                userRequestDto.dateOfBirth
            )
        }

        fun of(id: String, userRequestDto: UserRequestDto): User {
            return User(
                id,
                userRequestDto.lastName,
                userRequestDto.firstName,
                userRequestDto.dateOfBirth
            )
        }
    }
}