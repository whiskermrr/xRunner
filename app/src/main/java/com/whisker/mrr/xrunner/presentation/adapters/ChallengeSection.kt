package com.whisker.mrr.xrunner.presentation.adapters

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.whisker.mrr.domain.common.daysBetween
import com.whisker.mrr.domain.model.ChallengeDifficulty
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.model.ChallengeModel
import com.whisker.mrr.xrunner.utils.setTextAndVisibility
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection
import java.util.*

class ChallengeSection(private val isActive: Boolean, private val challenges: List<ChallengeModel>) :
    StatelessSection(SectionParameters.builder()
        .itemResourceId(R.layout.challenge_item_layout)
        .headerResourceId(R.layout.challenge_list_header)
        .build()) {

    override fun getContentItemsTotal(): Int {
        return challenges.size
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, postion: Int) {
        (holder as ChallengeViewHolder).bind(challenges[postion])
    }

    override fun getItemViewHolder(view: View?): RecyclerView.ViewHolder {
        return ChallengeViewHolder(view!!)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        (holder as ChallengeHeaderViewHolder).bind(isActive)
    }

    override fun getHeaderViewHolder(view: View?): RecyclerView.ViewHolder {
        return ChallengeHeaderViewHolder(view!!)
    }

    inner class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivChallengeIcon: ImageView = itemView.findViewById(R.id.ivChallengeIcon)
        private val tvChallengeTitle: TextView = itemView.findViewById(R.id.tvChallengeTitle)
        private val tvChallengeDistance: TextView = itemView.findViewById(R.id.tvChallengeDistance)
        private val tvChallengeSpeed: TextView = itemView.findViewById(R.id.tvChallengeSpeed)
        private val tvChallengeTime: TextView = itemView.findViewById(R.id.tvChallengeTime)
        private val progressChallenge: ProgressBar = itemView.findViewById(R.id.progressChallenge)
        private val tvChallengeDeadline: TextView = itemView.findViewById(R.id.tvChallengeDeadline)

        fun bind(challenge: ChallengeModel) {
            val iconDrawable = when(challenge.difficulty) {
                ChallengeDifficulty.EASY -> R.drawable.ic_challenge_easy
                ChallengeDifficulty.NORMAL -> R.drawable.ic_challenge_normal
                ChallengeDifficulty.HARD -> R.drawable.ic_challenge_hard
                else -> R.drawable.ic_challenge_easy
            }

            ivChallengeIcon.setImageDrawable(itemView.context.getDrawable(iconDrawable))
            tvChallengeTitle.text = challenge.title
            tvChallengeDistance.setTextAndVisibility(challenge.distance)
            tvChallengeSpeed.setTextAndVisibility(challenge.speed)
            tvChallengeTime.setTextAndVisibility(challenge.time)
            progressChallenge.progress = challenge.progress
            challenge.deadline?.let {
                val days = Date(it).daysBetween(Date())
                tvChallengeDeadline.text = if(days > 1) {
                    itemView.context.getString(R.string.challenge_deadline_days_format, days)
                } else {
                    itemView.context.getString(R.string.challenge_deadline_day_format, days)
                }
                tvChallengeDeadline.visibility = View.VISIBLE
            } ?: run { tvChallengeDeadline.visibility = View.GONE }
        }
    }

    inner class ChallengeHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvChallengeHeader: TextView = itemView.findViewById(R.id.tvChallengeHeader)

        fun bind(isActive: Boolean) {
            val text = if(isActive) "Active" else "Finished"
            tvChallengeHeader.text = text
        }
    }
}