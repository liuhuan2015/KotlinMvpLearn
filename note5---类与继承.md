>类与继承

### 1 . 类与继承
#### 1.1 类
 Kotlin中使用关键字class声明类
 ```java
class Invoice { ... }
```
类声明由类名、类头（指定其类型参数、主构造函数等）以及由花括号包围的类体构成。类头与类体都是可选的； 如果一个类没有类体，可以省略花括号。
```java
class Empty
```

**构造函数**

在 Kotlin 中的一个类可以有一个**主构造函数**以及一个或多个**次构造函数**。主构造函数是类头的一部分：它跟在类名（与可选的类型参数）后。
```java
class Person constructor(firstName: String) { ... }
```
如果主构造函数没有任何注解或者可见性修饰符，可以省略这个 constructor 关键字。
```java
class Person(firstName: String) { ... }
```
主构造函数不能包含任何的代码。初始化的代码可以放到以 init 关键字作为前缀的**初始化块（initializer blocks）** 中。

在实例初始化期间，初始化块按照它们出现在类体中的顺序执行，与属性初始化器交织在一起：
```java
class InitOrderDemo(name: String) {
    val firstProperty = "First property: $name".also(::println)

    init {
        println("First initializer block that prints ${name}")
    }

    val secondProperty = "Second property: ${name.length}".also(::println)

    init {
        println("Second initializer block that prints ${name.length}")
    }
}
```
注意，主构造的参数可以在初始化块中使用。它们也可以在类体内声明的属性初始化器中使用：
```java
class Customer(name: String) {
    val customerKey = name.toUpperCase()
}
```
事实上，声明属性以及从主构造函数初始化属性，Kotlin 有简洁的语法：
```java
class Person(val firstName: String, val lastName: String, var age: Int) { …… }
```
与普通属性一样，主构造函数中声明的属性可以是可变的（var）或只读的（val）。

如果构造函数有注解或可见性修饰符，这个 constructor 关键字是必需的，并且这些修饰符在它前面：
```java
class Customer public @Inject constructor(name: String) { …… }
```
**次构造函数**

类也可以声明前缀有 constructor的次构造函数：
```java
class Person {
    constructor(parent: Person) {
        parent.children.add(this)
    }
}
```
如果类有一个主构造函数，那么每个次构造函数都需要委托给主构造函数， 可以直接委托或者通过别的次构造函数间接委托。<br>
委托到同一个类的另一个构造函数用 this 关键字即可：
```java
class Person(val name: String) {
    constructor(name: String, parent: Person) : this(name) {
        parent.children.add(this)
    }
}
```
请注意，初始化块中的代码实际上会成为主构造函数的一部分。委托给主构造函数会作为次构造函数的第一条语句，<br>
因此所有初始化块中的代码都会在次构造函数体之前执行。即使该类没有主构造函数，这种委托仍会隐式发生，并且仍会执行初始化块：
```java
class Constructors {
    init {
        println("Init block")
    }

    constructor(i: Int) {
        println("Constructor")
    }
}
```
如果一个非抽象类没有声明任何（主或次）构造函数，它会有一个生成的不带参数的主构造函数。构造函数的可见性是 public。<br>
如果不希望类有一个公有构造函数，需要声明一个带有非默认可见性的空的主构造函数：
```java
class DontCreateMe private constructor () { ... }
```
注意：在 JVM 上，如果主构造函数的所有的参数都有默认值，编译器会生成 一个额外的无参构造函数，它将使用默认值。这使得 Kotlin 更易于使用像 Jackson 或者 JPA 这样的通过无参构造函数创建类的实例的库。
```java
class Customer(val customerName: String = "")
```
**创建类的实例**

要创建一个类的实例，我们就像普通函数一样调用构造函数：
```java
val invoice = Invoice()

val customer = Customer("Joe Smith")
```
注意：Kotlin没有 new 关键字

**类成员**

类可以包含：

构造函数与初始化块

函数

属性

嵌套类与内部类

对象声明

**继承**

在 Kotlin 中所有类都有一个共同的超类 Any，这对于没有超类型声明的类是默认超类：
```java
class Example // 从 Any 隐式继承
```
注意：Any 并不是 java.lang.Object；尤其是，它除了 equals()、hashCode() 与 toString() 外没有任何成员。 

要声明一个显式的超类型，我们把类型放到类头的冒号之后：
```java
open class Base(p: Int)

class Derived(p: Int) : Base(p)
```
类上的 open 标注与 Java 中 final 相反，它允许其他类从这个类继承。默认情况下，在 Kotlin 中所有的类都是 final。<br>
对应于《Effective Java》第三版书中的第 19 条：要么为继承而设计，并提供文档说明，要么就禁止继承。

如果派生类有一个主构造函数，其基类型可以（并且必须） 用基类的主构造函数参数就地初始化。

如果类没有主构造函数，那么每个次构造函数必须使用 super 关键字初始化其基类型，或委托给另一个构造函数做到这一点。<br>
注意，在这种情况下，不同的次构造函数可以调用基类型的不同的构造函数：
```java
class MyView : View {
    constructor(ctx: Context) : super(ctx)

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)
}
```
**覆盖方法**

