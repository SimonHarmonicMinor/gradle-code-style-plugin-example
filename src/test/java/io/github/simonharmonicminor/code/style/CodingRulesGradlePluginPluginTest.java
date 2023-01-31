package io.github.simonharmonicminor.code.style;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Stream;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

class CodingRulesGradlePluginPluginTest {

  @Test
  void shouldApplyPluginSuccessfully() {
    final Project project = ProjectBuilder.builder().build();
    project.getPluginManager().apply("java");

    assertDoesNotThrow(
        () -> new CodingRulesGradlePluginPlugin().apply(project)
    );

    final Task task = project.getTasks().getByName("runStaticAnalysis");
    assertNotNull(task, "runStaticAnalysis task should be registered");
    final Set<String> codeStyleTasks =
        Stream.of("checkstyleMain", "checkstyleTest", "pmdTest", "pmdMain").collect(toSet());
    assertTrue(
        task.getDependsOn().containsAll(codeStyleTasks),
        format(
            "Task runStaticAnalysis should contain '%s' tasks, but actually: %s",
            codeStyleTasks,
            task.getDependsOn()
        )
    );
  }
}