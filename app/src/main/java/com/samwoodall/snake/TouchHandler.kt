package com.samwoodall.snake

import android.view.MotionEvent

class TouchHandler(private val swipeListener: SwipeListener) {
    private var x1 = 0f
    private var y1 = 0f

    fun handleTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
                y1 = event.y
                return true
            }
            MotionEvent.ACTION_UP -> {
                val deltaX = event.x - x1
                val deltaY = event.y - y1

                when {
                    (deltaX > 70f && Math.abs(deltaX) > Math.abs(deltaY)) -> swipeListener.onSwipe(Direction.RIGHT)
                    (deltaX < -70f && Math.abs(deltaX) > Math.abs(deltaY)) -> swipeListener.onSwipe(Direction.LEFT)
                    (deltaY < -70f) -> swipeListener.onSwipe(Direction.UP)
                    (deltaY > 70f) -> swipeListener.onSwipe(Direction.DOWN)
                }
            }
        }
        return false
    }
}

interface SwipeListener {
    fun onSwipe(direction: Direction)
}

enum class Direction { UP, DOWN, LEFT, RIGHT, NONE }