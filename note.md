笔记
>记录一下在这个项目中遇到的问题
### 一 . 周末在家，想把这个项目clone下来，coding一下，结果发现clone速度巨慢，上网搜了好几种方案，最中解决了，记录一下
#### 方案一
在hosts文件中添加如下配置，参照文章[GitHub clone 速度过慢的问题](https://blog.csdn.net/sinat_38843093/article/details/79716804)：
```text
151.101.112.249 http://global-ssl.fastly.Net
192.30.253.112 http://github.com
```
但是发现好像没有解决问题
#### 方案二 这个需要我们有ss。
参照文章[提高Github Clone速度](https://www.jianshu.com/p/5e74b1042b70)
使用git内置代理，直接走系统中运行的代理工具中转，比如，你的 SS 本地端口是 1080（一般port均为1080），那么可以如下方式走代理：
```text
git config --global http.proxy socks5://127.0.0.1:1080
git config --global https.proxy socks5://127.0.0.1:1080
```
此时，我又重新执行了一下clone命令，速度很快，溜的飞起。

### 二 . 看到项目中使用了com.android.support:support-annotations这个依赖，以前没有注意这个依赖的作用，这次了解一下
>学习目标文章[使用注解(Annotations)改进代码检验](https://www.jianshu.com/p/73110a4b70f8)，这是一篇对谷歌官方文档的翻译文章<br>

使用Lint等工具进行代码检查可以帮助我们查找问题改善代码质量，但是这些工具只能进行推断并不能真正进行检查。<br>

因为Android使用一个int类型作为ID，表示字符串，图片，颜色和其他类型的资源，这时如果你在一个需要使用颜色资源ID的地方使用了一个字符串资源ID，代码检查工具是不会报错的，但是你的应用仍然会发生绘制错误或者是不能运行。<br>


注解(Annotations)就是让你给代码检查工具Lint等一个提示，让它们注意检查这些微妙的代码问题。这些注解就好像元数据标签(mentadata tags)一样和变量，参数，返回值绑定在一起，检查它们是否合法。<br>
当运行代码检查工具的时候，注解就会帮助你检查比如空指针，资源类型冲突等问题。<br>

Android通过Annotations Support Library支持多种注解，我们可以通过android.support.annotation包获取这些注解。<br>

#### 一 . 给项目添加注解功能

点击"project structure",在弹出的框中我们就可以为我们的项目添加support-annotations这个依赖了。<br>

注意：如果我们使用了appcompat库，那我们就不需要添加 support-annotations 依赖了，因为 appcompat 库已经添加过这个依赖了，此时我们可以直接使用annotations。<br>

#### 二 . 运行代码检查功能
在AS的工具栏中选择Analyze > Inspect Code ，会启动代码检查功能，包括确认注解的有效性和Lint自动检查两部分。Android Studio会显示冲突信息，标记出在代码中潜藏的问题，并且给出相应的解决和建议。<br>

注意：即使因为注解问题产生了警告，这些警告也不会阻止代码的编译。<br>

#### 三 . 比较常用的一些注解

**1 . 空值注解（Nullness Annotations）** <br>

@Nullable 注解表示变量，参数，返回值可以为null, @NonNull 注解表示变量，参数，返回值不能为null。<br>

下面的例子使用了@NonNull注解标记了context和attrs两个参数，表示要检查传入的这两个参数的值不能为null；用@NonNull标记了这个方法，表示同时要检查onCreateView(...)方法的返回值不能为null<br>

```java
import android.support.annotation.NonNull;
...

/** Add support for inflating the <fragment> tag. **/
@NonNull
@Override
public View onCreateView(String name, @NonNull Context context,
  @NonNull AttributeSet attrs) {
  ...
  }
...

```
**2 . 空值分析（Nullability Analysis）这个好像没怎么使用过**<br>

Android Studio支持空值分析(nullability analysis)去自动推断并且在代码中插入空值注解(nullness annotations)。空值分析会扫描所有方法层次结构中的调用关系，去检查：<br>

* 调用的方法可以返回空
* 调用的方法不能返回空
* 变量、字段、局部变量、参数等可以为空
* 变量、字段、局部变量、参数等不能为空

分析完成后会在检查的位置自动插入适当的空值注解。<br>

在Android Studio中选择Analyze > Infer Nullity开启空值分析。Android Studio会在检测的地方插入Android版本的 @Nullable 和 @NonNull 注解。

**3 . 资源注解（Resource Annotations）**<br>

确认资源类型非常有用，因为Android对于资源的引用，比如drawable，string，都是用整数类型进行传递的。如果代码期待接收特定的资源类型，比如Drawable，就可以把该资源引用的int值传递过去。<br>
但是实际上也有可能错传了一个R.string资源的int值过去。所以确认资源的类型很有用。<br>

我们可以添加了一个@StringRes注解，去检查参数的是不是一个R.string类型的资源：<br>
```java
public abstract void setTitle(@StringRes int resId) { … }
```
如果参数不是一个R.string类型的资源，那么在代码检查期间就会产生一个警告。<br>

其他[@DrawableRes][drawableres],[@DimenRes][dimenres],[@ColorRes][colorres],[@InterpolatorRes][interpolatorres]等资源注解都可以按这种格式使用。<br>
如果你的参数支持多个资源格式，你可以对其添加多个资源注解。[@AnyRes][anyres]注解表示该参数可以是任意一种R资源格式。<br>

如果使用了[@ColorRes][colorres]指定了一个资源类型的参数，但是用RRGGBB 或 AARRGGBB表示的颜色整数值却并不会被认可。<br>
同样用[@ColorInt][colorint]指定的资源也只认可能被解析的颜色整数值。编译工具会标记出这些不正确的代码。<br>

**4 . 线程注解（Thread Annotations）**<br>

线程注解用来检查方法是不是在一个特定的线程中被调用。支持以下注解：<br>
 * @MainThread
 * @UiThread
 * @WorkerThread
 * @BinderThread
 * @AnyThread
 >**注意：**编译工具将@MainThread和@UiThread看成是可互换的，所以你可以在标注为@MainThread的方法中调用@UiThread的方法，反之亦然。
 >但是，在不同线程上有多个视图的系统应用程序的情况下，UI线程可能不等同于主线程。因此，您应该使用@UiThread注解与应用程序视图层次结构相关的方法，并使用@MainThread注解仅与应用程序生命周期相关联的方法。<br>
 
 如果类中的所有方法都共享相同的线程，则可以向类添加单个线程注解，以验证类中的所有方法是否都从同一类型的线程中被调用。<br>
 
 线程注解的一个常见用法是验证AsyncTask类中被覆盖的方法，因为此类在后台执行，并仅在UI线程上发布结果。<br>
 
 **5 . 值约束注解（Value Constraint Annotations）**<br>
 
 使用@IntRange，@FloatRange和@Size注解验证传进来的参数的值。当用户可能输入不在范围内的参数时，@IntRange和@FloatRange非常有用。
 
 **6 . 权限注解（Permission Annotations）**<br>
 
 使用@RequiresPermission注解来验证方法调用者的权限。要从列表中检查单个权限的有效权限，请使用anyOf属性。 <br>
 要检查一组权限，请使用allOf属性。以下示例注解setWallpaper()方法，以确保方法的调用者具有permission.SET_WALLPAPERS权限：
 ```java
@RequiresPermission(Manifest.permission.SET_WALLPAPER)
public abstract void setWallpaper(Bitmap bitmap) throws IOException;
```
**7 . 返回值注解（Return Value Annotations）**<br>

使用@CheckResult注解来验证方法的结果或返回值实际上是否被使用。在容易混淆的方法结果上添加@CheckResult注解用以区分，而不是对每个非void方法都进行注解。<br>

**8 . 类型定义注解（Typedef Annotations）**<br>

可以使用@IntDef和@StringDef注解，来创建一个整数或者字符串集合的枚举类型。Typedef注解确保特定参数，返回值或字段只能使用特定的一组常量。它们还可以使代码拥有自动补全功能。<br>

@Typedef注解使用@interface声明新的枚举注解类型，然后使用@IntDef或@StringDef以及@Retention注解类标注它，并且它们三个是定义一个枚举类所必须的。<br>
@Retention(RetentionPolicy.SOURCE)注解告诉编译器不要将被标记了的枚举类型数据存储在.class文件中。<br>

以下示例说明了创建这种注解的步骤，以确保作为方法参数传递的值是一组已经定义好的常量集合中的一个：<br>
```java
import android.support.annotation.IntDef;
...
public abstract class ActionBar {
...
// Define the list of accepted constants and declare the NavigationMode annotation
@Retention(RetentionPolicy.SOURCE)
@IntDef({NAVIGATION_MODE_STANDARD, NAVIGATION_MODE_LIST, NAVIGATION_MODE_TABS})
public @interface NavigationMode {}

// Declare the constants
public static final int NAVIGATION_MODE_STANDARD = 0;
public static final int NAVIGATION_MODE_LIST = 1;
public static final int NAVIGATION_MODE_TABS = 2;

// Decorate the target methods with the annotation
@NavigationMode
public abstract int getNavigationMode();

// Attach the annotation
public abstract void setNavigationMode(@NavigationMode int mode);

```

如果mode参数的值不是已经定义的(NAVIGATION_MODE_STANDARD, NAVIGATION_MODE_LIST, or NAVIGATION_MODE_TABS)其中的一个，编译时就会受到警告。<br>







 
 
 
     

























