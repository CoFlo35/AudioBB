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
    //private var userPromptTextView:TextView = findViewById(R.id.textView)

    lateinit var viewModel:SharedViewModel


    var twopane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("Create", "Oncreate()")

        var bookList:BookList = BookList(getData())

        twopane = findViewById<View>(R.id.fragmentContainerView2) != null
       // Log.d("tag", twopane.toString())


        fragment1 = BookListFragment.newInstance(bookList)
        fragment2 = BookDetailsFragment()

        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        //Log.d("listen", viewModel.getTitle().value.toString())

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


    }



    override fun onBackPressed() {
        viewModel.setBook(Book("",""))
        if(twopane){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView2, fragment2)
                .addToBackStack(null)
                .commit()
        }else{
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment1)
                .addToBackStack(null)
                .commit()
        }

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

    override fun selectionMade() {
        if(!twopane){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment2)
                .addToBackStack(null)
                .commit()
        }
    }


}