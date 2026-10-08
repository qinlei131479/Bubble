package com.bubblecloud.codegen.util;

import com.bubblecloud.codegen.config.CodeGenDefaultProperties;
import com.bubblecloud.common.core.exception.CheckedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CodegenOutputPathValidatorTests {

	@TempDir
	Path tempDir;

	@Test
	void shouldAcceptFileInsideAllowedRoot() throws Exception {
		Path root = Files.createDirectory(tempDir.resolve("workspace"));
		CodegenOutputPathValidator validator = validator(root);

		assertThat(validator.validateOutputFile(root.resolve("src/Test.java").toString()))
			.isEqualTo(root.toRealPath().resolve("src/Test.java"));
	}

	@Test
	void shouldRejectFileOutsideAllowedRoot() throws Exception {
		Path root = Files.createDirectory(tempDir.resolve("workspace"));
		Path outside = Files.createDirectory(tempDir.resolve("outside"));
		CodegenOutputPathValidator validator = validator(root);

		assertThatThrownBy(() -> validator.validateOutputFile(outside.resolve("Test.java").toString()))
			.isInstanceOf(CheckedException.class)
			.hasMessage("路径不在允许的代码生成目录内");
	}

	@Test
	void shouldRejectSymlinkEscape() throws Exception {
		Path root = Files.createDirectory(tempDir.resolve("workspace"));
		Path outside = Files.createDirectory(tempDir.resolve("outside"));
		Path link = root.resolve("link");
		Files.createSymbolicLink(link, outside);
		CodegenOutputPathValidator validator = validator(root);

		assertThatThrownBy(() -> validator.validateOutputFile(link.resolve("Test.java").toString()))
			.isInstanceOf(CheckedException.class)
			.hasMessage("路径不在允许的代码生成目录内");
	}

	@Test
	void shouldRejectZipEntryTraversal() throws Exception {
		Path root = Files.createDirectory(tempDir.resolve("workspace"));
		CodegenOutputPathValidator validator = validator(root);

		assertThatThrownBy(() -> validator.validateZipEntry("../../etc/passwd"))
			.isInstanceOf(CheckedException.class)
			.hasMessage("代码文件名包含路径穿越");
	}

	@Test
	void shouldAllowRelativeZipEntry() throws Exception {
		Path root = Files.createDirectory(tempDir.resolve("workspace"));
		CodegenOutputPathValidator validator = validator(root);

		assertThatCode(() -> validator.validateZipEntry("src/main/java/Test.java")).doesNotThrowAnyException();
		assertThat(validator.validateZipEntry("src/../Test.java")).isEqualTo("Test.java");
	}

	private CodegenOutputPathValidator validator(Path root) {
		CodeGenDefaultProperties properties = new CodeGenDefaultProperties();
		properties.setAllowedOutputRoots(List.of(root.toString()));
		return new CodegenOutputPathValidator(properties);
	}
}
