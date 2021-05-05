package com.decagon.android.sq007.secondimplementation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.NewContactModel
import com.decagon.android.sq007.R
import kotlinx.android.synthetic.main.contact_list_recycler_view_items.view.*

class SecondRecyclerAdapter(private val recyclerViewModelList: ArrayList<NewContactModel>, private val onClickListener: SecondImplementationActivity) :
    RecyclerView.Adapter<SecondRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val name = itemView.contact_nameTv!!
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION)
                onClickListener.onItemClick(position, recyclerViewModelList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_list_recycler_view_items, parent, false)
        Log.d("CHECKINGOKAY", "IT IS CALLING")
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recyclerViewModelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("CHECKINGOKAY", "checker this $recyclerViewModelList")
        holder.itemView.apply {

            contact_nameTv.text = recyclerViewModelList[position].newContactName

//            contact_image_view.setImageResource(recyclerViewModelList[position].contactImage)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, recyclerViewModelList: ArrayList<NewContactModel>)
    }
}
