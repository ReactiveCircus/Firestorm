# Firestorm

Gradle plugin for running multi-module Android instrumented tests on Firebase Test Lab.

...

### Assembler Plugin

...

### Runner Plugin

...

## Installation

Apply the **Runner Plugin** in the top-level `build.gradle`:

```gradle
buildscript {
    ext.firestormVersion = 'x.y.z'
}

plugins {
    ...
    // apply runner plugin
    id 'firestorm-runner' version "$firestormVersion"
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}
```

Or use the traditional syntax:

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

// apply runner plugin
apply plugin: 'firestorm-runner'

```

Apply the **Assembler Plugin** to an Android application subproject:

In `app/build.gradle` file:

```gradle
plugins {
    id 'com.android.application'
    id 'firestorm-assembler' version "$firestormVersion"
}
```

Or use the traditional syntax:

```gradle
apply plugin: 'com.android.application'
apply plugin: 'firestorm-assembler'
```

Apply the **Assembler Plugin** to an Android library subproject:

In `library/build.gradle` file:

```gradle
plugins {
    id 'com.android.library'
    id 'firestorm-assembler' version "$firestormVersion"
}
```

Or use the traditional syntax:

```gradle
apply plugin: 'com.android.library'
apply plugin: 'firestorm-assembler'
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
