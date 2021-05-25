package com.sudodevoss.easysignin.networking.ioc

import com.sudodevoss.easysignin.networking.data.account.datasource.AccountDataSource
import com.sudodevoss.easysignin.networking.data.account.datasource.AccountDataSourceImpl
import com.sudodevoss.easysignin.networking.data.account.repository.AccountRepositoryImpl
import com.sudodevoss.easysignin.networking.data.account.usecases.FetchUserInfoUseCaseImpl
import com.sudodevoss.easysignin.networking.data.account.usecases.UpdateUserInfoUseCaseImpl
import com.sudodevoss.easysignin.networking.data.authentication.datasource.AuthenticationDataSource
import com.sudodevoss.easysignin.networking.data.authentication.datasource.AuthenticationRemoteDataSourceImpl
import com.sudodevoss.easysignin.networking.data.authentication.repository.AuthenticationRepositoryImpl
import com.sudodevoss.easysignin.networking.data.authentication.usecases.LoginUseCaseImpl
import com.sudodevoss.easysignin.networking.data.authentication.usecases.RegisterUseCaseImpl
import com.sudodevoss.easysignin.networking.data.authentication.usecases.ResetPasswordUseCaseImpl
import com.sudodevoss.easysignin.networking.domain.account.repository.AccountRepository
import com.sudodevoss.easysignin.networking.domain.account.usecases.FetchUserInfoUseCase
import com.sudodevoss.easysignin.networking.domain.account.usecases.UpdateUserInfoUseCase
import com.sudodevoss.easysignin.networking.domain.authentication.repository.AuthenticationRepository
import com.sudodevoss.easysignin.networking.domain.authentication.usecases.LoginUseCase
import com.sudodevoss.easysignin.networking.domain.authentication.usecases.RegisterUseCase
import com.sudodevoss.easysignin.networking.domain.authentication.usecases.ResetPasswordUseCase
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

/**
 * Dependency Injection configurator
 * Provides [arrayOf]<[DI.Module]> for injection by module client
 */
object DIConfigurator {
    /**
     * Authentication Module with Authentication interfaces
     */
    private val authenticationDI = DI.Module(name = "authentication") {
        bind<LoginUseCase>() with provider { LoginUseCaseImpl(instance(), instance()) }
        bind<RegisterUseCase>() with provider { RegisterUseCaseImpl(instance(), instance()) }
        bind<ResetPasswordUseCase>() with provider { ResetPasswordUseCaseImpl(instance()) }
        bind<AuthenticationRepository>() with provider { AuthenticationRepositoryImpl(instance()) }
        bind<AuthenticationDataSource>() with provider { AuthenticationRemoteDataSourceImpl() }
    }

    /**
     * Account Module with Account interfaces
     */
    private val accountDI = DI.Module(name = "account") {
        bind<FetchUserInfoUseCase>() with provider { FetchUserInfoUseCaseImpl(instance()) }
        bind<UpdateUserInfoUseCase>() with provider { UpdateUserInfoUseCaseImpl(instance()) }
        bind<AccountDataSource>() with provider { AccountDataSourceImpl() }
        bind<AccountRepository>() with provider { AccountRepositoryImpl(instance()) }
    }

    /**
     * Gets [DI.Module] [ArrayList] for use by module client
     */
    fun getDIModules() = arrayListOf<DI.Module>(authenticationDI, accountDI)

}