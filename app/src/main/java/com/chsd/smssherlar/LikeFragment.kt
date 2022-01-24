package com.chsd.smssherlar

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.chsd.smssherlar.adapters.SMS_Adapter
import com.chsd.smssherlar.models.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.bottom_dialog.view.*
import kotlinx.android.synthetic.main.fragment_like.*
import kotlinx.android.synthetic.main.fragment_like.view.*
import kotlinx.android.synthetic.main.send_sms.*
import java.net.MalformedURLException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LikeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LikeFragment : Fragment(), DD {
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

    lateinit var alert: BottomSheetDialog
    lateinit var viewdialog: View
    lateinit var root: View
    lateinit var list: ArrayList<SMS>
    lateinit var smsAdapter: SMS_Adapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_like, container, false)
        root.back_btnl.setOnClickListener {
            root.findNavController().popBackStack()
        }
        SharedPref.getContext(requireContext())
        val gson = Gson()
        if (SharedPref.sms != null) {
            var type = object : TypeToken<List<SMS>>() {}.type
            list = gson.fromJson<List<SMS>>(SharedPref.sms, type) as ArrayList<SMS>
            smsAdapter = SMS_Adapter(list, object : SMS_Adapter.onClick {
                override fun SMSOnclick(data: SMS) {
                    alert =
                        BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
                    viewdialog =
                        LayoutInflater.from(context).inflate(R.layout.bottom_dialog, null, false)
                    viewdialog.alert_like.setImageResource(R.drawable.like)
                    viewdialog.send_sms.setOnClickListener {
                        SendSMS(data)
                    }
                    viewdialog.like.setOnClickListener {
                        Like(data)
                    }
                    viewdialog.share.setOnClickListener {
                        Share(data)
                    }
                    viewdialog.copy_btn.setOnClickListener {
                        Copy(data)
                    }
                    viewdialog.d_name.text = data.name
                    viewdialog.d_content.text = data.sms_content

                    alert.setContentView(viewdialog)
                    alert.show()
                }

            }, object : SMS_Adapter.Delete {
                override fun Del(position: Int) {
                    list.removeAt(position)
                    SharedPref.sms = Gson().toJson(list)
                    smsAdapter.notifyItemRemoved(position)
                    smsAdapter.notifyItemChanged(position)
                }
            })
            root.rv_like.adapter = smsAdapter
        }
        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LikeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LikeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    lateinit var number: String
    lateinit var text: String
    override fun SendSMS(sms: SMS) {
        viewdialog.send_sms.setOnClickListener {
            alert.dismiss()
        }
        var sms_alert =
            BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
        var smsdialog =
            LayoutInflater.from(context).inflate(R.layout.send_sms, null, false)
        sms_alert.setContentView(smsdialog)
        sms_alert.show()
        sms_alert.send_btn.setOnClickListener {
            number = sms_alert.phone_number.text.toString()
            text = sms.sms_content!!
            if (text.length >= 520) {
                val sendIntent1 = Intent(Intent.ACTION_SEND)
                try {
                    sendIntent1.type = "text/x-vcard"
                    sendIntent1.putExtra("address", number)
                    sendIntent1.putExtra("sms_body", text)
                    sendIntent1.putExtra(
                        Intent.EXTRA_STREAM,
                        Uri.parse(number)
                    )
                } catch (e: MalformedURLException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
                startActivity(sendIntent1)
            } else {
                val smsManager: SmsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(number, null, text, null, null)
                Toast.makeText(requireContext(), "send", Toast.LENGTH_SHORT).show()
            }
            sms_alert.dismiss()
        }
    }

    override fun Like(sms: SMS) {
//        SharedPref.getContext(requireContext())
//        val gson = Gson()
//        var like_list = ArrayList<SMS>()
//        like_list.add(sms)
//        val toJson = gson.toJson(like_list)
//        if (SharedPref.sms != null) {
//            var type = object : TypeToken<List<SMS>>() {}.type
//            val list1 = gson.fromJson<List<SMS>>(SharedPref.sms, type) as ArrayList<SMS>
//            list1.addAll(like_list)
//            SharedPref.sms = gson.toJson(list1)
//        } else {
//            SharedPref.sms = toJson
//        }
    }

    override fun Share(sms: SMS) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, sms.sms_content)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun Copy(sms: SMS) {
        val clipboard: ClipboardManager? =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText("label", sms.sms_content)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Copied", Toast.LENGTH_SHORT).show()
        alert.dismiss()
    }
}