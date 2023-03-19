package com.motorshare.motorshare.Model

//model class to store data
class Model {
    var customerAddress: String? = null
    var destinationAddress: String? = null
    var phoneNumber: String? = null
    var profilePic: String? = null
    var userName: String? = null
    var riderName: String? = null
    var rideDate: String? = null
    var rideTime: String? = null
    var ridePrice: String? = null
    var totalPassengers: String? = null
    var userId: String? = null
   // var phoneNumber: String? = null

    constructor() {}
    constructor(
        customerAddress: String?,
        destinationAddress: String?,
        phoneNumber: String?,
        profilePic: String?,
        userName: String?,
        riderName: String?,
        rideDate: String?,
        rideTime: String?,
        ridePrice: String?,
        totalPassengers: String?,
        userId: String?,
       // phoneNumber: String?
    ) {
        this.customerAddress = customerAddress
        this.destinationAddress = destinationAddress
        this.phoneNumber = phoneNumber
        this.profilePic = profilePic
        this.userName = userName
        this.riderName = riderName
        this.rideDate = rideDate
        this.rideTime = rideTime
        this.ridePrice = ridePrice
        this.totalPassengers = totalPassengers
        this.userId = userId
       // this.phoneNumber = phoneNumber
    }
}