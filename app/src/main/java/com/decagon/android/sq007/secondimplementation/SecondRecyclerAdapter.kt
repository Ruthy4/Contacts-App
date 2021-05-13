package com.decagon.android.sq007.secondimplementation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.decagon.android.sq007.NewContactModel
import com.decagon.android.sq007.R
import kotlinx.android.synthetic.main.contact_list_recycler_view_items.view.*

class SecondRecyclerAdapter(
    private val recyclerViewModelList: ArrayList<NewContactModel>,
    private val onClickListener: SecondImplementationActivity
) :
    RecyclerView.Adapter<SecondRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view),
        View.OnClickListener {
        var name = itemView.contact_nameTv!!

        init {
            itemView.setOnClickListener(this)
        }

        // calls when the recyclerview has been clicked
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION)
                onClickListener.onItemClick(position, recyclerViewModelList)
        }
    }

    // creates the the viewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_list_recycler_view_items, parent, false)
        return ViewHolder(view)
    }

    // get the size of the list
    override fun getItemCount(): Int {
        return recyclerViewModelList.size
    }

    // method to bind the list with the viewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = recyclerViewModelList[position]
        holder.itemView.apply {

            contact_nameTv.text = recyclerViewModelList[position].newContactName

            val generator: ColorGenerator = ColorGenerator.MATERIAL
            val color = generator.randomColor
            val drawable = TextDrawable.builder().beginConfig()
                .width(100)
                .height(100)
                .endConfig()
                .buildRound(model.newContactName?.substring(0, 1), color)

            contact_image_view.setImageDrawable(drawable)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, recyclerViewModelList: ArrayList<NewContactModel>)
    }
}
