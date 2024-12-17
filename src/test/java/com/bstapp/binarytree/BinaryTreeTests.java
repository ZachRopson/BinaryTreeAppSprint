package com.bstapp.binarytree;

import com.bstapp.binarytree.entity.TreeData;
import com.bstapp.binarytree.repository.TreeRepository;
import com.bstapp.binarytree.service.BinaryTreeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

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
		System.out.println("Generated JSON: " + jsonOutput); // Debugging output
		assertTrue(jsonOutput.contains("\"value\":10"), "Root node missing");
		assertTrue(jsonOutput.contains("\"left\":{\"value\":5"), "Left child missing");
		assertTrue(jsonOutput.contains("\"right\":{\"value\":15"), "Right child missing");
	}

	// 2. Test Process Route (POST /process-tree)
	@Test
	public void testProcessTreeEndpoint() {
		String inputNumbers = "10,5,15";

		// Prepare form data
		LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("numbers", inputNumbers);

		// Send POST request
		ResponseEntity<String> response = restTemplate.postForEntity("/process-tree", formData, String.class);

		// Debugging output
		System.out.println("Response Body: " + response.getBody());

		// Verify response status and content
		assertEquals(200, response.getStatusCodeValue(), "Response status is not 200 OK");
		String responseBody = response.getBody();

		// Validate the JSON structure
		assertTrue(responseBody.contains("\"value\":10"), "Root node missing");
		assertTrue(responseBody.contains("\"left\":{\"value\":5"), "Left child missing");
		assertTrue(responseBody.contains("\"right\":{\"value\":15"), "Right child missing");
	}

	// 3. Test Input Storage in Database
	@Test
	public void testDatabaseStorage() {
		TreeData data = new TreeData();
		data.setInputNumbers("10,5,15");
		data.setTreeJson("{\"value\":10,\"left\":{\"value\":5},\"right\":{\"value\":15}}");

		treeRepository.save(data);

		List<TreeData> results = treeRepository.findAll();
		assertFalse(results.isEmpty(), "Database is empty");
		assertEquals("10,5,15", results.get(0).getInputNumbers(), "Stored input numbers mismatch");
	}

	// 4. Test Previous Trees Endpoint (GET /previous-trees)
	@Test
	public void testPreviousTreesEndpoint() {
		ResponseEntity<String> response = restTemplate.getForEntity("/previous-trees", String.class);
		assertEquals(200, response.getStatusCodeValue(), "Response status is not 200 OK");
	}
}

