package com.panaceasoft.pscity.db;

import com.panaceasoft.pscity.viewobject.ItemMap;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ItemMapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ItemMap itemMap);

    @Query("DELETE FROM ItemMap WHERE mapKey = :key")
    void deleteByMapKey(String key);

    @Query("SELECT max(sorting) from ItemMap WHERE mapKey = :value ")
    int getMaxSortingByValue(String value);

    @Query("SELECT * FROM ItemMap")
    List<ItemMap> getAll();

    @Query("DELETE FROM ItemMap")
    void deleteAll();
}
