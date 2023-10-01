package com.devmandrik.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class User {
    private Long id;
    private String name;
    private String firstName;
    private String lastName;
    private List<Bank> banks;
}
