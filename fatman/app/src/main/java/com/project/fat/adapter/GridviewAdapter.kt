package com.project.fat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.fat.R

class GridviewAdapter: RecyclerView.Adapter<GridviewAdapter.ViewHolder>() {
    private var dataset: ArrayList<List<String>> = arrayListOf<List<String>>().apply {
        for(i in 1..11){
            add(listOf("${i}st몬스터")) //recyclerview에 담을 item 임의로 10개 생성
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //recyclerview_item파일의 정보를 Adapter에 붙임
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gridview_item,parent,false)
        //뷰 홀더에 view를 담아서 리턴
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private var items : TextView = itemView.findViewById(R.id.gridTxt)

        fun bind(data: List<String>){
            items.text = data[0]
        }
    }

}