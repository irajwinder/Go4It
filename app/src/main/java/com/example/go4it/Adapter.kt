package com.example.go4it

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//Reference Link : https://www.geeksforgeeks.org/android-recyclerview-in-kotlin/
//to set and send data to recycler view
//Send Data to the RecyclerView
class Adapter  (private var orderList: ArrayList<Order>,
                private var listener: OnItemClickListener
)
    : RecyclerView.Adapter<Adapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.order_list, parent, false)
        return MyViewHolder(itemView)
    }

    //Send Data to order history
    //Reference: https://www.geeksforgeeks.org/android-recyclerview-in-kotlin/
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val newList = orderList[position]
        holder.tvUserPreference.text = newList.userpreference
        holder.tvMealsPerWeek.text = newList.mealsperweek
        holder.tvInstructions.text = newList.deliveryinstruction
        holder.tvAmount.text = newList.amount.toString()
    }

    override fun getItemCount(): Int = orderList.size

        inner class MyViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView), View.OnClickListener{
            val tvUserPreference:TextView =itemView.findViewById(R.id.textView_user_preference)
            val tvMealsPerWeek:TextView =itemView.findViewById(R.id.textView_meals_per_week)
            val tvInstructions:TextView =itemView.findViewById(R.id.textView_instructions)
            val tvAmount:TextView =itemView.findViewById(R.id.textView_amount)

            init {
                itemView.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}