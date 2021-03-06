package com.hello.seoulnuri.view.planner.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.hello.seoulnuri.R
import com.hello.seoulnuri.model.PlannerListData
import com.hello.seoulnuri.model.planner.PlannerDeleteRequest
import com.hello.seoulnuri.model.planner.PlannerDeleteResponse
import com.hello.seoulnuri.network.ApplicationController
import com.hello.seoulnuri.network.NetworkService
import com.hello.seoulnuri.utils.SharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by VictoryWoo
 */
class PlannerAdapter(var item_list: ArrayList<PlannerListData>, var context: Context)
    : RecyclerView.Adapter<PlannerAdapter.PlannerViewHolder>() {

    var check: Int = 0
    private lateinit var onItemClick: View.OnClickListener
    lateinit var networkService: NetworkService

    fun setOnItemClickListener(l: View.OnClickListener) {
        onItemClick = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlannerViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.planner_list, parent, false)
        networkService = ApplicationController.instance!!.networkService
        SharedPreference.instance!!.load(context)
        return PlannerViewHolder(view)

    }

    override fun getItemCount(): Int = item_list.size

    override fun onBindViewHolder(holder: PlannerViewHolder, position: Int) {
        holder.date_text.text = item_list[position].planner_date
        holder.location_text.text = item_list[position].planner_location
        if (check == 1) {
            holder.deleteBtn.visibility = View.VISIBLE

        } else {
            holder.deleteBtn.visibility = View.GONE
        }

        holder.deleteBtn.setOnClickListener {
            Log.v("delete position", position.toString())
            item_list.removeAt(position)
            //datas.removeAt(position)

            notifyDataSetChanged()
        }
    }

    fun deletePlanner(idx : Int){

        var token = SharedPreference.instance!!.getPrefStringData("token","")!!
        var plannerDeleteRequest = PlannerDeleteRequest(idx);
        var plannerDeleteResponse = networkService.deletePlanner(token,plannerDeleteRequest)


        plannerDeleteResponse.enqueue(object : Callback<PlannerDeleteResponse> {
            override fun onFailure(call: Call<PlannerDeleteResponse>?, t: Throwable?) {
                Log.v("failure ",t!!.message)
            }

            override fun onResponse(call: Call<PlannerDeleteResponse>?, response: Response<PlannerDeleteResponse>?) {
                if(response!!.isSuccessful){

                    Log.v("yong",response!!.body()!!.message!!)

                } else{


                }
            }

        })

    }

    class PlannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date_text: TextView = itemView.findViewById(R.id.planner_list_date)
        var location_text: TextView = itemView.findViewById(R.id.planner_list_location)
        var deleteBtn: ImageButton = itemView.findViewById(R.id.delete_btn)
    }

    fun change(checkNum: Int) {
        check = checkNum
        notifyDataSetChanged()
    }


}