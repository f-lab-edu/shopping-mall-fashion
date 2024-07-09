package com.example.flab.soft.shoppingmallfashion.address.service;

import com.example.flab.soft.shoppingmallfashion.address.repository.Address;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Addresses {
    private List<Address> addresses;
}
