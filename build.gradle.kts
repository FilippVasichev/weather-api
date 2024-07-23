import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait

plugins {
    id("java")
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    id("org.flywaydb.flyway") version "9.22.1"
    id("nu.studer.jooq") version "8.2"
}

group = "com.weather"
version = "1.1"


repositories {
    mavenCentral()
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.testcontainers:postgresql:1.19.0")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.postgresql:postgresql:42.3.8")
    jooqGenerator("org.postgresql:postgresql:42.3.8")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core:10.15.2")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:10.15.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")


    testImplementation("io.rest-assured:rest-assured:5.3.2")
    testImplementation("io.rest-assured:json-path:5.3.2")
    testImplementation("io.rest-assured:xml-path:5.3.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.testcontainers:postgresql:1.19.0")
    testImplementation("org.testcontainers:junit-jupiter:1.15.0")
}

tasks.test {
    useJUnitPlatform()
}

val containerInstance: PostgreSQLContainer<Nothing> =
    PostgreSQLContainer<Nothing>("postgres:15.4").apply {
        waitingFor(Wait.forListeningPort())
        start()
    }

flyway {
    url = containerInstance.jdbcUrl
    user = containerInstance.username
    password = containerInstance.password
}

jooq {
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)
    configurations {
        create("main") {
            jooqConfiguration.apply {
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = containerInstance.jdbcUrl
                    user = containerInstance.username
                    password = containerInstance.password
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        isIncludeIndexes = false
                        excludes = "flyway.*"
                    }
                    generate.apply {
                        isPojos = true
                        isDaos = true
                        isTables = true
                    }
                    target.apply {
                        packageName = "com.weather"
                        directory = "src/main/jook"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}


tasks.named("generateJooq").configure {
    dependsOn(tasks.named("flywayMigrate"))
    doLast {
        containerInstance.stop()
    }
}

sourceSets {
    main {
        java.srcDirs("src/main/java", "src/main/jooq")
    }
}
