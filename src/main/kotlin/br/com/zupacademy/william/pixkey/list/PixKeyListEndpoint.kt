package br.com.zupacademy.william.pixkey.list

import br.com.zupacademy.william.KeymanagerListGrpcServiceGrpc
import br.com.zupacademy.william.ListRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import javax.inject.Inject

@Controller("/customer/{idCustomer}/pix/keys")
class PixKeyListEndpoint(@field:Inject val grpcClient: KeymanagerListGrpcServiceGrpc.KeymanagerListGrpcServiceBlockingStub) {

    @Get
    fun list(@PathVariable idCustomer: String): HttpResponse<Any> {

        val listGrpcRequest = ListRequest.newBuilder()
            .setIdCustomer(idCustomer)
            .build()

        val listResponse = grpcClient.list(listGrpcRequest)
            .pixKeysList
            .map { pixKey -> ListPixKeySingleResponse(pixKey) }
            .toList()

        return HttpResponse.ok(listResponse)
    }
}
