package br.com.zupacademy.william.pixkey.registry

import br.com.zupacademy.william.KeymanagerRegistryServiceGrpc
import br.com.zupacademy.william.RegistryResponse
import br.com.zupacademy.william.pixkey.AccountType
import br.com.zupacademy.william.pixkey.GrpcClientFactory
import br.com.zupacademy.william.pixkey.KeyType
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RegistryPixKeyControllerTest {

    @field:Inject
    lateinit var grpcClient: KeymanagerRegistryServiceGrpc.KeymanagerRegistryServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `should registry a new pix key`() {
        val idPix = 1

        `when`(grpcClient.registry(any()))
            .thenReturn(RegistryResponse.newBuilder()
                .setIdPix(idPix.toString())
                .build())

        val idCustomer = UUID.randomUUID().toString()
        val request = HttpRequest.POST(
            "/customer/$idCustomer/pix/keys",
            RegistryPixKeyRequest(KeyType.PHONE, "+5515981476877", AccountType.CONTA_CORRENTE)
        )
        val response = client.toBlocking().exchange(request, RegistryPixKeyRequest::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertEquals("http://localhost:8080/customer/$idCustomer/pix/keys/$idPix", response.header("Location"))
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() = mock(KeymanagerRegistryServiceGrpc.KeymanagerRegistryServiceBlockingStub::class.java)
    }
}