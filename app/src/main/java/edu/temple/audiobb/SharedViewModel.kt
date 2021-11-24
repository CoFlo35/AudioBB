package edu.temple.audiobb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.temple.audlibplayer.PlayerService
import java.io.Serializable

class SharedViewModel: ViewModel() {

    //create storage of the values to be held in the ViewModel
    /*
    private val title : MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
    private val author : MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }

     */
    private val bookList:MutableLiveData<BookList> by lazy{
        MutableLiveData()
    }

    private val book:MutableLiveData<Book> by lazy{
        MutableLiveData()
    }


   // provide getter and setter functions
    fun setBook(_book:Book?){
        this.book.value = _book
    }

    fun getBook():LiveData<Book>{
        return book
    }

    fun getBookList():LiveData<BookList>{
        return bookList
    }

    fun setBookList(bookList: BookList?){
        this.bookList.value = bookList
    }
    /*

    fun setTitle(_title:String){
        this.title.value = _title
    }

    fun setAuthor(_author:String){
        this.author.value = _author
    }

    fun getTitle():LiveData<String>{
        return title
    }

    fun getAuthor():LiveData<String>{
        return author
    }

     */

}