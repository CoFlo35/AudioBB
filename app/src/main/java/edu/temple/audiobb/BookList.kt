package edu.temple.audiobb

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

}