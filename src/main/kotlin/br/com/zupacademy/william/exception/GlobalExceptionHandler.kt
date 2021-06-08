package br.com.zupacademy.william.exception

import br.com.zupacademy.william.CorpoDeErro
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.server.exceptions.ExceptionHandler
import javax.inject.Singleton

@Singleton
class GlobalExceptionHandler : ExceptionHandler<StatusRuntimeException, HttpResponse<Any>> {

    override fun handle(request: HttpRequest<*>, exception: StatusRuntimeException): HttpResponse<Any> {

        val statusCode = exception.status.code
        val (httpStatus, message) =
            when (statusCode) {
                Status.NOT_FOUND.code -> Pair(HttpStatus.NOT_FOUND, exception.message)
                Status.ALREADY_EXISTS.code -> Pair(HttpStatus.BAD_REQUEST, exception.message)
                Status.INVALID_ARGUMENT.code -> {
                    val statusProto = StatusProto.fromThrowable(exception)

                    val fieldErrors: List<String> = statusProto!!.detailsList
                        .map { detail -> detail.unpack(CorpoDeErro::class.java) }
                        .flatMap { detailUnpacked -> detailUnpacked.errorsList }
                        .map { error -> error.erro }
                        .toList()

                    val errorsBody = ErrorsBody(fieldErrors)

                    Pair(HttpStatus.BAD_REQUEST, errorsBody)
                }
                else -> Pair(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error")
            }

        return HttpResponse
            .status<JsonError>(httpStatus)
            .body(message)//JsonError(message))
    }
}

data class ErrorsBody(
    val errors: List<String>
)