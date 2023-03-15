package com.kreuzwerker.awsstarter.user.adapters.db

import com.kreuzwerker.awsstarter.user.adapters.db.entity.UserJpaEntity
import com.kreuzwerker.awsstarter.user.adapters.db.repositories.UserJpaRepository
import com.kreuzwerker.awsstarter.user.application.ports.outbound.UserDatabaseOutputPort
import com.kreuzwerker.awsstarter.user.domain.models.User
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserDatabaseAdapter(private val userJpaRepository: UserJpaRepository) : UserDatabaseOutputPort {
    override fun getUser(id: String): User {
        val userJpaEntity = userJpaRepository.findByUserId(id)
        return if (userJpaEntity != null) {
            User.of(userJpaEntity)
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

    override fun getAllUsers(): List<User> {
        val userJpaEntities = userJpaRepository.findAll()
        return if (!userJpaEntities.isNullOrEmpty()) {
            userJpaEntities.map { User.of(it) }
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

    override fun createUser(user: User): User {
        val existingUser =
            userJpaRepository.findByFirstNameAndLastNameAndDateOfBirth(user.firstName, user.lastName, user.dateOfBirth)
        if (existingUser == null) {
            val userJpaEntity = UserJpaEntity.of(user)
            val savedEntity = userJpaRepository.save(userJpaEntity)
            return User.of(savedEntity)
        } else {
            throw ResponseStatusException(HttpStatus.CONFLICT)
        }
    }

    override fun updateUser(user: User): User {
        val userJpaEntity =
            userJpaRepository.findByUserId(user.userId)
        return if (userJpaEntity != null) {
            userJpaEntity.firstName = user.firstName
            userJpaEntity.lastName = user.lastName
            userJpaEntity.dateOfBirth = user.dateOfBirth
            val savedEntity = userJpaRepository.save(userJpaEntity)
            User.of(savedEntity)
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

    override fun deleteUser(user: User) {
        userJpaRepository.deleteByUserId(user.userId)
    }

}