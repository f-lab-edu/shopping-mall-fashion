package com.example.flab.soft.shoppingmallfashion.item.service;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.domain.Sex;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemQueryService {
    private final ItemRepository itemRepository;

    public Page<ItemBriefDto> getItems(Integer minPrice, Integer maxPrice,
                                       Long categoryId, Long storeId, Sex sex, Pageable pageable) {
        return itemRepository.findAllByFilters(minPrice, maxPrice, categoryId, storeId, sex, pageable)
                .map(ItemBriefDto::new);
    }

    public ItemDetailsDto getItemDetails(Long itemId) {
        Item item = itemRepository.findItemJoinFetchById(itemId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));
        return ItemDetailsDto.builder().item(item).build();
    }
}
