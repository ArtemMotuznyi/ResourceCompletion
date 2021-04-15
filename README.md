# ResourceCompletion

![Build](https://github.com/ArtemMotuznyi/ResourceCompletion/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
---
Based on the [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template).

<!-- Plugin description -->
Plugin for search resource in android project by value.

|  Resource | View attributes in xml layout | Kotlin-Java  |
| -------------- | ------------------- | -----------------|
|  String | - text</br>- hint</br>  | - |
|  Color  | - textColor</br>- textColorHint</br>- background| - |

## Usage
- Strings

![Screenshot from 2021-04-15 04-48-46](https://user-images.githubusercontent.com/14909351/114802216-fde8f700-9da5-11eb-9781-c40cee7e2557.png)

![ezgif com-gif-maker](https://user-images.githubusercontent.com/14909351/114804935-d0527c80-9daa-11eb-92c1-c65c7547a8cd.gif)

- Colors

![Screenshot from 2021-04-15 04-52-23](https://user-images.githubusercontent.com/14909351/114802590-a7c88380-9da6-11eb-8e00-1db8cfdbcdbe.png)

![ezgif com-gif-maker (1)](https://user-images.githubusercontent.com/14909351/114805058-fc6dfd80-9daa-11eb-84e6-e1c01347d07a.gif)


<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Serval"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/ArtemMotuznyi/ResourceCompletion/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>
  

## Roadmap
- Implemented a search for string resources by value in Kotlin|Java code
- Implemented a search for color resources by value in Kotlin|Java code
- Implemented a search for plurals resources by value in Kotlin|Java code
- Implemented a search for array resources by value in Kotlin|Java code
- Increase the number of available view attributes
- Improve the implementation of searching for files with resources

## License
[MIT](https://choosealicense.com/licenses/mit/)