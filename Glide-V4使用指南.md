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
```java
@GlideExtension
public class CustomGlideExtension {
    //缩略图的最小尺寸，单位：px
    private static final int MINI_THUMB_SIZE = 100;

    /**
     * 将构造方法设为私有，作为工具类使用
     */
    private CustomGlideExtension() {
    }

    /**
     * 1.自己新增的方法的第一个参数必须是RequestOptions options
     * 2.方法必须是静态的
     * @param options
     */
    @GlideOption
    public static void miniThumb(RequestOptions options) {
        options
                .fitCenter()
                .override(MINI_THUMB_SIZE);
    }
}
```
编译工程，打开build目录中的GlideOptions，可以看见自动生成了两个方法:
```java
public class GlideOptions extends RequestOptions {

  /**
   * @see CustomGlideExtension#miniThumb(RequestOptions)
   */
  public GlideOptions miniThumb() {
    CustomGlideExtension.miniThumb(this);
    return this;
  }

  /**
   * @see CustomGlideExtension#miniThumb(RequestOptions)
   */
  public static GlideOptions miniThumbOf() {
    return new GlideOptions().miniThumb();
  }

  ...
}
```
现在可以使用我们自定义的方法了
```java
GlideApp.with(fragment)
   .load(url)
   .miniThumb(thumbnailSize)
   .into(imageView);
```
3.2.2 GlideType
以添加对Gif格式的支持为例，只是举例，实际上API中已经支持了。

在刚才的CustomGlideExtension类中加上：
```java
@GlideExtension
public class CustomGlideExtension {

    private static final RequestOptions DECODE_TYPE_GIF = GlideOptions.decodeTypeOf(GifDrawable.class).lock();

    @GlideType(GifDrawable.class)
    public static void asGIF(RequestBuilder<GifDrawable> requestBuilder) {
        requestBuilder
                .transition(new DrawableTransitionOptions())
                .apply(DECODE_TYPE_GIF);
    }
}
```
编译工程，打开build目录中的GlideRequests，可以看见自动生成了一个方法:
```java
public class GlideRequests extends RequestManager {
  /**
   * @see CustomGlideExtension#asGIF(RequestBuilder)
   */
  public GlideRequest<GifDrawable> asGIF() {
    GlideRequest<GifDrawable> requestBuilder = this.as(GifDrawable.class);
    CustomGlideExtension.asGIF(requestBuilder);
    return requestBuilder;
  }
}
```
现在我们就可以使用自己添加的类型了：
```java
GlideApp.with(fragment)
  .asGIF()
  .load(url)
  .into(imageView);
```
### 4. 占位符
占位符就是请求的图片没有加载出来的时候显示的默认图片。

Glide支持三种不同情况下的占位符：
* Placeholder 请求图片加载中
* Error 请求图片加载错误
* Fallback 请求url/model为空

设置占位符：
```java
GlideApp.with(fragment)
  .load(url)
  .placeholder(R.drawable.placeholder) 
  .error(new ColorDrawable(Color.RED))
  .fallback(new ColorDrawable(Color.GREY))
  .into(view);
```
原文中这里有一个显示优先级的图，这里就不贴了。

### 5. Options
#### 5.1 RequestOptions
Glide中的大多请求参数都可以通过RequestOptions类和apply()方法来设置。

Glide中请求参数主要有：
* Placeholders 占位符
* Transformations 变换
* Caching Strategies 缓存策略
* 组件特定参数：编码质量，解码参数等。
比如，要将图片的显示方式设为CenterCrop，可以这么做：
```java
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

Glide.with(fragment)
    .load(url)
    .apply(centerCropTransform(context))
    .into(imageView);
```
但是其实完全可以在layout文件中设置ImageView为android:scaleType="centerCrop"，Glide会自动根据这个属性设置图片的显示方式。

apply方法可以调用多次，但是如果两次apply存在冲突的设置，会以最后一次为准。

#### 5.2 TransitionOptions
TransitionOptions决定图片加载过程如何从占位符图片（或者之前的图片）过渡
* 淡入
* 交叉淡入
* 不过渡
```java
Glide.with(fragment)
    .load(url)
    .transition(DrawableTransitionOptions.withCrossFade())
    .into(view);
```
注意：
>TransitionOptions是和要加载的资源的类型绑定的，也就是说，如果请求一张位图(Bitmap),就需要使用BitmapTransitionOptions，<br>
 而不是DrawableTransitionOptions。因此，如果请求的是张位图，就需要用简单的淡入，而不能用 
 交叉淡入(DrawableTransitionOptions.withCrossFade())。 
 如果既不是Bitmap也不是Drawable可以使用GenericTransitionOptions

