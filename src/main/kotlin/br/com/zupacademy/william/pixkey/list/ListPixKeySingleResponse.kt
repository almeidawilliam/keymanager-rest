package br.com.zupacademy.william.pixkey.list

import br.com.zupacademy.william.PixKey
import br.com.zupacademy.william.pixkey.AccountType
import br.com.zupacademy.william.pixkey.KeyType
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class ListPixKeySingleResponse(pixKey: PixKey) {
    val idPix = pixKey.idPix
    val keyType = KeyType.valueOf(pixKey.keyType.name)
    val keyValue = pixKey.keyValue
    val accountType = AccountType.valueOf(pixKey.accountType.name)
    val createdAt =
        LocalDateTime
            .ofInstant(
                Instant.ofEpochSecond(pixKey.createdAt.seconds, pixKey.createdAt.nanos.toLong()),
                ZoneOffset.UTC
            )
}
