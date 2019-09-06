# Firestorm

Gradle plugin for running modularized Android instrumented tests on Firebase Test Lab.

...

## Installation

The **Firestorm Plugin** is available from both **Maven Central** and **Gradle Plugin Portal**, add repositories in top-level `build.gradle`...

...

The plugin should be applied directly to **Android Application** or **Android Library** subprojects with instrumented tests.

In the subproject's `build.gradle` file:

Android Application project:

```gradle
plugins {
    id 'com.android.application'
    id 'io.github.reactivecircus.firestorm' version "$firestormVersion"
}
```

Android Library project:

```gradle
plugins {
    id 'com.android.library'
    id 'io.github.reactivecircus.firestorm' version "$firestormVersion"
}
```

To use the traditional syntax, declare the plugin in the `buildscript` block within the top-level `build.gradle`:

```
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

```gradle
apply plugin: 'com.android.application'
apply plugin: 'io.github.reactivecircus.firestorm'
```

Android Library project:

```gradle
apply plugin: 'com.android.library'
apply plugin: 'io.github.reactivecircus.firestorm'
```

## Configurations

...

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
