package com.kreuzwerker.awsstarter.user.adapters.db.repositories

import com.kreuzwerker.awsstarter.user.adapters.db.entity.UserJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface UserJpaRepository : JpaRepository<UserJpaEntity, Long> {
    fun findByUserId(userId: String): UserJpaEntity?

    fun findByFirstNameAndLastNameAndDateOfBirth(firstName: String, lastName: String, dateOfBirth: LocalDate): UserJpaEntity?

    // Can't return the entity back only True/False as an Integer
    fun deleteByUserId(userId: String): Long
}