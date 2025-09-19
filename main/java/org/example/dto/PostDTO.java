package org.example.dto;

import org.example.enums.HomeType;
import org.example.enums.SellingType;

public record PostDTO(String id, HomeType homeType, AddressDTO address,
                      double square, int roomCount, int price,
                      SellingType sellingType, String currentUserId) {}
