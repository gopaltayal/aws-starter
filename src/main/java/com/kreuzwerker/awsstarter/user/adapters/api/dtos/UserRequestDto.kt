package com.kreuzwerker.awsstarter.user.adapters.api.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class UserRequestDto(

    // @field is needed in Kotlin for these validations to work!
    // Normal ones will not work and you won't know why bad requests are allowed.
    @field:NotBlank
    var lastName: String,

    @field:NotBlank
    var firstName: String,

    @field:NotNull
    var dateOfBirth: LocalDate
)