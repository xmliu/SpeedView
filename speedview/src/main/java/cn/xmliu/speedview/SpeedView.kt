package cn.xmliu.speedview

/**
 * Date: 2020/6/4 17:30
 * Email: diyangxia@163.com
 * Author: liuxunming
 * Description: 码表
 */

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class SpeedView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    /**
     * 当前速度，单位km/h
     */
    private var curSpeed: Int = -1

    /**
     * 画笔
     */
    private val paint = Paint()

    /**
     * 画外层圆弧，路径和椭圆
     */
    private var rectF: RectF? = null
    private var arcPath: Path? = null

    /**
     * 中心点坐标、半径
     */
    private var centerX: Float? = null
    private var centerY: Float? = null
    private var radius: Float? = null

    /**
     * 码表外层RectF
     */
    private var left = -1F
    private var top = -1F
    private var right = -1F
    private var bottom = -1F

    /**
     * 偏移量
     */
    private val pointerOffset = 20
    private val unitOffset = 140
    private val numberOffset = 70
    private val smallOffset = 25
    private val bigOffset = 40

    /**
     * 码表表盘数组
     */
    private val bigMarkArr: MutableList<Double> = mutableListOf()
    private val smallMarkArr: MutableList<Double> = mutableListOf()
    private val numberArr: MutableList<String> = mutableListOf()

    init {
        // 大刻度数组
        for (i in 180..360 step 15)
            bigMarkArr.add(i.toDouble())
        // 小刻度数组
        for (i in 187..352 step 15)
            smallMarkArr.add(i + 0.5)
        // 速度数字数组 12组 需要和刻度数组数量相等
        for (i in 0..60 step 5)
            numberArr.add(i.toString())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        centerX = width / 2.toFloat()
        centerY = height * 5 / 6.toFloat()
        radius = height * 3 / 4.toFloat()

        left = centerX!! - radius!!
        top = centerY!! - radius!!
        right = centerX!! + radius!!
        bottom = centerY!! + radius!!

        rectF = RectF(left, top, right, bottom)
        arcPath = Path()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawColor(Color.BLACK)

        drawHalf(canvas)
        drawCenter(canvas)
        drawBig(canvas)
        drawSmall(canvas)
        drawNumber(canvas)
        drawUnit(canvas)
        drawPointer(canvas)
    }

    /**
     * 画外层半圆
     */
    private fun drawHalf(canvas: Canvas?) {
        paint.isAntiAlias = true
        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10F
        arcPath!!.rewind() // 清除直线数据，保留数据结构，方便快速重用
        arcPath!!.arcTo(rectF, 178F, 184F)// 多截取一点弧会好看点
        canvas?.drawPath(arcPath!!, paint)
    }

    /**
     * 画圆点
     */
    private fun drawCenter(canvas: Canvas?) {
        val centerRadius = 3F
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL_AND_STROKE
        canvas?.drawCircle(centerX!!, centerY!!, centerRadius, paint)
    }

    /**
     * 画大刻度
     */
    private fun drawBig(canvas: Canvas?) {
        paint.color = Color.WHITE
        paint.strokeWidth = 10F
        paint.style = Paint.Style.FILL_AND_STROKE
        for (item in bigMarkArr) {
            val radian = toRadians(item) // 角度转弧度
            val firstX = getRoundX(radian).toFloat()
            val firstY = getRoundY(radian).toFloat()
            val secondX = (firstX - cos(radian) * bigOffset).toFloat()
            val secondY = (firstY - sin(radian) * bigOffset).toFloat()
            canvas?.drawLine(firstX, firstY, secondX, secondY, paint)
        }
    }

    /**
     * 画小刻度
     */
    private fun drawSmall(canvas: Canvas?) {
        paint.color = Color.WHITE
        paint.strokeWidth = 5F
        paint.style = Paint.Style.FILL_AND_STROKE
        for (item in smallMarkArr) {
            val radian = toRadians(item) // 角度转弧度
            val firstX = getRoundX(radian).toFloat()
            val firstY = getRoundY(radian).toFloat()
            val secondX = (firstX - cos(radian) * smallOffset).toFloat()
            val secondY = (firstY - sin(radian) * smallOffset).toFloat()
            canvas?.drawLine(firstX, firstY, secondX, secondY, paint)
        }
    }

    /**
     * 画数字
     */
    private fun drawNumber(canvas: Canvas?) {
        paint.textSize = 25F
        paint.strokeWidth = 5F
        paint.textSkewX = 0F // 倾斜度设置为0，就是非斜体
        paint.style = Paint.Style.FILL
        for (index in numberArr.indices) {
            val radian = toRadians(bigMarkArr[index]) // 角度转弧度
            val firstX = getRoundX(radian)
            val firstY = getRoundY(radian)
            val secondX = (firstX - cos(radian) * numberOffset - index * 3).toFloat()//距离微调
            val secondY = (firstY - sin(radian) * numberOffset + index).toFloat()//距离微调
            canvas?.drawText(numberArr[index], secondX, secondY, paint)
        }
    }

    /**
     * 画单位 km/h
     */
    private fun drawUnit(canvas: Canvas?) {
        paint.textSize = 20F
        paint.textSkewX = -0.25F // 斜体
        val one = toRadians(270.0) // 角度转弧度 最顶端的圆点
        val testX = getRoundX(one)
        val testY = getRoundY(one)
        val finalX = (testX - cos(one) * unitOffset - 27).toFloat()//距离微调
        val finalY = (testY - sin(one) * unitOffset + 9).toFloat()//距离微调
        canvas?.drawText("km/h", finalX, finalY, paint)
    }

    /**
     * 画指针
     */
    private fun drawPointer(canvas: Canvas?) {
        paint.color = Color.RED
        paint.strokeWidth = 8F
        // (180 + 3 * curSpeed)的意义在于把速度转换为角度
        val pointerDegree = (180 + 3 * curSpeed).toDouble()
        val radian = toRadians(pointerDegree)
        val firstX = getRoundX(radian)
        val firstY = getRoundY(radian)
        val secondX = (firstX - cos(radian) * pointerOffset).toFloat()
        val secondY = (firstY - sin(radian) * pointerOffset).toFloat()
        canvas?.drawLine(centerX!!, centerY!!, secondX, secondY, paint)
    }

    /**
     * 计算在圆上某个角度的点的x坐标
     */
    private fun getRoundX(radian: Double): Double {
        return centerX!! + cos(radian) * radius!!
    }

    /**
     * 计算在圆上某个角度的点的y坐标
     */
    private fun getRoundY(radian: Double): Double {
        return centerY!! + sin(radian) * radius!!
    }

    fun setCurSpeed(speed: Int) {
        curSpeed = speed
        invalidate()
    }

    /**
     *  类似Math.toRadians 角度转弧度
     */
    fun toRadians(degree:Double):Double{
        return degree/180.0*Math.PI
    }
}