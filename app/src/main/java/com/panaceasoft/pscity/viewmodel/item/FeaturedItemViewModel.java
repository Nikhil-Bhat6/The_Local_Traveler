package com.panaceasoft.pscity.viewmodel.item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.panaceasoft.pscity.repository.item.ItemRepository;
import com.panaceasoft.pscity.utils.AbsentLiveData;
import com.panaceasoft.pscity.viewmodel.common.PSViewModel;
import com.panaceasoft.pscity.viewobject.Item;
import com.panaceasoft.pscity.viewobject.common.Resource;
import com.panaceasoft.pscity.viewobject.holder.ItemParameterHolder;

import java.util.List;

import javax.inject.Inject;

public class FeaturedItemViewModel extends PSViewModel {

    private final LiveData<Resource<List<Item>>> featuredItemListByKeyData;
    private final MutableLiveData<FeaturedItemViewModel.ItemTmpDataHolder> itemListByKeyObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPagefeaturedItemListByKeyData;
    private final MutableLiveData<FeaturedItemViewModel.ItemTmpDataHolder> nextPageItemListByKeyObj = new MutableLiveData<>();

    public ItemParameterHolder featuredItemParameterHolder = new ItemParameterHolder().getFeaturedItem();

    @Inject
    FeaturedItemViewModel(ItemRepository repository)
    {

        featuredItemListByKeyData = Transformations.switchMap(itemListByKeyObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getItemListByKey(obj.loginUserId, obj.limit, obj.offset, obj.itemParameterHolder);

        });

        nextPagefeaturedItemListByKeyData = Transformations.switchMap(nextPageItemListByKeyObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextPageProductListByKey(obj.itemParameterHolder, obj.loginUserId, obj.limit, obj.offset);

        });
    }

    //region getItemList

    public void setFeaturedItemListByKeyObj(String loginUserId, String limit, String offset, ItemParameterHolder parameterHolder) {

        FeaturedItemViewModel.ItemTmpDataHolder tmpDataHolder = new FeaturedItemViewModel.ItemTmpDataHolder(limit, offset, loginUserId, parameterHolder);

        this.itemListByKeyObj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<List<Item>>> getFeaturedItemListByKeyData() {

        return featuredItemListByKeyData;

    }

    public void setNextPageFeaturedItemListByKeyObj(String limit, String offset, String loginUserId, ItemParameterHolder parameterHolder) {

        FeaturedItemViewModel.ItemTmpDataHolder tmpDataHolder = new FeaturedItemViewModel.ItemTmpDataHolder(limit, offset, loginUserId, parameterHolder);

        this.nextPageItemListByKeyObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getNextPageFeaturedItemListByKeyData() {
        return nextPagefeaturedItemListByKeyData;
    }

    //endregion

    class ItemTmpDataHolder {

        private String limit, offset, loginUserId;
        private ItemParameterHolder itemParameterHolder;

        public ItemTmpDataHolder(String limit, String offset, String loginUserId, ItemParameterHolder itemParameterHolder) {
            this.limit = limit;
            this.offset = offset;
            this.loginUserId = loginUserId;
            this.itemParameterHolder = itemParameterHolder;
        }
    }
}
