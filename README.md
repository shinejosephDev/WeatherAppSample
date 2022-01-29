
# Weather App Sample

This is a sample Weather app with following features - 
 * Weather forecast of current location
 * Search city and add to favorites
 * Weather forecast of any city from search


## Environment Variables

`Staging` and `Production` environments are set in `build.gradle`


## Architecture

Architecture of the app is based on MVVM and Clean Architecture ith the support of Hilt.
Logic that would traditionally sit in View Models now exists in the form of Use Cases. It also has a dedicated Domain layer allowing easier swapping of Data and Presentation layers.
Following Libraries are used 

* Hilt
* Room
* Jetpack Navigation
* Glide
* Retrofit
* Coroutines


## TO-DO

* Unit testing
* Multi module architecture
* CSV Export
