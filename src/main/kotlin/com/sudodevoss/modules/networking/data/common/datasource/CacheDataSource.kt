package com.sudodevoss.easysignin.networking.data.common.datasource

interface CacheDataSource<T> {
    suspend fun read(): T
    suspend fun write(input: T)
}