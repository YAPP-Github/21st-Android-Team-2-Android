package com.yapp.itemfinder.testbase

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantExecutorExtension::class)
abstract class JUnit5Test {

    @CallSuper
    @BeforeEach
    open fun setup() {
        MockitoAnnotations.openMocks(this)
        mockkStatic(Dispatchers::class)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { Dispatchers.IO } returns UnconfinedTestDispatcher()
    }

    @CallSuper
    @AfterEach
    open fun done() {
        Dispatchers.resetMain()
    }

    protected fun setStandardTestDispatchers() {
        every { Dispatchers.IO } returns StandardTestDispatcher()
        Dispatchers.setMain(StandardTestDispatcher())
    }

    protected fun <T> LiveData<T>.test(): LiveDataTestObserver<T> {
        val testObserver = LiveDataTestObserver<T>()
        observeForever(testObserver)
        return testObserver
    }
}
