package com.bstapp.binarytree;

import com.bstapp.binarytree.entity.TreeData;
import com.bstapp.binarytree.repository.TreeRepository;
import com.bstapp.binarytree.service.BinaryTreeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BinaryTreeTests {

	@Autowired
	private TreeRepository treeRepository;

	@Autowired
	private TestRestTemplate restTemplate;

	// 1. Test the BinaryTreeService Logic
	@Test
	public void testBinaryTreeService() {
		BinaryTreeService bst = new BinaryTreeService();
		bst.insert(10);
		bst.insert(5);
		bst.insert(15);

		String jsonOutput = bst.toJson();
		assertTrue(jsonOutput.contains("10"));
		assertTrue(jsonOutput.contains("5"));
		assertTrue(jsonOutput.contains("15"));
	}

	// 2. Test Process Route (POST /process-tree)
	@Test
	public void testProcessTreeEndpoint() {
		String inputNumbers = "10,5,15";
		ResponseEntity<String> response = restTemplate.postForEntity(
				"/process-tree?numbers=" + inputNumbers,
				null,
				String.class
		);

		assertEquals(200, response.getStatusCodeValue());
		assertTrue(response.getBody().contains("10"));
		assertTrue(response.getBody().contains("5"));
		assertTrue(response.getBody().contains("15"));
	}

	// 3. Test Input Storage in Database
	@Test
	public void testDatabaseStorage() {
		TreeData data = new TreeData();
		data.setInputNumbers("10,5,15");
		data.setTreeJson("{\"value\":10,\"left\":{\"value\":5},\"right\":{\"value\":15}}");

		treeRepository.save(data);

		List<TreeData> results = treeRepository.findAll();
		assertFalse(results.isEmpty());
		assertEquals("10,5,15", results.get(0).getInputNumbers());
	}

	// 4. Test Previous Trees Endpoint (GET /previous-trees)
	@Test
	public void testPreviousTreesEndpoint() {
		ResponseEntity<String> response = restTemplate.getForEntity("/previous-trees", String.class);
		assertEquals(200, response.getStatusCodeValue());
	}
}
