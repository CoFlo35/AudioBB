package edu.temple.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

lateinit var items:BookList
class BookListFragment : Fragment() {
    private lateinit var layout:View
    private lateinit var recyclerView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        layout = inflater.inflate(R.layout.fragment_book_list, container, false)
        recyclerView = layout.findViewById(R.id.fragmentRecyclerView)
        recyclerView.adapter = BookListAdapter(items)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return layout
    }

    companion object {

        @JvmStatic
        fun newInstance(_items:BookList) =
            BookListFragment().apply {
                items = _items
            }

    }
}

