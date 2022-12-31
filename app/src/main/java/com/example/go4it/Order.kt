package com.example.go4it

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order (
    var address1: String ?=null,
    var address2: String ?=null,
    var city: String ?=null,
    var state: String ?=null,
    var zipcode: String ?=null,
    var userpreference: String ?=null,
    var userid: String ?=null,
    var orderid: String ?=null,
    var otherinstructions: String ?=null,
    var mealsperweek: String ?=null,
    var deliveryinstruction:String ?=null,
    var amount: String ?=null

): Parcelable