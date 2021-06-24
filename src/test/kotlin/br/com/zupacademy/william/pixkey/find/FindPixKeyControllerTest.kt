package br.com.zupacademy.william.pixkey.find

import br.com.zupacademy.william.*
import br.com.zupacademy.william.Customer
import br.com.zupacademy.william.CustomerAccount
import br.com.zupacademy.william.pixkey.GrpcClientFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import java.time.Instant
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class FindPixKeyControllerTest {

    @field:Inject
    lateinit var grpcClient: KeymanagerFindServiceGrpc.KeymanagerFindServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    fun `should find a pix key with all details`() {
        val idCustomer = UUID.randomUUID().toString()
        val idPix = 1L

        val grpcClientResponse = FindResponse.newBuilder()
            .setIdPix(idPix)
            .setKeyType(KeyType.PHONE)
            .setKeyValue("+5515999998888")
            .setCustomer(
                Customer.newBuilder()
                    .setIdCustomer(UUID.randomUUID().toString())
                    .setName("Joaquim da Silva")
                    .setCpf("45455167006")
                    .build()
            )
            .setCustomerAccount(
                CustomerAccount.newBuilder()
                    .setInstitutionName("ITAÚ UNIBANCO S.A")
                    .setAccountType(AccountType.CONTA_CORRENTE)
                    .setAgency("0001")
                    .setAccount("200154")
                    .build()
            )
            .setCreatedAt(
                com.google.protobuf.Timestamp.newBuilder()
                    .setSeconds(Instant.now().epochSecond)
                    .setNanos(Instant.now().nano)
            )
            .build()

        `when`(grpcClient.find(any()))
            .thenReturn(grpcClientResponse)

        val httpRequest = HttpRequest.GET<Any>("/customer/$idCustomer/pix/keys/$idPix")
        val httpResponse = httpClient.toBlocking().exchange(httpRequest, Any::class.java)

        assertEquals(HttpStatus.OK, httpResponse.status)
        assertTrue(httpResponse.body != null)
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class mockClients {

        @Singleton
        fun mockGrpcClient() = mock(KeymanagerFindServiceGrpc.KeymanagerFindServiceBlockingStub::class.java)
    }
}