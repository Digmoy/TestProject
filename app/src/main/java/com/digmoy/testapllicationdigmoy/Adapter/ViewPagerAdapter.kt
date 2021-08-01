package com.digmoy.testapllicationdigmoy.Adapter



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.digmoy.testapllicationdigmoy.R


class ViewPagerAdapter(private val context: Context) : PagerAdapter() {

    private var layoutInflater: LayoutInflater? = null
    private val images = arrayOf<Int>(R.drawable.android, R.drawable.kotlin, R.drawable.flutter)

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater!!.inflate(R.layout.custom_layout, null)
        val imageView: ImageView = view.findViewById<View>(R.id.imageView) as ImageView
        imageView.setImageResource(images[position])


        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)

    }


}