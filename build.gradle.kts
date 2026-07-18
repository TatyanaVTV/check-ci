import org.gradle.api.tasks.testing.logging.TestLogEvent
import com.github.spotbugs.snom.Effort
import com.github.spotbugs.snom.Confidence

plugins {
    java
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.6"
    id("checkstyle")
    id("com.github.spotbugs") version "6.5.9"
}

group = "ru.vtvhw.ci"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter")
    checkstyle("com.puppycrawl.tools:checkstyle:10.17.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
    }
}

// Checkstyle
checkstyle {
    toolVersion = "10.17.0"
    configFile = file("config/checkstyle/checkstyle.xml")
    isShowViolations = true
    maxErrors = 0
    maxWarnings = 0
    enableExternalDtdLoad = false
}

// SpotBugs
spotbugs {
    toolVersion = "4.8.6"
    excludeFilter = file("config/spotbugs/spotbugs-exclude.xml")
    effort = Effort.MAX
    reportLevel = Confidence.HIGH
}

tasks.spotbugsMain {
    reports.create("html") {
        required = true
        outputLocation = layout.buildDirectory.file("reports/spotbugs/spotbugs-main.html").get().asFile
        setStylesheet("fancy-hist.xsl")
    }
}

tasks.spotbugsTest {
    reports {
        create("html") {
            required = true
            outputLocation = layout.buildDirectory.file("reports/spotbugs/spotbugs-test.html").get().asFile
            setStylesheet("fancy-hist.xsl")
        }
    }
}