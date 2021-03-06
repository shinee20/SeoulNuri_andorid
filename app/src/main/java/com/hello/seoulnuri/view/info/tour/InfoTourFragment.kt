package com.hello.seoulnuri.view.info.tour

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.hello.seoulnuri.R
import com.hello.seoulnuri.info.Info_Detail_Intro
import com.hello.seoulnuri.model.InfoItem
import com.hello.seoulnuri.model.info.InfoTourResponse
import com.hello.seoulnuri.model.login.LoginCategoryRequest
import com.hello.seoulnuri.model.search.filter.FilterData
import com.hello.seoulnuri.network.ApplicationController
import com.hello.seoulnuri.network.NetworkService
import com.hello.seoulnuri.utils.SharedPreference
import com.hello.seoulnuri.utils.ToastMaker
import com.hello.seoulnuri.view.info.adapter.FilterAdapter
import com.hello.seoulnuri.view.info.adapter.InfoReservationAdapter
import com.hello.seoulnuri.view.main.MainActivity
import kotlinx.android.synthetic.main.fragment_info_reservation.view.*
import kotlinx.android.synthetic.main.fragment_info_tour.*
import kotlinx.android.synthetic.main.fragment_info_tour.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by VictoryWoo
 */
class InfoTourFragment : Fragment(), View.OnClickListener {


