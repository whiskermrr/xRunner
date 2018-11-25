package com.whisker.mrr.xrunner.presentation.summary

import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.domain.model.Route
import com.whisker.mrr.xrunner.presentation.BaseMapFragment
import com.whisker.mrr.xrunner.utils.LocationUtils
import com.whisker.mrr.xrunner.utils.xRunnerConstants
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_summary_run.*

class SummaryRunFragment : BaseMapFragment(), OnMapReadyCallback {

    private lateinit var viewModel: SummaryRunViewModel
    private val disposables: CompositeDisposable = CompositeDisposable()
    private lateinit var finalRoute: Route
    override val isMyLocationEnabled: Boolean
        get() = false

    private val routeSavedObserver = Observer<Boolean> {
        Toast.makeText(mainActivity, "Route Saved", Toast.LENGTH_SHORT).show()
        viewModel.getIsRouteSaved().removeObservers(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null) {
            finalRoute = arguments?.getParcelable(xRunnerConstants.EXTRA_FINAL_ROUTE_KEY)!!
        }

        polylineOptions.addAll(finalRoute.waypoints)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_summary_run, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = mainActivity.run {
            ViewModelProviders.of(this, viewModelFactory).get(SummaryRunViewModel::class.java)
        }

        liteMapView.onCreate(savedInstanceState)
        liteMapView.onResume()
        liteMapView.getMapAsync(this)

        bSaveSnapshot.setOnClickListener {
            takeSnapshot()
        }

        viewModel.getIsRouteSaved().observe(this, routeSavedObserver)
        viewModel.saveRoute(finalRoute)
    }

    override fun onMapCreated() {
        val pairCenterDistance = LocationUtils.getDistanceBetweenMostDistinctPoints(finalRoute.waypoints)
        val zoom = LocationUtils.getZoomBasedOnDistance(pairCenterDistance.second, getScreenWidth())
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pairCenterDistance.first, zoom))
    }

    private fun getScreenWidth() : Int {
        val size = Point()
        mainActivity.windowManager.defaultDisplay.getSize(size)
        return size.x
    }

    private fun onSnapshotSaved() {
        bSaveSnapshot.isEnabled = true
        Toast.makeText(context, "Snapshot Saved!", Toast.LENGTH_SHORT).show()
    }

    private fun takeSnapshot() {
        disposables.add(
            Single.create(SingleOnSubscribe<Bitmap> { subscriber ->
                mMap.snapshot { bitmap ->
                    subscriber.onSuccess(bitmap)
                }})
                .observeOn(Schedulers.io())
                .flatMapCompletable {
                    viewModel.saveSnapshot(it)
                }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    onSnapshotSaved()
                }
        )
    }
}