package ps.room.sladeacademy.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ps.room.sladeacademy.R
/**
 * A simple [Fragment] subclass.
 * Use the [ClassSixFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClassSixFragment : Fragment() {
    private var param1: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_SECTION_NUMBER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_six, container, false)
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number_six"
        @JvmStatic
        fun newInstance(param1: Int) =
            ClassSixFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, param1)
                }
            }
    }
}