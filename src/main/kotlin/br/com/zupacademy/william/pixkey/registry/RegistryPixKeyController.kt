package br.com.zupacademy.william.pixkey.registry

import br.com.zupacademy.william.KeymanagerRegistryGrpcServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/customer/{idCustomer}/pix/keys")
class RegistryPixKeyController(
    @field:Inject val grpcClient: KeymanagerRegistryGrpcServiceGrpc.KeymanagerRegistryGrpcServiceBlockingStub
) {

    @Post
    fun registry(
        @PathVariable idCustomer: String,
        @Body @Valid registryPixKeyRequest: RegistryPixKeyRequest
    ): HttpResponse<Any> {
        val clientRequest = registryPixKeyRequest.toGrpcDto(idCustomer)
        val response = grpcClient.registry(clientRequest)

        return HttpResponse.created(
            HttpResponse.uri("localhost:8080/customer/$idCustomer/pix/keys/${response.idPix}")
        )
    }
}