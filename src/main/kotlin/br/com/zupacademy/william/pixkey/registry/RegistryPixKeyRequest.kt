package br.com.zupacademy.william.pixkey.registry

import br.com.zupacademy.william.RegistryRequest
import br.com.zupacademy.william.pixkey.AccountType
import br.com.zupacademy.william.pixkey.KeyType
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
data class RegistryPixKeyRequest(

    @field:NotNull
    val keyType: KeyType,

    @field:Size(max = 77)
    val keyValue: String,

    @field:NotNull
    val accountType: AccountType
) {

    fun toGrpcDto(idCustomer: String): RegistryRequest {
        return RegistryRequest.newBuilder()
            .setIdCustomer(idCustomer)
            .setKeyType(br.com.zupacademy.william.KeyType.valueOf(keyType.name))
            .setAccountType(accountType.grpcEnum)
            .setKeyValue(if (keyType == KeyType.RANDOM) "" else keyValue)
            .build()
    }
}
