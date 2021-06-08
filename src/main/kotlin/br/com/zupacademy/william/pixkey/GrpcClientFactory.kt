package br.com.zupacademy.william.pixkey

import br.com.zupacademy.william.KeymanagerFindGrpcServiceGrpc
import br.com.zupacademy.william.KeymanagerListGrpcServiceGrpc
import br.com.zupacademy.william.KeymanagerRegistryGrpcServiceGrpc
import br.com.zupacademy.william.KeymanagerRemoveGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    @Singleton
    fun pixKeyList() = KeymanagerListGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun pixKeyFind() = KeymanagerFindGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun pixKeyRegistry() = KeymanagerRegistryGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun pixKeyRemove() = KeymanagerRemoveGrpcServiceGrpc.newBlockingStub(channel)

}