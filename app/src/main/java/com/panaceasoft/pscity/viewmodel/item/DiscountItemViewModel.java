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

public class DiscountItemViewModel extends PSViewModel {

    private final LiveData<Resource<List<Item>>> discountItemListByKeyData;
    private final MutableLiveData<DiscountItemViewModel.ItemTmpDataHolder> itemListByKeyObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageDiscountItemListByKeyData;
    private final MutableLiveData<DiscountItemViewModel.ItemTmpDataHolder> nextPageItemListByKeyObj = new MutableLiveData<>();

    public ItemParameterHolder discountItemParameterHolder = new ItemParameterHolder().getDiscountItem();

    @Inject
    DiscountItemViewModel(ItemRepository repository) {

        discountItemListByKeyData = Transformations.switchMap(itemListByKeyObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getItemListByKey(obj.loginUserId, obj.limit, obj.offset, obj.itemParameterHolder);

        });

        nextPageDiscountItemListByKeyData = Transformations.switchMap(nextPageItemListByKeyObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextPageProductListByKey(obj.itemParameterHolder, obj.loginUserId, obj.limit, obj.offset);

        });
    }

    //region getItemList

    public void setDiscountItemListByKeyObj(String loginUserId, String limit, String offset, ItemParameterHolder parameterHolder) {

        DiscountItemViewModel.ItemTmpDataHolder tmpDataHolder = new DiscountItemViewModel.ItemTmpDataHolder(limit, offset, loginUserId, parameterHolder);

        this.itemListByKeyObj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<List<Item>>> getDiscountItemListByKeyData() {
        return discountItemListByKeyData;
    }

    public void setNextPageDiscountItemListByKeyObj(String limit, String offset, String loginUserId, ItemParameterHolder parameterHolder) {

        DiscountItemViewModel.ItemTmpDataHolder tmpDataHolder = new DiscountItemViewModel.ItemTmpDataHolder(limit, offset, loginUserId, parameterHolder);

        this.nextPageItemListByKeyObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getNextPageDiscountItemListByKeyData() {
        return nextPageDiscountItemListByKeyData;
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

