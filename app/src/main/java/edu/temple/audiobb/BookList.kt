package edu.temple.audiobb

import android.util.Log
import java.io.Serializable

class BookList : Serializable{
    private val bookList : MutableList<Book> by lazy {
        ArrayList()
    }

    fun add(book: Book) {
        bookList.add(book)
    }

    fun remove(book: Book){
        bookList.remove(book)
    }
    fun getIndexByTitle(_title:String):Int{
        for(i in 0 until bookList.size){
            var book = bookList[i]
            if(book.title == _title){
                Log.d("bookList search", "we found a match")
                return i

            }else{
                Log.d("bookList search", "${book.title} does not match: ${_title}")
            }
        }

        return -1
    }

    operator fun get(index: Int) = bookList[index]

    fun size() = bookList.size

    fun print():String{
        var str = ""
        if(bookList.size !=0) {
            for (i in 0 until bookList.size) {
                str = str + bookList[i].title + "\n"
            }
        }else{
            if(bookList.size == 0){
                str = "The bookList is empty"
            }
        }

        return str
    }

    fun contains(book:Book):Boolean{
        var contains = false;
        if(bookList.size != 0){
            for (i in 0 until bookList.size) {
                if(book == bookList[i]){
                    contains = true
                }
            }
        }
        return contains
    }

}