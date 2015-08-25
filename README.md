Bypass (Custome)
======

Skip the HTML, Bypass takes markdown and renders it directly on Android and iOS.

## License

[Apache License v2.0](http://www.tldrlegal.com/l/APACHE2)

## Requirements

- Boost must be [installed on your system](http://www.boost.org/doc/libs/1_53_0/more/getting_started/index.html)

This is the custome versino of the original * [Bypass library] (https://github.com/Uncodin/bypass). This library has fixed some bugs in the original library and added few more functions. 

1. COLOR: the library now supports colror in PHP format "<font color="Red">this is red</font>".
2. EMPHASE in PARAGRAPH: If you need to emphase the entire paragraph, the original library will only show *** at the first line. In this library, you can parse the entire Paragraph with no problem.
3. Youtube Video: Although there is no youtube functions in the markdown language, I have added a parser to parse youtube video links and disply the video using youtube Api. 


## Android

If you were usign Eclipse, please follow the method below: 

[Building And Using](platform/android/README.md)

If you were using android studio, here's what you can do. 
1. download the [android NDK](https://developer.android.com/ndk/index.html). \n
2. use terminal to cd to you bypass/platform/android/library folder. \n
3. type PATH=$PATH:/Users/xxx/NDK/android-ndk-xxx in the terminal (xxx are your own directory)\n
4. type ndk-build in your terminal. \n
5. open your android studio and add the library to your dependency as module. \n
6. replace the io file in your bypass/platform/android/library/libs \n
7. in android studi. add the library to your dependency from file>project structure>Dependency


## iOS

[Building And Using](platform/ios/README.md)

##Usage display


## 3rd Party Libraries

- [**Boost**](http://www.boost.org/) - [license](http://www.boost.org/LICENSE_1_0.txt)
- [**libsoldout**](http://fossil.instinctive.eu/libsoldout/home) - [license](http://fossil.instinctive.eu/libsoldout/artifact/c8d2f5b1e9e1df422ca06d1bc846d9e3055a925b)


