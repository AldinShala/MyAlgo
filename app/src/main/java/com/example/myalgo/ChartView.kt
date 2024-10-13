package com.example.myalgo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint()
    private var values: IntArray = IntArray(0)
    private var currentIndex: Pair<Int, Int>? = null
    private var targetValue: Int = -1 // Target value for linear search
    private var foundIndex: Int = -1  // Highlight index if found in search

    // Set new values to display
    fun setValues(newValues: IntArray) {
        values = newValues
        foundIndex = -1 // Reset found index when new values are set
        invalidate() // Redraw the view
    }

    // Update current comparison indices (for sorting)
    fun updateCurrentIndex(index1: Int, index2: Int) {
        currentIndex = Pair(index1, index2)
        invalidate() // Redraw the view
    }

    // Set target value for linear search
    fun setTargetValue(target: Int) {
        targetValue = target
        invalidate() // Redraw the view
    }

    // Highlight found value in linear search
    fun highlightFoundValue(index: Int) {
        foundIndex = index
        invalidate() // Redraw the view
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (values.isEmpty()) return // Avoid drawing if values are empty

        val barWidth = width / values.size.toFloat()
        for (i in values.indices) {
            // Determine the color based on the current state
            paint.color = when {
                foundIndex == i -> Color.GREEN // Highlight found value in linear search
                targetValue == values[i] && foundIndex == -1 -> Color.YELLOW // Target value before it's found
                currentIndex?.first == i || currentIndex?.second == i -> Color.BLACK // Highlight comparison in sorting
                else -> Color.GRAY // Default bar color
            }

            val barHeight = (values[i] * height / 100).toFloat() // Scale the bar height

            // Draw the rectangle (bar)
            canvas.drawRect(
                (i * barWidth),
                (height - barHeight),
                ((i + 1) * barWidth),
                height.toFloat(),
                paint
            )

            // Draw the value text on top of the bar
            paint.color = Color.WHITE // Set text color to white
            paint.textSize = 20f // Text size for the numbers
            val textX = (i * barWidth) + barWidth / 2 - paint.measureText(values[i].toString()) / 2
            canvas.drawText(values[i].toString(), textX, height - barHeight - 10, paint)
        }
    }
}
