package br.com.zupacademy.william.pixkey.list

import br.com.zupacademy.william.*
import br.com.zupacademy.william.pixkey.GrpcClientFactory
import com.google.protobuf.Timestamp
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
internal class PixKeyListControllerTest {

    @field:Inject
    lateinit var grpcClient: KeymanagerListServiceGrpc.KeymanagerListServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    fun `should return list of pix keys`() {
        val idCustomer = UUID.randomUUID().toString()

        val grpcClientResponse = ListResponse.newBuilder()
            .setIdCustomer(idCustomer)
            .addAllPixKeys(
                listOf(
                    PixKey.newBuilder()
                        .setIdPix(1)
                        .setKeyType(KeyType.PHONE)
                        .setKeyValue("+5515999998888")
                        .setAccountType(AccountType.CONTA_CORRENTE)
                        .setCreatedAt(
                            Timestamp.newBuilder()
                                .setSeconds(Instant.now().epochSecond)
                                .setNanos(Instant.now().nano)
                                .build()
                        )
                        .build(),
                    PixKey.newBuilder()
                        .setIdPix(2)
                        .setKeyType(KeyType.RANDOM)
                        .setKeyValue(UUID.randomUUID().toString())
                        .setAccountType(AccountType.CONTA_CORRENTE)
                        .setCreatedAt(
                            Timestamp.newBuilder()
                                .setSeconds(Instant.now().epochSecond)
                                .setNanos(Instant.now().nano)
                                .build()
                        )
                        .build()
                )
            )
            .build()

        `when`(grpcClient.list(any()))
            .thenReturn(grpcClientResponse)

        val httpRequest = HttpRequest.GET<Any>("/customer/$idCustomer/pix/keys")
        val httpResponse = httpClient.toBlocking().exchange(httpRequest, Any::class.java)

        assertEquals(HttpStatus.OK, httpResponse.status)
        assertTrue(httpResponse.body.isPresent)

    }


    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockClient {

        @Singleton
        fun mockGrpcClient() = mock(KeymanagerListServiceGrpc.KeymanagerListServiceBlockingStub::class.java)
    }
}