#### 5.3 RequestBuilder
作用：
1. 指定加载类型。asBitmap()、asGif()、asDrawable()、asFile()。
2. 指定要加载的url/model。
3. 指定要加载到哪个View。
4. 指定要应用的RequestOption
5. 指定要应用的TransitionOption
6. 指定要加载的缩略图
如何得到RequestBuilder：
```java
RequestBuilder<Drawable> requestBuilder = Glide.with(fragment);
```
默认得到一个Drawable RequestBuilder，如果要指定类型为Bitmap,可以这样写：
```java
RequestBuilder<Bitmap> requestBuilder = Glide.with(fragment).asBitmap();
```
应用RequestOptions和transitionOptions
```java
RequestBuilder<Drawable> requestBuilder = Glide.with(fragment);
requestBuilder.apply(requestOptions);
requestBuilder.transition(transitionOptions);
```
RequestBuilder也可以重复使用：
```java
RequestBuilder<Drawable> requestBuilder =
        Glide.with(fragment)
            .asDrawable()
            .apply(requestOptions);

for (int i = 0; i < numViews; i++) {
   ImageView view = viewGroup.getChildAt(i);
   String url = urls.get(i);
   requestBuilder.load(url).into(view);
}
```
### 6. Transformations
 Glide会自动读取ImageView的缩放类型，所以一般在layout文件指定scaleType即可。
 
 CenterCrop, CenterInside, CircleCrop, FitCenter, RoundedCorners
 
 Glide支持在java代码中设置这些缩放类型，有三种用法：
 #### 6.1 使用RequestOptions
 ```java
RequestOptions options = new RequestOptions();
options.centerCrop();

Glide.with(fragment)
    .load(url)
    .apply(options)
    .into(imageView);
```
#### 6.2 使用RequestOptions中的transform方法
```java
Glide.with(fragment)
    .load(url)
    .apply(RequestOptions.fitCenterTransform())
    .into(imageView);
```
#### 6.3 V4特性
```java
GlideApp.with(fragment)
  .load(url)
  .fitCenter()
  .into(imageView);
```
第三种方法最简便，推荐。

**多个变换**
```java
Glide.with(fragment)
  .load(url)
  .transform(new MultiTransformation(new FitCenter(), new YourCustomTransformation())
  .into(imageView);
```
### 7. Transitions(动画)
#### 7.1 普通动画
Glide中的过渡动画是指占位符到请求图片或缩略图到完整尺寸请求图片的动画。过渡动画只能针对单一请求，不能跨请求执行。

过渡动画执行时机：
* 1. 图片在磁盘缓存
* 2. 图片在本地
* 3. 图片在远程
如果图片在内存缓存上是不会执行过渡动画的。如果需要在内存缓存上加载动画，可以这样：
```java
GlideApp.with(this).load(R.drawable.img_default).listener(new RequestListener(){

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
        if (dataSource == DataSource.MEMORY_CACHE) {
            //当图片位于内存缓存时，glide默认不会加载动画
            imageView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in));
        }
        return false;
    }
}).fitCenter().transition(GenericTransitionOptions.with(R.anim.fade_in)).into(imageView);
```
通常的用法如下：
```java
Glide.with(fragment)
    .load(url)
    .transition(DrawableTransitionOptions.withCrossFade())
    .into(view);
```
有三种TransitionOptions：
1. GenericTransitionOptions 通用型
2. DrawableTransitionOptions
3. BitmapTransitionOptions

如果要使用自定义的动画，可以使用GenericTransitionOptions.with(int viewAnimationId)或者BitmapTransitionOptions.withCrossFade(int animationId, int duration)
或者DrawableTransitionOptions.withCrossFade(int animationId, int duration)。

出于性能考虑，最好不要在ListView,GridView,RecycleView中使用过渡动画,使用TransitionOptions.dontTransition()可以不加载动画，也可以使用dontAnimate不加载动画。
```java
GlideApp.with(mContext)
    .load(imgUrl)
    .placeholder(R.drawable.img_default)
    .dontAnimate()
    .into(holder.imageview);
```
#### 7.2 自定义过渡动画
1. 实现TransitionFactory 
2. 重写build() 
可以控制图片在内存缓存上是否执行动画

具体写法参考DrawableCrossFadeFactory，然后调用TransitionOptions的with(TransitionFactory transitionFactory)加载。
### 8. 基本配置
#### 8.1 配置内存缓存
Glide会自动合理分配内存缓存，但是也可以自己手动分配。

**方法一：通过MemorySizeCalculator设置**
```java
@GlideModule
public class CustomGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .build();
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
    }
}
```
setMemoryCacheScreens设置MemoryCache应该能够容纳的像素值的设备屏幕数，说白了就是缓存多少屏图片，默认值是2。

**方法二**
```java
@GlideModule
public class CustomGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int memoryCacheSizeBytes = 1024 * 1024 * 20; // 20mb
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
    }
}
```

**方法三**
```java
@GlideModule
public class YourAppGlideModule extends AppGlideModule {
  @Override
  public void applyOptions(Context context, GlideBuilder builder) {
    builder.setMemoryCache(new CustomGlideMemoryCache());
  }
}
```
自己实现MemoryCache接口。

