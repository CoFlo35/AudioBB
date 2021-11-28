package edu.temple.audiobb

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlayerServiceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
lateinit var POC:Any
private lateinit var layout:View
private var text:String? = null
private lateinit var nowPlayingTextView:TextView
private lateinit var playButton: ImageButton
private var isPlaying = false
class PlayerServiceFragment : Fragment() {
    //initialize views to be referenced from layout
    private lateinit var viewModel: SharedViewModel
    private lateinit var pauseButton: ImageButton
    private lateinit var stopButton: ImageButton
    private lateinit var progressBar: SeekBar
    //initialize a boolean to change the pause icon to a play icon
    //once an audio is playing
    private var changeIcon = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //create reference to ViewModel in this classes particular context
        activity.let{
            viewModel = ViewModelProvider(it!!).get(SharedViewModel::class.java)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_player_service, container, false)

        //bind all the views to their respective places
        playButton = layout.findViewById(R.id.startButton)
        pauseButton = layout.findViewById(R.id.pauseButton)
        stopButton = layout.findViewById(R.id.stopButton)
        progressBar = layout.findViewById(R.id.progressBar)
        nowPlayingTextView = layout.findViewById(R.id.nowPlayingTextView)

        //if a book is not playing, make the original start button visible
        if(!isPlaying) {
            playButton.visibility = View.VISIBLE
        }
        //otherwise, a book is playing so hide the original play button from the display
        else{
            playButton.visibility = View.INVISIBLE}

        //if text has been set, then change the DisplayText to the book currently playing
        if(text != null){
            nowPlayingTextView.text = text
        }



        //when the progress is changed handle the response, bind this to the interface
        progressBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(name: SeekBar?, positon: Int, p2: Boolean) {
                var progress = progressBarNumber(progressBar)
                (activity as ControlService).seekBarMoved(progress)
            }

            //no need as we dont use it
            override fun onStartTrackingTouch(p0: SeekBar?) {
                //do nothing
            }
            //not need as we dont use it
            override fun onStopTrackingTouch(p0: SeekBar?) {
                //do nothing
            }
        })

        //set the onclick listener for the original play button, handle all internals that
        //are hidden from the Main activity
        playButton.setOnClickListener(){
            var currentBook = viewModel.getBook().value
            //if there is a book selected, change the DisplayText and make the button invisibl
            if(currentBook?.title != "" && currentBook?.title != null) {
                nowPlayingTextView.text = "Now Playing: " + currentBook?.title.toString()
                playButton.visibility = View.INVISIBLE

                //bind this onclick to the interface
                (activity as ControlService).playClicked()
                }
            else{
                //if no book has been selected, present a message to the user via the
                //DisplayText
                nowPlayingTextView.text = "No Book Selected"
            }
            }

        //set the onclick listener for the PauseButton, handle all internals hidden from the
        //Main activity
        pauseButton.setOnClickListener(){
            //if the origional PlayButton is invisible, a book is currently running audio
            if(playButton.visibility == View.INVISIBLE) {
                //Log.d("checkButton", "checkButton is: " + changeIcon)

                //if ChangeIcon is false, set the icon of the button to the pause icon
                if (!changeIcon) {
                    Log.d("checkButton", "pause Icon")
                    pauseButton.setImageResource(R.drawable.ic_pause)
                }
                //otherwise, set the icon of the button to the play icon
                else {
                    Log.d("checkButton", "Play Icon")
                    pauseButton.setImageResource(R.drawable.ic_play)
                }
                //flip the value of changeIcon from false-->true or vice versa to track which
                // icon should be added on the next click
                changeIcon = !changeIcon
                //bind to the interface
                (activity as ControlService).pauseClicked()
            }


        }

        //Set the onclick listener for the stop button to handle all events internal to this fragment
        stopButton.setOnClickListener(){
            //set the progress of the SeekBar back to 0
            progressBar.progress = 0
            //remove the DisplayText of currently playing book
            nowPlayingTextView.text = ""

            //make the original play button visible to the user for their next selection
            playButton.visibility = View.VISIBLE
            //set the pausebutton icon back to the pause icon
            pauseButton.setImageResource(R.drawable.ic_pause)
            //reset the value of changeIcon to match its first instantiation
            changeIcon = true
            //bind onclick to interface
            (activity as ControlService).stopClicked()
            (activity as ControlService).updateDisplay()

        }

        return layout
    }

    //helper functions to remove a view dynamically
    fun View?.removeSelf() {
        this ?: return
        val parent = parent as? ViewGroup ?: return
        parent.removeView(this)
    }

    //helper function to add a view dynamically
    fun View?.addTo(parent: ViewGroup?) {
        this ?: return
        parent ?: return
        parent.addView(this)
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PlayerServiceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic

        //companion object static functions
        fun newInstance(_text:String){
            PlayerServiceFragment.apply { text = _text }

        }

        fun progressBarNumber(progressBar:SeekBar):Int{
            return progressBar.progress.toInt()
        }

        fun changeNowPlayingText(_text:String){
            Log.d("orientChange", "Changing TextView to " + _text)
            PlayerServiceFragment.apply { text = _text }
        }

        fun getNowPlayingText():String{
            return nowPlayingTextView.text.toString()
        }

        fun restartUpdateControls(){
            playButton.visibility = View.INVISIBLE
        }

        fun audioPlaying(playing:Boolean){
            PlayerServiceFragment.apply { isPlaying = playing }
        }

    }

    //create the interface as to be overrided on the side of the Main activity to perform its
    // own tasks
    interface ControlService{
        fun playClicked()
        fun pauseClicked()
        fun stopClicked()
        fun updateDisplay()
        fun seekBarMoved(progress:Int)

    }

}