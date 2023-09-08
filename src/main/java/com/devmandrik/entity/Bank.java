package com.devmandrik.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Bank {
    private Long id;
    private String name;
    private List<User> users;
}
