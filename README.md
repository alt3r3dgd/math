# me.altered.math
An oversimplified library to work with matrices and vectors in Kotlin.

### How to install
- Grab a jar from releases
- Place it in the `libs` folder in the project root
- Go to `build.gradle`/`build.gradle.kts`:

_build.gradle:_
```groovy
dependencies {
    implementation file("libs/math.main.jar")
}
```

_build.gradle.kts:_
```kotlin
dependencies {
    implementation(file("libs/math.main.jar"))
}
```
- Import `me.altered.math` in your `.kt` files!

### Notes
- If you can't multiply or divide a number by a matrix, make sure to import `me.altered.math.Matrix`