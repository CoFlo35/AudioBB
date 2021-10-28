package edu.temple.audiobb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    private var title : MutableLiveData<String> = MutableLiveData()
    private var author : MutableLiveData<String> = MutableLiveData()

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
}