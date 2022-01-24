package com.chsd.smssherlar.models

interface DD {
    fun SendSMS(sms: SMS)

    fun Like(sms: SMS)

    fun Share(sms: SMS)

    fun Copy(sms: SMS)
}