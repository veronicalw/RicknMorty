package com.leony.app.ricknmorty.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.leony.app.ricknmorty.R
import com.leony.app.ricknmorty.data.listener.GetCharacterListItemClickListener
import com.leony.app.ricknmorty.data.model.GetCharacterListResponse
import com.leony.app.ricknmorty.data.model.GetCharacterResults
import com.squareup.picasso.Picasso

class GetCharacterListAdapter(
    val itemClickListener: GetCharacterListItemClickListener,
    val context: Context
) : RecyclerView.Adapter <GetCharacterListAdapter.GetCharacterListViewHolder>() {

    private var charList : MutableList<GetCharacterResults> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GetCharacterListViewHolder {
        return GetCharacterListViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item_character_list, parent, false), itemClickListener)
    }

    /**
     * Binding item data to the ViewHolder using its position
     */
    override fun onBindViewHolder(holder: GetCharacterListViewHolder, position: Int) {
        holder.onBind(charList[position])
    }

    /**
     * Size of the Character's list
     */
    override fun getItemCount(): Int {
        return charList.size
    }

    /**
     * This public method called by the override class (GetCharacterDetailView - displayCharacterDetailResult())
     * to pass data to the adapter.
     */
    fun updateListCharacter(response: GetCharacterListResponse) {
        if (response.results.isNotEmpty()){
            this.charList = response.results
            notifyDataSetChanged()
        }
    }

    /**
     * Custom View Holder
     */
    class GetCharacterListViewHolder(val view: View, val itemClickListener: GetCharacterListItemClickListener) : RecyclerView.ViewHolder(view){
        private val charName by lazy { view.findViewById(R.id.itemName) as TextView}
        private val charSpecies by lazy { view.findViewById(R.id.itemSpecies) as TextView}
        private val charStatus by lazy { view.findViewById(R.id.itemStatus) as TextView}
        private val charImage by lazy { view.findViewById(R.id.itemImage) as ImageView }

        fun onBind(response: GetCharacterResults){
            charName.text = response.name
            charSpecies.text = response.species
            charStatus.text = response.status
            Picasso.get().load(response.image).placeholder(R.color.light_gray).error(R.color.light_gray).into(charImage)
            view.setOnClickListener {
                response.id?.let { it1 -> itemClickListener.onClickedCharacterId(it1) }
            }
        }
    }
}