package com.example.prosthetictools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prosthetictools.databinding.ProstheticCardListBinding

class ProstheticListAdapter(
    private val prostheticList: ArrayList<Prosthetic>,
    private val onClick: (Prosthetic) -> Unit,
) : RecyclerView.Adapter<ProstheticListAdapter.ListViewHolder>() {
    inner class ListViewHolder(
        var binding: ProstheticCardListBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ProstheticCardListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val prosthetic = prostheticList[position]
        holder.binding.ivProsthetic.setImageResource(prosthetic.photo)
        holder.binding.tvProsthetic.text = prosthetic.name
        holder.binding.tvFlavorText.text = prosthetic.description
        holder.itemView.setOnClickListener {
            onClick(prosthetic)
        }
    }

    override fun getItemCount(): Int = prostheticList.size
}