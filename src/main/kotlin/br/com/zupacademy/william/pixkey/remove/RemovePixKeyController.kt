package br.com.zupacademy.william.pixkey.remove

import br.com.zupacademy.william.KeymanagerRemoveGrpcServiceGrpc
import br.com.zupacademy.william.RemoveRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import javax.inject.Inject

@Controller("/customer/{idCustomer}/pix/keys/{idPixKey}")
class RemovePixKeyController(
    @field:Inject val grpcClient: KeymanagerRemoveGrpcServiceGrpc.KeymanagerRemoveGrpcServiceBlockingStub
) {

    @Delete
    fun remove(
        @PathVariable idCustomer: String,
        @PathVariable idPixKey: Long
    ): HttpResponse<Any> {
        val clientRequest = RemoveRequest.newBuilder()
            .setIdCustomer(idCustomer)
            .setIdPix(idPixKey)
            .build()

        grpcClient.remove(clientRequest)
        return HttpResponse.noContent()
    }
}