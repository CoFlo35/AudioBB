package edu.temple.audiobb

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment

class BookSearch:AppCompatDialogFragment() {
    private lateinit var cancelButton:Button
    private lateinit var okButton:Button
    private lateinit var searchEditText: EditText
    private lateinit var listener:BookSeachListener


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builder : AlertDialog.Builder = AlertDialog.Builder(activity)
        var inflater: LayoutInflater = requireActivity().layoutInflater
        var view: View = inflater.inflate(R.layout.activity_book_search, null)

        builder.setView(view)


        cancelButton = view.findViewById(R.id.cancelButton)
        okButton = view.findViewById(R.id.okButton)
        searchEditText = view.findViewById(R.id.searchEditText)

        cancelButton.setOnClickListener(){
            dialog!!.dismiss()
        }

        okButton.setOnClickListener(){
            var searchTerm = searchEditText.text.toString()

            listener.applySearch(searchTerm)
            dialog!!.dismiss()

        }

        return builder.create()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = context as BookSeachListener


    }



   public interface BookSeachListener{

        fun applySearch(searchTerm:String){
            Log.d("SearchTerm", "Search For: " + searchTerm)
        }
    }









}