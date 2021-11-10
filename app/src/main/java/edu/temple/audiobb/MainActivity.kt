package edu.temple.audiobb

import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text

//THIS IS THE WEB API BRANCH

class MainActivity : AppCompatActivity(), BookListFragment.EventInterface, BookSearch.BookSeachListener {
    //initialize references to all views and objects
    private lateinit var fragment1:BookListFragment
    private lateinit var fragment2:BookDetailsFragment
    private lateinit var viewModel:SharedViewModel
    private val volleyQueue:RequestQueue by lazy { Volley.newRequestQueue(this) }
    private var bookList: BookList? = BookList()
    lateinit var editText:EditText
    private var instanceState: Bundle? = null
    private lateinit var searchButton:Button
    private var term:String = ""
    private lateinit var storeBookList: BookList

    //initialize bool to test if there are multiple fragment containers
    //if there are two then the device is in landscape
    private var twopane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        twopane = findViewById<View>(R.id.fragmentContainerView2) != null
        instanceState = savedInstanceState
        searchButton = findViewById(R.id.searchButton)
        viewModel = ViewModelProvider(this!!).get(SharedViewModel::class.java)

        supportActionBar?.title = "Book Search Database"

        searchButton.setOnClickListener(){

            var bookSearch: BookSearch = BookSearch()
            bookSearch.show(supportFragmentManager, "bookSearchDialog")


        }

        //check if the provided bundle on OnCreate has stored values
        if(savedInstanceState != null){
            with(savedInstanceState) {

                //grab the term and bookList from the bundle
                term = savedInstanceState.getString("term").toString()
                bookList = getSerializable("bookList") as BookList

                //create the BookList fragment with the same bookList that was stored from previous
                //lifecycle, add it to its designated container
                fragment1 = BookListFragment.newInstance(bookList!!)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView,fragment1)
                    .commit()
            }
        }

        getData(term)

    }

    // when back button pressed, set the current ViewModel book to an empty one
    // if the deivce is in landscape, replace the fragment container 2 with a
    // new BookDetails fragment
    override fun onBackPressed() {
        viewModel.setBook(Book("","",0,""))
        fragment1 = BookListFragment.newInstance(viewModel.getBookList().value!!)
        fragment2 = BookDetailsFragment()
        if(twopane){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView2, fragment2)
                //.replace(R.id.fragmentContainerView,fragment1)
                .addToBackStack(null)
                .commit()
        }
        else{

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment1)
               .addToBackStack(null)
                .commit()
            getData(term)


        }



    }
    // this function makes a GET request to retrieve a JSON Array of JSON Objects from the
    // designated URL, we will call this again if the search term is changed to update
    // the BookList
    private fun getData(search:String){
        var returnList = BookList()
        val url = "https://kamorris.com/lab/cis3515/search.php?term="+search
        //Log.d("test", url)

        volleyQueue.add (
            JsonArrayRequest(Request.Method.GET
                , url
                , null
                , {
                    Log.d("Response", it.toString())
                    try {

                        //check if the BookList has books, if it does make the bookList reference
                        // a new BookList as to keep from adding all new search matches to the end
                        // of the previous list
                        if(bookList!!.size() != null || bookList!!.size() != 0){
                            bookList = BookList()
                        }
                        addBooks(it)
                    } catch (e : JSONException) {
                        e.printStackTrace()
                    }
                }
                , {
                    Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
                   // Log.d("Response", it.toString())

                })
        )


    }

    //Function called when user selects a book from the BookListFragment recyclerview
    override fun selectionMade() {
        fragment2 = BookDetailsFragment()
        //if a selection is made in portrait, swap the BookDetails and
        // BookList fragments
        if(!twopane){
            //getData(term)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment2)
                .addToBackStack(null)
                .commit()
        }else{
            //if a selection is made in landscape, replace the BookDetailsFragment with a new
            // and updated BookDetailsFragment
            getData(term)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView2, fragment2)
                .addToBackStack(null)
                .commit()

        }
    }


    //This helper method will sort and display the fragments in their desginated containers
    //based on the oreintation of the device
    fun createLayout(_bookList: BookList?){

        storeBookList = _bookList!!

        //create the fragments, make the BookList with a new instance which
        //provides the BookList
        bookList = _bookList
        var fragmentList = BookListFragment.newInstance(_bookList!!)
        fragment2 = BookDetailsFragment()

        //always replace the list fragment with a new list fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragmentList)
            .commit()

        //If there are two fragment containers and there is a book chosen, display the selected
        //book in the second container on the layout
            if (twopane && viewModel.getBook().value?.title != "") {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainerView2, BookDetailsFragment())
                    .replace(R.id.fragmentContainerView, fragmentList)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit()
                Log.d("fillContainer", "Config Change Line 220 did this")
            }
        //if a book is selected and device is in portrait, replace the BookListFragment with a
        //BookDetails fragment until the user clicks the back button to make a new selection
        if(!twopane && viewModel.getBook().value?.title != null && viewModel.getBook().value?.title != ""){
            Log.d("checkViewModel", "Book Name is: " + viewModel.getBook().value?.title)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, BookDetailsFragment())
                .commit()
        }

        // if there are two fragment containers and the second container is empty
        // add a BookDetailsFragment to hold its own place
        if(twopane){
            if(supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) == null){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, fragmentList)
                    .add(R.id.fragmentContainerView2,BookDetailsFragment())
                    .commit()
                    Log.d("fillContainer", "Config Change Line 217 did this")
            }
        }
    }

    //callback funtion for when the books are added, after retrieving the BookList from the
    //ViewModel, call the helper function to manage the fragments on the layout
    fun booksAdded(){
        var bookList = viewModel.getBookList().value
        Log.d("trackBooks", bookList!!.print())
        createLayout(bookList)

    }

    //take an Array of JSON objects and convert them all to book objects
    //add all book objects to the new empty BookList and add the BookList to the
    //ViewModel
    fun addBooks(jsonArr:JSONArray){

        for(i in 0 until jsonArr.length()+1){
            //Log.d("Check", counter.toString())
            var obj= jsonArr.getJSONObject(i)
            //counter = counter +1
            var book = Book(obj.getString("title"), obj.getString("author")
                ,obj.getInt("id"), obj.getString("cover_url"))
            bookList?.add(book)
            viewModel.setBookList(bookList)
            booksAdded()
        }

    }

    //when a term is searched from the BookSearch Dialog, get the term
    //and queue a search for the new list of books that matches
    //the term
    override fun applySearch(searchTerm: String) {
        term = searchTerm
        getData(term)
        Log.d("retrieveTerm", "Searched Term is: "+term)

    }

    // save the term and the current BookList to be reused on Config changes
    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run{
            putString("term", term)
            putSerializable("bookList", storeBookList)
        }
        super.onSaveInstanceState(outState)
    }

}