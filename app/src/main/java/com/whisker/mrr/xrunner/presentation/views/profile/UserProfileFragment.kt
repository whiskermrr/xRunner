package com.whisker.mrr.xrunner.presentation.views.profile

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

class UserProfileFragment : BaseFragment() {

    companion object {
        const val LEVEL_ANIMATION_DURATION: Long = 700
    }

    private lateinit var viewModel: UserProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserProfileViewModel::class.java)

        viewModel.userStats().observe(this, Observer { stats ->
            showLevelNumber(stats.level)
            showLevelProgress(stats.percentExp)
        })
    }

    private fun showLevelNumber(level: Int) {
        val animator = ValueAnimator()
        animator.setObjectValues(0, level)
        animator.duration = LEVEL_ANIMATION_DURATION
        animator.addUpdateListener { animation ->
            tvLevel.text = animation.animatedValue.toString()
        }
        animator.start()
    }

    private fun showLevelProgress(percentExp: Int) {
        val progressAnimation = ObjectAnimator.ofInt(expBar, "progress", 0, percentExp)
        progressAnimation.duration = LEVEL_ANIMATION_DURATION
        progressAnimation.interpolator = DecelerateInterpolator()
        progressAnimation.start()
    }
}