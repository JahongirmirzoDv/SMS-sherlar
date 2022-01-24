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
import com.chsd.smssherlar.models.DD
import com.chsd.smssherlar.models.SMS
import com.chsd.smssherlar.models.SharedPref
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.bottom_dialog.view.*
import kotlinx.android.synthetic.main.fragment_sevgiragment.view.fr_name
import kotlinx.android.synthetic.main.fragment_tabrik.view.*
import kotlinx.android.synthetic.main.send_sms.*
import java.net.MalformedURLException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TabrikFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TabrikFragment : Fragment(), DD {
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_tabrik, container, false)
        if (arguments != null) {
            root.fr_name.text = requireArguments().get("name").toString()
        }
        root.back_btnt.setOnClickListener {
            root.findNavController().popBackStack()
        }
        loadData()
        val smsAdapter = SMS_Adapter(list, object : SMS_Adapter.onClick {
            override fun SMSOnclick(data: SMS) {
                data.delete = R.drawable.delete
                alert =
                    BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
                viewdialog =
                    LayoutInflater.from(context).inflate(R.layout.bottom_dialog, null, false)
                data.like_image = R.drawable.like
                var isHave = false
                if (SharedPref.like != null) {
                    var type = object : TypeToken<List<String>>() {}.type
                    val list1 =
                        Gson().fromJson<List<String>>(SharedPref.like, type) as ArrayList<String>
                    for (i in list1.indices) {
                        if (list1[i] == data.name) {
                            viewdialog.alert_like.setImageResource(R.drawable.like)
                            isHave = true
                        }
                    }
                }
                viewdialog.send_sms.setOnClickListener {
                    SendSMS(data)
                }
                viewdialog.like.setOnClickListener {
                    Like(data)
                    if (!isHave) {
                        addLike(data)
                    }
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

            }

        })
        root.rv_tabrik.adapter = smsAdapter


        return root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TabrikFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TabrikFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun addLike(sms: SMS) {
        viewdialog.alert_like.setImageResource(R.drawable.like)
        var like_list = ArrayList<String>()
        like_list.add(sms.name!!)
        SharedPref.getContext(requireContext())
        SharedPref.like = Gson().toJson(like_list)
    }

    fun loadData() {
        list = ArrayList()
        list.add(
            SMS(
                "Tug'ilgan kunga tabrik ona oyi aya uchun",
                "Doim kulib yuring tabassum bilan.\n" +
                        "Yashang doim baxtga oshyona.\n" +
                        "Mexrdan poyizga to'shayman gilam.\n" +
                        "Tug'ulgan kuningiz bilan ona. "
            )
        )
        list.add(
            SMS(
                "Tug'ilgan kun tabrik ota ada dada uchun",
                "Siz mening savlatim, siz mening faxrim.\n" +
                        "Ishonchli qalqonim, xayotga dalda.\n" +
                        "Xayotda takrorsiz, eng oliy baxtim.\n" +
                        "Tug'ilgan kuningiz bilan dada."
            )
        )
        list.add(
            SMS(
                "Tug'ilgankun tilaklari opa yanga opacha uchun.",
                "Soxtalik ko'paydi, tabiydan bugun.\n" +
                        "Siz bilan butundir gar dunyo birkam!\n" +
                        "Eng sodiq do'stimsiz ukangiz uchun.\n" +
                        "Tug'ilgan kun ingiz bilan opam. "
            )
        )
        list.add(
            SMS(
                "Eng shirin orzularni tilayman sizga",
                "Muborak ayyomning tiniq tongiday,\n" +
                        "Musaffo ranglarga to’lganda borliq.\n" +
                        "Xushbo’y tabiatning yetti rangiday,\n" +
                        "Osmon gardishida qolganda yorliq.\n" +
                        "Eng porloq tuyg’ularni tilayman sizga,\n" +
                        "Eng shirin orzularni tilayman sizga.\n" +
                        "Tug’ilgan kuningiz bo’lsin muborak!"
            )
        )
        list.add(
            SMS(
                "BAXOR CHECHAKLARI",
                "Baxor chechaklari bölsin armug'on \n" +
                        "\n" +
                        "Siz nurli insonsiz baxtli begumon. \n" +
                        "\n" +
                        "Bayramla qutlayman böling wodumon \n" +
                        "\n" +
                        "Har kuniz ötsin bayramday xar on!"
            )
        )
        list.add(
            SMS(
                "SIZNI DEDIM",
                "Sokin tunda bezota qilib, \n" +
                        "Eng so'nggilar safida bo'lib. \n" +
                        "So'zlarimni guldasta bilib, \n" +
                        "Deyman Tug'ilgan kuningiz bilan!"
            )
        )
        list.add(
            SMS(
                "SUYINCHI",
                "Suyunchini kottasini \n" +
                        "Tabriknomani dodasini \n" +
                        "Kutmoqdaman sizlardan \n" +
                        "Do'st-u og'a inidan!"
            )
        )
        list.add(
            SMS(
                "DO'STLIK",
                "Bir sinfda o`qir ekanmiz , \n" +
                        "So`zlarimiz doim samimiy \n" +
                        "Bu dunyoda yashar ekanmiz \n" +
                        "Do`stligimiz bo`lsin abadiy"
            )
        )
        list.add(
            SMS(
                "TAVALLUD MUBORAK",
                "Azizim baxt iqbol doim yor bo'lsin \n" +
                        "Yurgan yo'llariz gullarga to'lsin \n" +
                        "Omadingiz ko'rib ko'zlar quvonsin \n" +
                        "Tug'ulgan kuningiz muborak bo'lsin"
            )
        )
        list.add(
            SMS(
                "ORZUGA TO'LSIN ",
                "Bahor ko`rkamligi, subxidam tongi, \n" +
                        "Beg`ubor qalbingiz hamrohi bo`lsin! \n" +
                        "Quvonchlar tilayman yangidan-yangi, \n" +
                        "Dilingiz shodlikka, orzuga to`lsin!!!"
            )
        )
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
        SharedPref.getContext(requireContext())
        val gson = Gson()
        var like_list = ArrayList<SMS>()
        like_list.add(sms)
        val toJson = gson.toJson(like_list)
        if (SharedPref.sms != null) {
            var type = object : TypeToken<List<SMS>>() {}.type
            val list1 = gson.fromJson<List<SMS>>(SharedPref.sms, type) as ArrayList<SMS>
            list1.addAll(like_list)
            SharedPref.sms = gson.toJson(list1)
        } else {
            SharedPref.sms = toJson
        }
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