package com.panaceasoft.pscity.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.panaceasoft.pscity.viewobject.Item;
import com.panaceasoft.pscity.viewobject.ItemFavourite;

import java.util.List;


@Dao
public interface ItemDao {

    //region Item list

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Item item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Item> itemList);

    @Query("DELETE FROM Item")
    void deleteAll();

    @Query("DELETE FROM Item WHERE id = :id")
    void deleteItemById(String id);

    @Query("DELETE FROM ItemFavourite")
    void deleteAllFavouriteItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavourite(ItemFavourite itemFavourite);

    @Query("SELECT i.* FROM Item i, ItemMap im WHERE i.id = im.itemId AND im.mapKey = :value ORDER BY im.sorting asc")
    LiveData<List<Item>> getItemByKey (String value);

    //item favorite

    @Query("SELECT isFavourited FROM Item WHERE id =:itemId")
    public abstract String selectFavouriteById(String itemId);

    @Query("UPDATE Item SET isFavourited =:is_favourited WHERE id =:itemId")
    public abstract void updateProductForFavById(String itemId,String is_favourited);

    @Query("DELETE FROM ItemFavourite where itemId = :itemId")
    public abstract void deleteFavouriteItemByItemId(String itemId);

    //item detail

    @Query("SELECT * FROM Item WHERE id =:itemId ORDER BY addedDate DESC")
    public abstract LiveData<Item> getItemById(String itemId);

    @Query("SELECT prd.* FROM Item prd, ItemFavourite fp WHERE prd.id = fp.itemId order by fp.sorting ")
    LiveData<List<Item>> getAllFavouriteProducts();

    @Query("SELECT * FROM Item WHERE id =:itemId ORDER BY addedDate DESC")
    LiveData<Item> getProductById(String itemId);

    @Query("SELECT max(sorting) from ItemFavourite")
    int getMaxSortingFavourite();

    @Query("SELECT i.* FROM Item i, ItemMap im WHERE i.id = im.itemId AND im.mapKey = :value ORDER BY im.sorting asc limit:limit")
    LiveData<List<Item>> getItemByKeyByLimit (String value,String limit);

    @Query("SELECT * FROM Item WHERE id = :catId")
    Item getOneItemObject(String catId);

    @Query("SELECT * FROM Item WHERE id = :catId")
    LiveData<Item> getOneItem(String catId);
    //endregion
}
