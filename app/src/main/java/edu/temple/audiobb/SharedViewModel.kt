package edu.temple.audiobb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.Serializable

class SharedViewModel: ViewModel(), Serializable {
    private val title : MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
    private val author : MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }

    private val book:MutableLiveData<Book> by lazy{
        MutableLiveData<Book>()
    }

    fun setBook(_book:Book?){
        this.book.value = _book
    }

    fun getBook():LiveData<Book>{
        return book
    }

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

    fun clear(){
        title.value = ""
        author.value = ""
    }
}