plugins {
  kotlin("multiplatform")
  kotlin("plugin.compose") version "2.3.0"
  id("org.jetbrains.compose") version "1.6.11"
}

kotlin {
  jvm("desktop")
  
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material)
        implementation(compose.ui)
        implementation(compose.components.resources)
        implementation(compose.components.uiToolingPreview)
        implementation("com.github.hopskipnfall.KailleraProtocol-kmp:kailleraprotocol-jvm:1b008db2ad8cb91881c1641e6a1a9187c30b5948")
        implementation("io.ktor:ktor-network:2.3.11")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
        implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.3.0")
      }
    }
    val commonTest by getting
    val desktopMain by getting {
      dependencies {
        implementation(compose.desktop.currentOs)
      }
    }
    val desktopTest by getting
  }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb)
            packageName = "MooseClient"
            packageVersion = "1.0.0"
        }
    }
}
