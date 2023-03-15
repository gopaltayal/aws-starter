package com.kreuzwerker.awsstarter.user.adapters.db.entity

import com.kreuzwerker.awsstarter.user.domain.models.User
import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "users")
class UserJpaEntity(var userId: String, var firstName: String, var lastName: String, var dateOfBirth: LocalDate) :
    BaseEntity() {
    companion object {
        fun of(user: User): UserJpaEntity {
            return UserJpaEntity(UUID.randomUUID().toString(), user.firstName, user.lastName, user.dateOfBirth)
        }
    }
}



