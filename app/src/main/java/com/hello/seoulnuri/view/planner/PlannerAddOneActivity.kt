package com.hello.seoulnuri.view.planner

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.hello.seoulnuri.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.hello.seoulnuri.base.Init
import kotlinx.android.synthetic.main.activity_planner_add_one.*
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.location.Location
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.maps.model.*
import com.hello.seoulnuri.model.MarkerData
import com.hello.seoulnuri.utils.ToastMaker
import java.util.*


class PlannerAddOneActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener, Init, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {
    override fun onMyLocationButtonClick(): Boolean {
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        val SEOUL = LatLng(37.27, 126.24)
        val markerOptions = MarkerOptions()
        markerOptions.position(SEOUL)
        markerOptions.title("인천")
        markerOptions.snippet("내가 사는 곳")

        mMap!!.addMarker(markerOptions)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(SEOUL))
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(10f))
        ToastMaker.makeLongToast(this, "MyLocation button clicked")
        return false
    }

    override fun onMyLocationClick(location: Location) {

        ToastMaker.makeLongToast(this, "Current location : " + location)

    }

    override fun init() {
        planner_add_one_next_btn.setOnClickListener(this)

        //허가 요청
        list = listOf<String>(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)

        locations = ArrayList()
        locations.add(SEOUL)
        locations.add(Gyeonghui_Palace)
        locations.add(Chungjeongno_Station)
        locations.add(City_Hall_Station)


    }

    override fun onClick(v: View?) {
        when (v!!) {
            planner_add_one_next_btn -> {
                startActivity(Intent(this, PlannerAddPathCheckActivity::class.java))
                finish()
            }
        }
    }



    override fun onMapReady(map: GoogleMap?) {

        /*FIXME
        * 두 개의 권한을 받아왔으면
        * 지도에 현재 위치 버튼 표시하고, 설정된 위도, 경도로 지도 상에 주소 표시
        * 그리고 혀재 위치 버튼 활성화하고, 마커 찍어 놓기
        * */
        mMap = map
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mMap!!.isMyLocationEnabled = true
            //mapFragment!!.getMapAsync(this)

            val main_icon = BitmapDescriptorFactory.fromResource(R.drawable.button_spot_select)
            val sub_icon = BitmapDescriptorFactory.fromResource(R.drawable.button_spot)


            var list : ArrayList<Marker> = ArrayList()



            val markerOptions = MarkerOptions()
            markerOptions
                    .position(SEOUL)
                    .title("서울")
                    .snippet("한국의 수도")
                    .icon(main_icon)

            marker_seoul = mMap!!.addMarker(markerOptions)
            list.add(marker_seoul)

            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(SEOUL))
            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(10f))


            marker_Gyeonghui_Palace = mMap!!.addMarker(MarkerOptions()
                            .position(Gyeonghui_Palace)
                            .title("경희궁")
                            .snippet("우리나라꺼")
                            .icon(sub_icon))
            list.add(marker_Gyeonghui_Palace)

            marker_Horyu_Station = mMap!!.addMarker(MarkerOptions()
                    .position(Chungjeongno_Station)
                    .title("충정로역")
                    .snippet("지하철역")
                    .icon(sub_icon))
            list.add(marker_Horyu_Station)

            marker_Hyehwa_Station = mMap!!.addMarker(MarkerOptions()
                    .position(City_Hall_Station)
                    .title("덕수궁")
                    .snippet("우리나라꺼")
                    .icon(sub_icon))
            list.add(marker_Hyehwa_Station)

            for (i in 0..list.size-1){
                Log.v("Marker : ",list[i].title)
            }

            //createMarker(markerItems)

            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(SEOUL))
            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(10f))

            mMap!!.setOnMyLocationButtonClickListener(this)
            mMap!!.setOnMyLocationClickListener(this)

        }
    }

    fun createMarker(items : ArrayList<MarkerData>){
        var markerList = ArrayList<Marker>(items.size)
        markerOptions = ArrayList()
        for (position in 0..items.size-1){

            markerOptions[position]
                    .position(LatLng(items[position].latitude, items[position].longitude))
                    .title(items[position].title)
                    .snippet(items[position].snippet)
                    .icon(BitmapDescriptorFactory.fromResource(items[position].iconResID))
            markerList[position] = mMap!!.addMarker(markerOptions[position])

            for (i in 0..markerList.size-1){
                Log.v("Marker : ",markerList[i].title)
            }
        }


    }

    private var mMap: GoogleMap? = null
    val PERMISSION_REQUST_CODE: Int = 100
    lateinit var mapFragment: SupportMapFragment
    lateinit var list: List<String>

    val SEOUL: LatLng = LatLng(37.56, 126.97)
    val Gyeonghui_Palace: LatLng = LatLng(37.570369, 126.969009)
    val Chungjeongno_Station: LatLng = LatLng(37.560055,126.96367199999997)
    val City_Hall_Station: LatLng = LatLng(37.5658049,126.97514610000007)
    lateinit var locations : ArrayList<LatLng>

    lateinit var marker_seoul : Marker
    lateinit var marker_Gyeonghui_Palace : Marker
    lateinit var marker_Horyu_Station : Marker
    lateinit var marker_Hyehwa_Station : Marker


    lateinit var markerArray: ArrayList<Marker>
    lateinit var markerItems : ArrayList<MarkerData>
    lateinit var markerOptions : ArrayList<MarkerOptions>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planner_add_one)

        init()
        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
             //managePermissions.checkPermissions()

         }*/



        // 권한을 요청하기
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUST_CODE)
        //managePermissions = ManagePermissions(this, list, PermissionRequestCode)

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        markerItems = ArrayList()
        markerItems.add(MarkerData(37.56,126.97,"SEOUL","한국의 수도",R.drawable.button_spot_select))
        markerItems.add(MarkerData(37.570369,126.969009,"Gyeonghui_Palace","경희궁",R.drawable.button_spot))
        markerItems.add(MarkerData(37.558553,126.978218,"Horyu_Station","회현역",R.drawable.button_spot))
        markerItems.add(MarkerData(37.582163,127.001978,"Hyehwa_Station","혜화역",R.drawable.button_spot))


    }


    // 요청하고 결과 받아서 처리
    /*FIXME
    * 권한 요청하고 받은 결과를 처리하는 부분
    * 아래에서 결과를 받고 requestCode가 PERMISSION_REQUST_CODE와 일치하고, PERMISSION_GRANTED한 상태이면
    * mapFragment!!.getMapAsync(this)을 통해서 mapFragment에 지도 보여주기
    * */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            mapFragment!!.getMapAsync(this)
        } else {
            // Permission was denied. Display an error message.
        }
    }


}
