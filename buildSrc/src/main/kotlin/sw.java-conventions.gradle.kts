plugins {
    `java-library`
    `maven-publish`
    id("sw.license-conventions")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
    implementation("ch.qos.logback:logback-classic:1.2.10")
}

java.sourceCompatibility = JavaVersion.VERSION_17

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}