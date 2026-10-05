package com.bubblecloud.common.file.oss.http;

import com.bubblecloud.common.file.core.FileBucket;
import com.bubblecloud.common.file.core.FileObjectInfo;
import com.bubblecloud.common.file.core.FileObjectSummary;
import com.bubblecloud.common.file.oss.service.OssTemplate;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * aws 对外提供服务端点
 *
 * @author lengleng
 * @author 858695266
 * <p>
 * oss.info
 */
@RestController
@AllArgsConstructor
@RequestMapping("/oss")
@ConditionalOnProperty(name = "file.oss.info", havingValue = "true")
public class OssEndpoint {

	private final OssTemplate template;

	/**
	 * Bucket Endpoints
	 */
	@SneakyThrows
	@PostMapping("/bucket/{bucketName}")
	public FileBucket createBucker(@PathVariable String bucketName) {

		template.createBucket(bucketName);
		return template.getBucket(bucketName).get();

	}

	@SneakyThrows
	@GetMapping("/bucket")
	public List<FileBucket> getBuckets() {
		return template.getAllBuckets();
	}

	@SneakyThrows
	@GetMapping("/bucket/{bucketName}")
	public FileBucket getBucket(@PathVariable String bucketName) {
		return template.getBucket(bucketName).orElseThrow(() -> new IllegalArgumentException("Bucket Name not found!"));
	}

	@SneakyThrows
	@DeleteMapping("/bucket/{bucketName}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteBucket(@PathVariable String bucketName) {
		template.removeBucket(bucketName);
	}

	/**
	 * Object Endpoints
	 */
	@SneakyThrows
	@PostMapping("/object/{bucketName}")
	public FileObjectInfo createObject(@RequestBody MultipartFile object, @PathVariable String bucketName) {
		String name = object.getOriginalFilename();
		@Cleanup
		InputStream inputStream = object.getInputStream();
		template.putObject(bucketName, name, inputStream, object.getSize(), object.getContentType());
		return template.getObjectInfo(bucketName, name);

	}

	@SneakyThrows
	@PostMapping("/object/{bucketName}/{objectName}")
	public FileObjectInfo createObject(@RequestBody MultipartFile object, @PathVariable String bucketName,
			@PathVariable String objectName) {
		@Cleanup
		InputStream inputStream = object.getInputStream();
		template.putObject(bucketName, objectName, inputStream, object.getSize(), object.getContentType());
		return template.getObjectInfo(bucketName, objectName);

	}

	@SneakyThrows
	@GetMapping("/object/{bucketName}/{objectName}")
	public List<FileObjectSummary> filterObject(@PathVariable String bucketName, @PathVariable String objectName) {

		return template.getAllObjectsByPrefix(bucketName, objectName, true);

	}

	@SneakyThrows
	@GetMapping("/object/{bucketName}/{objectName}/{expires}")
	public Map<String, Object> getObject(@PathVariable String bucketName, @PathVariable String objectName,
			@PathVariable Integer expires) {
		Map<String, Object> responseBody = new HashMap<>(8);
		// Put Object info
		responseBody.put("bucket", bucketName);
		responseBody.put("object", objectName);
		responseBody.put("url", template.getObjectURL(bucketName, objectName, expires));
		responseBody.put("expires", expires);
		return responseBody;
	}

	@SneakyThrows
	@ResponseStatus(HttpStatus.ACCEPTED)
	@DeleteMapping("/object/{bucketName}/{objectName}/")
	public void deleteObject(@PathVariable String bucketName, @PathVariable String objectName) {

		template.removeObject(bucketName, objectName);
	}

}
