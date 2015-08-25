Bypass (Custome)
======

Skip the HTML, Bypass takes markdown and renders it directly on Android and iOS.

## License

[Apache License v2.0](http://www.tldrlegal.com/l/APACHE2)

## Requirements

- Boost must be [installed on your system](http://www.boost.org/doc/libs/1_53_0/more/getting_started/index.html)

This is the custome versino of the original * [Bypass library] (https://github.com/Uncodin/bypass). I have added some new methods in to the library with to support following contents.

1. COLOR: the library now supports colror in PHP format <font color="Red">this is red</font>.
2. EMPHASE in PARAGRAPH: If you need to emphase the entire paragraph, the original library has some problems, now you can emphase the entire paragraph. 



## Android

If you were usign Eclipse, please follow the method below: 

[Building And Using](platform/android/README.md)

If you were using android studio, here's what you can do. 
1. download the android ndk file. 
2. use comment line to cd the comment line to you bypass/platform/android/library. 
3. PATH=$PATH:/Users/xxx/NDK/android-ndk-xxx
4. type :ndk-build. 
5. open your android studio and add the library to your dependency as module. 
6. replace the io file in your bypass/platform/android/library/libs 
7. in android studi. add the library to your dependency from file>project structure>Dependency


## iOS

[Building And Using](platform/ios/README.md)

## 3rd Party Libraries

- [**Boost**](http://www.boost.org/) - [license](http://www.boost.org/LICENSE_1_0.txt)
- [**libsoldout**](http://fossil.instinctive.eu/libsoldout/home) - [license](http://fossil.instinctive.eu/libsoldout/artifact/c8d2f5b1e9e1df422ca06d1bc846d9e3055a925b)
