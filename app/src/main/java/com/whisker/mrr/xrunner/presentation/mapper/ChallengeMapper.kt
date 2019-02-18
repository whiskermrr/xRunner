package com.whisker.mrr.xrunner.presentation.mapper

import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.xrunner.presentation.model.ChallengeModel
import com.whisker.mrr.xrunner.utils.KILOMETERS
import com.whisker.mrr.xrunner.utils.METERS
import com.whisker.mrr.xrunner.utils.toDistance
import java.util.*

class ChallengeMapper {

    companion object {

        fun transformList(challenges: List<Challenge>) : List<ChallengeModel> {
            val models = mutableListOf<ChallengeModel>()

            for(challenge in challenges) {
                models.add(transform(challenge))
            }

            return models
        }

        fun transform(challenge: Challenge) : ChallengeModel {
            val model = ChallengeModel()
            model.title = challenge.title
            model.isFinished = challenge.isFinished
            model.deadline = challenge.deadline
            model.progress = challenge.progress
            model.difficulty = challenge.difficulty
            model.experience = challenge.experience

            challenge.distance?.let {
                model.distance = String.format(
                    Locale.getDefault(),
                    "%dkm",
                    (it / 1000).toInt())
            }

            challenge.time?.let {
                val calendar = Calendar.getInstance()
                calendar.time = Date(it)
                val hours = calendar.get(Calendar.HOUR) - 1
                model.time = String.format(Locale.getDefault(), "%dh", hours)

            }

            challenge.speed?.let {
                model.speed = String.format(Locale.getDefault(), "%skm/s", it.toString())
            }

            return model
        }
    }
}