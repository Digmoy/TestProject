package com.digmoy.testapllicationdigmoy.Utils

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.digmoy.testapllicationdigmoy.R

fun Activity.nextFragment(id: Int, bundle: Bundle? = null) {
    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(id, bundle)
}

fun Activity.popFragment() {
    Navigation.findNavController(this, R.id.nav_host_fragment).popBackStack()
}

fun Fragment.showToast(message: String?) {
    if (message != null) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}
