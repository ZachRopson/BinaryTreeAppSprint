package com.bstapp.binarytree.controller;

import com.bstapp.binarytree.entity.TreeData;
import com.bstapp.binarytree.repository.TreeRepository;
import com.bstapp.binarytree.service.BinaryTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class TreeController {

    @Autowired
    private TreeRepository treeRepository;

    @GetMapping("/")
    public String redirectToInputForm() {
        return "redirect:/enter-numbers";
    }

    // Map root path to the input form
    @GetMapping("/enter-numbers")
    public String showInputForm() {
        return "input-numbers"; // View: input-numbers.html
    }

    @PostMapping("/process-tree")
    public ResponseEntity<String> processTree(@RequestParam String numbers, Model model) {
        BinaryTreeService bst = new BinaryTreeService();

        // Insert values into the binary tree
        Arrays.stream(numbers.split(","))
                .mapToInt(Integer::parseInt)
                .forEach(bst::insert);

        String treeJson = bst.toJson();

        // Save tree data to the repository
        TreeData data = new TreeData();
        data.setInputNumbers(numbers);
        data.setTreeJson(treeJson);
        treeRepository.save(data);

        // Return JSON response for tests
        return ResponseEntity.ok(treeJson);
    }

    @GetMapping("/previous-trees")
    public String showPreviousTrees(Model model) {
        List<TreeData> results = treeRepository.findAll();
        model.addAttribute("results", results);
        return "previous-trees"; // View: previous-trees.html
    }
}

