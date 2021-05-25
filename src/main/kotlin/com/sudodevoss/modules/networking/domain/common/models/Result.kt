package com.sudodevoss.easysignin.networking.domain.common.models

sealed class Result<out TSuccess, out TFailure> {
    data class Success<TSuccess>(val value: TSuccess) : Result<TSuccess, Nothing>()
    data class Failure<TFailure>(val reason: TFailure) : Result<Nothing, TFailure>()
}