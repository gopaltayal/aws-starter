package com.kreuzwerker.awsstarter.e2e

import com.kreuzwerker.awsstarter.user.adapters.api.dtos.UserRequestDto
import com.kreuzwerker.awsstarter.user.adapters.db.entity.UserJpaEntity
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate

class UserCrudUseCaseE2ETest : BaseE2ETest() {
    private val url = "/api/v1/user"

    @Test
    fun `createUser creates a user in the database`(){
        val firstName = "Gopal"
        val lastName = "Tayal"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        val createRequestDto = UserRequestDto(lastName, firstName, dateOfBirth)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/user").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(createRequestDto))
        ).andExpect(MockMvcResultMatchers.status().isOk).andExpect(
            MockMvcResultMatchers.content().contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(lastName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value(dateOfBirth.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userId").isString)
    }

    @Test
    fun `getUser fetches a user from the database based on ID`(){
        val id = "U123"
        givenThatUserExistsInDB(id)

        mockMvc.perform(MockMvcRequestBuilders.get("$url/$id")).andExpect(MockMvcResultMatchers.status().isOk).andExpect(
            MockMvcResultMatchers.content().contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Tayal"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Gopal"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value(LocalDate.of(1992,12,21).toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(id))
    }

    @Test
    fun `getAllUsers fetches all users from the database`(){
        val id = "U123"
        givenThatUserExistsInDB(id)
        mockMvc.perform(MockMvcRequestBuilders.get(url)).andExpect(MockMvcResultMatchers.status().isOk).andExpect(
            MockMvcResultMatchers.content().contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("Tayal"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Gopal"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].dateOfBirth").value(LocalDate.of(1992,12,21).toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(id))
    }

    @Test
    fun `updateUser updates a user in the database based on ID`(){
        val id = "U123"
        givenThatUserExistsInDB(id)
        val firstName = "Angelica"
        val lastName = "Lonegtti"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        val updateRequestDto = UserRequestDto(lastName, firstName, dateOfBirth)

        mockMvc.perform(
            MockMvcRequestBuilders.put("$url/$id").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(updateRequestDto))
        ).andExpect(MockMvcResultMatchers.status().isOk).andExpect(
            MockMvcResultMatchers.content().contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(lastName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value(dateOfBirth.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(id))
    }

    @Test
    fun `deleteUser deletes a user in the database based on ID`(){
        val id = "U123"
        givenThatUserExistsInDB(id)
        mockMvc.perform(MockMvcRequestBuilders.delete("$url/$id")).andExpect(MockMvcResultMatchers.status().isOk).andExpect(
            MockMvcResultMatchers.content().contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Tayal"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Gopal"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value(LocalDate.of(1992,12,21).toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(id))
    }

    private fun givenThatUserExistsInDB(id: String) {
        userJpaRepository.saveAndFlush(UserJpaEntity(id, "Gopal", "Tayal", LocalDate.of(1992,12,21)))
    }
}