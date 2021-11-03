package edu.temple.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import org.w3c.dom.Text


class BookDetailsFragment : Fragment() {
    //store variables to hold the layout and the views on it
    lateinit var layout:View
    lateinit var titleTextView:TextView
    lateinit var authorTextView: TextView
    lateinit var coverImageView : ImageView
    //reference the viewModel
    lateinit var viewModel: SharedViewModel
    lateinit var book:Book


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //bind the viewModel
        activity.let{
            viewModel = ViewModelProvider(it!!).get(SharedViewModel::class.java)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_book_details, container, false)

        //bind the views
        titleTextView = layout.findViewById(R.id.displayTitleName)
        authorTextView = layout.findViewById(R.id.displayAuthorName)
        coverImageView = layout.findViewById(R.id.displayBookCover)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //observe when the book in the ViewModel changes
        ViewModelProvider(requireActivity())
            .get(SharedViewModel::class.java)
            .getBook()
            .observe(requireActivity(), {
                updateDisplay(it)
            })


    }
    //create a function to update the BookDetails
    private fun updateDisplay(_book: Book) {
        book = _book
        titleTextView.text = book.title
        authorTextView.text = book.author
        //coverImageView.setImageResource(book.coverURL)
    }
}



