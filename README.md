<p align="center">
  <h1 align="center">AppUpdateChecker</h1>
  <p align="center">
    An Android library that checks for app updates. 
    <br/>
  </p>
</p>

![Downloads](https://img.shields.io/github/downloads/Sharkaboi/AppUpdateChecker/total) ![Contributors](https://img.shields.io/github/contributors/Sharkaboi/AppUpdateChecker?color=dark-green) ![Issues](https://img.shields.io/github/issues/Sharkaboi/AppUpdateChecker) ![License](https://img.shields.io/github/license/Sharkaboi/AppUpdateChecker) ![Forks](https://img.shields.io/github/forks/Sharkaboi/AppUpdateChecker?style=social) ![Stargazers](https://img.shields.io/github/stars/Sharkaboi/AppUpdateChecker?style=social)

## TechStack
* Kotlin
* Coroutines
* Retrofit
* Moshi
* SimpleXML

## Instructions

* Add Jitpack to your project

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

* Add Dependency  
[![](https://jitpack.io/v/Sharkaboi/AppUpdateChecker.svg)](https://jitpack.io/#Sharkaboi/AppUpdateChecker)
```groovy
dependencies {
	implementation 'com.github.Sharkaboi:AppUpdateChecker:<Latest version>'
}
```

## Usage

* Initialize update checker instance
```kotlin
val updateChecker = AppUpdateChecker(
	context = context,
	// Any of the sources mentioned below
	source = AppUpdateCheckerSource.*(), 
	// Optional : Takes versionName mentioned in gradle by default
	currentVersionTag = "v1.0.0" 
)
```
This is so that you can instantiate with your existing DI setup.

* Available sources :
```kotlin
// Github source
AppUpdateCheckerSource.GithubSource(
	ownerUsername = "Sharkaboi",
	repoName = "AppUpdateChecker"
)

// FDroid source
AppUpdateCheckerSource.FDroidSource(
	// Optional : Takes packageName from context by default
	packageName = "com.sharkaboi.appupdatechecker" 
)

// JSON source
AppUpdateCheckerSource.JsonSource(
	// JSON structure mentioned below
	jsonEndpoint = "https://mywebsite.com/version.json"
)

// XML source
AppUpdateCheckerSource.XMLSource(
	// XML structure mentioned below
	xmlEndpoint = "https://mywebsite.com/version.xml"
)
```
JSON structure : 
```javascript
{
	"latestVersion": "v1.2",
	"latestVersionUrl": "https://mywebsite.com/v1.2",
	"releaseNotes": "My epic changelog"
}
```
XML structure : 
```xml
<version>
	<latestVersion>v1.2</latestVersion>
	<latestVersionUrl>https://mywebsite.com/v1.2</latestVersionUrl>
	<releaseNotes>My epic changelog</releaseNotes>
</version>
```

* Check update
```kotlin
viewModelScope.launch {
	val result = updateChecker.checkUpdate()

	//or

	val deferred = updateChecker.checkUpdateAsync()
	val result = deferred.await()

        when(result){
		// Update found
		is UpdateState.UpdateAvailable ->
			
		// Already has the latest version installed
		UpdateState.LatestVersionInstalled -> 
			
		// Mentioned package name not found in FDroid 
		UpdateState.FDroidInvalid -> 
			
		// Mentioned package name was invalid
		UpdateState.FDroidMalformed -> 
			
		// Mentioned repo with username not found in Github
		UpdateState.GithubInvalid -> 
			
		// Mentioned repo or username was invalid
		UpdateState.GithubMalformed -> 
			
		// Mentioned JSON endpoint had invalid structure or wasn't reachable
		UpdateState.JSONInvalid -> 
			
		// Mentioned JSON endpoint was not a valid URL
		UpdateState.JSONMalformed -> 
			
		// Mentioned XML endpoint had invalid structure or wasn't reachable
		UpdateState.XMLInvalid -> 

		// Mentioned XML endpoint was not a valid URL
		UpdateState.XMLMalformed -> 
	
		// No network found
		UpdateState.NoNetworkFound -> 

		// Generic error to wrap other errors
		is UpdateState.GenericError -> 
        }
    }
```
## Background

This project is heavily inspired by [javiersantos/AppUpdater](https://github.com/javiersantos/AppUpdater). The project had been stale for a while and had been on AsyncTasks so decided to write my own library.

Of course, the library is very early stage and doesn't have all the features but is WIP.

## Roadmap

See the [open issues](https://github.com/Sharkaboi/AppUpdateChecker/issues) for a list of proposed features (and known issues).

## Contributing

PR's are welcome. Please try to follow the template.

### Creating A Pull Request

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


## Authors

* [Sharkaboi](https://github.com/Sharkaboi)

## Credits

* [javiersantos](https://github.com/javiersantos)
* [Shields](https://shields.io/)

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
