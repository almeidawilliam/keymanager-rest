package br.com.zupacademy.william.exception

import br.com.zupacademy.william.CorpoDeErro
import br.com.zupacademy.william.ErrorDetails
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GlobalExceptionHandlerTest {

    val request = HttpRequest.GET<Any>("/")

    @Test
    fun `should return 404 when grpc returns not found`() {
        val message = "ACCOUNT NOT FOUND"
        val exception = StatusRuntimeException(Status.NOT_FOUND.withDescription(message))

        val response = GlobalExceptionHandler().handle(request, exception)

        assertEquals(HttpStatus.NOT_FOUND, response.status)
        assertEquals("NOT_FOUND: $message", response.body())
    }

    @Test
    fun `should return 400 when grpc returns already exists`() {
        val message = "KEY ALREADY EXISTS"
        val exception = StatusRuntimeException(Status.ALREADY_EXISTS.withDescription(message))

        val response = GlobalExceptionHandler().handle(request, exception)

        assertEquals(HttpStatus.BAD_REQUEST, response.status)
        assertEquals("ALREADY_EXISTS: $message", response.body())
    }

    @Test
    fun `should return 500 when grpc returns any other`() {
        val exception = StatusRuntimeException(Status.INTERNAL)

        val response = GlobalExceptionHandler().handle(request, exception)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.status)
        assertEquals("Internal server error", response.body())
    }

    @Test
    fun `should return 400 when grpc returns invalid argument`() {
        val errorDetails = ErrorDetails.newBuilder()
            .setCampo("blabla")
            .setErro("invalid key (CPF)")
            .build()

        val errorBody = CorpoDeErro.newBuilder()
            .addAllErrors(listOf(errorDetails))
            .build()

        val protoStatus = com.google.rpc.Status.newBuilder()
            .setCode(Status.INVALID_ARGUMENT.code.value())
            .setMessage("Invalid arguments")
            .addDetails(com.google.protobuf.Any.pack(errorBody))
            .build()

        val exceptionResponse = StatusProto.toStatusRuntimeException(protoStatus)

        val response = GlobalExceptionHandler().handle(request, exceptionResponse)

        val expectedErrors = ErrorsBody(listOf("invalid key (CPF)"))

        assertEquals(HttpStatus.BAD_REQUEST, response.status)
        assertEquals(expectedErrors, response.body())
    }
}