清理内存缓存，在主线程调用：
```java
GlideApp.get(context).clearMemory();
```
在使用的时候，可以跳过内存缓存：
```java
 GlideApp.with(getActivity())
         .load(url)
         .skipMemoryCache(true)
         .dontAnimate()
         .centerCrop()
         .into(imageView);
```
#### 8.2 磁盘缓存
Glide使用DiskLruCacheWrapper作为默认的磁盘缓存，默认大小是250M,缓存文件放在APP的缓存文件夹下。
```java
@GlideModule
public class CustomGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int diskCacheSizeBytes = 1024 * 1024 * 100; // 100 MB
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));
//        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "cacheFolderName", diskCacheSizeBytes));
//        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context));
    }
}
```
用法如上，可以指定缓存在内部存储或外部存储，也可以指定缓存大小和文件夹。

**自定义磁盘缓存**
```java
@GlideModule
public class CustomGlideModule extends AppGlideModule {
  @Override
  public void applyOptions(Context context, GlideBuilder builder) {
    builder.setDiskCache(new DiskCache.Factory() {
        @Override
        public DiskCache build() {
          return new YourAppCustomDiskCache();
        }
    });
  }
}
```
自己实现DiskCache接口。

清理磁盘缓存，在子线程调用：
```java
GlideApp.get(context).clearDiskCache();
```
加载图片时设置磁盘缓存策略：
```java
GlideApp.with(getActivity())
         .load(url)
         .diskCacheStrategy(DiskCacheStrategy.ALL)
         .dontAnimate()
         .centerCrop()
         .into(imageView);
```

默认的策略是DiskCacheStrategy.AUTOMATIC 

DiskCacheStrategy有五个常量：
* DiskCacheStrategy.ALL 使用DATA和RESOURCE缓存远程数据，仅使用RESOURCE来缓存本地数据。
* DiskCacheStrategy.NONE 不使用磁盘缓存
* DiskCacheStrategy.DATA 在资源解码前就将原始数据写入磁盘缓存
* DiskCacheStrategy.RESOURCE 在资源解码后将数据写入磁盘缓存，即经过缩放等转换后的图片资源。
* DiskCacheStrategy.AUTOMATIC 根据原始图片数据和资源编码策略来自动选择磁盘缓存策略。

#### 8.3 禁止解析Manifest文件
主要针对V3升级到V4的用户，可以提升初始化速度，避免一些潜在的错误
```java
@GlideModule
public class CustomGlideModule extends AppGlideModule {
  @Override
  public boolean isManifestParsingEnabled() {
    return false;
  }
}
```
#### 8.4 View的尺寸
Glide对ImageView的width和height属性是这样解析的：
* 如果width和height都大于0，则使用layout中的尺寸。
* 如果width和height都是WRAP_CONTENT，则使用屏幕尺寸。
* 如果width和height中至少有一个值<=0并且不是WRAP_CONTENT，那么就会在布局的时候添加一个OnPreDrawListener监听ImageView的尺寸

Glide对WRAP_CONTENT的支持并不好，所以尽量不要用。

那么如何在运行时修改ImageView尺寸呢？

**方法一 继承ImageViewTarget**

这里指定的View的类型是ImageView,资源类型是Bitmap，可根据需要修改，onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition)方法中可以通过bitmap获取图片的尺寸。
```java
public class CustomImageViewTarget extends ImageViewTarget<Bitmap> {

        private int width, height;

        public CustomImageViewTarget(ImageView view) {
            super(view);
        }

        public CustomImageViewTarget(ImageView view, int width, int height) {
            super(view);
            this.width = width;
            this.height = height;
        }

        @Override
        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
            super.onResourceReady(bitmap,transition);
        }

        @Override
        protected void setResource(@Nullable Bitmap resource) {
            view.setImageBitmap(resource);
        }

        @Override
        public void getSize(SizeReadyCallback cb) {
            if (width > 0 && height > 0) {
                cb.onSizeReady(width, height);
                return;
            }
            super.getSize(cb);
        }
    }
```
使用：
```java
GlideApp.with(context)
        .asBitmap()
        .load(url)
        .dontAnimate()
        .placeholder(R.drawable.img_default)
        .into(new CustomImageViewTarget(imageview, 300, 300));
```
**方法二 使用override**
```java
GlideApp.with(mContext)
    .load(url)
    .override(width,height)
    .into(view);
```
**获取bitmap**
如果只想用Glide解析url获取一个bitmap，然后自己对其进行处理，可以使用SimpleTarget<Z>
```java
/**
   * Constructor for the target that uses {@link Target#SIZE_ORIGINAL} as the target width and
   * height.
   */
  public SimpleTarget() {
    this(SIZE_ORIGINAL, SIZE_ORIGINAL);
  }

  /**
   * Constructor for the target that takes the desired dimensions of the decoded and/or transformed
   * resource.
   *
   * @param width  The width in pixels of the desired resource.
   * @param height The height in pixels of the desired resource.
   */
  public SimpleTarget(int width, int height) {
    this.width = width;
    this.height = height;
  }
```
SimpleTarget也可以指定宽和高，用法示例：
```java
Glide.with(itemView.getContext()).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
    @Override
    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

    }
});
```




















 























