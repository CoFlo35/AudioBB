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
   //store the fragments and viewModel to be instantiated later
    private lateinit var fragment1:BookListFragment
    private lateinit var fragment2:BookDetailsFragment
    private lateinit var viewModel:SharedViewModel

    //initialize bool to test if there are multiple fragment containers
    //if there are two then the device is in landscape
    var twopane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("Create", "Oncreate()")

        //create a bookList to hold the book items
        var bookList = getData()

        //test if there are 2 fragment containers
        twopane = findViewById<View>(R.id.fragmentContainerView2) != null
       // Log.d("tag", twopane.toString())

        //create the fragments, make the BookList with a new instance which
        //provides the BookList
        fragment1 = BookListFragment.newInstance(bookList)
        fragment2 = BookDetailsFragment()

        // bind the viewModel to a ViewModelProvider and provide the scope
        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        //Log.d("listen", viewModel.getTitle().value.toString())


        //open a fragment transaction, place BookList fragment in its container
        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, fragment1)
                .commit()
        }else{
            if (twopane && viewModel.getBook().value?.title != "") {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView,fragment1)
                    .replace(R.id.fragmentContainerView2, BookDetailsFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit()
            }
            if(!twopane && viewModel.getBook().value?.title != ""){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView,fragment2)
                    .commit()
            }
        }


        // if there are two fragment containers, fill the second with the BookDetails fragment
        if(twopane){
            if(supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) == null){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment1)
                    .add(R.id.fragmentContainerView2,fragment2)
                    .commit()
            }
        }



    }

    // when back button pressed, set the current ViewModel book to an empty one
    // if the deivce is in landscape, replace the fragment container 2 with a
    // new BookDetails fragment
    override fun onBackPressed() {
        viewModel.setBook(Book("",""))
        if(twopane){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView2, fragment2)
                .replace(R.id.fragmentContainerView,fragment1)
                .addToBackStack(null)
                .commit()
            //if the device is in Portrait, replace the  BookDetails fragment with
            // a BookList fragment
        }else{
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment1)
                .addToBackStack(null)
                .commit()
        }





    }


    //supply the list of items
    private fun getData():BookList{
        var items = BookList()
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

    //if a selection is made in portrait, swap the BookDetails and
    // BookList fragments
    override fun selectionMade() {
        if(!twopane){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment2)
                .addToBackStack(null)
                .commit()
        }
    }


}