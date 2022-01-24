package com.chsd.smssherlar.models

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class SendSMSdata(
    var number: String,
    var text: String,
    var context: Context,
    var activity: Activity
) {
    fun send() {
//        var send = "SMS_SENT"
//        var DELIvered = "SMS_DELIVERED"
//        val sentPI = PendingIntent.getBroadcast(
//            context, 0,
//            Intent(send), 0
//        )
//        val deliveredPI = PendingIntent.getBroadcast(
//            context, 0,
//            Intent(DELIvered), 0
//        )
//        context.registerReceiver(object : BroadcastReceiver() {
//            override fun onReceive(arg0: Context?, arg1: Intent?) {
//                when (resultCode) {
//                    Activity.RESULT_OK -> Toast.makeText(
//                        context, "SMS sent",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> Toast.makeText(
//                        activity.baseContext, "Generic failure $resultCode",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    SmsManager.RESULT_ERROR_NO_SERVICE -> Toast.makeText(
//                        activity.baseContext, "No service",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    SmsManager.RESULT_ERROR_NULL_PDU -> Toast.makeText(
//                        activity.baseContext, "Null PDU",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    SmsManager.RESULT_ERROR_RADIO_OFF -> Toast.makeText(
//                        activity.baseContext, "Radio off",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }, IntentFilter(send))
//
//        val sms = SmsManager.getDefault()
//        sms.sendTextMessage(number, null, text, sentPI, deliveredPI)
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.SEND_SMS
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.SEND_SMS), 0
                );
            }
        }

    }
}