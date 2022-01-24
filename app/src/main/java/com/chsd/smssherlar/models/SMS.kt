package com.chsd.smssherlar.models

import java.io.Serializable

class SMS : Serializable {
    var name: String? = null
    var sms_content: String? = null
    var like_image: Int = 0
    var delete:Int = 0

    constructor(name: String?, sms_content: String?) {
        this.name = name
        this.sms_content = sms_content

    }

    constructor(name: String?, sms_content: String?, like_image: Int,delete:Int) {
        this.name = name
        this.sms_content = sms_content
        this.like_image = like_image
        this.delete = delete
    }

}
