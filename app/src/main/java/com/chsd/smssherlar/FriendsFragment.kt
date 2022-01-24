package com.chsd.smssherlar

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.chsd.smssherlar.adapters.SMS_Adapter
import com.chsd.smssherlar.models.DD
import com.chsd.smssherlar.models.SMS
import com.chsd.smssherlar.models.SharedPref
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.bottom_dialog.view.*
import kotlinx.android.synthetic.main.fragment_friends.view.*
import kotlinx.android.synthetic.main.fragment_sevgiragment.view.fr_name
import kotlinx.android.synthetic.main.send_sms.*
import java.net.MalformedURLException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FriendsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FriendsFragment : Fragment(), DD {
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
        root = inflater.inflate(R.layout.fragment_friends, container, false)
        if (arguments != null) {
            root.fr_name.text = requireArguments().get("name").toString()
        }
        root.back_btnf.setOnClickListener {
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
        root.rv_friends.adapter = smsAdapter

        return root
    }

    private fun addLike(sms: SMS) {
        viewdialog.alert_like.setImageResource(R.drawable.like)
        var like_list = ArrayList<String>()
        like_list.add(sms.name!!)
        SharedPref.getContext(requireContext())
        SharedPref.like = Gson().toJson(like_list)
    }

    private fun loadData() {
        list = ArrayList()
        list.add(
            SMS(
                "Chin do'st",
                "Og'ir kunda tirgak do'st,\n" +
                        "Havotirda sergak do'st,\n" +
                        "100 ta dushman oldida,\n" +
                        "Qochmas aslo erkak do'st!\n" +
                        "\n" +
                        "Dushmanlari ortganda,\n" +
                        "Sohta do'stlar qochganda,\n" +
                        "Barcha qo'lin tortganda,\n" +
                        "Qo'lin cho'zar Chindan do'st!"
            )
        )
        list.add(
            SMS(
                "DO‘STGA DO‘ST BO‘L",
                "Сохталикни йиғиштир дўстим,\n" +
                        "Нафс қулига айланиб қолма.\n" +
                        "Омад келиб кўпайса пулинг,\n" +
                        "Дўстга дўст бўл ўзгариб қолма.\n" +
                        "\n" +
                        "Молу дунё ўткинчи билгин,\n" +
                        "Сўзларимга қовоғинг солма.\n" +
                        "Сенга берса худо уч тўрт сўм,\n" +
                        "Дўстга дўст бўл ўзгариб қолма.\n" +
                        "\n" +
                        "Ташвиш бўлса бирга енгамиз,\n" +
                        "Дўстим қўрқма ҳавотир олма.\n" +
                        "Яхшиллигим унутмасанг бас,\n" +
                        "Дўстга дўст бўл ўзгариб қолма.\n" +
                        "\n" +
                        "Уйда жуфту ҳалолинг туриб,\n" +
                        "Зинокорга бойланиб қолма.\n" +
                        "Гап гапирса масқара қилиб,\n" +
                        "Дўстга дўст бўл ўзгариб қолма.\n" +
                        "\n" +
                        "Худо суйган бандаси бўлгин,\n" +
                        "Номоз ўқиб ҳеч қачон толма.\n" +
                        "Худо берса сенга мол дунё,\n" +
                        "Дўстга дўст бўл ўзгариб қолма.\n" +
                        "\n" +
                        "Жоним ачиб гапирдим сенга,\n" +
                        "Хафа бўлиб кўнглингга олма.\n" +
                        "Дўстлигимиз  абадий бўлсин,\n" +
                        "Дўстга дўст бўл ўзгариб қолма."
            )
        )
        list.add(
            SMS(
                "Unutma do'st",
                "Ey, do‘st, unut g‘amingni,\n" +
                        "In’om bil har damingni.\n" +
                        "O‘lchab bos qadamingni,\n" +
                        "Guvohingni unutma!\n" +
                        "Ey, do‘st, qo‘rqma yomondan,\n" +
                        "Yo hiylagar shaytondan,\n" +
                        "Shart shuki, har zamonda\n" +
                        "Panohingni unutma!\n" +
                        "Ey, do‘st, unday ko‘kdan tush,\n" +
                        "Kelmagay Tangrimga xush.\n" +
                        "Savobingni qilmay pesh\n" +
                        "Gunohingni unutma!\n" +
                        "Ey, do‘st ber bir qultum may,\n" +
                        "Ko‘ngling yig‘laydi tinmay.\n" +
                        "Kimsan yo‘q deb xo‘rsinmay\n" +
                        "Hamrohingni unutma!\n" +
                        "Ey, do‘st, xushlaring qayda?\n" +
                        "Izg‘ivermay har joyda,\n" +
                        "Otangdan qolgan uyda\n" +
                        "Chirog‘ingni unutma!...\n" +
                        "Ey, do‘st, sezmaysan, esiz!\n" +
                        "Ortingda yo‘q bitta iz...\n" +
                        "Kim bo‘pti ..............iz,\n" +
                        "Ollohingni unutma!..."
            )
        )
        list.add(
            SMS(
                "Dóst",
                "Dunyoda ne go'zal surar\n" +
                        "bo'lsangiz,deymanki do'stimni\n" +
                        "bir tabasumi .Dunyoda nedur\n" +
                        "baxt surar bulsangiz ,baxt\n" +
                        "do'stlar safda ko'rsam o'zimni"
            )
        )
        list.add(
            SMS(
                "Do'stimga",
                "Armonli dunyoda xazil qilaman! \n" +
                        "Netay iloji yo’q sabr qilaman. \n" +
                        "Siz menga imkoniyat bersangiz agar \n" +
                        "Sizga do’st bo’lishga vada beraman!"
            )
        )
        list.add(
            SMS(
                "Do'stimga",
                "Do'stlar yaxshilarni avaylab asrang. \n" +
                        "Salom degan soz'ni salmog'in oqlang. \n" +
                        "O'lganda yuz soat yig'lab turguncha, \n" +
                        "Uni tirigida bir soat yo'qlang."
            )
        )
        list.add(
            SMS(
                "Do'stimga",
                "Bir tomchi suvdan favvora bo'lmas, \n" +
                        "Do'sti bor inson hech qachon bechora bo'lmas. \n" +
                        "Ishongin so'zlarim, rostdir hammasi, \n" +
                        "Dunyoda hech bir do'st sendek bo'lolmas."
            )
        )
        list.add(
            SMS(
                "Do'stimga",
                "D-östim senga eslash uchun deb \n" +
                        "Ö-lmas dilga yozdim sahifa \n" +
                        "S-evgi qalbga tushar yil sayin \n" +
                        "T-ayyorladim arzimas sovg'a \n" +
                        "I-shingizda rivoj tilayman \n" +
                        "M-uhabbatda baxtiyor böling \n" +
                        "G-archi qarib 100 kirganda \n" +
                        "A-srang buni avaylab qöying!"
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
         * @return A new instance of fragment FriendsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FriendsFragment().apply {
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
           if (text.length >= 520){
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
           }else{
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