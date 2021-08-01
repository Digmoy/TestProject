package com.digmoy.testapllicationdigmoy.ui.home

import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.digmoy.testapllicationdigmoy.Adapter.PhotoAdapter
import com.digmoy.testapllicationdigmoy.ApiModel.Photos
import com.digmoy.testapllicationdigmoy.R
import com.digmoy.testapllicationdigmoy.ViewModel.PhotoViewModel
import com.digmoy.testapllicationdigmoy.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding
    private var modeList : ArrayList<Photos> = ArrayList()
    private lateinit var adapter : PhotoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        getPhotoList()
    }

    private fun getPhotoList() {
        val viewModel = ViewModelProvider(this).get(PhotoViewModel::class.java)
        viewModel.getPhoto().observe(requireActivity(), Observer {
            if (it != null){
                modeList = it as ArrayList<Photos>
                adapter = PhotoAdapter(requireContext(),modeList)
                val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(
                    requireActivity(),
                    2
                )
                binding.recHome.layoutManager = mLayoutManager
                binding.recHome.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(0), true))
                binding.recHome.itemAnimator = DefaultItemAnimator()
                binding.recHome.adapter = adapter
            }
        })

        viewModel.makeApiCall()
    }

    class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }

    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                r.displayMetrics
            )
        )
    }

}