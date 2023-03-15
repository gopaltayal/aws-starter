package com.kreuzwerker.awsstarter.user.adapters.api

import com.kreuzwerker.awsstarter.user.adapters.api.dtos.UserRequestDto
import com.kreuzwerker.awsstarter.user.adapters.api.dtos.UserResponseDto
import com.kreuzwerker.awsstarter.user.application.ports.inbound.UserWebInputPort
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserApiAdapter(private val userWebInputPort: UserWebInputPort) {
    // Why we didn't autowire?
    // Because we would be unable to test this with Mockito.

    @Operation(
        summary = "Create a single user in the system with various details"
    )
    @PostMapping
    fun createUser(@RequestBody @Valid userRequestDto: UserRequestDto): UserResponseDto {
        return userWebInputPort.createUser(userRequestDto)
    }

    @Operation(
        summary = "Get User by its Unique system generated ID"
    )
    @GetMapping("/{id}")
    fun getUser(@PathVariable @Parameter(description = "User ID", example = "U211292") id: String): UserResponseDto {
        return userWebInputPort.getUser(id)
    }

    @Operation(
        summary = "Get all Users registered in the system"
    )
    @GetMapping
    fun getAllUsers(): List<UserResponseDto> {
        return userWebInputPort.getAllUsers()
    }

    @Operation(
        summary = "Update a single user in the system with its unique System ID"
    )
    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable @Parameter(description = "User ID", example = "U211292") id: String,
        @RequestBody @Validated userRequestDto: UserRequestDto
    ): UserResponseDto {
        return userWebInputPort.updateUser(id, userRequestDto)
    }

    @Operation(
        summary = "Delete a single user in the system with its unique System ID"
    )
    @DeleteMapping("{id}")
    fun deleteUser(
        @PathVariable @Parameter(description = "User ID", example = "U211292") id: String
    ): UserResponseDto {
        return userWebInputPort.deleteUser(id)
    }
}