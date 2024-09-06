package com.example.flab.soft.shoppingmallfashion.item.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemSearchTagsPutRequest {
    List<String> itemSearchTags;
}
