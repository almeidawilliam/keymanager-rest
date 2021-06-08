package br.com.zupacademy.william.pixkey

enum class AccountType(val grpcEnum: br.com.zupacademy.william.AccountType) {
    CONTA_CORRENTE(br.com.zupacademy.william.AccountType.CONTA_CORRENTE),
    CONTA_POUPANCA(br.com.zupacademy.william.AccountType.CONTA_POUPANCA)
}
