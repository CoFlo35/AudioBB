package edu.temple.audiobb

import android.os.Bundle
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
    lateinit var viewModel: SharedViewModel
    lateinit var playButton: ImageButton
    lateinit var pauseButton: ImageButton
    lateinit var stopButton: ImageButton
    lateinit var progressBar: SeekBar
    lateinit var nowPlayingTextView:TextView



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

        nowPlayingTextView.text = "Now Playing: "

        playButton.setOnClickListener(){
            POC

            }





        return layout
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

    }

}