plugins {
    kotlin("jvm") version "1.9.22"
}

group = "edu.myrza"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.netty:netty-all:4.1.113.Final")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}