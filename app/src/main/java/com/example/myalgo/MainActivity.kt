package com.example.myalgo

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var chartView: ChartView
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chartView = findViewById(R.id.chartView)
        val startBubbleSortButton: View = findViewById(R.id.startBubbleSortButton)
        val startInsertionSortButton: View = findViewById(R.id.startInsertionSortButton)
        val startMergeSortButton: View = findViewById(R.id.startMergeSortButton)
        val startQuickSortButton: View = findViewById(R.id.startQuickSortButton)

        startBubbleSortButton.setOnClickListener {
            startBubbleSort()
        }

        startInsertionSortButton.setOnClickListener {
            startInsertionSort()
        }

        startMergeSortButton.setOnClickListener {
            startMergeSort()
        }

        startQuickSortButton.setOnClickListener {
            startQuickSort()
        }
    }

    private fun startBubbleSort() {
        val randomValues = IntArray(100) { (1..100).random() }
        chartView.setValues(randomValues)

        CoroutineScope(Dispatchers.Main).launch {
            bubbleSort(randomValues)
        }
    }

    private suspend fun bubbleSort(values: IntArray) {
        val n = values.size
        for (i in 0 until n - 1) {
            for (j in 0 until n - i - 1) {
                chartView.updateCurrentIndex(j, j + 1)
                delay(30) // Delay for visualization
                if (values[j] > values[j + 1]) {
                    val temp = values[j]
                    values[j] = values[j + 1]
                    values[j + 1] = temp
                }
            }
        }
        chartView.setValues(values)
    }

    private fun startInsertionSort() {
        val randomValues = IntArray(100) { (1..100).random() }
        chartView.setValues(randomValues)

        CoroutineScope(Dispatchers.Main).launch {
            insertionSort(randomValues)
        }
    }

    private suspend fun insertionSort(values: IntArray) {
        val n = values.size
        for (i in 1 until n) {
            val key = values[i]
            var j = i - 1

            while (j >= 0 && values[j] > key) {
                chartView.updateCurrentIndex(j, j + 1)
                delay(30) // Delay for visualization
                values[j + 1] = values[j]
                j--
            }
            values[j + 1] = key
        }
        chartView.setValues(values)
    }

    private fun startMergeSort() {
        val randomValues = IntArray(100) { (1..100).random() }
        chartView.setValues(randomValues)

        CoroutineScope(Dispatchers.Main).launch {
            mergeSort(randomValues, 0, randomValues.size - 1)
        }
    }

    private suspend fun mergeSort(values: IntArray, left: Int, right: Int) {
        if (left < right) {
            val mid = (left + right) / 2

            mergeSort(values, left, mid)
            mergeSort(values, mid + 1, right)
            merge(values, left, mid, right)
        }
    }

    private suspend fun merge(values: IntArray, left: Int, mid: Int, right: Int) {
        val n1 = mid - left + 1
        val n2 = right - mid

        val L = IntArray(n1)
        val R = IntArray(n2)

        for (i in 0 until n1) {
            L[i] = values[left + i]
        }
        for (j in 0 until n2) {
            R[j] = values[mid + 1 + j]
        }

        var i = 0
        var j = 0
        var k = left

        while (i < n1 && j < n2) {
            chartView.updateCurrentIndex(k, k + 1)
            delay(30) // Delay for visualization
            if (L[i] <= R[j]) {
                values[k] = L[i]
                i++
            } else {
                values[k] = R[j]
                j++
            }
            k++
        }

        while (i < n1) {
            values[k] = L[i]
            i++
            k++
        }

        while (j < n2) {
            values[k] = R[j]
            j++
            k++
        }

        chartView.setValues(values)
    }

    private fun startQuickSort() {
        val randomValues = IntArray(100) { (1..100).random() }
        chartView.setValues(randomValues)

        CoroutineScope(Dispatchers.Main).launch {
            quickSort(randomValues, 0, randomValues.size - 1)
        }
    }

    private suspend fun quickSort(values: IntArray, low: Int, high: Int) {
        if (low < high) {
            val pivotIndex = partition(values, low, high)

            chartView.updateCurrentIndex(pivotIndex, pivotIndex) // Highlight the pivot
            delay(30) // Delay for visualization

            quickSort(values, low, pivotIndex - 1)
            quickSort(values, pivotIndex + 1, high)
        }
    }

    private suspend fun partition(values: IntArray, low: Int, high: Int): Int {
        val pivot = values[high]
        var i = (low - 1)

        for (j in low until high) {
            chartView.updateCurrentIndex(j, high) // Highlight the current value
            delay(30) // Delay for visualization
            if (values[j] < pivot) {
                i++
                val temp = values[i]
                values[i] = values[j]
                values[j] = temp
            }
        }

        val temp = values[i + 1]
        values[i + 1] = values[high]
        values[high] = temp
        return i + 1
    }

    private suspend fun delay(time: Long) {
        withContext(Dispatchers.IO) {
            Thread.sleep(time)
        }
    }
}
