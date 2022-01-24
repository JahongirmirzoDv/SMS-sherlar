package com.chsd.smssherlar

import android.content.Intent
import android.content.pm.PackageManager
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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.bottom_dialog.view.*
import kotlinx.android.synthetic.main.bottom_dialog.view.send_sms
import kotlinx.android.synthetic.main.fragment_sevgiragment.view.*
import kotlinx.android.synthetic.main.send_sms.*

import com.chsd.smssherlar.models.*
import kotlinx.android.synthetic.main.bottom_dialog.*
import kotlinx.android.synthetic.main.sms_item.*

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri

import kotlinx.android.synthetic.main.fragment_sevgiragment.*
import java.net.MalformedURLException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Sevgiragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Sevgiragment : Fragment(), DD {
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
    lateinit var sms_list: ArrayList<SMS>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_sevgiragment, container, false)
        if (arguments != null) {
            root.fr_name.text = requireArguments().get("name").toString()
        }
        root.back_btn.setOnClickListener {
            root.findNavController().popBackStack()
        }
        loadData()
        val smsAdapter = SMS_Adapter(sms_list, object : SMS_Adapter.onClick {
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
        },object : SMS_Adapter.Delete {
            override fun Del(position: Int) {

            }

        })
        root.rv_sms.adapter = smsAdapter
        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Sevgiragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Sevgiragment().apply {
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
        sms_list = ArrayList()
        sms_list.add(
            SMS(
                "Bo'ldi endi sendan kechaman", "Shuncha azoblading qalbimni mana\n" +
                        "O'zing ayt nima kerakdir yana\n" +
                        "Yurak tugunlarin bugun yechaman\n" +
                        "Bo'ldi endi sendan kechaman\n" +
                        "\n" +
                        "Yurardim o'zimcha hayollar surib\n" +
                        "Ishqingda o'zimni har yoqqa urib\n" +
                        "Samolarga endi yolg'iz uchaman\n" +
                        "Bo'ldi endi sendan kecham"
            )
        )
        sms_list.add(
            SMS(
                "Kelin libosida bir qiz yig'laydi",
                "Kelin libosida bir qiz yigʻlaydi\n" +
                        "\n" +
                        "Orzulari armonli boʻlib\n" +
                        "Oydek yuzi gul kabi soʻlib\n" +
                        "Muhabbatdan koʻngliham qolib\n" +
                        "Kelin libosida bir qiz yigʻlaydi\n" +
                        "\n" +
                        "Kechalar bedor oromi yoʻqdur\n" +
                        "Kulsaham ichida dartlari koʻpdur\n" +
                        "Yanidagi yigitda koʻngliham yuq "
            )
        )
        sms_list.add(
            SMS(
                "Singlimning dugonasi",
                "Singlimning dugonasi”\n" +
                        "\n" +
                        "Tugamas ekan qizni.\n" +
                        "Noziyu baxonasi.\n" +
                        "Shaydo aylagan bizni.\n" +
                        "Singlimning dugonasi.\n" +
                        "\n" +
                        "Kiyinishi xush bichim.\n" +
                        "Yonar shunga bir ichim.\n" +
                        "Bilar meni sevishim.\n" +
                        "Singlimning dugonasi.\n" +
                        "\n" +
                        "Nozingni aylagin bas."
            )
        )
        sms_list.add(
            SMS(
                "Yiglama",
                "Òtgan ishga salovat\n" +
                        "Qilma afsus sen sira\n" +
                        "Qaytib kelmas onlarni\n" +
                        "Bòldi boshka oylanma\n" +
                        "\n" +
                        "Tohtat kòzing yoshini\n" +
                        "Kòp òzingni qiynama\n" +
                        "Eslamaydi u seni\n" +
                        "Endi boshqa yiģlama"
            )
        )
        sms_list.add(
            SMS(
                "Unutamiz.",
                "\n" +
                        "Ozor berib ko'p og'ritdik ko’nglimizni,\n" +
                        "Vafo nima bilmay jafolar yutamiz.\n" +
                        "Bir kun ishq uvoli urar ikkimizni,\n" +
                        "Kel yaxshisi bir-birimiz unutamiz.\n" +
                        "\n" +
                        "Ahir mensiz yashay olasan bu ayon,\n" +
                        "Men ham sensiz yosholaman etmay bayon.\n" +
                        "Bizlar birga bo'la olmaymiz"
            )
        )
        sms_list.add(
            SMS(
                "Sizni òylamasam.",
                "O'ksik dilginamni soģinch qiynaydi \n" +
                        "Yuragim siqilar havo yetmaydi. \n" +
                        "Soģinib dunyodan o'tarman ammo, \n" +
                        "Sizni o'ylamasam kunim òtmaydi \n" +
                        "Yasharsiz borsizku axir qaydadir \n" +
                        "Diydoriz ko'zim oldidan ketmaydi.\n" +
                        " Shirin kulgula"
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