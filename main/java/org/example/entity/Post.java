package org.example.entity;

import org.example.enums.SellingType;
import org.example.enums.HomeType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Post {
    private String id;
    private HomeType homeType;
    private Address address;
    private double square;
    private int roomCount;
    private int price;
    private SellingType sellingType;
    private String userId;

    @Override
    public String toString() {
        return "Post { id=" + id + ", homeType=" + homeType + ", address=" + address +
                ", square=" + square + ", roomCount=" + roomCount + ", price=" + price +
                ", sellingType=" + sellingType + ", userId='" + userId + " }";
    }
}
