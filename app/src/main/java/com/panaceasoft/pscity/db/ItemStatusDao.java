package com.panaceasoft.pscity.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.panaceasoft.pscity.viewobject.ItemStatus;

import java.util.List;

@Dao
public interface ItemStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ItemStatus itemStatus);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ItemStatus itemStatus);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ItemStatus> cityCategories);

    @Query("DELETE FROM ItemStatus")
    void deleteAllCityCategory();

    @Query("DELETE FROM ItemStatus WHERE id = :id")
    void deleteCityCategoryById(String id);

    @Query("SELECT max(addedDate) from ItemStatus ")
    int getMaxSortingByValue();
//
    @Query("SELECT * FROM ItemStatus  ORDER BY addedDate")
    LiveData<List<ItemStatus>> getAllCityCategoryById();

    @Query("DELETE FROM ItemStatus WHERE id =:id")
    public abstract void deleteItemCategoryById(String id);


}
