package com.whisker.mrr.xrunner.presentation.views.challenge

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.whisker.mrr.domain.common.toLongDate
import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.views.BaseDialogFragment
import com.whisker.mrr.xrunner.utils.TextValidator
import kotlinx.android.synthetic.main.fragment_add_challenge.*
import java.text.SimpleDateFormat
import java.util.*

class AddChallengeDialogFragment : BaseDialogFragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: AddChallengeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_challenge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddChallengeViewModel::class.java)

        viewModel.isChallengeSaved().observe(this, androidx.lifecycle.Observer {
            if(it) {
               activity?.onBackPressed()
            } else {
                Toast.makeText(context, "Problem with saving challenge.", Toast.LENGTH_SHORT).show()
            }
        })

        ivAddChallengeCancel.setOnClickListener {
            activity?.onBackPressed()
        }

        etChallengeDeadline.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show()
        }

        etChallengeTitle.addTextChangedListener(object : TextValidator(etChallengeTitle) {
            override fun validate(text: String) {
                layoutTextTime.isErrorEnabled = false
            }
        })

        etChallengeDistance.addTextChangedListener(object : TextValidator(etChallengeDistance) {
            override fun validate(text: String) {
                layoutTextDistance.isErrorEnabled = false
            }
        })

        etChallengeTime.addTextChangedListener(object : TextValidator(etChallengeTime) {
            override fun validate(text: String) {
                layoutTextTime.isErrorEnabled = false
            }
        })

        bSubmitChallenge.setOnClickListener {
            if(isInputValid()) {
                viewModel.saveChallenge(getChallengeFromInput())
            }
        }
    }

    private fun isInputValid() : Boolean {
        return true
    }

    private fun getChallengeFromInput() : Challenge {
        var distance = etChallengeDistance.text.toString().toFloatOrNull()
        distance?.let { distance *= 1000 }
        var time = etChallengeTime.text.toString().toLongOrNull()
        time?.let { time *= 3600 * 1000 }
        return Challenge(
            title = etChallengeTitle.text.toString(),
            distance = distance,
            time = time,
            speed = etChallengeSpeed.text.toString().toFloatOrNull(),
            deadline = etChallengeDeadline.text.toString().toLongDate(getString(R.string.challenge_deadline_format))
        )
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val sdf = SimpleDateFormat(getString(R.string.challenge_deadline_format), Locale.getDefault())
        etChallengeDeadline.setText(sdf.format(calendar.time))
    }
}