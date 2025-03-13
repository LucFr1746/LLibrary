## [![](https://jitpack.io/v/LucFr1746/LLibrary.svg)](https://jitpack.io/#LucFr1746/LLibrary)

## Using Maven

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

```xml
<dependency>
    <groupId>com.github.LucFr1746</groupId>
    <artifactId>LLibrary</artifactId>
    <version>VERSION</version>
</dependency>
```

## Using Gradle

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

```groovy
dependencies {
    implementation 'com.github.LucFr1746:LLibrary:Tag'
}
```

---

You may get the `VERSION`: [here](https://github.com/LucFr1746/LLibrary/releases) or on top of the page.

Add the API as dependency to your `plugin.yml`:

```yaml
depend: [LLibrary]
```

Or, if you are using `paper-plugin.yml`:

```yaml
dependencies:
  server:
    LLibrary:
      load: BEFORE
      required: true
      join-classpath: true
```

---
**Author:** [lucfr1746](https://github.com/lucfr1746)