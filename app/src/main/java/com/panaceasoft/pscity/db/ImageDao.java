package com.panaceasoft.pscity.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.panaceasoft.pscity.viewobject.Image;

import java.util.List;

/**
 * Created by Panacea-Soft on 12/8/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Dao
public interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Image image);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Image> imageList);

    @Query("SELECT * FROM Image")
    LiveData<List<Image>> getAll();

    @Query("SELECT * FROM Image WHERE imgParentId = :imgParentId AND imgType= :imagetype order by imgId")
    LiveData<List<Image>> getByImageIdAndType(String imgParentId,String imagetype);

    @Query("DELETE FROM Image WHERE imgParentId = :imgParentId AND imgType= :imagetype")
    void deleteByImageIdAndType(String imgParentId,String imagetype);

    @Query("DELETE FROM Image")
    void deleteTable();

    @Query("SELECT * FROM Image WHERE imgParentId = :item_id")
    LiveData<List<Image>> getImageByItemId(String item_id);

    @Query("SELECT * FROM Image WHERE imgParentId = :item_id LIMIT :limit")
    LiveData<List<Image>> getImageByItemIdLimit(String item_id,String limit);

    @Query("DELETE FROM Image WHERE imgId = :img_id")
    void deleteById(String img_id);
}
