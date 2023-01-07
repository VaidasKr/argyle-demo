# Argyle demo

## Getting started

This sample will warn you if you don't supply a apiSecretKey in your local.properties, which is used for authorizing
http requests, value should be provided for app to work correctly.  
Value should be provided in `local.properties` file like this:

    apiSecretKey = {key value}

## Architecture

MVVM architecture with Jetpack Compose framework for displaying UI, and Kotlin Coroutines for async operations

## Used libraries

* Android compose libraries
* Android splash screen
* Android start up
* Glide
* Kotlin serialization
* OkHttp
* Retrofit
* Koin
* Timber
* Mockk

## Bugs

No response caching present which would allow to use app while user is online
