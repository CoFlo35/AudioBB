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
class PlayerServiceFragment : Fragment() {
    private lateinit var viewModel: SharedViewModel
    private lateinit var playButton: ImageButton
    private lateinit var pauseButton: ImageButton
    private lateinit var stopButton: ImageButton
    private lateinit var progressBar: SeekBar
    private lateinit var nowPlayingTextView:TextView
    private var changeIcon = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity.let{
            viewModel = ViewModelProvider(it!!).get(SharedViewModel::class.java)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_player_service, container, false)

        playButton = layout.findViewById(R.id.startButton)
        pauseButton = layout.findViewById(R.id.pauseButton)
        stopButton = layout.findViewById(R.id.stopButton)
        progressBar = layout.findViewById(R.id.progressBar)
        nowPlayingTextView = layout.findViewById(R.id.nowPlayingTextView)


        progressBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(name: SeekBar?, positon: Int, p2: Boolean) {
                var progress = progressBarNumber(progressBar)
                (activity as ControlService).seekBarMoved(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                //do nothing
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                //do nothing
            }
        })

        playButton.setOnClickListener(){

            //(activity as ControlService).updateControls()
            var currentBook = viewModel.getBook().value
            if(currentBook?.title != "" && currentBook?.title != null) {
                nowPlayingTextView.text = "Now Playing: " + currentBook?.title.toString()
                playButton.removeSelf()

                //playButton.visibility = View.INVISIBLE
                (activity as ControlService).playClicked()
                }
            else{
                nowPlayingTextView.text = "No Book Selected"
            }
            }

        pauseButton.setOnClickListener(){
            //Log.d("checkButton", "Play button attached?: " + playButton.isAttachedToWindow)
            if(!playButton.isAttachedToWindow) {
                Log.d("checkButton", "checkButton is: " + changeIcon)
                if (!changeIcon) {
                    Log.d("checkButton", "pause Icon")
                    pauseButton.setImageResource(R.drawable.ic_pause)
                } else {
                    Log.d("checkButton", "Play Icon")
                    pauseButton.setImageResource(R.drawable.ic_play)
                }
                changeIcon = !changeIcon
                (activity as ControlService).pauseClicked()
            }


        }

        stopButton.setOnClickListener(){
            progressBar.progress = 0
            nowPlayingTextView.text = ""
            playButton.addTo(layout.findViewById(R.id.controlContainer))
            //Log.d("checkButton", "Play button attached?: " + playButton.isAttachedToWindow)
            //playButton.visibility = View.VISIBLE
            pauseButton.setImageResource(R.drawable.ic_pause)
            changeIcon = true
            (activity as ControlService).stopClicked()
            (activity as ControlService).updateDisplay()

        }


        return layout
    }

    fun View?.removeSelf() {
        this ?: return
        val parent = parent as? ViewGroup ?: return
        parent.removeView(this)
    }

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
        fun newInstance(lmbd: () -> Any){
            PlayerServiceFragment.apply { POC = lmbd }

        }

        fun progressBarNumber(progressBar:SeekBar):Int{
            return progressBar.progress.toInt()
        }

    }

    interface ControlService{
        fun playClicked()
        fun pauseClicked()
        fun stopClicked()
        fun updateDisplay()
        fun seekBarMoved(progress:Int)
        fun isPlaying():Boolean

    }

}