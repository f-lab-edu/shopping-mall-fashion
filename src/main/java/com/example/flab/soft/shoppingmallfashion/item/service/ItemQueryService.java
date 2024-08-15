package com.example.flab.soft.shoppingmallfashion.item.service;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemRelation;
import com.example.flab.soft.shoppingmallfashion.item.domain.Sex;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRelationRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemQueryService {
    private final ItemRepository itemRepository;
    private final ItemRelationRepository itemRelationRepository;

    @Transactional(readOnly = true)
    public Page<ItemBriefDto> getItems(Integer minPrice, Integer maxPrice,
                                       Long categoryId, Long storeId, Sex sex, Pageable pageable) {
        return itemRepository.findAllByFilters(minPrice, maxPrice, categoryId, storeId, sex, pageable)
                .map(ItemBriefDto::new);
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getItemDetails(Long itemId) {
        Item item = itemRepository.findItemJoinFetchById(itemId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));
        return ItemDetailsDto.builder().item(item).build();
    }

    @Transactional(readOnly = true)
    public List<ItemBriefDto> getSameStoreItems(Long storeId) {
        return itemRepository.findByStoreId(storeId).stream()
                .sorted(Comparator.comparing((Item item) ->
                        item.getItemStats().getOrderCount()).reversed())
                .limit(20)
                .map(ItemBriefDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ItemBriefDto> getSameCategoryItems(Long categoryId) {
        return itemRepository.findByCategoryId(categoryId).stream()
                .sorted(Comparator.comparing((Item item) ->
                        item.getItemStats().getOrderCount()).reversed())
                .limit(20)
                .map(ItemBriefDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ItemBriefDto> getRelatedItems(Long itemId) {
        return itemRelationRepository.findAllByIdJoinFetch(itemId).stream()
                .sorted(Comparator.comparing(ItemRelation::getWeight).reversed())
                .map(ItemRelation::getRelatedItem)
                .limit(20)
                .map(ItemBriefDto::new)
                .toList();
    }
}
