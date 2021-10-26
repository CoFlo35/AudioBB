package edu.temple.audiobb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView:RecyclerView
    private lateinit var _layoutManager: RecyclerView.LayoutManager
    private lateinit var fragment1:BookListFragment



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var bookList:BookList = BookList(getData())

        fragment1 = BookListFragment.newInstance(bookList)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView, fragment1)
            .commit()

        /*
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.hasFixedSize()
        _layoutManager = LinearLayoutManager(this)
        var _adapter = BookListAdapter(bookList)

        recyclerView.layoutManager = _layoutManager
        recyclerView.adapter = _adapter

         */




        //var textView:TextView = findViewById(R.id.textView)





    }

    private fun getData():MutableList<Book>{
        var items = mutableListOf<Book>()
        items.add(Book("Cat in the Hat", "Dr. Suess"))
        items.add(Book("Corduroy", "Don Freeman"))
        items.add(Book("The Snow Day", "Erza Jack Keats"))
        items.add(Book("The Giving Tree", "Shel Siverstein"))
        items.add(Book("The Lorax", "Dr.Suess"))
        items.add(Book("Olivia", "Ian Falconer"))
        items.add(Book("Winnie-the-Pooh", "Ernest H. Shepard"))
        items.add(Book("The Tale of Peter"+'\n'+" Rabbit", "Beatrix Potter"))
        items.add(Book("Go Dog Go!", "P. D. Eastman"))
        items.add(Book("Coraline", "Niel Gaiman"))
        items.add(Book("Charlie Brown", "Charles M. Schulz"))

        return items
    }
}