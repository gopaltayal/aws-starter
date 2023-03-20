package com.kreuzwerker.awsstarter.user.adapters.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.kreuzwerker.awsstarter.user.adapters.api.dtos.UserRequestDto
import com.kreuzwerker.awsstarter.user.adapters.api.dtos.UserResponseDto
import com.kreuzwerker.awsstarter.user.application.ports.inbound.UserWebInputPort
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

// This annotation allows you to test only controller functionality
// No need to check functionality as that will be unit tested in the use case layer
@WebMvcTest(UserApiAdapter::class)
internal class UserApiAdapterTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var userWebInputPort: UserWebInputPort

    @Test
    fun `getUser fetches user by id when user exists`() {
        val id = "U123"
        val firstName = "Gopal"
        val lastName = "Tayal"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        Mockito.`when`(userWebInputPort.getUser(id))
            .thenReturn(UserResponseDto(id, lastName, firstName, dateOfBirth))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/$id")).andExpect(status().isOk).andExpect(
            content().contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(jsonPath("$.lastName").value(lastName))
            .andExpect(jsonPath("$.firstName").value(firstName))
            .andExpect(jsonPath("$.dateOfBirth").value(dateOfBirth.toString()))
            .andExpect(jsonPath("$.userId").value(id))
    }

    @Test
    fun `getUser wraps into error response user doesn't exist`() {
        val id = "U123"
        Mockito.`when`(userWebInputPort.getUser(id))
            .thenThrow(ResponseStatusException(HttpStatus.NOT_FOUND))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/$id")).andExpect(status().isNotFound).andExpect(
            content().contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(jsonPath("$.statusCode").value(404))
            .andExpect(jsonPath("$.message").value("Couldn't find user in the system please check the ID"))
    }

    @Test
    fun `getAllUsers delegates to input port when users exist`() {
        val id = "U123"
        val firstName = "Gopal"
        val lastName = "Tayal"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        Mockito.`when`(userWebInputPort.getAllUsers())
            .thenReturn(listOf(UserResponseDto(id, lastName, firstName, dateOfBirth)))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user")).andExpect(status().isOk).andExpect(
            content().contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(jsonPath("$.size()").value(1))
            .andExpect(jsonPath("$[0].lastName").value(lastName))
            .andExpect(jsonPath("$[0].firstName").value(firstName))
            .andExpect(jsonPath("$[0].dateOfBirth").value(dateOfBirth.toString()))
            .andExpect(jsonPath("$[0].userId").value(id))
    }

    @Test
    fun `getAllUsers wraps into error response when no users exist`() {
        Mockito.`when`(userWebInputPort.getAllUsers())
            .thenThrow(ResponseStatusException(HttpStatus.NOT_FOUND))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user")).andExpect(status().isNotFound).andExpect(
            content().contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(jsonPath("$.statusCode").value(404))
            .andExpect(jsonPath("$.message").value("Couldn't find user in the system please check the ID"))
    }

    @Test
    fun `createUser creates non-existent user`() {
        val id = "U123"
        val firstName = "Gopal"
        val lastName = "Tayal"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        val createRequestDto = UserRequestDto(lastName, firstName, dateOfBirth)
        val userResponseDto = UserResponseDto(id, lastName, firstName, dateOfBirth)
        Mockito.`when`(userWebInputPort.createUser(createRequestDto))
            .thenReturn(userResponseDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/user").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(createRequestDto))
        ).andExpect(status().isOk).andExpect(
            content().contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(jsonPath("$.lastName").value(lastName))
            .andExpect(jsonPath("$.firstName").value(firstName))
            .andExpect(jsonPath("$.dateOfBirth").value(dateOfBirth.toString()))
            .andExpect(jsonPath("$.userId").value(id))
        Mockito.verify(userWebInputPort, times(1))
            .createUser(createRequestDto)
    }

    @Test
    fun `createUser wraps into error response when user already exists`() {
        val firstName = "Gopal"
        val lastName = "Tayal"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        val createRequestDto = UserRequestDto(lastName, firstName, dateOfBirth)
        Mockito.`when`(userWebInputPort.createUser(createRequestDto))
            .thenThrow(ResponseStatusException(HttpStatus.CONFLICT))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/user").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(createRequestDto))
        ).andExpect(status().isConflict).andExpect(
            content().contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(jsonPath("$.statusCode").value(409))
            .andExpect(jsonPath("$.message").value("User already exists in the system!"))
    }

    @Test
    fun `createUser wraps into error response on bad request`() {
        val firstName = "Gopal"
        val lastName = ""
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        val createRequestDto = UserRequestDto(lastName, firstName, dateOfBirth)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/user").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(createRequestDto))
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `updateUser updates existent user`() {
        val id = "U123"
        val firstName = "Gopal"
        val lastName = "Tayal"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        val updateRequestDto = UserRequestDto(lastName, firstName, dateOfBirth)
        val userResponseDto = UserResponseDto(id, lastName, firstName, dateOfBirth)
        Mockito.`when`(userWebInputPort.updateUser(id, updateRequestDto))
            .thenReturn(userResponseDto)

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/v1/user/$id").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(updateRequestDto))
        ).andExpect(status().isOk).andExpect(
            content().contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(jsonPath("$.lastName").value(lastName))
            .andExpect(jsonPath("$.firstName").value(firstName))
            .andExpect(jsonPath("$.dateOfBirth").value(dateOfBirth.toString()))
            .andExpect(jsonPath("$.userId").value(id))
        Mockito.verify(userWebInputPort, times(1))
            .updateUser(id, updateRequestDto)
    }

    @Test
    fun `updateUser wraps into error response when user is not found`() {
        val id = "U123"
        val firstName = "Gopal"
        val lastName = "Tayal"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        val updateRequestDto = UserRequestDto(lastName, firstName, dateOfBirth)
        Mockito.`when`(userWebInputPort.updateUser(id, updateRequestDto))
            .thenThrow(ResponseStatusException(HttpStatus.NOT_FOUND))

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/v1/user/$id").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(updateRequestDto))
        )
            .andExpect(status().isNotFound).andExpect(
                content().contentType(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(jsonPath("$.statusCode").value(404))
            .andExpect(jsonPath("$.message").value("Couldn't find user in the system please check the ID"))
    }

    @Test
    fun `updateUser wraps into error response on bad request`() {
        val firstName = "Gopal"
        val lastName = ""
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        val createRequestDto = UserRequestDto(lastName, firstName, dateOfBirth)

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/v1/user/U123").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(createRequestDto))
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `deleteUser deletes user by id when user exists`() {
        val id = "U123"
        val firstName = "Gopal"
        val lastName = "Tayal"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        Mockito.`when`(userWebInputPort.deleteUser(id))
            .thenReturn(UserResponseDto(id, lastName, firstName, dateOfBirth))

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/user/$id")).andExpect(status().isOk).andExpect(
            content().contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(jsonPath("$.lastName").value(lastName))
            .andExpect(jsonPath("$.firstName").value(firstName))
            .andExpect(jsonPath("$.dateOfBirth").value(dateOfBirth.toString()))
            .andExpect(jsonPath("$.userId").value(id))
    }

    @Test
    fun `deleteUser wraps into error response when user is not found`() {
        val id = "U123"
        Mockito.`when`(userWebInputPort.deleteUser(id))
            .thenThrow(ResponseStatusException(HttpStatus.NOT_FOUND))

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/user/$id")).andExpect(status().isNotFound).andExpect(
            content().contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(jsonPath("$.statusCode").value(404))
            .andExpect(jsonPath("$.message").value("Couldn't find user in the system please check the ID"))
    }
}