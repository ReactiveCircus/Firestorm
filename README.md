# Firestorm

[![CircleCI](https://circleci.com/gh/ReactiveCircus/Firestorm.svg?style=svg)](https://circleci.com/gh/ReactiveCircus/Firestorm) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Gradle plugin for running modularized Android instrumented tests on Firebase Test Lab.

...

## Installation

The **Firestorm Gradle Plugin** is available from both **Maven Central** and **Gradle Plugin Portal**. Make sure your top-level `build.gradle` has either `mavenCentral()` or `gradlePluginPortal()` defined in the `buildscript` block:

```groovy
buildscript {
    ext.firestormVersion = '0.1.0'
    
    repositories {
        mavenCentral()
        gradlePluginPortal()
        ...
    }
}
```

### Applying the plugin

The plugin should be applied directly to **Android Application** or **Android Library** subprojects with instrumented tests.

In the subproject's `build.gradle` file:

Android Application project:

```groovy
plugins {
    id 'com.android.application'
    id 'io.github.reactivecircus.firestorm' version "$firestormVersion"
}
```

Android Library project:

```groovy
plugins {
    id 'com.android.library'
    id 'io.github.reactivecircus.firestorm' version "$firestormVersion"
}
```

To use the traditional syntax, declare the plugin in the `buildscript` block within the top-level `build.gradle`:

```groovy
buildscript {
    ext.firestormVersion = 'x.y.z'
    
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    dependencies {
        ...
        classpath "io.github.reactivecircus.firestorm:firestorm-gradle-plugin:$firestormVersion"
    }
}
```

Then apply the plugin in the subproject's `build.gradle` file:

Android Application project:

```groovy
apply plugin: 'com.android.application'
apply plugin: 'io.github.reactivecircus.firestorm'
```

Android Library project:

```groovy
apply plugin: 'com.android.library'
apply plugin: 'io.github.reactivecircus.firestorm'
```

## Configurations

Available plugin configurations and default values:

```groovy
plugins {
    id 'com.android.application'
    id 'io.github.reactivecircus.firestorm'
}

firestorm {
    // Enable or disable the plugin
    enabled = true    

    // Whether to only trigger assembling APKs and running tests if the project source has meaningful git changes.
    smartTrigger = true

    // Configurations for smart trigger. Only relevant when [smartTrigger] is true.
    smartTriggerConfigs {
        // Set of include patterns to be considered when performing project source git changes detection.
        includes = [
            "src/**/*.kt",
            "src/**/*.java",
            "src/**/*.xml",
            "src/*/assets",
            "build.gradle",
            "build.gradle.kts",
            "*.properties"
        ]

        // Set of exclude patterns to be ignored when performing project source git changes detection.
        excludes = [
            "src/test*"
        ]

        // Whether to recursively check the project's transitive dependencies when performing project source git changes detection.
        checkDependencies = true 
    }
}
```

## License

```
Copyright 2019 Yang Chen

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
