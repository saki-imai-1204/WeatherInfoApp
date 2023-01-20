package hu.ait.weatherinfo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.ait.weatherinfo.DetailsActivity
import hu.ait.weatherinfo.data.City
import hu.ait.weatherinfo.databinding.ItemRowBinding
import hu.ait.weatherinfo.touch.TouchHelperCallback
import hu.ait.weatherinfo.viewmodel.CityViewModel
import java.util.*

class Adapter (
    private val context: Context,
    private val cityViewModel: CityViewModel
) : ListAdapter<City, Adapter.ViewHolder>(CityDiffCallback()),
    TouchHelperCallback
     {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemRowBinding = ItemRowBinding.inflate(
            LayoutInflater.from(context),
            parent, false
        )
        return ViewHolder(itemRowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(holder.adapterPosition)
        holder.bind(currentItem)
    }

    fun deleteLast() {
        val lastTodo = getItem(currentList.lastIndex)
        cityViewModel.deleteItem(lastTodo)
    }

    override fun onDismissed(position: Int) {
        cityViewModel.deleteItem(getItem(position))
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        val tmpList = mutableListOf<City>()
        tmpList.addAll(currentList)
        Collections.swap(tmpList, fromPosition, toPosition)
        submitList(tmpList)
    }

    inner class ViewHolder(private val itemRowBinding: ItemRowBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {
        fun bind(city: City) {
            itemRowBinding.tvCityName.text = city.name

            itemRowBinding.btnDelete.setOnClickListener{
                cityViewModel.deleteItem(city)
            }

            itemRowBinding.btnDetails.setOnClickListener {
                val intentDetails = Intent()
                intentDetails.setClass(
                    context, DetailsActivity::class.java
                )

                intentDetails.putExtra("key", itemRowBinding.tvCityName.text)

                context.startActivity(intentDetails)
            }
        }
    }
}

class CityDiffCallback : DiffUtil.ItemCallback<City>() {
    override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem._cityId == newItem._cityId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem == newItem
    }
}
