package com.nhom1.oxygen.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nhom1.oxygen.data.model.location.OLocation
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface SearchedLocationDao {
    @Query("SELECT * FROM searched_location")
    fun getSearchedLocation(): Single<List<OLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addSearchedLocation(location: OLocation): Completable
}