package com.lion.a066ex_animalmanager.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lion.a066ex_animalmanager.data.viewmodel.AnimalViewModel
import com.lion.a066ex_animalmanager.databinding.ItemFragmentHomeBinding

class HomeAnimalListAdapter(
    private val items: List<AnimalViewModel>,
    private val listener: HomeItemClickListener
) : RecyclerView.Adapter<HomeItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeItemViewHolder {
        return HomeItemViewHolder.from(parent, listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: HomeItemViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

}


class HomeItemViewHolder(
    private val binding: ItemFragmentHomeBinding,
    private val listener: HomeItemClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(animalViewModel: AnimalViewModel, pos: Int) {
        itemView.setOnClickListener {
            listener.onAnimalInfoClick(animalViewModel, pos) // position
        }
        with(binding) {
            tvItemFragmentHomeName.text = animalViewModel.animalName
        }
    }

    companion object {
        fun from(parent: ViewGroup, listener: HomeItemClickListener): HomeItemViewHolder {
            return HomeItemViewHolder(
                ItemFragmentHomeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), listener
            )
        }
    }
}