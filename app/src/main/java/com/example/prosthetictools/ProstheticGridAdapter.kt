package com.example.prosthetictools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prosthetictools.databinding.ProstheticCardBinding

class ProstheticGridAdapter(
    private val prostheticList: ArrayList<Prosthetic>,
    private val onClick: (Prosthetic) -> Unit,
) : RecyclerView.Adapter<ProstheticGridAdapter.GridViewHolder>() {
    inner class GridViewHolder(
        var binding: ProstheticCardBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val binding = ProstheticCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val prosthetic = prostheticList[position]
        holder.binding.ivProsthetic.setImageResource(prosthetic.photo)
        holder.binding.tvProsthetic.text = prosthetic.name
        holder.itemView.setOnClickListener {
            onClick(prosthetic)
        }
    }

    override fun getItemCount(): Int = prostheticList.size
}