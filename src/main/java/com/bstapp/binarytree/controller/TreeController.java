package com.bstapp.binarytree.controller;

import com.bstapp.binarytree.entity.TreeData;
import com.bstapp.binarytree.repository.TreeRepository;
import com.bstapp.binarytree.service.BinaryTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class TreeController {

    @Autowired
    private TreeRepository treeRepository;

    @GetMapping("/input-numbers")
    public String showInputForm() {
        return "input-numbers";
    }

    @PostMapping("/process-tree")
    public String processTree(@RequestParam String numbers, Model model) {
        BinaryTreeService bst = new BinaryTreeService();

        Arrays.stream(numbers.split(","))
                .mapToInt(Integer::parseInt)
                .forEach(bst::insert);

        String treeJson = bst.toJson();

        TreeData data = new TreeData();
        data.setInputNumbers(numbers);
        data.setTreeJson(treeJson);
        treeRepository.save(data);

        model.addAttribute("treeJson", treeJson);
        return "show-tree";
    }

    @GetMapping("/previous-trees")
    public String showPreviousTrees(Model model) {
        List<TreeData> results = treeRepository.findAll();
        model.addAttribute("results", results);
        return "previous-trees";
    }
}
