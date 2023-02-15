plugins {
  `kotlin-dsl`
}

repositories {
  jcenter()
  mavenLocal()
  mavenCentral()
}

dependencies {
  implementation("org.jsoup:jsoup:1.13.1")
  implementation("com.google.code.gson:gson:2.8.9")
}
