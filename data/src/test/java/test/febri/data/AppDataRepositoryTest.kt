package test.febri.data

import org.junit.Before
import org.mockito.Mock
import test.febri.data.remote.RemoteDataSource
import test.febri.domain.AppDataRepository

class AppDataRepositoryTest {

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource

    private lateinit var appDataRepository: AppDataRepository

    @Before
    fun setup() {

    }

}