我们之前提到过，Kotlin 力求清晰显式。与 Java 不同，Kotlin 需要显式标注可覆盖的成员（我们称之为开放）以及覆盖后的成员：
```java
open class Base {
    open fun v() { ... }
    fun nv() { ... }
}
class Derived() : Base() {
    override fun v() { ... }
}
```
Derived.v() 函数上必须加上 override标注。如果没写，编译器将会报错。 如果函数没有标注 open 如 Base.nv()，
则子类中不允许定义相同签名的函数， 不论加不加 override。在一个 final 类中（没有用 open 标注的类），开放成员是禁止的。

标记为 override 的成员本身是开放的，也就是说，它可以在子类中覆盖。如果你想禁止再次覆盖，使用 final 关键字：
```java
open class AnotherDerived() : Base() {
    final override fun v() { ... }
}
```
**覆盖属性**

属性覆盖与方法覆盖类似；在超类中声明然后在派生类中重新声明的属性必须以 override 开头，并且它们必须具有兼容的类型。
每个声明的属性可以由具有初始化器的属性或者具有 getter 方法的属性覆盖。
```java
open class Foo {
    open val x: Int get() { …… }
}

class Bar1 : Foo() {
    override val x: Int = ……
}
```
可以用一个 var 属性覆盖一个 val 属性，但反之则不行。因为一个 val 属性本质上声明了一个 getter 方法，
而将其覆盖为 var 只是在子类中额外声明一个 setter 方法。

注意，可以在主构造函数中使用 override 关键字作为属性声明的一部分。
```java
interface Foo {
    val count: Int
}

class Bar1(override val count: Int) : Foo

class Bar2 : Foo {
    override var count: Int = 0
}
```
**派生类初始化顺序**

在构造派生类的新实例的过程中，第一步完成其基类的初始化（在之前只有对基类构造函数参数的求值），因此发生在派生类的初始化逻辑运行之前。
```java
open class Base(val name: String) {

    init { println("Initializing Base") }

    open val size: Int = 
    name.length.also { println("Initializing size in Base: $it") }
}

class Derived(
    name: String,
    val lastName: String
) : Base(name.capitalize().also { println("Argument for Base: $it") }) {

    init { println("Initializing Derived") }

    override val size: Int =
    (super.size + lastName.length).also { println("Initializing size in Derived: $it") }
}

fun main(args: Array<String>) {
    println("Constructing Derived(\"hello\", \"world\")")
    val d = Derived("hello", "world")
}
```
运行结果：
```java
Constructing Derived("hello", "world")
Argument for Base: Hello
Initializing Base
Initializing size in Base: 5
Initializing Derived
Initializing size in Derived: 10
```
这意味着，基类构造函数执行时，派生类中声明或覆盖的属性都还没有初始化。如果在基类初始化逻辑中（直接或通过另一个覆盖的 open 成员的实现间接）使用了任何一个这种属性，
那么都可能导致不正确的行为或运行时故障。**设计一个基类时，应该避免在构造函数、属性初始化器以及 init 块中使用 open 成员**。

**调用超类实现**

派生类中的代码可以使用 super 关键字调用其超类的函数与属性访问器的实现：
```java
open class Foo {
    open fun f() { println("Foo.f()") }
    open val x: Int get() = 1
}

class Bar : Foo() {
    override fun f() { 
        super.f()
        println("Bar.f()") 
    }

    override val x: Int get() = super.x + 1
}
```
在一个内部类中访问外部类的超类，可以通过由外部类名限定的 super 关键字来实现：super@Outer：
```java
class Bar : Foo() {
    override fun f() { /* …… */ }
    override val x: Int get() = 0

    inner class Baz {
        fun g() {
            super@Bar.f() // 调用 Foo 实现的 f()
            println(super@Bar.x) // 使用 Foo 实现的 x 的 getter
        }
    }
}
```

**覆盖规则**

在 Kotlin 中，实现继承由下述规则规定：如果一个类从它的直接超类继承相同成员的多个实现， 它必须覆盖这个成员并提供其自己的实现（也许用继承来的其中之一）。
为了表示采用从哪个超类型继承的实现，我们使用由尖括号中超类型名限定的 super，如 super<Base>：
```java
open class A {
    open fun f() { print("A") }
    fun a() { print("a") }
}

interface B {
    fun f() { print("B") } // 接口成员默认就是“open”的
    fun b() { print("b") }
}

class C() : A(), B {
    // 编译器要求覆盖 f()：
    override fun f() {
        super<A>.f() // 调用 A.f()
        super<B>.f() // 调用 B.f()
    }
}
```
同时继承 A 与 B 没问题，并且 a() 与 b() 也没问题因为 C 只继承了每个函数的一个实现。 但是 f() 由 C 继承了两个实现，所以我们必须在 C 中覆盖 f() 并且提供我们自己的实现来消除歧义。

**抽象类**

类以及其中的某些成员可以声明为abstract。抽象成员在本类中可以不用实现。 需要注意的是，我们并不需要用 open 标注一个抽象类或者函数——因为这不言而喻。

可以用一个抽象成员覆盖一个非抽象的开放成员（这和Java不同）
```java
open class Base {
    open fun f() {}
}

abstract class Derived : Base() {
    override abstract fun f()
}
```

**伴生对象**

与 Java 或 C# 不同，在 Kotlin 中类没有静态方法。在大多数情况下，它建议简单地使用包级函数。

如果需要写一个可以无需用一个类的实例来调用、但是需要访问的类内部的函数（例如：工厂方法），可以把它写成该类内部对象声明中的一员。

更具体地讲，如果在类内声明了一个**伴生对象**， 就可以使用像在 Java/C# 中调用静态方法相同的语法来调用其成员，只使用类名作为限定符。

















