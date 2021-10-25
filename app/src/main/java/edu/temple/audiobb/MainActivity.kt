package edu.temple.audiobb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var bookList = BookList(getData())

        //var textView:TextView = findViewById(R.id.textView)





    }

    private fun getData():MutableList<Book>{
        var items = mutableListOf<Book>()
        items.add(Book("Cat in the Hat", "Dr. Suess"))
        items.add(Book("Where the Wild Things Are", "Maurice Sendak"))
        items.add(Book("The Snow Day", "Erza Jack Keats"))
        items.add(Book("The Giving Tree", "Shel Siverstein"))
        items.add(Book("The Lorax", "Dr.Suess"))
        items.add(Book("A Bear Called Paddington", "Peggy Fortnum"))
        items.add(Book("Winnie-the-Pooh", "Ernest H. Shepard"))
        items.add(Book("The Tale of Peter Rabbit", "Beatrix Potter"))
        items.add(Book("Go Dog Go!", "P. D. Eastman"))
        items.add(Book("The Very Hungry Caterpillar", "Eric Carle"))
        items.add(Book("The Little Engine that Could", "Watty Piper"))
        return items
    }
}