package com.panaceasoft.pscity.viewmodel.itemstatus;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.panaceasoft.pscity.repository.itemstatus.ItemStatusRepository;
import com.panaceasoft.pscity.utils.AbsentLiveData;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.common.PSViewModel;
import com.panaceasoft.pscity.viewobject.ItemStatus;
import com.panaceasoft.pscity.viewobject.common.Resource;
import com.panaceasoft.pscity.viewobject.holder.CategoryParameterHolder;
import com.panaceasoft.pscity.viewobject.holder.ItemParameterHolder;

import java.util.List;

import javax.inject.Inject;

public class ItemStatusViewModel extends PSViewModel {


    //region Variables

    private final LiveData<Resource<List<ItemStatus>>> itemStatusListData;
    private MutableLiveData<ItemStatusViewModel.TmpDataHolder> itemStatusListObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageLoadingStateData;
    private MutableLiveData<ItemStatusViewModel.TmpDataHolder> nextPageLoadingStateObj = new MutableLiveData<>();

    public ItemParameterHolder productParameterHolder = new ItemParameterHolder();

    public CategoryParameterHolder categoryParameterHolder = new CategoryParameterHolder();

    //endregion

    //region Constructors

    @Inject
    ItemStatusViewModel(ItemStatusRepository repository) {

        Utils.psLog("ItemStatusViewModel");

        itemStatusListData = Transformations.switchMap(itemStatusListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            Utils.psLog("ItemStatusViewModel : categories");
            return repository.getAllItemStatus(obj.limit, obj.offset);
        });

        nextPageLoadingStateData = Transformations.switchMap(nextPageLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            Utils.psLog("Category List.");
            return repository.getNextPageItemStatus(obj.limit, obj.offset);
        });

    }

    //endregion

    public void setItemStatusListObj(String limit, String offset) {
        if (!isLoading) {
            ItemStatusViewModel.TmpDataHolder tmpDataHolder = new ItemStatusViewModel.TmpDataHolder();
            tmpDataHolder.offset = offset;
            tmpDataHolder.limit = limit;
            itemStatusListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<ItemStatus>>> getItemStatusListData() {
        return itemStatusListData;
    }

    //Get Latest Category Next Page
    public void setNextPageLoadingStateObj(String limit, String offset) {

        if (!isLoading) {
            ItemStatusViewModel.TmpDataHolder tmpDataHolder = new ItemStatusViewModel.TmpDataHolder();
            tmpDataHolder.offset = offset;
            tmpDataHolder.limit = limit;
            nextPageLoadingStateObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Boolean>> getNextPageLoadingStateData() {
        return nextPageLoadingStateData;
    }


    class TmpDataHolder {
        public String limit = "";
        public String offset = "";
    }
}