    fun checkVisible(flag: Boolean) {
        if (flag) {
            filter_layout.visibility = View.VISIBLE
        } else {
            filter_layout.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when (v!!) {
            information_toggle_total -> { // 전체
                Log.v("124","필터에서 전체 버튼 누르기")
                if (!information_toggle_total.isSelected) {
                    Log.v("537", "누르기")
                    information_toggle_total.isSelected = true
                    information_toggle_ear.isSelected = false
                    information_toggle_wheel.isSelected = false
                    information_toggle_elder.isSelected = false
                    //filter_layout.visibility = View.GONE
                    filterItems.removeAll(filterItems)
                    checkVisible(true)
                    handi_type.add(9)
                    filter.add(99)
                    println("1153 : "+handi_type)

                } else {
                    Log.v("538", "또 누르기")
                    information_toggle_total.isSelected = false
                    checkVisible(false)
                }
            }
            information_toggle_eye -> { // 시각
                if (!information_toggle_eye.isSelected) {
                    information_toggle_eye.isSelected = true
                    checkVisible(true)
                    if (!eyeCheckStatus) {
                        addFilterItem(2, !eyeCheckStatus)
                        eyeCheckStatus = false

                    }
                    information_toggle_total.isSelected = false
                } else {
                    information_toggle_eye.isSelected = false
                    //checkVisible(false)
                }

            }
            information_toggle_ear -> { // 청각
                if (!information_toggle_ear.isSelected) {
                    information_toggle_ear.isSelected = true
                    checkVisible(true)
                    if (!earCheckStatus) {
                        addFilterItem(3, !earCheckStatus)
                        earCheckStatus = false

                    }
                } else {
                    information_toggle_ear.isSelected = false
                    //checkVisible(false)
                }

            }
            information_toggle_wheel -> { // 지체
                if (!information_toggle_wheel.isSelected) {
                    information_toggle_wheel.isSelected = true
                    checkVisible(true)
                    if (!wheelCheckStatus) {
                        addFilterItem(4, !wheelCheckStatus)
                        wheelCheckStatus = true // 누른 상태를 의미
                    }

                } else {
                    information_toggle_wheel.isSelected = false
                    //checkVisible(false)
                    //removeFilterItem(4,checkStatus)
                    if (wheelCheckStatus) {
                        removeFilterItem(4, wheelCheckStatus)
                        //wheelCheckStatus = false // 누른 상태를 의미
                    }

                }

            }
            information_toggle_elder -> { // 노인
                if (!information_toggle_elder.isSelected) {
                    information_toggle_elder.isSelected = true
                    checkVisible(true)
                    addFilterItem(5, true)
                } else {
                    information_toggle_elder.isSelected = false
                    //checkVisible(false)
                }

            }
            v!! -> {
                Log.v("124","다른 버튼을 누름")
                Log.v("124","뷰의 아이디 : "+v!!.id.toString())
                Log.v("124","filter_list 아이디 : "+filter_list.id)
                Log.v("124","info_tour_recyclerview 아이디 : "+info_tour_recyclerview.id)
                Log.v("124",filter_list.id.toString())
                if(v!!.id == -1){
                    Log.v("124","filter누름")
                    val index_position = filter_list.getChildAdapterPosition(v!!)
                    filterItems[index_position].filter_status = !filterItems[index_position].filter_status
                    filterAdapter.notifyDataSetChanged()
                    ToastMaker.makeShortToast(context!!, index_position.toString())
                } else if(v!!.id == 2131296347){
                    Log.v("124","관광 누름")
                    val index_tour_position = info_tour_recyclerview.getChildAdapterPosition(v!!)
                    val intent : Intent = Intent(context, InfoTourDetailActivity::class.java)
                    intent.putExtra("index",index_tour_position)
                    startActivity(intent)

                }

            }

        }
    }

    fun removeFilterItem(type: Int, flag: Boolean) {
        when (type) {
            2 -> { // 3개
                filterItems.removeAll(filterItems)
                eyeCheckStatus = false
                filterAdapter.notifyDataSetChanged()
            }
            3 -> { // 3개
                filterItems.removeAll(filterItems)
                earCheckStatus = false
                filterAdapter.notifyDataSetChanged()
            }
            4 -> { // 3개
                filterItems.removeAt(0)
                filterItems.removeAt(1)
                filterItems.removeAt(2)
                wheelCheckStatus = false
                filterAdapter.notifyDataSetChanged()
            }
            5 -> { // 3개
                filterItems.removeAll(filterItems)
                elderCheckStatus = false
                filterAdapter.notifyDataSetChanged()
            }

        }

    }

    fun addFilterItem(type: Int, flag: Boolean) {
        when (type) {
            2 -> {
                filterItems.add(FilterData("점자블록", false))
                filterItems.add(FilterData("점자홍보", false))
                filterItems.add(FilterData("보조견 동반", false))
                filterItems.add(FilterData("오디오가이", false))
                filterItems.add(FilterData("안내요원", false))
                filterItems.add(FilterData("점자표지판(안내판)", false))
                filterAdapter.notifyDataSetChanged()
            }
            3 -> {
                filterItems.add(FilterData("수화안내", false))
                filterItems.add(FilterData("자막비디오 가이", false))
                filterItems.add(FilterData("관람석", false))
                filterAdapter.notifyDataSetChanged()
            }
            4 -> {
                filterItems.add(FilterData("휠체어", false))
                filterItems.add(FilterData("엘리베이터", false))
                filterItems.add(FilterData("관람석", false))
                filterAdapter.notifyDataSetChanged()
            }
            5 -> {
                filterItems.add(FilterData("휠체어", false))
                filterItems.add(FilterData("엘리베이터", false))
                filterAdapter.notifyDataSetChanged()
            }
        }

    }

    fun init(v: View) {
        v.information_toggle_total.setOnClickListener(this)
        v.information_toggle_eye.setOnClickListener(this)
        v.information_toggle_ear.setOnClickListener(this)
        v.information_toggle_wheel.setOnClickListener(this)
        v.information_toggle_elder.setOnClickListener(this)
        info_tour_list = ArrayList()
        networkService = ApplicationController.instance!!.networkService
        SharedPreference.instance!!.load(context!!)
        handi_type = ArrayList()
        filter = ArrayList()

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fContext = (activity as MainActivity?)!!
    }

    lateinit var info_tour_list: ArrayList<InfoItem>
    lateinit var info_tour_adpater: InfoReservationAdapter
    lateinit var fContext: MainActivity
    lateinit var filterAdapter: FilterAdapter
    lateinit var filterItems: ArrayList<FilterData>
    var checkStatus: Boolean = true

    var wheelCheckStatus: Boolean = false
    var eyeCheckStatus: Boolean = false
    var earCheckStatus: Boolean = false
    var elderCheckStatus: Boolean = false
    lateinit var filter : ArrayList<Int>
    lateinit var networkService : NetworkService
    lateinit var handi_type : ArrayList<Int>
    lateinit var infoTourRequest : LoginCategoryRequest
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_info_tour, container, false)
        init(view)

