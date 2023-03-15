package com.kreuzwerker.awsstarter.user.adapters.api

import com.kreuzwerker.awsstarter.user.adapters.api.dtos.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException

@ControllerAdvice
class ControllerAdviceConfig {

    @ExceptionHandler
    fun handle(ex: ResponseStatusException): ResponseEntity<ErrorResponse> {
        var message = "An exception occurred in the system"
        if (ex.statusCode.value() == 404) {
            message = "Couldn't find user in the system please check the ID"
        } else if (ex.statusCode.value() == 409) {
            message = "User already exists in the system!"
        }
        val errorMessage = ErrorResponse(
            ex.statusCode.value(),
            message
        )
        return ResponseEntity(errorMessage, ex.statusCode)
    }

    @ExceptionHandler
    fun handleInvalidRequests(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errorMessage = ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.message ?: "Bad Request Received, please check message syntax!"
        )
        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleAny(ex: Exception): ResponseEntity<ErrorResponse> {
        val errorMessage = ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Couldn't handle error! Contact Support"
        )
        return ResponseEntity(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}