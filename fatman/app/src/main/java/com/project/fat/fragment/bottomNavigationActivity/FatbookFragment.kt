package com.project.fat.fragment.bottomNavigationActivity

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.fat.R
import com.project.fat.adapter.GridviewAdapter
import com.project.fat.databinding.FragmentFatbookBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FatbookFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FatbookFragment : Fragment() {
    private lateinit var fatbookBinding: FragmentFatbookBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment



        fatbookBinding = FragmentFatbookBinding.inflate(layoutInflater)
        btnPressed()

        fatbookBinding.gridview.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context,3)
            adapter = GridviewAdapter()
            addItemDecoration(GridSpaceItemDecoration(3,5))
        }

        return fatbookBinding.root
    }

    private fun btnPressed(){
        fatbookBinding.btnHistory.setOnClickListener {
            fatbookBinding.btnHistory.setBackgroundResource(R.drawable.dark_blue_round_view_with_shadow)
            fatbookBinding.btnFatbook.setBackgroundResource(R.drawable.light_blue_round_view_with_shadow)
        }
        fatbookBinding.btnFatbook.setOnClickListener {
            fatbookBinding.btnHistory.setBackgroundResource(R.drawable.light_blue_round_view_with_shadow)
            fatbookBinding.btnFatbook.setBackgroundResource(R.drawable.dark_blue_round_view_with_shadow)
        }
    }
    class GridSpaceItemDecoration(private val spanCount: Int, private val space: Int): RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val column = position % spanCount + 1      // 1부터 시작

            /** 첫번째 행(row-1)에 있는 아이템인 경우 상단에 [space] 만큼의 여백을 추가한다 */
            if (position < spanCount){
                outRect.top = space
            }
            /** 마지막 열(column-N)에 있는 아이템인 경우 우측에 [space] 만큼의 여백을 추가한다 */
            if (column == spanCount){
                outRect.right = space
            }
            /** 모든 아이템의 좌측과 하단에 [space] 만큼의 여백을 추가한다. */
            outRect.left = space
            outRect.bottom = space
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FatbookFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FatbookFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}