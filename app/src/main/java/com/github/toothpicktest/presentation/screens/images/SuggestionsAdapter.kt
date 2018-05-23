package com.github.toothpicktest.presentation.screens.images

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.toothpicktest.R
import com.github.toothpicktest.presentation.screens.images.SuggestionsAdapter.SuggestionViewHolder
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_tag_suggestion.view.remove
import kotlinx.android.synthetic.main.item_tag_suggestion.view.suggestion
import kotlinx.android.synthetic.main.item_tag_suggestion.view.suggestion_text

class SuggestionsAdapter(
        private val context: Context
) : RecyclerView.Adapter<SuggestionViewHolder>() {

    private val items = mutableListOf<String>()

    private val selectSuggestionClicks = PublishSubject.create<String>()
    private val removeSuggestionClicks = PublishSubject.create<String>()

    class SuggestionViewHolder(
            view: View
    ) : RecyclerView.ViewHolder(view) {
        val view: View = view.suggestion
        val text: TextView = view.suggestion_text
        val remove: View = view.remove
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): SuggestionViewHolder {
        val li = LayoutInflater.from(context)
        val view = li.inflate(R.layout.item_tag_suggestion, parent, false)
        return SuggestionViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(
            holder: SuggestionViewHolder,
            position: Int
    ) {
        val suggestion = items[position]
        holder.text.text = suggestion
        RxView.clicks(holder.view).map { suggestion }.subscribe(selectSuggestionClicks)
        RxView.clicks(holder.remove).map {
            items.removeAt(position)
            notifyItemChanged(position)
            suggestion
        }.subscribe(removeSuggestionClicks)
    }

    fun getSuggestionClicks(): Observable<String> = selectSuggestionClicks

    fun getRemoveSuggestionClicks(): Observable<String> = removeSuggestionClicks

    fun replaceItems(items: Collection<String>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}