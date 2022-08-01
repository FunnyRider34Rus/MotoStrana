package com.elpablo.motostrana.utils

import android.net.Uri
import android.util.Log
import com.elpablo.motostrana.App
import com.elpablo.motostrana.App.Companion.initUser
import com.elpablo.motostrana.App.Companion.remoteDB
import com.google.firebase.storage.StorageReference

fun saveUserDatatoDB(id: String,
                     phone: String,
                     nickname: String,
                     fullname: String,
                     photoURL: String,
                     status: String) {
    val fullnameList = fullname.split(" ")
    val name = fullnameList[0].replaceFirstChar(kotlin.Char::uppercase)
    val surname = fullnameList[1].replaceFirstChar(kotlin.Char::uppercase)
    val newNickname = nickname.lowercase()
    val newFullname = "$name $surname"

    val dateMap = mutableMapOf<String, Any?>()
    dateMap[CONST.CHILD_ID] = id
    dateMap[CONST.CHILD_PHONE] = phone
    dateMap[CONST.CHILD_NICKNAME] = newNickname
    dateMap[CONST.CHILD_FULLNAME] = newFullname
    dateMap[CONST.CHILD_URLPHOTO] = photoURL
    dateMap[CONST.CHILD_STATUS] = status
    remoteDB.child(CONST.NODE_USERS).child(id)
        .updateChildren(dateMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.w(CONST.TAG, "UserData write to realtimeDataBase is successful")
            } else {
                Log.w(CONST.TAG, "Error write UserData to realtimeDataBase")
            }
        }
    initUser()
}
inline fun putURLToDatabase(url: String, crossinline function: () -> Unit) {
    saveUserDatatoDB(
        App.USER.id,
        App.USER.phone,
        App.USER.nickname,
        App.USER.fullname,
        url,
        App.USER.status
    )
}

inline fun getURLFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener {function(it.toString())}
        .addOnFailureListener { Log.w(CONST.TAG, it.message.toString())}
}

inline fun putImageToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener {function()}
        .addOnFailureListener { Log.w(CONST.TAG, it.message.toString())}
}