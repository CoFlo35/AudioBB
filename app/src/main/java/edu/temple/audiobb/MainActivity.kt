package edu.temple.audiobb

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), BookListFragment.EventInterface {
    private lateinit var recyclerView:RecyclerView
    private lateinit var _layoutManager: RecyclerView.LayoutManager
    private lateinit var fragment1:BookListFragment
    private lateinit var fragment2:BookDetailsFragment

    lateinit var viewModel:SharedViewModel


    var twopane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("Create", "Oncreate()")

        var bookList:BookList = BookList(getData())

        twopane = findViewById<View>(R.id.fragmentContainerView2) != null
        Log.d("tag", twopane.toString())


        fragment1 = BookListFragment.newInstance(bookList)
        fragment2 = BookDetailsFragment()

        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        //twopane = findViewById<View>(R.id.fragmentContainerView2) != null

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView, fragment1)
            .commit()


        if(twopane){
            if(supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) == null){
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainerView2,fragment2)
                    .commit()
            }
        }




        /*
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.hasFixedSize()
        _layoutManager = LinearLayoutManager(this)
        var _adapter = BookListAdapter(bookList)

        recyclerView.layoutManager = _layoutManager
        recyclerView.adapter = _adapter

         */

        //var textView:TextView = findViewById(R.id.textView)

        fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {

        }

    }


    override fun onBackPressed() {
        super.onBackPressed()

        viewModel.setAuthor("")
        viewModel.setTitle("")
        BookDetailsFragment.updateDisplay()
    }
    /*
    fun selectionMade(title:String, author:String){
        viewModel.setTitle(title)
        viewModel.setAuthor(title)
        BookDetailsFragment.updateDisplay()
        if(!twopane){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment2)
                .addToBackStack(null)
                .commit()
        }
    }

     */

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