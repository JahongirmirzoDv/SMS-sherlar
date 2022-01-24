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
import kotlinx.android.synthetic.main.fragment_parents.view.*
import kotlinx.android.synthetic.main.fragment_sevgiragment.view.fr_name
import kotlinx.android.synthetic.main.send_sms.*
import java.net.MalformedURLException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ParentsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ParentsFragment : Fragment(), DD {
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
        root = inflater.inflate(R.layout.fragment_parents, container, false)
        if (arguments != null) {
            root.fr_name.text = requireArguments().get("name").toString()
        }
        root.back_btnp.setOnClickListener {
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
        }, object : SMS_Adapter.Delete {
            override fun Del(position: Int) {

            }

        })
        root.rv_parents.adapter = smsAdapter

        return root
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
                "Onamning Umrini qilgin ziyoda",
                "Dunyoda neki g'am boridan totgan.\n" +
                        "Tashvishlar zarbidan yelkasi qotgan.\n" +
                        "Necha bir tonglari uyqusiz otgan.\n" +
                        "Ey Tangrim besh kunlik yorug' dunyoda.\n" +
                        "Onamning umrini qilgin Ziyoda.\n" +
                        "\n" +
                        "Anqiydi onamdan mehrning isi.\n" +
                        "Zarra ham topilmas qahrning izi.\n" +
                        "Ro'yobga chiqsinda onam orzusi.\n" +
                        "Onamning orzusin yo'yib ro'yoga.\n" +
                        "Onamning umrini qilgin Ziyoda.\n" +
                        "\n" +
                        "Chehrasi xush onam ko'ngli yarimta.\n" +
                        "Onamning yuragi bo'lingan nimta.\n" +
                        "Farzandlar rohatin ko'rsin albatta.\n" +
                        "Rohatda yursinda mayli piyoda.\n" +
                        "Onamning umrini qilgin ziyoda.\n" +
                        "\n" +
                        "Volidam oldida qarzlarim katta.\n" +
                        "Bu qarzni uzib bo'lmas albatta.\n" +
                        "Rizolar qilolsam borki fursatda.\n" +
                        "Onamning boridan boshim samoda.\n" +
                        "Onamning umrini qilgin ziyoda.\n" +
                        "\n" +
                        "Oq suti oldida neki xizmat kam.\n" +
                        "Boshimga ko'tarib hajga borsam ham.\n" +
                        "Ulug' Payg'ambarlar siylagan odam.\n" +
                        "Istagim bu azim ko'hna dunyoda.\n" +
                        "Onamning umrini qilgin Ziyoda."
            )
        )
        sms_list.add(
            SMS(
                "ONAM",
                "Tunni ulab tonglarga\n" +
                        "Mexir berib onglarga\n" +
                        "Yetkazib yoronlarga\n" +
                        "Ona sizni yaxshi koraman\n" +
                        "\n" +
                        "Kozlarizda mexir tolangiz\n" +
                        "XUDOYIMGA yetib nolangiz\n" +
                        "Kuyov boldi bugun bolangiz\n" +
                        "ona sizni yaxshi koraman"
            )
        )
        sms_list.add(
            SMS(
                "Otamga Mashina Mindiray",
                "\n" +
                        "07:18Otamga Mashina Mindiray (Qo'shiq Matni) / Xudoyberdi To'ychiyev\n" +
                        "Baxtni ham boqmoqqa ko'ndiray\n" +
                        "Kamlarin barchasin to'ldiray\n" +
                        "Ollohim menga ham boylik ber\n" +
                        "Otamga mashina mindiray\n" +
                        "\n" +
                        "Yo'llarda oyog'i tolmasin\n" +
                        "Yuklari yerlarda qolmasin\n" +
                        "Mehmonga piyoda bormasin\n" +
                        "Otamga mashina mindiray\n" +
                        "\n" +
                        "Yo'qdan ham amallab yo'ndiray\n" +
                        "Ko'zlarin quvonchdan kuldiray\n" +
                        "Farzandlik burchimni bildiray\n" +
                        "Otamga mashina mindiray\n" +
                        "\n" +
                        "Qarzimni qaytaray ertaroq\n" +
                        "Yoshlanmay turibon ko'z qaroq\n" +
                        "Yaqinga aylansin deb yiroq\n" +
                        "Otamga mashina mindiray\n" +
                        "\n" +
                        "Kunimga yaraydi shu inson\n" +
                        "Qarzlarim ko'p hali bir jahon\n" +
                        "Qaydandir axtarib bir imkon\n" +
                        "Otamga mashina mindiray\n" +
                        "\n" +
                        "Shaytonga dilimni yor qilmay\n" +
                        "Nomardga qadrimni xor qilmay\n" +
                        "O'tayin mashina men minmay\n" +
                        "Otamga mashina mindiray\n"
            )
        )
        sms_list.add(
            SMS(
                "Ota-onam g'animat",
                "Синдирдим уларни дил нихолини\n" +
                        "Кузини ёшини окиздим факат\n" +
                        "Сурамай куйибман хатто холини\n" +
                        "Отам ганиматдир, онам ганимат\n" +
                        "\n" +
                        "Дуосин олишни уйламапман хеч\n" +
                        "Оксик кунгилларин овламапман хеч\n" +
                        "Сал колса колардим барчасига кеч\n" +
                        "Отам ганиматдир, онам ганимат\n" +
                        "\n" +
                        "Бойлик деб, пул учун югирибман-а\n" +
                        "Дузахга йул учун югирибман-а\n" +
                        "Вактимни кизгониб килибман таьна\n" +
                        "Отам ганиматдир, онам ганимат."
            )
        )
        sms_list.add(
            SMS(
                "Otalarni ardoqlang",
                "Jannat ona oyog'i \n" +
                        "Ostidaku deyishar.\n" +
                        "Jannatni istaganlar\n" +
                        "Onasin rozi qilar.\n" +
                        "\n" +
                        "Ona buyuk,muqaddas\n" +
                        "Shubxa yo'q bu fikrga.\n" +
                        "Biroq otamiz ham bor\n" +
                        "Ular ham jannat bizga.\n" +
                        "\n" +
                        "Ona ona deymizku\n" +
                        "Otamiz qolar yolg'iz.\n" +
                        "Ular bo'lmaganida\n" +
                        "Bo'lmasdiku o'g'il qiz.\n" +
                        "\n" +
                        "Bu ko'rkam zamonlarda\n" +
                        "Ayol aziz mukarram.\n" +
                        "Erkaklarga so'z qolmas\n" +
                        "Go'yo emasku odam.\n" +
                        "\n" +
                        "Otalarning hurmatin\n" +
                        "O'z joyiga qo'ymaymiz.\n" +
                        "Lekin onamiz ko'nglin\n" +
                        "Har soniyada o'ylaymiz.\n" +
                        "\n" +
                        "Telefonga qaraylik\n" +
                        "Qachon qildik qo'ng'iroq.\n" +
                        "Onajonim dedigu\n" +
                        "Otamiz chetda biroq."
            )
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ParentsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ParentsFragment().apply {
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