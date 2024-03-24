# AppUpdateChecker

An Android library that checks for app updates.

[![](https://jitpack.io/v/Sharkaboi/AppUpdateChecker.svg)](https://jitpack.io/#Sharkaboi/AppUpdateChecker) ![License](https://img.shields.io/github/license/Sharkaboi/AppUpdateChecker) ![Contributors](https://img.shields.io/github/contributors/Sharkaboi/AppUpdateChecker?color=dark-green) ![Issues](https://img.shields.io/github/issues/Sharkaboi/AppUpdateChecker) ![Forks](https://img.shields.io/github/forks/Sharkaboi/AppUpdateChecker?style=social) ![Stargazers](https://img.shields.io/github/stars/Sharkaboi/AppUpdateChecker?style=social)

## Instructions

- Add Jitpack to your project

```groovy
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```

- Add
  Dependency [![](https://jitpack.io/v/Sharkaboi/AppUpdateChecker.svg)](https://jitpack.io/#Sharkaboi/AppUpdateChecker)

```groovy
dependencies {
    implementation 'com.github.Sharkaboi:AppUpdateChecker:<Latest version>'
}
```

## Usage

### Initialize update checker instance

```kotlin
val updateChecker = AppUpdateChecker(
    // Any of the sources mentioned below
    source = < Source >(
    ... // params
currentVersion = "v1.0.0"
)
)
```

This is so that you can instantiate with your existing DI setup.

### Available update check sources

#### Github release tags

##### Using github public API

Github public API has rate limit as
mentioned [here](https://docs.github.com/en/rest/using-the-rest-api/rate-limits-for-the-rest-api).

```kotlin
GithubTagSource(
    ownerUsername = "Sharkaboi",
    repoName = "AppUpdateChecker",
    currentVersion = "v1.0.0"
)
```

##### Using github authenticated API

Github authenticated API has higher rate limit. Check
github's [docs](https://docs.github.com/en/rest/using-the-rest-api/rate-limits-for-the-rest-api#getting-a-higher-rate-limit)
to obtain a token.

```kotlin
GithubTagSource(
    ownerUsername = "Sharkaboi",
    repoName = "AppUpdateChecker",
    currentVersion = "v1.0.0",
    bearerToken = "<MY TOKEN>"
)
```

#### FDroid

##### With version name

```kotlin
FDroidVersionNameSource(
    packageName = "com.sharkaboi.appupdatechecker",
    currentVersion = "v1.0.0"
)
```

##### With version code

```kotlin
FDroidVersionCodeSource(
    packageName = "com.sharkaboi.appupdatechecker",
    currentVersion = 10100
)
```

#### JSON

##### With version name

```kotlin
JsonVersionNameSource(
    jsonEndpoint = "https://mywebsite.com/version.json",
    currentVersion = "v1.0.0"
)
```

JSON structure supported by default :

```javascript
{
	"latestVersionName": "v1.2",
	"latestVersionUrl": "https://mywebsite.com/v1.2",
	"releaseNotes": "My epic changelog"
}
```

##### With version code

```kotlin
JsonVersionCodeSource(
    jsonEndpoint = "https://mywebsite.com/version.json",
    currentVersion = 10100
)
```

JSON structure supported by default :

```javascript
{
	"latestVersionCode": 10102,
	"latestVersionUrl": "https://mywebsite.com/v1.2",
	"releaseNotes": "My epic changelog"
}
```

#### XML

##### With version name

```kotlin
XMLVersionNameSource(
    xmlEndpoint = "https://mywebsite.com/version.xml",
    currentVersion = "v1.0.0"
)
```

XML structure supported by default :

```xml

<version>
    <latestVersionName>v1.2</latestVersionName>
    <latestVersionUrl>https://mywebsite.com/v1.2</latestVersionUrl>
    <releaseNotes>My epic changelog</releaseNotes>
</version>
```

##### With version code

```kotlin
XMLVersionCodeSource(
    xmlEndpoint = "https://mywebsite.com/version.xml",
    currentVersion = 10100
)
```

XML structure supported by default :

```xml

<version>
    <latestVersionCode>10102</latestVersionCode>
    <latestVersionUrl>https://mywebsite.com/v1.2</latestVersionUrl>
    <releaseNotes>My epic changelog</releaseNotes>
</version>
```

#### Custom source

```kotlin
class CustomVersionSource(
    override val currentVersion: String,
    override var versionComparator: VersionComparator<String> = DefaultStringVersionComparator
) : AppUpdateCheckerSource<String>() {
    private val customSource = "https://mywebsite.com/latestVersion"

    override suspend fun queryVersionDetails(): VersionDetails<String> {
        // Do your processing to fetch from source here
        ...
        // Handle exceptions if needed here, wrap with AppUpdateCheckerException if needed
        return VersionDetails(
            latestVersion = version,
            latestVersionUrl = "https://mywebsite.com/download.apk",
            releaseNotes = null
        )
    }
}
```

### Check for update

```kotlin
viewModelScope.launch {
    val result = updateChecker.checkUpdate()

    // or

    val deferred = updateChecker.checkUpdateAsync()
    val result = deferred.await()

    when (result) {
        // Update found
        is UpdateResult.NoUpdate -> println(result.versionDetails.toString())
        // Already has the latest version installed
        UpdateState.NoUpdate ->..
    }
}
```

### Error handling

```kotlin

if (throwable is AppUpdateCheckerException) {
    when (throwable) {
        is GenericError -> {
            // Generic errors (network, ssl, parse)
        }

        is InvalidEndPointException -> {
            // Invalid endpoint passed for sources with endpoints (json/xml)
        }

        is InvalidPackageNameException -> {
            // Invalid package name passed
        }

        is InvalidRepositoryNameException -> {
            // Invalid repository name passed
        }

        is InvalidUserNameException -> {
            // Invalid username passed
        }

        is InvalidVersionException -> {
            // Invalid version format for default version name comparator
        }

        is PackageNotFoundException -> {
            // Remote Service returned 404
        }

        is RemoteError -> {
            // Other remote server error code
        }
    }
}
```

### Custom version comparator

```kotlin
val source = JsonVersionNameSource(
    jsonEndpoint = "https://mywebsite.com/version.json",
    currentVersion = "v1.0-alpha"
)

val customVersionComparator = object : VersionComparator<String> {
    override fun isNewerVersion(
        currentVersion: String,
        newVersion: String
    ): Boolean {
        // Perform custom logic here or can use default comparators provided with library.
        return DefaultStringVersionComparator.isNewerVersion(
            currentVersion.substringBefore('-'),
            newVersion.substringBefore('-')
        )
    }
}
source.setCustomVersionComparator(customVersionComparator)
val testChecker = AppUpdateChecker(source = source)
val result = testChecker.checkUpdate()
...
```

## TechStack

- Kotlin
- Coroutines
- Retrofit
- Moshi
- SimpleXML

## Background

This project is heavily inspired
by [javiersantos/AppUpdater](https://github.com/javiersantos/AppUpdater). The project had been stale
for a while and had been on AsyncTasks so decided to write my own library.

Of course, the library is very early stage and doesn't have all the features but is WIP.

## Roadmap

See the [open issues](https://github.com/Sharkaboi/AppUpdateChecker/issues) for a list of proposed
features (and known issues).

## Contributing

PR's are welcome. Please try to follow the template.

### Creating A Pull Request

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Authors

- [Sharkaboi](https://github.com/Sharkaboi)

## Credits

- [javiersantos](https://github.com/javiersantos)
- [Shields](https://shields.io/)

## License

```
MIT License

Copyright (c) 2021 Sarath S

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
