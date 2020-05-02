package com.panaceasoft.pscity.viewmodel.itemcategory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.panaceasoft.pscity.repository.itemcategory.ItemCategoryRepository;
import com.panaceasoft.pscity.utils.AbsentLiveData;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.common.PSViewModel;
import com.panaceasoft.pscity.viewobject.ItemCategory;
import com.panaceasoft.pscity.viewobject.common.Resource;
import com.panaceasoft.pscity.viewobject.holder.CategoryParameterHolder;
import com.panaceasoft.pscity.viewobject.holder.ItemParameterHolder;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Panacea-Soft on 11/25/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

public class ItemCategoryViewModel extends PSViewModel {


    //region Variables

    private final LiveData<Resource<List<ItemCategory>>> categoryListData;
    private MutableLiveData<ItemCategoryViewModel.TmpDataHolder> categoryListObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageLoadingStateData;
    private MutableLiveData<ItemCategoryViewModel.TmpDataHolder> nextPageLoadingStateObj = new MutableLiveData<>();

    public ItemParameterHolder productParameterHolder = new ItemParameterHolder();

    public CategoryParameterHolder categoryParameterHolder = new CategoryParameterHolder();

    //endregion

    //region Constructors

    @Inject
    ItemCategoryViewModel(ItemCategoryRepository repository) {

        Utils.psLog("ItemCategoryViewModel");

        categoryListData = Transformations.switchMap(categoryListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            Utils.psLog("ItemCategoryViewModel : categories");
            return repository.getAllSearchCityCategory(obj.limit, obj.offset);
        });

        nextPageLoadingStateData = Transformations.switchMap(nextPageLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            Utils.psLog("Category List.");
            return repository.getNextSearchCityCategory(obj.limit, obj.offset);
        });

    }

    //endregion

    public void setCategoryListObj(String limit, String offset) {
        if (!isLoading) {
            TmpDataHolder tmpDataHolder = new TmpDataHolder();
            tmpDataHolder.offset = offset;
            tmpDataHolder.limit = limit;
            categoryListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<ItemCategory>>> getCategoryListData() {
        return categoryListData;
    }

    //Get Latest Category Next Page
    public void setNextPageLoadingStateObj(String limit, String offset) {

        if (!isLoading) {
            TmpDataHolder tmpDataHolder = new TmpDataHolder();
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
        public String cityId = "";
    }
}
