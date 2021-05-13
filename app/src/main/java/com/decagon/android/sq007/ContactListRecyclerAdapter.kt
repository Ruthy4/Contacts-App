package com.decagon.android.sq007

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import kotlinx.android.synthetic.main.contact_list_recycler_view_items.view.*

class ContactListRecyclerAdapter(private val recyclerViewModelList: ArrayList<NewContactModel>, private val onClickListener: MainActivity) :
    RecyclerView.Adapter<ContactListRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
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
    // creates the the viewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_list_recycler_view_items, parent, false)

        return ViewHolder(view)
    }

    // get the size of all items in the list populating recycler view
    override fun getItemCount(): Int {
        return recyclerViewModelList.size
    }

    // method to bind the list with the viewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = recyclerViewModelList[position]

        holder.itemView.apply {

            contact_nameTv.text = recyclerViewModelList[position].newContactName

            // generator to add colour to the contact image
            val generator: ColorGenerator = ColorGenerator.MATERIAL
            val color = generator.randomColor
            val drawable2 = TextDrawable.builder().beginConfig()
                .width(100)
                .height(100)
                .endConfig()
                .buildRound(model.newContactName?.substring(0, 1), color)

            contact_image_view.setImageDrawable(drawable2)
        }
    }

    // enables navigation to the ContactsDetailsActivity
    interface OnItemClickListener {
        fun onItemClick(position: Int, recyclerViewModelList: ArrayList<NewContactModel>)
    }
}
