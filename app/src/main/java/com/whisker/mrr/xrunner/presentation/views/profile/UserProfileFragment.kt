package com.whisker.mrr.xrunner.presentation.views.profile

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.views.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*

class UserProfileFragment : BaseFragment() {

    companion object {
        const val LEVEL_ANIMATION_DURATION: Long = 500
    }

    private lateinit var viewModel: UserProfileViewModel
    private val animations: MutableList<Animator> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserProfileViewModel::class.java)

        viewModel.userStats().observe(this, Observer { stats ->
            showLevelNumber(stats.level)
            showLevelProgress(stats.percentExp)
            showTotalDistance(stats.totalKilometers, stats.totalMeters)
            showTotalTime(stats.totalHours, stats.totalMinutes)
            showAveragePace(stats.averagePaceMin, stats.averagePaceSec)
        })
    }

    private fun showLevelNumber(level: Int) {
        val animator = ValueAnimator()
        animations.add(animator)
        animator.setObjectValues(0, level)
        animator.duration = LEVEL_ANIMATION_DURATION
        animator.addUpdateListener { animation ->
            tvLevel.text = animation.animatedValue?.toString() ?: "0"
        }
        animator.start()
    }

    private fun showLevelProgress(percentExp: Int) {
        val progressAnimator = ObjectAnimator.ofInt(expBar, "progress", 0, percentExp)
        animations.add(progressAnimator)
        progressAnimator.duration = LEVEL_ANIMATION_DURATION
        progressAnimator.interpolator = DecelerateInterpolator()
        progressAnimator.start()
    }

    private fun showTotalDistance(kilometers: Int, meters: Int) {
        val kilometersAnimator = ValueAnimator()
        animations.add(kilometersAnimator)
        kilometersAnimator.setObjectValues(0, kilometers)

        val metersAnimator = ValueAnimator()
        animations.add(metersAnimator)
        metersAnimator.setObjectValues(0, meters / 100)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(kilometersAnimator, metersAnimator)
        animatorSet.duration = LEVEL_ANIMATION_DURATION

        val animatorListener = ValueAnimator.AnimatorUpdateListener {
            tvTotalDistance.text =
                    String.format(
                        Locale.getDefault(),
                        getString(R.string.distance_format_3),
                        (kilometersAnimator.animatedValue?.toString() ?: "0"),
                        (metersAnimator.animatedValue ?: "0"))
        }

        if(kilometers > meters / 100) {
            kilometersAnimator.addUpdateListener(animatorListener)
        } else {
            metersAnimator.addUpdateListener(animatorListener)
        }

        animatorSet.start()
    }

    private fun showTotalTime(hours: Int, minutes: Int) {
        val hoursAnimator = ValueAnimator()
        animations.add(hoursAnimator)
        hoursAnimator.setObjectValues(0, hours)

        hoursAnimator.addUpdateListener {
            tvTotalTimeHours.text = it.animatedValue?.toString() ?: "0"
        }

        val minutesAnimator = ValueAnimator()
        animations.add(minutesAnimator)
        minutesAnimator.setObjectValues(0, minutes)

        minutesAnimator.addUpdateListener {
            tvTotalTimeMinutes.text = it.animatedValue?.toString() ?: "0"
        }

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(hoursAnimator, minutesAnimator)
        animatorSet.duration = LEVEL_ANIMATION_DURATION
        animatorSet.start()
    }

    private fun showAveragePace(paceMin: Int, paceSec: Int) {
        val minutesAnimator = ValueAnimator()
        animations.add(minutesAnimator)
        minutesAnimator.setObjectValues(0, paceMin)

        val secondsAnimator = ValueAnimator()
        animations.add(secondsAnimator)
        secondsAnimator.setObjectValues(0, paceSec)

        val animatorListener = ValueAnimator.AnimatorUpdateListener {
            tvAveragePace.text =
                    String.format(
                        Locale.getDefault(),
                        getString(R.string.pace_format_2),
                        (minutesAnimator.animatedValue?.toString() ?: "0"),
                        (secondsAnimator.animatedValue?.toString() ?: "0"))
        }

        if(paceSec > paceMin) {
            secondsAnimator.addUpdateListener(animatorListener)
        } else {
            minutesAnimator.addUpdateListener(animatorListener)
        }

        val animatorSet = AnimatorSet()
        animatorSet.duration = LEVEL_ANIMATION_DURATION
        animatorSet.playTogether(minutesAnimator, secondsAnimator)
        animatorSet.start()
    }

    override fun onStop() {
        super.onStop()
        for(animator in animations) {
            if(animator.isRunning) {
                animator.cancel()
            }
        }
    }
}