        filterItems = ArrayList()

        requestInfoTour()

        /*info_tour_list.add(InfoItem(R.drawable.img_jw, 3.0, 2, "호텔imi"))
        info_tour_list.add(InfoItem(R.drawable.img_jw, 2.0, 20, "호텔jj"))
        info_tour_list.add(InfoItem(R.drawable.img_jw, 4.0, 10, "호텔jw"))
        info_tour_list.add(InfoItem(R.drawable.img_jw, 3.5, 8, "호텔 렐라"))
        info_tour_list.add(InfoItem(R.drawable.img_jw, 3.0, 2, "호텔imi"))
        info_tour_list.add(InfoItem(R.drawable.img_jw, 2.0, 20, "호텔jj"))
        info_tour_list.add(InfoItem(R.drawable.img_jw, 4.0, 10, "호텔jw"))
        info_tour_list.add(InfoItem(R.drawable.img_jw, 3.5, 8, "호텔 렐라"))*/

        info_tour_adpater = InfoReservationAdapter(context!!, infoList = info_tour_list)
        info_tour_adpater.setOnItemClickListener(this)
        view.info_tour_recyclerview.setHasFixedSize(true);
        view.info_tour_recyclerview.layoutManager = GridLayoutManager(activity, 2)
        view.info_tour_recyclerview.adapter = info_tour_adpater

        filterAdapter = FilterAdapter(filterItems, context!!)
        filterAdapter.setOnItemClickListener(this)
        view.filter_list.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        view.filter_list.adapter = filterAdapter
        return view
    }

    fun requestInfoTour() {
        //infoTourRequest = LoginCategoryRequest(handi_type)
        //Log.v("11577 : ", infoTourRequest.toString())
        handi_type.add(9)
        filter.add(99)
        Log.v("130",handi_type.toString())
        Log.v("130",filter.toString())
        println("130, ${handi_type}")
        println("130, ${filter}")
        var infoTourResponse = networkService
                .getInfoTour(SharedPreference.instance!!.getPrefStringData("data")!!,handi_type.toString(),filter.toString())

        infoTourResponse.enqueue(object : Callback<InfoTourResponse>{
            override fun onFailure(call: Call<InfoTourResponse>?, t: Throwable?) {
                Log.v("11588 : ", t!!.message)
            }

            override fun onResponse(call: Call<InfoTourResponse>?, response: Response<InfoTourResponse>?) {
                if(response!!.code() == 200){
                    //Log.v("11599 : ",response!!.body()!!.data.size.toString())
                    println("11599 data size : ${response!!.body()!!.data.get(0).tour_idx}")
                    println("11599 data message : ${response!!.message()}")
                    println("11599 data  : ${response!!.body()!!.status}")
                    info_tour_adpater = InfoReservationAdapter(context!!, infoList = info_tour_list)
                    info_tour_adpater.setOnItemClickListener(this@InfoTourFragment)
                    info_tour_recyclerview.setHasFixedSize(true)
                    info_tour_recyclerview.layoutManager = GridLayoutManager(activity, 2)
                    info_tour_recyclerview.adapter = info_tour_adpater


                }else{
                    Log.v("11600 : ",response!!.message())
                }
            }

        })

    }
}