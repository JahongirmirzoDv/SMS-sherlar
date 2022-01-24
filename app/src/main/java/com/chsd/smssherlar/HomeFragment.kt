package com.chsd.smssherlar

import android.os.Bundle
import android.service.media.MediaBrowserService
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.chsd.smssherlar.adapters.RvListAdapter
import com.chsd.smssherlar.models.Data
import com.chsd.smssherlar.models.SMS
import com.chsd.smssherlar.models.SharedPref
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
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

    lateinit var root: View
    lateinit var list: ArrayList<Data>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home, container, false)
        list = ArrayList()

        list.add(Data(R.drawable.img2, "Sevgi sherlari"))
        list.add(Data(R.drawable.img3, "Sog'inch armon"))
        list.add(Data(R.drawable.img4, "Tabrik sherlari"))
        list.add(Data(R.drawable.img5, "Ota-ona xaqida"))
        list.add(Data(R.drawable.img6, "Do'stlik sherlari"))
        list.add(Data(R.drawable.img7, "Hazil sherlar"))
        val builder = NavOptions.Builder()
        builder.setEnterAnim(R.anim.enter_anim)
        builder.setExitAnim(R.anim.exit_anim)
        builder.setPopEnterAnim(R.anim.enter2)
        builder.setPopExitAnim(R.anim.exit2)
        builder.build()
        val bundle = Bundle()
        SharedPref.getContext(requireContext())
        if (SharedPref.sms != null) {
            var type = object : TypeToken<List<SMS>>() {}.type
            val list = Gson().fromJson<List<SMS>>(SharedPref.sms, type)
            root.raqam.text = list.size.toString()
        }else{
            root.raqam.text = "0"
        }


        root.shadowView.setOnClickListener {
            bundle.putString("name", "Yangi \nshe'rlar")
            root.findNavController().navigate(R.id.newFragment, bundle, builder.build())
        }
        root.shadowView2.setOnClickListener {
            bundle.putString("name", "Saqlangan \nshe'rlar")
            root.findNavController().navigate(R.id.likeFragment, bundle, builder.build())
        }

        val rvListAdapter = RvListAdapter(list, object : RvListAdapter.onClick {
            override fun Onclick(data: Data) {
                when (data.name) {
                    "Sevgi sherlari" -> {
                        bundle.putString("name", "Sevgi \nshe'rlari")
                        root.findNavController()
                            .navigate(R.id.sevgirfagment, bundle, builder.build())
                    }
                    "Sog'inch armon" -> {
                        bundle.putString("name", "Sog'inch \narmon")
                        root.findNavController()
                            .navigate(R.id.soginchFragment, bundle, builder.build())
                    }
                    "Tabrik sherlari" -> {
                        bundle.putString("name", "Tabrik \nshe'rlari")
                        root.findNavController()
                            .navigate(R.id.tabrikFragment, bundle, builder.build())
                    }
                    "Ota-ona xaqida" -> {
                        bundle.putString("name", "Ota-ona \nxaqida")
                        root.findNavController()
                            .navigate(R.id.parentsFragment, bundle, builder.build())
                    }
                    "Do'stlik sherlari" -> {
                        bundle.putString("name", "Do'stlik \nshe'rlari")
                        root.findNavController()
                            .navigate(R.id.friendsFragment, bundle, builder.build())
                    }
                    "Hazil sherlar" -> {
                        bundle.putString("name", "Hazil \nshe'rlar")
                        root.findNavController()
                            .navigate(R.id.funnyFragment, bundle, builder.build())
                    }
                }
            }

        })
        root.rv_list.adapter = rvListAdapter

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}