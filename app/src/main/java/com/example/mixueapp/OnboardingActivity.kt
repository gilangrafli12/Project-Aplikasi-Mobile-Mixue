package com.example.mixueapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var dotsLayout: LinearLayout
    private lateinit var dots: Array<ImageView?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        dotsLayout = findViewById(R.id.dotsLayout)

        val fragments = listOf(
            SlideFragment1(),
            SlideFragment2(),
            SlideFragment3()
        )
        val adapter = SlidePagerAdapter(this, fragments)
        viewPager.adapter = adapter

        setupDots(fragments.size)
        selectDot(0)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                selectDot(position)
            }
        })
    }

    private fun setupDots(count: Int) {
        dots = arrayOfNulls(count)
        for (i in 0 until count) {
            dots[i] = ImageView(this).apply {
                setImageDrawable(getDrawable(R.drawable.dot_inactive))
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(8, 0, 8, 0)
                }
                layoutParams = params
            }
            dotsLayout.addView(dots[i])
        }
    }

    private fun selectDot(position: Int) {
        for (i in dots.indices) {
            val drawableId = if (i == position) R.drawable.dot_active else R.drawable.dot_inactive
            dots[i]?.setImageDrawable(getDrawable(drawableId))
        }
    }
}
