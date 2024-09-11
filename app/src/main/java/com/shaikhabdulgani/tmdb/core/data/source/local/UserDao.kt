package com.shaikhabdulgani.tmdb.core.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.shaikhabdulgani.tmdb.core.data.source.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM UserEntity WHERE uid=:uid")
    fun getUser(uid: String): UserEntity?

    @Upsert
    fun saveUser(user: UserEntity)

    @Query("DELETE FROM UserEntity")
    fun clear()
}