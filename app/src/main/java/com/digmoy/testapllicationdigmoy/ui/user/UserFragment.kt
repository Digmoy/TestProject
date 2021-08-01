package com.digmoy.testapllicationdigmoy.ui.user

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.digmoy.testapllicationdigmoy.Adapter.UserListAdapter
import com.digmoy.testapllicationdigmoy.RoomDB.Entity.UserTableModel
import com.digmoy.testapllicationdigmoy.R
import com.digmoy.testapllicationdigmoy.Repository.UserRepository
import com.digmoy.testapllicationdigmoy.RoomDB.UserDataBase
import com.digmoy.testapllicationdigmoy.Utils.Coroutines
import com.digmoy.testapllicationdigmoy.Utils.UserInterFace
import com.digmoy.testapllicationdigmoy.Utils.nextFragment
import com.digmoy.testapllicationdigmoy.ViewModel.UserViewModel
import com.digmoy.testapllicationdigmoy.ViewModel.UserViewModelFactory
import com.digmoy.testapllicationdigmoy.databinding.FragmentUserBinding


class UserFragment : Fragment(),View.OnClickListener,UserInterFace{

    private lateinit var binding : FragmentUserBinding
    private lateinit var viewModel : UserViewModel
    private lateinit var userDataBase: UserDataBase
    private lateinit var repository: UserRepository
    private lateinit var factory : UserViewModelFactory
    private lateinit var modeList : ArrayList<UserTableModel>
    private lateinit var adapter : UserListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(inflater,container,false)

        userDataBase = UserDataBase(requireContext())
        repository = UserRepository(userDataBase)
        factory = UserViewModelFactory(repository)
        viewModel = ViewModelProvider(this,factory)[UserViewModel::class.java]

        binding.fab.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.fab ->{
                requireActivity().nextFragment(R.id.action_userFragment_to_addUserFragment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getUserList()
    }

    private fun getUserList() {

        Coroutines.main {
            viewModel.getAllUser().observe(requireActivity(), {
                if (it == null) {
                    Toast.makeText(requireContext(), "Sorry no date found", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    modeList = it as ArrayList<UserTableModel>
                    adapter = UserListAdapter(requireContext(), modeList,this)
                    Log.e("sjdghjlgh", "" + modeList.size)
                    val mLayoutManager: RecyclerView.LayoutManager =
                        GridLayoutManager(requireContext(), 1)
                    binding.recUser.layoutManager = mLayoutManager
                    binding.recUser.addItemDecoration(GridSpacingItemDecoration(1, dpToPx(0), true))
                    binding.recUser.itemAnimator = DefaultItemAnimator()
                    binding.recUser.adapter = adapter

                }

            })
        }
    }

    class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
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
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }

    override fun getUserId(position: Int, id: Int) {
        Log.e("USER_ID",""+id)
        user_id = id.toString()
        requireActivity().nextFragment(R.id.action_userFragment_to_addUserFragment)

    }

    companion object{
        var user_id = ""
    }

}