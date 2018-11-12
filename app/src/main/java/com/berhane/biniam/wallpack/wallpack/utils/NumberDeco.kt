/*
 * DayTime:11/12/18 10:41 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import java.math.BigDecimal
import java.math.BigInteger
import java.text.DecimalFormat

class NumberDeco : TextView {


        /**
         * 起始值 默认 0
         */
        private var mNumStart = "0"
        /**
         * 结束值
         */
        private var mNumEnd: String? = null
        /**
         * 动画总时间 默认 2000 毫秒
         */
        private var mDuration: Long = 2000
        /**
         * 前缀
         */
        private var mPrefixString = ""
        /**
         * 后缀
         */
        private var mPostfixString = ""
        /**
         * 是否开启动画
         */
        private var isEnableAnim = true
        /**
         * 是否是整数
         */
        private var isInt: Boolean = false
        private var animator: ValueAnimator? = null

        private var updateTimes: Long = 0

        constructor(context: Context) : super(context) {}

        constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

        constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

        fun setNumberString(number: String) {
            setNumberString("0", number)
        }

        fun setNumberString(numberStart: String, numberEnd: String) {
            mNumStart = numberStart
            mNumEnd = numberEnd
            if (checkNumString(numberStart, numberEnd)) {
                // 数字合法　开始数字动画
                start()
            } else {
                // 数字不合法　直接调用　setText　设置最终值
                text = mPrefixString + numberEnd + mPostfixString
            }
        }

        fun setEnableAnim(enableAnim: Boolean) {
            isEnableAnim = enableAnim
        }

        fun setDuration(mDuration: Long) {
            this.mDuration = mDuration
        }

        fun setPrefixString(mPrefixString: String) {
            this.mPrefixString = mPrefixString
        }

        fun setPostfixString(mPostfixString: String) {
            this.mPostfixString = mPostfixString
        }

        /**
         * 校验数字的合法性
         *
         * @param numberStart 　开始的数字
         * @param numberEnd   　结束的数字
         * @return 合法性
         */
        private fun checkNumString(numberStart: String, numberEnd: String): Boolean {

            val regexInteger = "-?\\d*"
            isInt = numberEnd.matches(regexInteger.toRegex()) && numberStart.matches(regexInteger.toRegex())
            if (isInt) {
                val start = BigInteger(numberStart)
                val end = BigInteger(numberEnd)
                return end.compareTo(start) >= 0
            }
            val regexDecimal = "-?[1-9]\\d*.\\d*|-?0.\\d*[1-9]\\d*"
            if ("0" == numberStart) {
                if (numberEnd.matches(regexDecimal.toRegex())) {
                    val start = BigDecimal(numberStart)
                    val end = BigDecimal(numberEnd)
                    return end.compareTo(start) > 0
                }
            }
            if (numberEnd.matches(regexDecimal.toRegex()) && numberStart.matches(regexDecimal.toRegex())) {
                val start = BigDecimal(numberStart)
                val end = BigDecimal(numberEnd)
                return end.compareTo(start) > 0
            }
            return false
        }

        private fun start() {
            if (!isEnableAnim) {
                // 禁止动画
                text = mPrefixString + format(BigDecimal(mNumEnd)) + mPostfixString
                return
            }
            updateTimes = 0
            animator = ValueAnimator.ofObject(BigDecimalEvaluator(), BigDecimal(mNumStart), BigDecimal(mNumEnd))
            animator!!.duration = mDuration
            animator!!.interpolator = AccelerateDecelerateInterpolator()
            animator!!.addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as BigDecimal
                text = mPrefixString + format(value) + mPostfixString
            }
            animator!!.start()
        }

        override fun onDetachedFromWindow() {
            super.onDetachedFromWindow()
            if (animator != null) {
                animator!!.cancel()
            }
        }

        /**
         * 格式化 BigDecimal ,小数部分时保留两位小数并四舍五入
         *
         * @param bd 　BigDecimal
         * @return 格式化后的 String
         */
        private fun format(bd: BigDecimal): String {
            val pattern = StringBuilder()
            if (isInt) {
                pattern.append("#,###")
            } else {
                var length = 0
                val decimals = mNumEnd!!.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                if (decimals != null) {
                    length = decimals.length
                }
                pattern.append("#,##0")
                if (length > 0) {
                    pattern.append(".")
                    for (i in 0 until length) {
                        pattern.append("0")
                    }
                }
            }
            val df = DecimalFormat(pattern.toString())
            return df.format(bd)
        }

        private class BigDecimalEvaluator : TypeEvaluator<Any> {

            override fun evaluate(fraction: Float, startValue: Any, endValue: Any): Any {
                val start = startValue as BigDecimal
                val end = endValue as BigDecimal
                val result = end.subtract(start)
                return result.multiply(BigDecimal("" + fraction)).add(start)
            }
        }
    }

