package edu.temple.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import org.w3c.dom.Text

lateinit var layout:View
lateinit var titleTextView:TextView
lateinit var authorTextView: TextView
lateinit var bySplitTextView:TextView
lateinit var viewModel: SharedViewModel

class BookDetailsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_book_details, container, false)

        activity.let{
            viewModel = ViewModelProvider(it!!).get(SharedViewModel::class.java)
        }

        titleTextView = layout.findViewById(R.id.displayTitleName)
        authorTextView = layout.findViewById(R.id.displayAuthorName)
        bySplitTextView = layout.findViewById(R.id.displayBySplit)

        return layout
    }

    companion object {
        fun updateDisplay() {
            titleTextView.text = viewModel.getTitle().value.toString()
            authorTextView.text = viewModel.getAuthor().value.toString()
            bySplitTextView.text = "By"
            }
        }

}