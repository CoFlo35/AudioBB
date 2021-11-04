package edu.temple.audiobb

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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

class MainActivity : AppCompatActivity(), BookListFragment.EventInterface {
   //store the fragments and viewModel to be instantiated later
    private lateinit var fragment1:BookListFragment
    private lateinit var fragment2:BookDetailsFragment
    private lateinit var viewModel:SharedViewModel
    private val volleyQueue:RequestQueue by lazy { Volley.newRequestQueue(this) }
    private var bookList: BookList? = BookList()
    lateinit var editText:EditText
    private var instanceState: Bundle? = null

    //initialize bool to test if there are multiple fragment containers
    //if there are two then the device is in landscape
    private var twopane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        instanceState = savedInstanceState

        Log.d("Create", "Oncreate()")
        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        //viewModel.setBookList(bookList)

        getData("")

        //Log.d("Check","bookList Length: " + bookList.size().toString())


    }

    // when back button pressed, set the current ViewModel book to an empty one
    // if the deivce is in landscape, replace the fragment container 2 with a
    // new BookDetails fragment
    override fun onBackPressed() {
        viewModel.setBook(Book("","",0,""))
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
                        addBooks(it)
                        //grab array of all POJO's
                        //for each element of array create a book
                        //add book to booklist
                            //var counter =
                        //Log.d("Check", "Returned Array: " +it.length().toString())

                        //Log.d("Check", bookList.print())


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

    fun onFinish(){
       // Log.d("Check", "\n"+ bookList!!.print())


    }


    fun createLayout(_bookList: BookList?){
        //test if there are 2 fragment containers
        twopane = findViewById<View>(R.id.fragmentContainerView2) != null
        // Log.d("tag", twopane.toString())

        //create the fragments, make the BookList with a new instance which
        //provides the BookList
        //var testList = BookList()
        //testList.add(bookList[1])
        //Log.d("MyData", testList.print())
        fragment1 = BookListFragment.newInstance(bookList!!)
        fragment2 = BookDetailsFragment()

        // bind the viewModel to a ViewModelProvider and provide the scope

        //Log.d("listen", viewModel.getTitle().value.toString())

        //open a fragment transaction, place BookList fragment in its container

        if(instanceState == null) {
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

    fun booksAdded(){
        var bookList = viewModel.getBookList().value
        createLayout(bookList)
    }

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


}