package com.example.financialwunderwaffe.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserInfoDao {

    @Insert(entity = UserInfo::class)
    fun insertUserInfo(userInfo: UserInfo)

    @Delete
    fun deleteUserInfo(userInfo: UserInfo)

    @Query("SELECT * FROM user_info")
    fun getInfo(): List<UserInfo>

}