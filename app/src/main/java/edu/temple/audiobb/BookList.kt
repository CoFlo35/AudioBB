package edu.temple.audiobb

class BookList(_bookList:MutableList<Book>) {
    val bookList = _bookList

    fun size():Int{
        return bookList.size
    }

    fun get(n:Int):Book{
        return bookList[n]
    }

    fun add(book:Book){
        bookList.add(book)
    }

    fun remove(book:Book){
        bookList.remove(book)
    }


}