package com.samwoodall.snake

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.doOnLayout
import com.samwoodall.snake.SnakeView.Companion.SIZE_FACTOR
import com.samwoodall.snake.SnakeView.Companion.X_FACTOR
import com.samwoodall.snake.SnakeView.Companion.X_TRANSLATION
import com.samwoodall.snake.SnakeView.Companion.Y_FACTOR
import com.samwoodall.snake.SnakeView.Companion.Y_TRANSLATION
import java.util.*

class SnakeView : View, SwipeListener {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributes: AttributeSet) : super(context, attributes)

    private val paint: Paint = Paint().apply { color = Color.RED }
    private val boardPaint: Paint = Paint().apply { color = Color.BLACK }
    private val touchHandler = TouchHandler(this)

    companion object {
        const val X_FACTOR = 30f
        const val Y_FACTOR = 30f
        const val X_TRANSLATION = 30f
        const val Y_TRANSLATION = 30f
        const val SIZE_FACTOR = 20f
        const val FRAME_RATE = 16L
    }

    init {
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    private val board: List<Pair<Int, Int>> = createBoard()

    private fun createBoard(): List<Pair<Int, Int>> {
        val board = mutableListOf<Pair<Int, Int>>()
        for (i in 0..30) {
            for (j in 0..30) {
                board.add(Pair(i, j))
            }
        }
        return board
    }
    private val snake = Snake()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        board.forEach {
            canvas!!.drawCircle(X_TRANSLATION + (it.first * X_FACTOR), Y_TRANSLATION + (it.second * Y_FACTOR), SIZE_FACTOR, boardPaint)
        }

        snake.draw(canvas!!, paint)

        handler.postDelayed({
            snake.move()
            invalidate()
        }, FRAME_RATE )
    }

    override fun onSwipe(direction: Direction) {
        snake.headDirection = direction
        if (direction == Direction.LEFT) snake.addPart()
        snake.move()
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent) = if (touchHandler.handleTouchEvent(event)) true else super.onTouchEvent(event)
}

class Snake(var size: Int = 1, private val positions: LinkedList<Pair<Int, Int>> = LinkedList(listOf(Pair(0, 0))), var headDirection: Direction = Direction.RIGHT) {
    fun addPart() {
        size += 1
    }

    fun draw(canvas: Canvas, paint: Paint) {
        val posRev = positions.reversed()
        for (i in 0 until size) {
            canvas.drawCircle(X_TRANSLATION + (posRev[i].first * X_FACTOR), Y_TRANSLATION + (posRev[i].second * Y_FACTOR), SIZE_FACTOR, paint)
        }
    }

    fun move() {
        val lastPos = positions.last
        when (headDirection) {
            Direction.UP -> positions.add(Pair(lastPos.first, lastPos.second - 1))
            Direction.DOWN -> positions.add(Pair(lastPos.first, lastPos.second + 1))
            Direction.LEFT -> positions.add(Pair(lastPos.first - 1, lastPos.second))
            Direction.RIGHT ->  positions.add(Pair(lastPos.first + 1, lastPos.second))
            Direction.NONE -> {}
        }
    }
}
