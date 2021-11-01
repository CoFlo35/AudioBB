package edu.temple.audiobb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class BookListAdapter(_items:BookList, _ocl:View.OnClickListener):RecyclerView.Adapter<BookListAdapter.ViewHolder>() {

    //store the parameters
    private val bookList = _items
    private val ocl = _ocl

    //create ViewHolder
    class ViewHolder(_view: View, ocl:View.OnClickListener):RecyclerView.ViewHolder(_view){
        val titleTextView:TextView = _view.findViewById(R.id.titleTextView)
        val authorTextView:TextView = _view.findViewById(R.id.authorTextView)
        val view =_view.apply{setOnClickListener(ocl)}
        lateinit var book:Book

    }
    // inflate ViewHolder and create an instance
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        var vh = ViewHolder(v, ocl)
        return vh
    }

        //Bind the Title and Author to their Respective TextViews
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var book = bookList[position]
        holder.authorTextView.text = book.author
        holder.titleTextView.text = book.title
        //holder.titleTextView.text = book
        //holder.authorTextView.text = book.getAuthor()

    }

    //return current size
    override fun getItemCount(): Int {
        return bookList.size()
    }
}