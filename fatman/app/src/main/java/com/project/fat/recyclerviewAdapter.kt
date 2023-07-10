package com.project.fat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class recyclerviewAdapter : RecyclerView.Adapter<recyclerviewAdapter.ViewHolder>() {
    private var dataset: ArrayList<List<String>> = arrayListOf<List<String>>().apply {
        for(i in 1..10){
            add(listOf("${i}st.")) //recyclerview에 담을 item 임의로 10개 생성
        }
    }

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

        fun bind(data: List<String>){
            items.text = data[0]
        }
    }

}