package com.example.tokobuku.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class UserRole : Parcelable {
    @Parcelize
    object Admin : UserRole()

    @Parcelize
    object User : UserRole()
}
