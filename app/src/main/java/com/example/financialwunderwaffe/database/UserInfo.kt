package com.example.financialwunderwaffe.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "user_info")
data class UserInfo(
    @PrimaryKey
    val id: Long,

    @ColumnInfo
    val uid: UUID,

    @ColumnInfo
    val loginAndPassword: String,

    @ColumnInfo
    val code: Long

)