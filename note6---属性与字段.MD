>属性和字段

**声明属性**

Kotlin的类可以有属性。 属性可以用关键字var 声明为可变的，否则使用只读关键字val。
```java
class Address {
    var name: String = ……
    var street: String = ……
    var city: String = ……
    var state: String? = ……
    var zip: String = ……
}
```
要使用一个属性，只要用名称引用它即可，就像 Java 中的字段：
```java
fun copyAddress(address: Address): Address {
    val result = Address() // Kotlin 中没有“new”关键字
    result.name = address.name // 将调用访问器
    result.street = address.street
    // ……
    return result
}
```
**Getters 与 Setters**

声明一个属性的完整语法是
```java
var <propertyName>[: <PropertyType>] [= <property_initializer>]
    [<getter>]
    [<setter>]
```
其初始器（initializer）、getter 和 setter 都是可选的。属性类型如果可以从初始器 （或者从其 getter 返回值，如下文所示）中推断出来，也可以省略。

例如：
```java
var allByDefault: Int? // 错误：需要显式初始化器，隐含默认 getter 和 setter
var initialized = 1 // 类型 Int、默认 getter 和 setter
```
一个只读属性的语法和一个可变的属性的语法有两方面的不同：1、只读属性的用 val开始代替var 2、只读属性不允许 setter
```java
val simple: Int? // 类型 Int、默认 getter、必须在构造函数中初始化
val inferredType = 1 // 类型 Int 、默认 getter
```
可以编写自定义的访问器，非常像普通函数，最好在属性声明内部。这里有一个自定义 getter 的例子:
```java
val isEmpty: Boolean
    get() = this.size == 0
```
一个自定义setter的例子：
```java
var stringRepresentation: String
    get() = this.toString()
    set(value) {
        setDataFromString(value) // 解析字符串并赋值给其他属性
    }
```
按照惯例，setter 参数的名称是 value，但是如果你喜欢你可以选择一个不同的名称。

自 Kotlin 1.1 起，如果可以从 getter 推断出属性类型，则可以省略它：
```java
val isEmpty get() = this.size == 0  // 具有类型 Boolean
```
如果需要改变一个访问器的可见性或者对其注解，但是不需要改变默认的实现， 你可以定义访问器而不定义其实现:
```java
var setterVisibility: String = "abc"
    private set // 此 setter 是私有的并且有默认实现

var setterWithAnnotation: Any? = null
    @Inject set // 用 Inject 注解此 setter
```
**编译期常量**

已知值的属性可以使用 const 修饰符标记为**编译期常量**。 这些属性需要满足以下要求：

* 位于顶层或者是 object 的一个成员
* 用 String 或原生类型 值初始化
* 没有自定义 getter

这些属性可以用在注解中：
```java
const val SUBSYSTEM_DEPRECATED: String = "This subsystem is deprecated"

@Deprecated(SUBSYSTEM_DEPRECATED) fun foo() { …… }
```

**延迟初始化属性与变量**

一般地，属性声明为非空类型必须在构造函数中初始化。 然而，这经常不方便。例如：属性可以通过依赖注入来初始化， 或者在单元测试的 setup 方法中初始化。
这种情况下，不能在构造函数内提供一个非空初始器。 但是仍然想在类体中引用该属性时避免空检查。

为了处理这种情况，可以用 lateinit 修饰符标记该属性：
```java
public class MyTest {
    lateinit var subject: TestSubject

    @SetUp fun setup() {
        subject = TestSubject()
    }

    @Test fun test() {
        subject.method()  // 直接引用
    }
}
```
该修饰符只能用于在类体中的属性（不是在主构造函数中声明的 var 属性，并且仅当该属性没有自定义 getter 或 setter 时），
而自 Kotlin 1.2 起，也用于顶层属性与局部变量。该属性或变量必须为非空类型，并且不能是原生类型。

在初始化前访问一个 lateinit 属性会抛出一个特定异常，该异常明确标识该属性被访问及它没有初始化的事实。

要检测一个 lateinit var 是否已经初始化过，要在该属性的引用上使用 .isInitialized：
```java
if (foo::bar.isInitialized) {
    println(foo.bar)
}
```













