package com.example.prosthetictools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prosthetictools.databinding.ProstheticUpgradesMiniCardBinding

class ProstheticUpgradesListAdapter(
    private val prostheticUpgradesList: ArrayList<ProstheticUpgrades>,
    private val onClick: (ProstheticUpgrades) -> Unit,
) : RecyclerView.Adapter<ProstheticUpgradesListAdapter.ListViewHolder>() {
    inner class ListViewHolder(
        var binding: ProstheticUpgradesMiniCardBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ProstheticUpgradesMiniCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val prostheticUpgrades = prostheticUpgradesList[position]
        holder.binding.ivProsthetic.setImageResource(prostheticUpgrades.photo)
        holder.itemView.setOnClickListener {
            onClick(prostheticUpgrades)
        }
    }

    override fun getItemCount(): Int = prostheticUpgradesList.size
}