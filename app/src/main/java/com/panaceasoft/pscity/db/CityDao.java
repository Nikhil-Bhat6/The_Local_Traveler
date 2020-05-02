package com.panaceasoft.pscity.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.panaceasoft.pscity.viewobject.City;

import java.util.List;

@Dao
public interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<City> cities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(City city);

    @Query("DELETE FROM City")
    void deleteAll();

    @Query("SELECT * FROM City WHERE id = :id")
    LiveData<City> getCityById(String id);

    @Query("DELETE FROM City WHERE id = :id")
    void deleteCityById(String id);

    @Query("SELECT c.* FROM City c limit 1")
    LiveData<City> getCityInfo();


}
