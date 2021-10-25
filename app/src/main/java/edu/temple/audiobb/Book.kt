package edu.temple.audiobb

class Book (_title: String, _author:String){
    private var title = _title
    private var author = _author

    fun getTitle():String{
        return title
    }
    fun getAuthor():String{
        return author
    }

}