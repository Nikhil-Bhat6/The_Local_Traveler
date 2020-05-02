package com.panaceasoft.pscity.db;

import com.panaceasoft.pscity.viewobject.CommentDetail;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public abstract class CommentDetailDao {

    //region Comment Detail

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(CommentDetail commentDetail);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(CommentDetail commentDetail);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAllCommentDetailList(List<CommentDetail> commentDetailList);

    @Query("DELETE FROM CommentDetail WHERE headerId = :headerId")
    public abstract void deleteCommentDetailListByHeaderId(String headerId);

    @Query("SELECT * FROM CommentDetail WHERE headerId = :headerId ORDER BY addedDate desc")
    public abstract LiveData<List<CommentDetail>> getAllCommentDetailList(String headerId);

    @Query("DELETE FROM CommentDetail")
    public abstract void deleteAll();

    //endregion
}
