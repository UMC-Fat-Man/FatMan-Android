package com.project.fat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.fat.R
import com.project.fat.rankApi.RankObject
import com.project.fat.rankApi.RankService
import com.project.fat.rankApi.TotalRankResponseModel

class RecyclerviewAdapter : RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder>() {
    private var dataset: ArrayList<List<String>> = arrayListOf<List<String>>().apply {
        for(i in 1..10){
            add(listOf("${i}st.")) //recyclerview에 담을 item 임의로 10개 생성
        }
    }

    var rankingList = listOf<TotalRankResponseModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //recyclerview_item파일의 정보를 Adapter에 붙임
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item,parent,false)
        //뷰 홀더에 view를 담아서 리턴
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private var items : TextView = itemView.findViewById(R.id.nst_nickname)
        private var nickname : TextView = itemView.findViewById(R.id.nickname)
        private var fats : TextView = itemView.findViewById(R.id.howManyFats)
        private var distance : TextView = itemView.findViewById(R.id.howLongDst)

        fun bind(data: List<String>){
            items.text = data[0]

        }
    }




}