package au.sjowl.lib.view.progressbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import au.sjowl.lib.pieprogressbar.R

class PieProgressView : View {

    private val progressPaint = Paint().apply {
        style = Paint.Style.FILL
    }

    private val backgroundPaint = Paint().apply {
        style = Paint.Style.FILL
    }

    private val oval = RectF()

    var progress: Int = 10
        set(value) {
            if (field in (0..100)) {
                field = value
                invalidate()
            } else throw IllegalArgumentException("Progress must be in [0..100]")
        }
    var colorProgress: Int
        set(value) {
            progressPaint.color = value
        }
        get() = progressPaint.color

    var colorBackground: Int
        set(value) {
            backgroundPaint.color = value
        }
        get() = backgroundPaint.color

    private val startAngle = -90f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttributes(attrs)
    }

    private fun initAttributes(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.PieProgressView, 0, 0)
        colorProgress = a.getColor(R.styleable.PieProgressView_colorProgress, Color.BLACK)
        colorBackground = a.getColor(R.styleable.PieProgressView_colorBackground, Color.RED)
        progress = a.getInteger(R.styleable.PieProgressView_progress, 0)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
        val w = Math.min(measuredHeight, measuredWidth)
        setMeasuredDimension(w, w)

        with(oval) {
            if (measuredWidth < measuredHeight) {
                left = 0f
                right = measuredWidth * 1f
                top = 1f * (measuredHeight - w) / 2
                bottom = 1f * (measuredHeight + w) / 2
            } else {
                left = 1f * (measuredWidth - w) / 2
                right = 1f * (measuredWidth + w) / 2
                top = 0f
                bottom = measuredHeight * 1f
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawArc(oval, startAngle, progress * 360f / 100, true, progressPaint)
        canvas.drawArc(oval, progress * 360f / 100 + startAngle, (100 - progress) * 360f / 100, true, backgroundPaint)
    }
}

fun Context.getColorFromAttr(attrId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrId, typedValue, true)
    return typedValue.data
}