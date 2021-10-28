package edu.temple.audiobb

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

lateinit var items:BookList
class BookListFragment : Fragment() {
    private lateinit var layout:View
    private lateinit var recyclerView:RecyclerView
    private lateinit var viewModel:SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        layout = inflater.inflate(R.layout.fragment_book_list, container, false)
        recyclerView = layout.findViewById(R.id.fragmentRecyclerView)
        recyclerView.adapter = BookListAdapter(items, onClickListener)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        activity.let{
            viewModel = ViewModelProvider(it!!).get(SharedViewModel::class.java)
        }


        return layout
    }

    val onClickListener = View.OnClickListener {

        val itemPosition = recyclerView.getChildAdapterPosition(it)
        val title = items.get(itemPosition).getTitle().toString()
        val author = items.get(itemPosition).getAuthor().toString()
        Log.d("Fragment1", "${title} & ${author}")

        viewModel.setAuthor(author)
        viewModel.setTitle(title)
        BookDetailsFragment.updateDisplay()
        //(activity as EventInterface).selectionMade(title,author)
    }

    companion object {

        @JvmStatic
        fun newInstance(_items:BookList) =
            BookListFragment().apply {
                items = _items
            }

    }

    interface EventInterface{

    }
}

