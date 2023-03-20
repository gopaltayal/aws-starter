package com.kreuzwerker.awsstarter.user.application.usecases

import com.kreuzwerker.awsstarter.user.adapters.api.dtos.UserRequestDto
import com.kreuzwerker.awsstarter.user.application.ports.outbound.UserDatabaseOutputPort
import com.kreuzwerker.awsstarter.user.domain.models.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
internal class UserCrudUseCaseTest {

    @Mock
    lateinit var userDatabaseOutputPort: UserDatabaseOutputPort

    @InjectMocks
    lateinit var userCrudUseCase: UserCrudUseCase

    // Mockito.any() doesn't work with Kotlin
    // https://stackoverflow.com/questions/59230041/argumentmatchers-any-must-not-be-null
    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

    @Test
    fun `createUser delegates to output port`(){
        val id = "U123"
        val firstName = "Gopal"
        val lastName = "Tayal"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        val userRequestDto = UserRequestDto(lastName, firstName, dateOfBirth)
        val user = User(id, lastName, firstName, dateOfBirth)
        Mockito.`when`(userDatabaseOutputPort.createUser(any(User::class.java))).thenReturn(user)

        val userResponseDto = userCrudUseCase.createUser(userRequestDto)

        Assertions.assertThat(userResponseDto.userId).isEqualTo(id)
        Assertions.assertThat(userResponseDto.firstName).isEqualTo(firstName)
        Assertions.assertThat(userResponseDto.lastName).isEqualTo(lastName)
        Assertions.assertThat(userResponseDto.dateOfBirth).isEqualTo(dateOfBirth)
        Mockito.verify(userDatabaseOutputPort, times(1)).createUser(any(User::class.java))
    }

    @Test
    fun `getUser delegates to output port`(){
        val id = "U123"
        val firstName = "Gopal"
        val lastName = "Tayal"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        val user = User(id, lastName, firstName, dateOfBirth)
        Mockito.`when`(userDatabaseOutputPort.getUser(id)).thenReturn(user)

        val userResponseDto = userCrudUseCase.getUser(id)

        Assertions.assertThat(userResponseDto.userId).isEqualTo(id)
        Assertions.assertThat(userResponseDto.firstName).isEqualTo(firstName)
        Assertions.assertThat(userResponseDto.lastName).isEqualTo(lastName)
        Assertions.assertThat(userResponseDto.dateOfBirth).isEqualTo(dateOfBirth)
        Mockito.verify(userDatabaseOutputPort, times(1)).getUser(id)
    }

    @Test
    fun `getAllUsers delegates to output port`(){
        val id = "U123"
        val firstName = "Gopal"
        val lastName = "Tayal"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        val user = User(id, lastName, firstName, dateOfBirth)
        Mockito.`when`(userDatabaseOutputPort.getAllUsers()).thenReturn(listOf(user))

        val userResponseDtos = userCrudUseCase.getAllUsers()

        Assertions.assertThat(userResponseDtos.size).isEqualTo(1)
        Assertions.assertThat(userResponseDtos[0].userId).isEqualTo(id)
        Assertions.assertThat(userResponseDtos[0].firstName).isEqualTo(firstName)
        Assertions.assertThat(userResponseDtos[0].lastName).isEqualTo(lastName)
        Assertions.assertThat(userResponseDtos[0].dateOfBirth).isEqualTo(dateOfBirth)
        Mockito.verify(userDatabaseOutputPort, times(1)).getAllUsers()
    }

    @Test
    fun `updateUser delegates to output port`(){
        val id = "U123"
        val firstName = "Gopal"
        val lastName = "Tayal"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        val user = User(id, lastName, firstName, dateOfBirth)
        val userRequestDto = UserRequestDto(lastName, firstName, dateOfBirth)
        Mockito.`when`(userDatabaseOutputPort.updateUser(any(User::class.java))).thenReturn(user)

        val userResponseDto = userCrudUseCase.updateUser(id, userRequestDto)

        Assertions.assertThat(userResponseDto.userId).isEqualTo(id)
        Assertions.assertThat(userResponseDto.firstName).isEqualTo(firstName)
        Assertions.assertThat(userResponseDto.lastName).isEqualTo(lastName)
        Assertions.assertThat(userResponseDto.dateOfBirth).isEqualTo(dateOfBirth)
        Mockito.verify(userDatabaseOutputPort, times(1)).updateUser(any(User::class.java))
    }

    @Test
    fun `deleteUser delegates to output port`(){
        val id = "U123"
        val firstName = "Gopal"
        val lastName = "Tayal"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        val user = User(id, lastName, firstName, dateOfBirth)
        Mockito.`when`(userDatabaseOutputPort.getUser(id)).thenReturn(user)

        val userResponseDto = userCrudUseCase.deleteUser(id)

        Assertions.assertThat(userResponseDto.userId).isEqualTo(id)
        Assertions.assertThat(userResponseDto.firstName).isEqualTo(firstName)
        Assertions.assertThat(userResponseDto.lastName).isEqualTo(lastName)
        Assertions.assertThat(userResponseDto.dateOfBirth).isEqualTo(dateOfBirth)
        Mockito.verify(userDatabaseOutputPort, times(1)).deleteUser(user)
    }
}