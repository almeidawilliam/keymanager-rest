package br.com.zupacademy.william.pixkey.find

import br.com.zupacademy.william.FindResponse
import br.com.zupacademy.william.pixkey.AccountType
import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

data class FindPixKeyResponse(
    @JsonIgnore
    val findResponse: FindResponse
) {
    val idPix = findResponse.idPix
    val keyType = br.com.zupacademy.william.pixkey.KeyType.valueOf(findResponse.keyType.name)
    val keyValue = findResponse.keyValue
    val customer = Customer(findResponse.customer)
    val customerAccount = CustomerAccount(findResponse.customerAccount)
    val createdAt =
        LocalDateTime
            .ofInstant(
                Instant.ofEpochSecond(findResponse.createdAt.seconds, findResponse.createdAt.nanos.toLong()),
                ZoneOffset.UTC
            )
}

data class Customer(
    @JsonIgnore
    val customer: br.com.zupacademy.william.Customer
) {
    val idCustomer: String = customer.idCustomer
    val cpf: String = customer.cpf
    val name: String = customer.name
}

data class CustomerAccount(
    @JsonIgnore
    val customerAccount: br.com.zupacademy.william.CustomerAccount
) {
    val institutionName: String = customerAccount.institutionName
    val accountType: AccountType = AccountType.valueOf(customerAccount.accountType.name)
    val agency: String = customerAccount.agency
    val account: String = customerAccount.account
}
