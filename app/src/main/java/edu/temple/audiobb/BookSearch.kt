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
        //create a builder that takes the context of the parent activity
        var builder : AlertDialog.Builder = AlertDialog.Builder(activity)
        //create an inflater to inflate the layout
        var inflater: LayoutInflater = requireActivity().layoutInflater
        //get a reference to the BookSearch layout and inflate it
        var view: View = inflater.inflate(R.layout.activity_book_search, null)

        builder.setView(view)


        //bind the buttons and editText to their references
        cancelButton = view.findViewById(R.id.cancelButton)
        okButton = view.findViewById(R.id.okButton)
        searchEditText = view.findViewById(R.id.searchEditText)

        //if the cancel button is clicked, close dialog
        cancelButton.setOnClickListener(){
            dialog!!.dismiss()
        }

        //if the ok button is clicked, trigger the ApplySearch method and close the dialog
        okButton.setOnClickListener(){
            var searchTerm = searchEditText.text.toString()

            listener.applySearch(searchTerm)
            dialog!!.dismiss()

        }

        return builder.create()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        //instantiate the listener to call the applySearch method when the user clicks
        //the ok button
        listener = context as BookSeachListener


    }

    //interface to be shared between the BookSearch Dialog and the MainActivity
   public interface BookSeachListener{

        fun applySearch(searchTerm:String){
            Log.d("SearchTerm", "Search For: " + searchTerm)
        }
    }









}