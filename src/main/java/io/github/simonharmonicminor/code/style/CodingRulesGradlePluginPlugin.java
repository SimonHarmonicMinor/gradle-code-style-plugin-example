package io.github.simonharmonicminor.code.style;

import static java.util.Collections.emptyList;

import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.quality.Checkstyle;
import org.gradle.api.plugins.quality.CheckstyleExtension;
import org.gradle.api.plugins.quality.Pmd;
import org.gradle.api.plugins.quality.PmdExtension;


public class CodingRulesGradlePluginPlugin implements Plugin<Project> {

  @Override
  public void apply(Project project) {
    project.getPluginManager().apply("checkstyle");
    project.getExtensions().configure(CheckstyleExtension.class, checkstyleExtension -> {
      checkstyleExtension.setToolVersion("10.5.0");
      checkstyleExtension.setIgnoreFailures(false);
      checkstyleExtension.setMaxWarnings(0);
      checkstyleExtension.setConfigFile(
          FileUtil.copyContentToTempFile("style/checkstyle.xml", ".checkstyle.xml")
      );
    });

    project.getPluginManager().apply("pmd");
    project.getExtensions().configure(PmdExtension.class, pmdExtension -> {
      pmdExtension.setConsoleOutput(true);
      pmdExtension.setToolVersion("6.52.0");
      pmdExtension.setIgnoreFailures(false);
      pmdExtension.setRuleSets(emptyList());
      pmdExtension.setRuleSetFiles(project.files(
          FileUtil.copyContentToTempFile("style/pmd.xml", ".pmd_test.xml")
      ));
    });

    final SortedSet<String> checkstyleTaskNames = project.getTasks()
        .withType(Checkstyle.class)
        .getNames();

    final SortedSet<String> pmdTaskNames = project.getTasks()
        .withType(Pmd.class)
        .getNames();

    project.task(
        "runStaticAnalysis",
        task -> task.setDependsOn(
            Stream.concat(
                checkstyleTaskNames.stream(),
                pmdTaskNames.stream()
            ).collect(Collectors.toList())
        )
    );
  }
}
