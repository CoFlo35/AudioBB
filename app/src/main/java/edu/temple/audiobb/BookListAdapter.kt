package edu.temple.audiobb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class BookListAdapter(_items:BookList, _ocl:View.OnClickListener):RecyclerView.Adapter<BookListAdapter.ViewHolder>() {

    private val items = _items
    private val ocl = _ocl

    class ViewHolder(_view: View, ocl:View.OnClickListener):RecyclerView.ViewHolder(_view){
        val titleTextView:TextView = _view.findViewById(R.id.titleTextView)
        val authorTextView:TextView = _view.findViewById(R.id.authorTextView)
        val view =_view.apply{setOnClickListener(ocl)}

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        var vh = ViewHolder(v, ocl)
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var book = items.get(position)
        holder.titleTextView.text = book.getTitle()
        holder.authorTextView.text = book.getAuthor()

    }

    override fun getItemCount(): Int {
        return items.size()
    }
}