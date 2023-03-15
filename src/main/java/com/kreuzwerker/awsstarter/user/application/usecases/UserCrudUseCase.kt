package com.kreuzwerker.awsstarter.user.application.usecases

import com.kreuzwerker.awsstarter.user.adapters.api.dtos.UserRequestDto
import com.kreuzwerker.awsstarter.user.adapters.api.dtos.UserResponseDto
import com.kreuzwerker.awsstarter.user.application.ports.inbound.UserWebInputPort
import com.kreuzwerker.awsstarter.user.application.ports.outbound.UserDatabaseOutputPort
import com.kreuzwerker.awsstarter.user.domain.models.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class UserCrudUseCase(private val userDatabaseOutputPort: UserDatabaseOutputPort) : UserWebInputPort {

    override fun createUser(userRequestDto: UserRequestDto): UserResponseDto {
        val user = User.of(userRequestDto)
        val savedUser = userDatabaseOutputPort.createUser(user)
        return UserResponseDto.of(savedUser)
    }

    override fun getUser(id: String): UserResponseDto {
        val user = userDatabaseOutputPort.getUser(id)
        return UserResponseDto.of(user)
    }

    override fun getAllUsers(): List<UserResponseDto> {
        val users = userDatabaseOutputPort.getAllUsers()
            return users.map { UserResponseDto.of(it) }
    }

    override fun updateUser(id: String, userRequestDto: UserRequestDto): UserResponseDto {
        val user = User.of(id, userRequestDto)
        val updatedUser = userDatabaseOutputPort.updateUser(user)
        return UserResponseDto.of(updatedUser)
    }

    override fun deleteUser(id: String): UserResponseDto {
        val user = userDatabaseOutputPort.getUser(id)
        userDatabaseOutputPort.deleteUser(user)
        return UserResponseDto.of(user)
    }
}