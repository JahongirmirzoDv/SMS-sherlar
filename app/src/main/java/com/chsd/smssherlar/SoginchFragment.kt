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
import kotlinx.android.synthetic.main.fragment_soginch.view.*
import kotlinx.android.synthetic.main.send_sms.*
import java.net.MalformedURLException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SoginchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SoginchFragment : Fragment(), DD {
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
        root = inflater.inflate(R.layout.fragment_soginch, container, false)
        if (arguments != null) {
            root.fr_name.text = requireArguments().get("name").toString()
        }
        root.back_btns.setOnClickListener {
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
        root.rv_soginch.adapter = smsAdapter


        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SoginchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SoginchFragment().apply {
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
                "Sog'inch",
                "Parchagina bulut,\n" +
                        "Cheksiz osmon,\n" +
                        "Adir ortidagi yolg'izoyoq yo'l.\n" +
                        "Barcha tashvishlarni unutib, shodon,\n" +
                        "Qaytgim kelayotir qoshingga butkul."
            )
        )
        list.add(
            SMS(
                "Men sizni sogindim, sogindim yomon",
                "Yo'q sizni unitmoq \n" +
                        "Emasdir onson\n" +
                        "Qalbimni  ortaydi siz yoqan chiroq\n" +
                        "Yurakda bir tuygu uradi tozon \n" +
                        "Men sizni sogindim, sogindim yomon"
            )
        )
        list.add(
            SMS(
                "Yoningizga qaytgim keladi",
                "Mendan o'tdi asli xatolik\n" +
                        "Sizga bir so'z aytgim keladi\n" +
                        "Boshim egib uzurim aytib\n" +
                        "Yoningizga qaytgim keladi\n" +
                        "\n" +
                        "Xar kun yig'lab eslayman sizni\n" +
                        "Xar kun ingrab qo'msayman sizni\n" +
                        "Tushlarimdan izlayman sizni\n" +
                        "Yoningizga qaytgim keladi\n" +
                        "\n" +
                        "Biz tufayli ayrodur yo'llar\n" +
                        "Biz tufayli ayrodur dillar\n" +
                        "Yuzlarimdan oqmoqda sellar\n" +
                        "Yoningizga qaytgim keladi\n" +
                        "\n" +
                        "Kechirsangiz kechira qolingda\n" +
                        "Xato bo'lmas aytingchi kimda\n" +
                        "O'jarlik xam evi bilanda\n" +
                        "Yoningizga qaytgim keladi\n" +
                        "\n" +
                        "Xotiralar qoldi bir qisim\n" +
                        "Ox yuragim ox tilim-tilim\n" +
                        "Og'riyapti manabu yerim\n" +
                        "Yoningizga qaytgim keladi"
            )
        )
        list.add(
            SMS(
                "Sog'inib charchadim",
                "\n" +
                        "07:36Sog'inib charchadim\n" +
                        "Sog'inib-sog'inib charchadim oxir,\n" +
                        "Izlay-izlay topmadim baribir.\n" +
                        "Lek dunyoda nimamdir kamdir,\n" +
                        "Sog'inib-sog'inib yashadim axir.\n" +
                        "\n" +
                        "\n" +
                        "Uchgim kelar xar on sen tomon,\n" +
                        "Yo‘llarimni to‘sadi g'ovlar.\n" +
                        "Kimdir xavas, kimdir xasad-la boqar\n" +
                        "Sog'inib-sog'inib yashadim axir.\n" +
                        "\n" +
                        "\n" +
                        "Ko‘zlarimdan tommasa yoshlar,\n" +
                        "Pok sevgiga egilmasa boshlar,\n" +
                        "Muxabbat yurakda g'alayon boshlar,\n" +
                        "Sog'inib-sog'inib yashadim axir.\n" +
                        "\n" +
                        "\n" +
                        "Sog'inch - go‘yo xavo, simiraman yutoqib,\n" +
                        "Xar laxzada seni o‘ylab qo‘yayapman yo‘qotib.\n" +
                        "Magar kelsang balki olarman topib,\n" +
                        "Sog'inib-sog'inib yashadim axir.\n" +
                        "\n" +
                        "\n" +
                        "Xamon o‘sha, mening oppoq orzularim,\n" +
                        "Xamon o‘sha, sening tongdek kulgularing.\n" +
                        "Xamon o‘sha, sening, mening tuyg'ularim,\n" +
                        "Sog'inchli sevgidan ketarsan yonib..."
            )
        )
        list.add(
            SMS(
                "Xayotimga kirib kelganingizda",
                "\n" +
                        "11:25Xayotimga kirib kelganingizda\n" +
                        "Xayotimga kirib kelganingizda -\n" +
                        "qancha voxtga kelganingizni,\n" +
                        "man uchun kim bölib\n" +
                        "qolishingizni bilmagandim....\n" +
                        " \n" +
                        "\n" +
                        "Yaxshi yomon kunlarim ötti siz\n" +
                        "bilan..... Xurmat qildingiz,\n" +
                        "g'amxörli qildingiz, xafa\n" +
                        "böganimda könglimni\n" +
                        " \n" +
                        "\n" +
                        "kötardingiz, xayrli tunlar\n" +
                        "tiladingiz, sevdingiz.... azob\n" +
                        "berdingiz.... Hayotimdan\n" +
                        "shunchaki ketdingiz....\n" +
                        " \n" +
                        "\n" +
                        "Qanchalik dilimni og'ritgan\n" +
                        "bölsangiz xam balkim bir kun\n" +
                        "kelib közlarimni yumgancha\n" +
                        "sizni yurak yuragmdan\n" +
                        " \n" +
                        "\n" +
                        "sog'inarman....Qaytarman\n" +
                        "ammo xozir emas....Bugun\n" +
                        "emas..."
            )
        )
        list.add(
            SMS(
                "SOG‘INDIM",
                "Bahorim ochilmay nahot bo‘lsa kuz,\n" +
                        " Nahot go‘zallikni ko‘rmas endi ko‘z\n" +
                        " Qalbim tub-tubini kuydirar bir so‘z:\n" +
                        " «Sog‘indim»…\n" +
                        "\n" +
                        " Go‘yo quvonchlarga berilgan barham,\n" +
                        " Iforin taratmas endi gullar ham\n" +
                        " Dudoqlar shivirlar, ko‘zlar bo‘lib nam:\n" +
                        " «Sog‘indim»…\n" +
                        "\n" +
                        " Ko‘kdagi oftob ham endi bermas nur,\n" +
                        " Sayrga chorlamas qushlar ham deb yur\n" +
                        " Kuylaydi yaproqlar faqat bir shivir:\n" +
                        " «Sog‘indim»…\n" +
                        "\n" +
                        " Xasta dil yiglaydi, oqshomlar beun,\n" +
                        " Yil bo‘lib cho‘zilgay bedor qaro tun\n" +
                        " Soatlar chiqqillar bir so‘z deb uzun:\n" +
                        " «Sog‘indim»…\n" +
                        "\n" +
                        " Quvonchlar chekindi, baxt ketdi yiroq,\n" +
                        " Diydordan boshqa yo‘q, taskin yaxshiroq\n" +
                        " So‘nmagan umidlar tinmay der biroq:\n" +
                        " «Sog‘indim»…\n" +
                        "\n" +
                        " Dunyoda shodligim, baxtim o‘zing bil,\n" +
                        " Intizor kutmoqda bir oshufta dil,\n" +
                        " Istasang gul bo‘lay, poyingga tez kel\n" +
                        " «Sog‘indim Dilbarim..."
            )
        )
        list.add(
            SMS(
                "Har qadamda sizni o'yladim!",
                "Bilarmisiz sizni kutganim,\n" +
                        "Har soniya yodda tutganim.\n" +
                        "Bir daqiqa yo'q unutganim,\n" +
                        "Har qadamda sizni o'yladim! "
            )
        )
        list.add(
            SMS(
                "Sizni ko'rgim kelar har nedan kechib",
                "Yolg'izlik qiynasa yuragim ezib,\n" +
                        "Sog'inib har damda sizni izlayman.\n" +
                        "Yurakdagi jarohat ketmasa bitib,\n" +
                        "Sizni ko'rgim kelar har nedan kechib! "
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