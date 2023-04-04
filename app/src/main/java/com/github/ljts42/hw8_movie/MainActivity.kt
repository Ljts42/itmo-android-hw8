package com.github.ljts42.hw8_movie

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sin

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.animImageView)
        animateImage(imageView)

        val customView = findViewById<CustomView>(R.id.custom_view)
        customView.startAnimating()
    }

    private fun animateImage(imageView: ImageView) {
        val translationXAnimator = ObjectAnimator.ofFloat(imageView, "translationX", 600f, -600f)
        translationXAnimator.repeatCount = ValueAnimator.INFINITE
        translationXAnimator.repeatMode = ValueAnimator.RESTART
        translationXAnimator.duration = 6000

        val yCoordInterpolator = TimeInterpolator { input ->
            sin(input * Math.PI * 2).toFloat()
        }

        val translationYAnimator = ObjectAnimator.ofFloat(imageView, "translationY", 0f, 100f)
        translationYAnimator.interpolator = yCoordInterpolator
        translationYAnimator.repeatCount = ValueAnimator.INFINITE
        translationYAnimator.repeatMode = ValueAnimator.RESTART
        translationYAnimator.duration = 2000

        val rotationInterpolator = TimeInterpolator { input ->
            sin((input - 0.25) * Math.PI * 2).toFloat()
        }
        val rotationAnimator = ObjectAnimator.ofFloat(imageView, "rotation", -15f, 15f)
        rotationAnimator.duration = 2000
        rotationAnimator.interpolator = rotationInterpolator
        rotationAnimator.repeatCount = ValueAnimator.INFINITE
        rotationAnimator.repeatMode = ValueAnimator.RESTART

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translationXAnimator, translationYAnimator, rotationAnimator)

        animatorSet.start()
    }
}