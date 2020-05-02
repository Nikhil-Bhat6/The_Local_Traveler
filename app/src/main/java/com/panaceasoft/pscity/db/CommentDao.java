package com.panaceasoft.pscity.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.panaceasoft.pscity.viewobject.Comment;

import java.util.List;

@Dao
public abstract class CommentDao {
    //region Discounts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Comment comment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAllCommentList(List<Comment> commentList);

    @Query("DELETE FROM Comment WHERE itemId = :itemId")
    public abstract void deleteAllCommentList(String itemId);

    @Query("SELECT * FROM Comment WHERE itemId = :itemId order by addedDate desc")
    public abstract LiveData<List<Comment>> getAllCommentList(String itemId);

    @Query("DELETE FROM Comment")
    public abstract void deleteAll();

}
