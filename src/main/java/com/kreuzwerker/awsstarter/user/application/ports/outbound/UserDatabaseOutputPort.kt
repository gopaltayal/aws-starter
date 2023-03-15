package com.kreuzwerker.awsstarter.user.application.ports.outbound

import com.kreuzwerker.awsstarter.user.domain.models.User

interface UserDatabaseOutputPort {
    fun getUser(id: String): User
    fun getAllUsers(): List<User>
    fun createUser(user: User) : User
    fun updateUser(user: User) : User
    fun deleteUser(user: User)
}