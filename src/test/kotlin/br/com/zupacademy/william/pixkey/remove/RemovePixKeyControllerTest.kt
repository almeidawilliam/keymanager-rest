package br.com.zupacademy.william.pixkey.remove

import br.com.zupacademy.william.KeymanagerRegistryGrpcServiceGrpc
import br.com.zupacademy.william.KeymanagerRemoveGrpcServiceGrpc
import br.com.zupacademy.william.RemoveResponse
import br.com.zupacademy.william.pixkey.GrpcClientFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RemovePixKeyControllerTest {

    @field:Inject
    lateinit var grpcClient: KeymanagerRemoveGrpcServiceGrpc.KeymanagerRemoveGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    fun `should remove a pix key`() {
        val idCustomer = UUID.randomUUID().toString()
        val idPixKey = 1L

        `when`(grpcClient.remove(any()))
            .thenReturn(
                RemoveResponse.newBuilder()
                    .setIdPix(idPixKey)
                    .setIdCustomer(idCustomer)
                    .build()
            )

        val httpRequest = HttpRequest.DELETE<Any>("/customer/$idCustomer/pix/keys/$idPixKey")
        val httpResponse = httpClient.toBlocking().exchange(httpRequest, Any::class.java)

        assertEquals(HttpStatus.NO_CONTENT, httpResponse.status)
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockClient {

        @Singleton
        fun mockGrpcClient() = mock(KeymanagerRemoveGrpcServiceGrpc.KeymanagerRemoveGrpcServiceBlockingStub::class.java)
    }

}