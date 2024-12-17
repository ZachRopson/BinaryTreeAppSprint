package com.bstapp.binarytree.repository;

import com.bstapp.binarytree.entity.TreeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreeRepository extends JpaRepository<TreeData, Long> {}
