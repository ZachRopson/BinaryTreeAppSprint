package com.bstapp.binarytree.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TreeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String inputNumbers;

    @Lob
    private String treeJson;
}
