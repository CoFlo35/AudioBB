package edu.temple.audiobb


import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import edu.temple.audlibplayer.PlayerService
import org.json.JSONArray
import org.json.JSONException
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder

//THIS IS THE WEB API BRANCH

private const val AUTO_SAVE_KEY = "auto_save"

class MainActivity : AppCompatActivity(), BookListFragment.EventInterface, BookSearch.BookSeachListener, PlayerServiceFragment.ControlService {
    //initialize references to all views and objects
    private lateinit var fragment1:BookListFragment
    private lateinit var fragment2:BookDetailsFragment
    private lateinit var viewModel:SharedViewModel
    private val volleyQueue:RequestQueue by lazy { Volley.newRequestQueue(this) }
    private var bookList: BookList? = BookList()
    private var instanceState: Bundle? = null
    private lateinit var searchButton:Button
    private var term:String = ""
    private lateinit var storeBookList: BookList
    private lateinit var preferences: SharedPreferences
    private val internalFileName ="my_file"
    private lateinit var file: File
    private var autoSave = false
    private lateinit var controlFragment:PlayerServiceFragment
    private var restartWithTitle:String? = null
    private var isPlaying = false
    private var orientSwitch:Boolean = false
    //initialize bool to test if there are multiple fragment containers
    //if there are two then the device is in landscape
    private var twopane = false
    private lateinit var playerBinder: PlayerService.MediaControlBinder
    var isConnected = false


    var playerHandler = Handler(Looper.getMainLooper()){
        if(it.obj != null) {
           // Log.d("MessageCheck", it.what.toString())
        }
       true
    }

    //create the service Connection object for the service
    private val serviceConnection = object:ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            //set isConnected to true
            isConnected = true
            //Log.d("inspect", service.toString())
            //bind the playerBinder and give it a handler
            playerBinder = service as PlayerService.MediaControlBinder
            playerBinder.setProgressHandler(playerHandler)

            Log.d("orientSwitch", "The value of orientSwitch is: " + orientSwitch.toString())

