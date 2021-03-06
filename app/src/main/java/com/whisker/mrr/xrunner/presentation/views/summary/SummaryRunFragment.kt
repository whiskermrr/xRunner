package com.whisker.mrr.xrunner.presentation.views.summary

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.domain.common.bus.RxBus
import com.whisker.mrr.domain.common.bus.event.NetworkStateEvent
import com.whisker.mrr.xrunner.presentation.model.RouteModel
import com.whisker.mrr.xrunner.presentation.views.base.BaseMapFragment
import com.whisker.mrr.xrunner.utils.getScreenWidth
import com.whisker.mrr.xrunner.utils.XRunnerConstants
import com.whisker.mrr.xrunner.utils.calculateZoom
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_summary_run.*
import org.jetbrains.anko.textColor

class SummaryRunFragment : BaseMapFragment() {

    private lateinit var viewModel: SummaryRunViewModel
    private val disposables: CompositeDisposable = CompositeDisposable()
    private lateinit var finalRoute: RouteModel
    override val isMyLocationEnabled: Boolean
        get() = false

    private val routeSavedObserver = Observer<Boolean> {
        Toast.makeText(mainActivity, "RouteModel Saved", Toast.LENGTH_SHORT).show()
        viewModel.getIsRouteSaved().removeObservers(this)
    }

    private val snapshotSavedObserver = Observer<Boolean> {
        if(it) onSnapshotSaved()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null) {
            finalRoute = arguments?.getParcelable(XRunnerConstants.EXTRA_FINAL_ROUTE_KEY)!!
        }
        polylineOptions.addAll(finalRoute.waypoints)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_summary_run, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.toolbar.title = getString(R.string.title_summary)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SummaryRunViewModel::class.java)

        RxBus.subscribeSticky(NetworkStateEvent::class.java.name, this, Consumer { event ->
            if(event is NetworkStateEvent && event.isNetworkAvailable) {
                tvCantLoadSnapshot.visibility = View.GONE
                routeProgressBar.visibility = View.VISIBLE
                liteMapView.onCreate(savedInstanceState)
                liteMapView.onResume()
                liteMapView.getMapAsync(this)
            }
        })

        initStatsView()

        bSaveSnapshot.setOnClickListener {
            takeSnapshot()
        }

        viewModel.getIsRouteSaved().observe(this, routeSavedObserver)
        viewModel.getIsSnapshotSaved().observe(this, snapshotSavedObserver)
        viewModel.saveRoute(finalRoute)
    }

    override fun onMapCreated() {
        mMap.setOnMapLoadedCallback {
            enableSnapshotButton()
            showMapSnapshot()
            mMap.setOnMapLoadedCallback(null)
        }
        mMap.calculateZoom(finalRoute.waypoints, mainActivity.getScreenWidth())
    }

    private fun showMapSnapshot() {
        routeProgressBar.visibility = View.GONE
        liteMapView.visibility = View.VISIBLE
    }

    private fun enableSnapshotButton() {
        bSaveSnapshot.isEnabled = true
        bSaveSnapshot.background = mainActivity.getDrawable(R.drawable.rounded_corners_button_black)
        bSaveSnapshot.textColor = ContextCompat.getColor(mainActivity, R.color.colorAccent)
    }

    private fun onSnapshotSaved() {
        routeProgressBar.visibility = View.GONE
        bSaveSnapshot.background = mainActivity.getDrawable(R.drawable.rounded_corners_button_success)
        bSaveSnapshot.textColor = ContextCompat.getColor(mainActivity, R.color.colorFlashGreen)
        bSaveSnapshot.text = getString(R.string.saved)
    }

    private fun initStatsView() {
        val stats = finalRoute.routeStats
        tvRouteTitle.text = finalRoute.name
        tvSummaryDistance.text = getString(R.string.distance_format, stats.kilometers, stats.meters)
        tvSummaryPace.text = getString(R.string.pace_format, stats.paceMin, stats.paceSec)
        tvSummaryTime.text = if(stats.hours == 0) {
            getString(R.string.minutes_time_format, stats.minutes, stats.seconds)
        } else {
            getString(R.string.hours_time_format, stats.hours, stats.minutes, stats.seconds)
        }
        tvSummarySpeed.text = getString(R.string.average_speed_format, stats.averageSpeed)
        tvSummaryHeartbeat.text = getString(R.string.empty_record)
    }

    private fun takeSnapshot() {
        disposables.add(
            Single.create<Bitmap> { subscriber ->
                mMap.snapshot { bitmap ->
                    subscriber.onSuccess(bitmap)
                }}
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    bSaveSnapshot.isEnabled = false
                    routeProgressBar.visibility = View.VISIBLE
                }
                .subscribe({
                    viewModel.saveSnapshot(it, finalRoute.date.toString())
                }, Throwable::printStackTrace)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.dispose()
        RxBus.unsubscribeSticky(this)
    }
}