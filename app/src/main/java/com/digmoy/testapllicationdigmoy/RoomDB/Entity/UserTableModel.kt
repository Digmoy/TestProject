package com.digmoy.testapllicationdigmoy.RoomDB.Entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "user")
@Parcelize
data class UserTableModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name : String?,
    val email : String?,
    val phone : String?,
    val address : String?,
    val byteArray: ByteArray?) : Parcelable


//    @ColumnInfo(name = "username") var username: String,
//    @ColumnInfo(name = "email") var email: String,
//    @ColumnInfo(name = "phone") var phone: String,
//    @ColumnInfo(name = "address") var address: String,
//    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) var byteArray: ByteArray
