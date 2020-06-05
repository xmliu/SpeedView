# SpeedView
欢迎使用码表自定义View

![image](https://github.com/xmliu/SpeedView/blob/master/image/main.gif)

# Gradle
Step 1. 在你的根目录下的build.gradle文件中增加JitPack仓库依赖
 
```kotlin
allprojects {
    repositories {
        google()
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

Step 2. 在你的module目录下的build.gradle文件中增加speedview依赖。

```kotlin
    implementation 'com.github.xmliu:speedview:1.0.0'
```

Step 3. xml中添加控件

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <cn.xmliu.speedview.SpeedView
        android:id="@+id/speedView"
        android:layout_centerInParent="true"
        android:layout_width="300dp"
        android:layout_height="180dp"/>

</RelativeLayout>
```

Step 4. 在activity中你需要的位置设置当前速度

```kotlin
    speedView.setCurSpeed(38)
```