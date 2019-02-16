package com.whisker.mrr.xrunner.presentation.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.whisker.mrr.domain.model.ChallengeDifficulty
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.model.ChallengeModel
import com.whisker.mrr.xrunner.utils.setTextAndVisibility
import kotlinx.android.synthetic.main.challenge_item_layout.view.*
import org.jetbrains.anko.layoutInflater

class ChallengesAdapter : RecyclerView.Adapter<ChallengesAdapter.ChallengeViewHolder>() {

    private val challenges: MutableList<ChallengeModel> = mutableListOf()

    fun setItems(newChallenges: List<ChallengeModel>) {
        challenges.clear()
        challenges.addAll(newChallenges)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val view = parent.context.layoutInflater.inflate(R.layout.challenge_item_layout, parent, false)
        return ChallengeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return challenges.size
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.bind(challenges[position])
    }

    inner class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(challenge: ChallengeModel) {
            val iconDrawable = when(challenge.difficulty) {
                ChallengeDifficulty.EASY -> R.drawable.ic_bronze_challenge
                ChallengeDifficulty.NORMAL -> R.drawable.ic_silver_challenge
                ChallengeDifficulty.HARD -> R.drawable.ic_gold_challenge
                else -> R.drawable.ic_bronze_challenge
            }

            itemView.ivChallengeIcon.setImageDrawable(itemView.context.getDrawable(iconDrawable))
            itemView.tvChallengeTitle.text = challenge.title
            itemView.tvChallengeDistance.setTextAndVisibility(challenge.distance)
            itemView.tvChallengeSpeed.setTextAndVisibility(challenge.speed)
            itemView.tvChallengeTime.setTextAndVisibility(challenge.time)
            itemView.progressChallenge.progress = challenge.progress
        }
    }
}