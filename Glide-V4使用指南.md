Glide-V4使用指南
>学习目标文章[Glide最新版V4使用指南](https://blog.csdn.net/u013005791/article/details/74532091)

**概述**<br>
Glide是一个Android的图片加载和缓存库，它主要专注于大量图片的流畅加载，Glide几乎可以胜任任何需要使用的图片从网络拉取，压缩，显示的场景。
### 1. 集成
Github地址：https://github.com/bumptech/glide<br>

Android Studio 3.0 使用:
```java
repositories {
  mavenCentral()
  google()
}

dependencies {
  implementation 'com.github.bumptech.glide:glide:4.8.0'
  annotationProcessor 'com.github.bumptech.glide:compiler:4.8.0'
}
```
Kotlin:
```java
dependencies {
    implementation 'com.github.bumptech.glide:glide:4.3.1'
    kapt 'com.github.bumptech.glide:compiler:4.3.1'
}
```
使用implementation还是api需要视情况而定，implementation只能用于当前module，如果在库中以这种方式设置依赖，那么在app的module是引用不到的，但是api可以，api相当于compile。<br>

在proguard中添加混淆规则：<br>
```java
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
```
### 2. 基本用法
大多数情况下**加载图片**只需要一行代码：
```java
Glide.with(fragment)
    .load(myUrl)
    .into(imageView);
```
取消加载：
```java
Glide.with(fragment).clear(imageView);
```
在实际的使用中一般是不会取消加载的<br>

因为在with方法中传入的Activity或Fragment被销毁的时候，Glide会自动取消加载并回收所有在加载过程中所使用的资源。

### 3. 注解（V4新特性）和自定义方法
Glide使用了annotation processor来生成API，允许应用修改RequestBuilder、RequestOptions和任意的包含在单一流式API库中的方法。这是V4的特性，运用注解后使用起来更方便：
```java
GlideApp.with(fragment)
   .load(myUrl)
   .placeholder(R.drawable.placeholder)
   .fitCenter()
   .into(imageView);
```
Glidev4中的Glide.with().load()中没有之前版本的fitCenter和placeholder这样的方法，但是GlideApp有，可以直接在builder中使用。GlideApp可以代替之前版本的Glide开头的方法。<br>

这样做的目的是：
* 1. 对于library项目来讲可以使用自定义方法继承Glide的API。 
* 2. 对于应用来讲，在继承Glide的API后，可以添加自定义方法。

虽然可以手动继承RequestOptions，但是显然这样做更加麻烦，也破坏了流式api特性。<br>

#### 3.1 在项目中实现AppGlideModule：
```java
@GlideModule
public class CustomGlideModule extends AppGlideModule {}
```
这个类实现必须要有 @GlideModule 注解，如果添加的方法失效，那就检查下这里。<br>

如果是library就实现LibraryGlideModule,以使用OkHttp为例：
```java
@GlideModule
public final class OkHttpLibraryGlideModule extends LibraryGlideModule {
  @Override
  public void registerComponents(Context context, Registry registry) {
    registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
  }
}
```
OkHttpUrlLoader是Glide的OKHttp扩展库中的类,如果需要使用Glide的实现，可以在依赖中添加：
```java
compile 'com.github.bumptech.glide:okhttp3-integration:4.3.1'
```
Android Studio 3.0:
```java
implementation 'com.github.bumptech.glide:okhttp3-integration:4.3.1'
```
添加完依赖后不需要自己实现OkHttpLibraryGlideModule类，库中已经自带了，会自动使用OKHttp的。<br>

然后编译工程就可以在build中生成四个类：<br>
* GlideApp
* GlideOptions
* GlideRequest
* GlideRequests

#### 3.2 GlideExtension
为了添加新的方法，修改已有的方法或者添加对其他类型格式的支持，需要在扩展中使用加了注解的静态方法。

GlideOption用来添加自定义的方法，GlideType用来支持新的格式。

##### 3.2.1 GlideOption
先新建一个CustomGlideExtension类：










 























