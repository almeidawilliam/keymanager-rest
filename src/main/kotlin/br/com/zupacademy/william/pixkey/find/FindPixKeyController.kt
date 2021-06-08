package br.com.zupacademy.william.pixkey.find

import br.com.zupacademy.william.FindRequest
import br.com.zupacademy.william.KeymanagerFindGrpcServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import javax.inject.Inject

@Controller("/customer/{idCustomer}/pix/keys/{idPixKey}")
class FindPixKeyController(@field:Inject val grpcClient: KeymanagerFindGrpcServiceGrpc.KeymanagerFindGrpcServiceBlockingStub) {
    @Get
    fun find(
        @PathVariable idCustomer: String,
        @PathVariable idPixKey: Long
    ): HttpResponse<Any> {

        val grpcClientRequest = FindRequest.newBuilder()
            .setIdPix(idPixKey)
            .setIdCustomer(idCustomer)
            .build()

        val grpcClientResponse = grpcClient.find(grpcClientRequest)
        return HttpResponse.ok(FindPixKeyResponse(grpcClientResponse))
    }
}

