package com.panaceasoft.pscity.viewmodel.comment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.repository.comment.CommentRepository;
import com.panaceasoft.pscity.utils.AbsentLiveData;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.common.PSViewModel;
import com.panaceasoft.pscity.viewobject.Comment;
import com.panaceasoft.pscity.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

public class CommentListViewModel extends PSViewModel {

    //for recent comment list

    public final String PRODUCT_ID_KEY = "itemId";
    public String itemId = "";

    private final LiveData<Resource<List<Comment>>> commentListData;
    private MutableLiveData<CommentListViewModel.TmpDataHolder> commentListObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageCommentLoadingData;
    private MutableLiveData<CommentListViewModel.TmpDataHolder> nextPageLoadingStateObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> sendCommentHeaderPostData;
    private MutableLiveData<com.panaceasoft.pscity.viewmodel.comment.CommentListViewModel.TmpDataHolder> sendCommentHeaderPostDataObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> commentCountLoadingData;
    private MutableLiveData<CommentListViewModel.TmpDataHolder> commentCountLoadingStateObj = new MutableLiveData<>();
    //region Constructor

    @Inject
    CommentListViewModel(CommentRepository commentRepository) {
        // Latest comment List
        commentListData = Transformations.switchMap(commentListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Comment List.");
            return commentRepository.getCommentList(Config.API_KEY, obj.itemId, obj.limit, obj.offset);
        });

        nextPageCommentLoadingData = Transformations.switchMap(nextPageLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Comment List.");
            return commentRepository.getNextPageCommentList(obj.itemId, obj.limit, obj.offset);
        });

        sendCommentHeaderPostData = Transformations.switchMap(sendCommentHeaderPostDataObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }
            return commentRepository.uploadCommentHeaderToServer(
                    obj.itemId,
                    obj.userId,
                    obj.headerComment);
        });

        commentCountLoadingData = Transformations.switchMap(commentCountLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Comment List.");
            return commentRepository.getCommentDetailReplyCount(obj.comment_id);
        });
    }

    //endregion
    public void setSendCommentHeaderPostDataObj(String itemId,
                                                String userId,
                                                String headerComment
    ) {
        if (!isLoading) {
            com.panaceasoft.pscity.viewmodel.comment.CommentListViewModel.TmpDataHolder tmpDataHolder = new com.panaceasoft.pscity.viewmodel.comment.CommentListViewModel.TmpDataHolder();
            tmpDataHolder.itemId = itemId;
            tmpDataHolder.userId = userId;
            tmpDataHolder.headerComment = headerComment;
            sendCommentHeaderPostDataObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Boolean>> getsendCommentHeaderPostData() {
        return sendCommentHeaderPostData;
    }


    //region Getter And Setter for Comment List

    public void setCommentListObj(String limit, String offset, String itemId) {
        if (!isLoading) {
            CommentListViewModel.TmpDataHolder tmpDataHolder = new CommentListViewModel.TmpDataHolder();
            tmpDataHolder.limit = limit;
            tmpDataHolder.offset = offset;
            tmpDataHolder.itemId = itemId;
            commentListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<Comment>>> getCommentListData() {
        return commentListData;
    }

    //Get Comment Next Page
    public void setNextPageCommentLoadingObj(String itemId, String limit, String offset) {

        if (!isLoading) {
            CommentListViewModel.TmpDataHolder tmpDataHolder = new CommentListViewModel.TmpDataHolder();
            tmpDataHolder.limit = limit;
            tmpDataHolder.itemId = itemId;
            tmpDataHolder.offset = offset;
            nextPageLoadingStateObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public void setCommentCountLoadingObj(String comment_id) {

        if (!isLoading) {
            CommentListViewModel.TmpDataHolder tmpDataHolder = new CommentListViewModel.TmpDataHolder();
            tmpDataHolder.comment_id = comment_id;
            commentCountLoadingStateObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Boolean>> getCommentCountLoadingStateData() {
        return commentCountLoadingData;
    }


    public LiveData<Resource<Boolean>> getNextPageLoadingStateData() {
        return nextPageCommentLoadingData;
    }

    //endregion

    //region Holder
    class TmpDataHolder {
        public String limit = "";
        public String offset = "";
        public String itemId = "";
        public String userId = "";
        public String headerComment = "";
        public String comment_id = "";
        public Boolean isConnected = false;
        public String cityId;
    }
    //endregion
}
