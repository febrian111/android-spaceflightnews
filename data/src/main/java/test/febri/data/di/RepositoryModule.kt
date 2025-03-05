package test.febri.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import test.febri.data.AppDataRepositoryImpl
import test.febri.domain.AppDataRepository


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(impl: AppDataRepositoryImpl): AppDataRepository
}