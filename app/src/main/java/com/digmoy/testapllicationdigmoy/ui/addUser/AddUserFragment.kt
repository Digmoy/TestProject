package com.digmoy.testapllicationdigmoy.ui.addUser

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.digmoy.testapllicationdigmoy.ImageCompression.ImageCompression.Companion.fileUri
import com.digmoy.testapllicationdigmoy.R
import com.digmoy.testapllicationdigmoy.Repository.UserRepository
import com.digmoy.testapllicationdigmoy.RoomDB.Entity.UserTableModel
import com.digmoy.testapllicationdigmoy.RoomDB.UserDataBase
import com.digmoy.testapllicationdigmoy.Utils.Coroutines
import com.digmoy.testapllicationdigmoy.Utils.nextFragment
import com.digmoy.testapllicationdigmoy.ViewModel.UserViewModel
import com.digmoy.testapllicationdigmoy.ViewModel.UserViewModelFactory
import com.digmoy.testapllicationdigmoy.databinding.FragmentAddUserBinding
import com.digmoy.testapllicationdigmoy.ui.image.ImageFragment
import com.digmoy.testapllicationdigmoy.ui.user.UserFragment.Companion.user_id
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


class AddUserFragment : Fragment() ,View.OnClickListener{

    private lateinit var binding : FragmentAddUserBinding
    private lateinit var viewModel : UserViewModel
    private lateinit var userDataBase: UserDataBase
    private lateinit var repository: UserRepository
    private lateinit var factory : UserViewModelFactory
    var fragment: Fragment? = null
    var mUsername : String = ""
    var mUserEmail : String =""
    var mUserPhone : String =""
    var mUserAddress : String =""
    var id : String = ""

    private var user: UserTableModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddUserBinding.inflate(inflater,container,false)

        userDataBase = UserDataBase(requireContext())
        repository = UserRepository(userDataBase)
        factory = UserViewModelFactory(repository)
        viewModel = ViewModelProvider(this,factory)[UserViewModel::class.java]


        image_view = binding.imageView
        binding.imageView.setOnClickListener(this)
        binding.btnUser.setOnClickListener(this)



        return binding.root
    }

    companion object{
        lateinit var image_view : ImageView
    }


    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.image_view ->{

                val someFragment: Fragment = ImageFragment()
                val transaction = requireFragmentManager().beginTransaction()
                transaction.replace(R.id.content, someFragment)
                transaction.commit()
            }
            R.id.btn_user ->{
                val id = if (user != null) user?.id else null
                Log.e("AddUser",""+ fileUri)
                mUsername = binding.name.text.toString()
                mUserEmail = binding.email.text.toString()
                mUserPhone = binding.phone.text.toString()
                mUserAddress = binding.address.text.toString()
                var cancel = false
                var focusView: View? = null
                if(TextUtils.isEmpty(mUsername))
                {
                    Toast.makeText(requireContext(), "Enter user name", Toast.LENGTH_SHORT).show()
                    focusView = binding.name
                    cancel = true
                }
                else if (TextUtils.isEmpty(mUserEmail))
                {
                    Toast.makeText(requireContext(), "Enter user email", Toast.LENGTH_SHORT).show()
                    focusView = binding.email
                    cancel = true
                }
                else if (!isEmailValid(mUserEmail)){
                    Toast.makeText(requireContext(), "Enter user valid email", Toast.LENGTH_SHORT).show()
                    focusView = binding.email
                    cancel = true
                }
                else if (TextUtils.isEmpty(mUserPhone))
                {
                    Toast.makeText(requireContext(), "Enter user phone", Toast.LENGTH_SHORT).show()
                    focusView = binding.phone
                    cancel = true
                }
                else if (!isPhoneValid(mUserPhone))
                {
                    Toast.makeText(requireContext(), "Enter user valid phone", Toast.LENGTH_SHORT).show()
                    focusView = binding.phone
                    cancel = true
                }
                else if (TextUtils.isEmpty(mUserAddress))
                {
                    Toast.makeText(requireContext(), "Enter user address", Toast.LENGTH_SHORT).show()
                    focusView = binding.address
                    cancel = true
                }
                if (cancel) {
                    focusView?.requestFocus()
                } else {
                    val iStream: InputStream = fileUri?.let {
                        requireActivity().getContentResolver().openInputStream(it) }!!
                    val inputData: ByteArray = getBytes(iStream)!!


                    val user = UserTableModel(id,mUsername,mUserEmail,mUserPhone,mUserAddress,inputData)
                    Coroutines.main {
                        if (user_id =="")
                        {
                            viewModel.insertUser(user).also {
                                Toast.makeText(requireContext(),"Successfully save user",Toast.LENGTH_SHORT).show()
                                requireActivity().nextFragment(R.id.action_addUser_to_userList)
                            }
                        }
                        else
                        {
                            viewModel.updateUser(user).also {
                                Toast.makeText(requireContext(),"Successfully update user",Toast.LENGTH_SHORT).show()
                                requireActivity().nextFragment(R.id.action_addUser_to_userList)
                            }
                        }

                    }
                }
            }
        }
    }



    private fun isPhoneValid(phone: String): Boolean {
        return phone.length > 9
    }
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onResume() {
        super.onResume()
        if (user_id=="")
        {
            binding.btnUser.text = "Add User"
        }
        else
        {
            binding.btnUser.text = "Update User"
            Coroutines.main {
                viewModel.getSelectUser(user_id.toInt()).observe(requireActivity(),{
                    if (it == null) {
                        Toast.makeText(requireContext(), "Sorry no date found", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        fileUri = getUri(it.byteArray)
                        binding.name.setText(it.name)
                        binding.email.setText(it.email)
                        binding.phone.setText(it.phone)
                        binding.address.setText(it.address)
                        setImageViewWithByteArray(binding.imageView, it.byteArray!!)
                    }
                })
            }
        }
    }

    private fun getUri(byteArray: ByteArray?): Uri? {
        val buf = byteArray!!
        val s = String(buf, charset("UTF-8"))
        val uri = Uri.parse(s)

        return uri
    }




    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }


    fun setImageViewWithByteArray(view: ImageView, data: ByteArray) {
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
        view.setImageBitmap(bitmap)
    }

}