            //if a storage file exists and an orientation change has not occured,
            //load the last book that was playing
            if(file.exists() && !orientSwitch){
                isPlaying =true
                //Log.d("In/Out", "File Exists")
                try {
                    //read from the storage file
                    val br = BufferedReader(FileReader(file))
                    val text = StringBuilder()
                    var line: String?
                    while (br.readLine().also { line = it } != null) {
                        text.append(line)
                    }
                    //close the reader
                    br.close()

                    //Log.d("onRestart", "Book Played With File")

                    //grab the string that was read from the file
                    restartWithTitle = text.toString()
                    //Log.d("restartTitle", restartWithTitle.toString())

                    //change the text of the nowPlayingText
                    PlayerServiceFragment.changeNowPlayingText(text.toString())

                }catch (e:IOException){
                    e.printStackTrace()
                    //Log.d("In/Out", "reading excpetion")
                }
                //call helper function to locate the book from the bookList
                checkFile()
            }

        }

        //when the service is disconnected, set isConnected to false
        override fun onServiceDisconnected(p0: ComponentName?) {
            isConnected = false
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //determine whether the device is in landscape or portrait
        twopane = findViewById<View>(R.id.fragmentContainerView2) != null
        instanceState = savedInstanceState
        //bind the searchButton
        searchButton = findViewById(R.id.searchButton)

        //set orientation change to false for now
        orientSwitch = false
        //determine the path to the storage file
        preferences = getPreferences(MODE_PRIVATE)
        file = File(filesDir, internalFileName)
        autoSave = preferences.getBoolean(AUTO_SAVE_KEY,false)

        //create a reference in this context to the ViewModel
        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        //I was getting unknown errors without providing a tiny delay, could not find a soultion
        //online about what in particular might have caused the issue, this seemed to work very well
        Thread.sleep(2000)

        //hide the action bar for more room on the layout
        supportActionBar?.hide()

        //create an onclick listener for displaying the dialog to search a book
        searchButton.setOnClickListener(){
            val bookSearch: BookSearch = BookSearch()
            bookSearch.show(supportFragmentManager, "bookSearchDialog")

        }

        //initialize the control fragment to hold controls
        controlFragment = PlayerServiceFragment()

        //add this to the layout
        supportFragmentManager.beginTransaction()
            .replace(R.id.controlFragmentContainer, PlayerServiceFragment())
            .commit()


        //check if the provided bundle on OnCreate has stored values
        if(savedInstanceState != null){
            with(savedInstanceState) {
                //if there is a savedInstanceState, an orientation change has occured
                orientSwitch = true
                //grab the term and bookList from the bundle
                term = savedInstanceState.getString("term").toString()
                //grab the most current bookList
                bookList = getSerializable("bookList") as BookList
                //Log.d("onRestart", "Book Index of: " + n )
                //remake the BookListFragment with the bookList
                fragment1 = BookListFragment.newInstance(bookList!!)
                //check is a book was playing when the orientation was changed
                isPlaying = savedInstanceState.getBoolean("isPlaying")
                //grab the index of the particular book by the title in the Bundle
                val n = bookList!!.getIndexByTitle("Flatland").toString()

                //Log.d("orientSwitch", "Rerieved from storage is: "+savedInstanceState.getString("restartTitle").toString())
                //grab the title of the book that was last playing
                val restartTitle = savedInstanceState.getString("restartTitle")

                //change the DisplayText of the controlFragment to the current book
                PlayerServiceFragment.changeNowPlayingText(restartTitle.toString())

                //send the boolean value of if a book is currently playing and use it to hide
                //or reveal the orgional play button
                PlayerServiceFragment.audioPlaying(isPlaying)

                //create the BookList fragment with the same bookList that was stored from previous
                //lifecycle, add it to its designated container
                //fragment1 = BookListFragment.newInstance(bookList!!)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView,fragment1)
                    .commit()

            }
        }

        getData(term)

    }

    // if the SeekBar has moved, grab the progress and using the duration of the particlar
    //book, find where the audio should be played from
    override fun seekBarMoved(progress:Int) {
        val book = viewModel.getBook().value
                  if(book?.title != "" && book?.title != null) {

                      val dur = book.duration

                      val percent = progress.toFloat().div(100)
                      Log.d("progress", percent.toString())
                      if(this::playerBinder.isInitialized) {
                          playerBinder.seekTo(dur.toFloat().times(percent).toInt())
                      }
                  }

    }


    override fun playClicked() {
        if (isConnected) {
            val book = viewModel.getBook().value
            if (book?.title != "" && book?.title != null) {
                playerBinder.play(book.id)
                //Log.d("playButton", "Playing Book # ${book.id}")

                playerBinder.setProgressHandler(playerHandler)
                //create the editor to write into the storage file
                val editor = preferences.edit()
                editor.putBoolean(AUTO_SAVE_KEY,autoSave)
                editor.apply()

                autoSave = true
                //if the data should be saved then save it
                if(autoSave){
                    try{
                        val outputStream = FileOutputStream(file)
                        outputStream.write(PlayerServiceFragment.getNowPlayingText().toByteArray())
                        outputStream.close()
                        Log.d("In/Out", "wrote to file")
                    }catch(e:Exception){
                        e.printStackTrace()
                        Log.d("In/Out", "Writing Error")
                    }
                }

            }
        }

    }

    //helper function to update display if the device is in portrait mode
    override fun updateDisplay() {
        if(!twopane) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, BookListFragment())
                .commit()
        }
    }

    //pause the audio is the pause button is clicked
    override fun pauseClicked() {
        playerBinder.pause()
    }

    // if the stop button is clicked, then stop the audio, unbind the current service,
    //delete the current storage file, and set the save state to false
    override fun stopClicked() {
        playerBinder.stop()
        unbindService(serviceConnection)
        bindService(Intent(this,PlayerService::class.java)
            ,serviceConnection
            , BIND_AUTO_CREATE)
        file.delete()
        Log.d("In/Out", "file deleted")
        autoSave = false

    }


    // when back button pressed, set the current ViewModel book to an empty one
    // if the deivce is in landscape, replace the fragment container 2 with a
    // new BookDetails fragment
    override fun onBackPressed() {
        viewModel.setBook(Book("","",0,"", 0))
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
                        //bind to the audio service
                        bindService(Intent(this,PlayerService::class.java)
                            ,serviceConnection
                            , BIND_AUTO_CREATE)

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
        val fragmentList = BookListFragment.newInstance(_bookList)
        fragment2 = BookDetailsFragment()

        //always replace the list fragment with a new list fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragmentList)
            .replace(R.id.controlFragmentContainer, controlFragment)
            .commit()

        //PlayerServiceFragment.audioPlaying(isPlaying)

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
        val bookList = viewModel.getBookList().value
        Log.d("trackBooks", bookList!!.print())


        createLayout(bookList)

    }





    //take an Array of JSON objects and convert them all to book objects
    //add all book objects to the new empty BookList and add the BookList to the
    //ViewModel
    fun addBooks(jsonArr:JSONArray){

        for(i in 0 until jsonArr.length()+1){
            //Log.d("Check", counter.toString())
            val obj= jsonArr.getJSONObject(i)
            //counter = counter +1
            val book = Book(obj.getString("title"), obj.getString("author")
                ,obj.getInt("id"), obj.getString("cover_url"), obj.getInt("duration"))
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
        viewModel.setBook(Book("","",0,"",0))
        Log.d("retrieveTerm", "Searched Term is: "+term)

    }



    // save the term and the current BookList as well as the title of the current book and
    // if it is currently playing at the time the activity was killed
    override fun onSaveInstanceState(outState: Bundle) {
        if(playerBinder.isPlaying){

        }
        Log.d("storeList", storeBookList.print())
        restartWithTitle = PlayerServiceFragment.getNowPlayingText()
        if(storeBookList != null) {
            outState.run {
                putString("term", term)
                putSerializable("bookList", storeBookList)
                putBoolean("isPlaying", playerBinder.isPlaying)
                val actualTitle = restartWithTitle
                Log.d("orientSwitch", "title in storage is: " + actualTitle)
                putString("restartTitle", actualTitle)

                //putSerializable("listFrag", BookListFragment())
            }
        }
        super.onSaveInstanceState(outState)
    }

    //helper function to restart the last audio of the book that was played before the
    //activity was completely exited (swiped up)
    fun checkFile(){

        if(restartWithTitle != null){
            Log.d("onCreateLayout", "Booklist size is: " + bookList!!.size().toString())

            val actualTitle = restartWithTitle?.toString()!!.drop(13)
            Log.d("onCreateLayout", "Start Title: " + actualTitle)
            val n = bookList!!.getIndexByTitle(actualTitle)
            if(n != -1){
                playerBinder.play(n+1)
                PlayerServiceFragment.restartUpdateControls()
            }
            //PlayerServiceFragment.changeNowPlayingText(actualTitle)
            Log.d("onCreateLayout", "The index is " + n.toString())
           // PlayerServiceFragment.restartUpdateControls()
            //var n = bookList!!.getIndexByTitle(restartWithTitle)
        }
    }



    //check whether or not the activity has been finished as compared to undergoing
    //an orientation change
    override fun onDestroy() {
        super.onDestroy()
        if(this.isFinishing){
            orientSwitch = false

        }
    }




}