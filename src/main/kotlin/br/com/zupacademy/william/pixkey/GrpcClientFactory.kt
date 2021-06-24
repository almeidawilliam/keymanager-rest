package br.com.zupacademy.william.pixkey

import br.com.zupacademy.william.KeymanagerFindServiceGrpc
import br.com.zupacademy.william.KeymanagerListServiceGrpc
import br.com.zupacademy.william.KeymanagerRegistryServiceGrpc
import br.com.zupacademy.william.KeymanagerRemoveServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    @Singleton
    fun pixKeyList() = KeymanagerListServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun pixKeyFind() = KeymanagerFindServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun pixKeyRegistry() = KeymanagerRegistryServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun pixKeyRemove() = KeymanagerRemoveServiceGrpc.newBlockingStub(channel)

}