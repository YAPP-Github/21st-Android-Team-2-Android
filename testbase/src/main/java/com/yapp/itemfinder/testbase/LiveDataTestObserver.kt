package com.yapp.itemfinder.testbase

import androidx.lifecycle.Observer

class LiveDataTestObserver<T> : Observer<T> {

    private val recordedActualValues: MutableList<T> = mutableListOf()

    override fun onChanged(t: T) {
        recordedActualValues.add(t)
    }

    fun assertValueSequence(expectedValues: List<T>, verbose: Boolean = false): LiveDataTestObserver<T> {
        var index = 0
        val actualIterator = recordedActualValues.iterator()
        val expectedIterator = expectedValues.iterator()

        var hasNextActual: Boolean
        var hasNextExpected: Boolean

        while (true) {
            hasNextActual = actualIterator.hasNext()
            hasNextExpected = expectedIterator.hasNext()
            if (!hasNextActual || !hasNextExpected) break

            val actual: T = actualIterator.next()
            val expected: T = expectedIterator.next()

            if (verbose) {
                println("[#$index   ACTUAL] $actual")
                println("[#$index EXPECTED] $expected")
            }

            if (!ObjectHelper.equals(actual, expected)) {
                throw AssertionError("Unexpected value at $index\n-   ACTUAL: ${actual}\n- EXPECTED: ${expected}")
            }

            index++
        }

        if (hasNextActual) {
            throw AssertionError("More values received than expected(actual: ${recordedActualValues.size}, expected: ${expectedValues.size}).")
        }
        if (hasNextExpected) {
            throw AssertionError("Fewer values received than expected(actual: ${recordedActualValues.size}, expected: ${expectedValues.size}).")
        }

        return this
    }
}
