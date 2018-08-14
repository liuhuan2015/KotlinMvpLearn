>Kotlin学习笔记-------编码规范<br>
### 1 . 属性名
常量名称（标有 const 的属性，或者保存不可变数据的没有自定义 get 函数的顶层/对象 val 属性）应该使用大写、下划线分隔的名称：
```java
const val MAX_COUNT = 8
val USER_NAME_FIELD = "UserName"
```
保存带有行为的对象或者可变数据的顶层/对象属性的名称应该使用常规驼峰名称：
```java
val mutableCollection: MutableSet<String> = HashSet()
```
保存单例对象引用的属性的名称可以使用与 object 声明相同的命名风格：
```java
val PersonComparator: Comparator<Person> = ...
```
对于枚举常量，可以使用大写、下划线分隔的名称 （enum class Color { RED, GREEN }）也可使用以大写字母开头的常规驼峰名称，具体取决于用途。
### 2 . 幕后属性的名称
如果一个类有两个概念上相同的属性，一个是公共 API 的一部分，另一个是实现细节，那么使用下划线作为私有属性名称的前缀：
```java
class C {
    private val _elementList = mutableListOf<Element>()

    val elementList: List<Element>
    get() = _elementList
}
```
### 3 . 格式化
在大多数情况下，Kotlin 遵循 Java 编码规范。<br>

**注意**：在 Kotlin 中，分号是可选的，因此换行很重要。语言设计采用 Java 风格的花括号格式，如果尝试使用不同的格式化风格，那么可能会遇到意外的行为。
### 4 . 类头格式化
具有少数主构造函数参数的类可以写成一行：
```java
class Person(id: Int, name: String)
```
具有较长类头的类应该格式化，以使每个主构造函数参数都在带有缩进的独立的行中。 另外，右括号应该位于一个新行上。<br>
如果使用了继承，那么超类的构造函数调用或者所实现接口的列表应该与右括号位于同一行：
```java
class Person(
    id: Int,
    name: String,
    surname: String
) : Human(id, name) { …… }
```
对于多个接口，应该将超类构造函数调用放在首位，然后将每个接口应放在不同的行中：
```java
class Person(
    id: Int,
    name: String,
    surname: String
) : Human(id, name),
    KotlinMaker { …… }
```
对于具有很长超类型列表的类，在冒号后面换行，并垂直对齐所有超类型名：
```java
class MyFavouriteVeryLongClassHolder :
    MyLongHolder<MyFavouriteVeryLongClass>(),
    SomeOtherInterface,
    AndAnotherOne {

    fun foo() { ... }
}
```
为了将类头与类体分隔清楚，当类头很长时，可以在类头后放一空行 （如上例所示）或者将左花括号放在独立行上：
```java
class MyFavouriteVeryLongClassHolder :
    MyLongHolder<MyFavouriteVeryLongClass>(),
    SomeOtherInterface,
    AndAnotherOne {

    fun foo() { ... }
}
```
### 5 . 修饰符
如果一个声明有多个修饰符，请始终按照以下顺序安放：
```java
public / protected / private / internal
expect / actual
final / open / abstract / sealed / const
external
override
lateinit
tailrec
vararg
suspend
inner
enum / annotation
companion
inline
infix
operator
data
```
将所有注解放在修饰符前：
```java
@Named("Foo")
private val foo: Foo
```
除非是在编写库，否则请省略多余的修饰符（例如 public）。
### 6 . 函数格式化
如果函数签名不适合单行，请使用以下语法：
```java
fun longMethodName(
    argument: ArgumentType = defaultValue,
    argument2: AnotherArgumentType
): ReturnType {
    // 函数体
}
```

函数参数使用常规缩进（4个空格），和构造函数参数一致。<br>

对于由单个表达式构成的函数体，优先使用表达式形式。<br>
```java
fun foo(): Int {     // 不良
    return 1 
}

fun foo() = 1        // 良好
```
### 7 . 表达式函数体格式化
如果函数的表达式函数体与函数声明不适合放在同一行，那么将 = 留在第一行。 将表达式函数体缩进 4 个空格。
```java
fun f(x: String) =
    x.length
```
### 8 . 属性格式化
对于非常简单的只读属性，一般使用单行格式：
```java
val isEmpty: Boolean get() = size == 0
```
对于更复杂的属性，总是将 get 与 set 关键字放在不同的行上：
```java
val foo: String
    get() { …… }
```
对于具有初始化器的属性，如果初始化器很长，那么在等号后增加一个换行并将初始化器缩进四个空格：
```java
private val defaultCharset: Charset? =
    EncodingRegistry.getInstance().getDefaultCharsetForPropertiesFiles(file)
```
### 9 . 方法调用格式化
在较长参数列表的左括号后添加一个换行符。按 4 个空格缩进参数。 将密切相关的多个参数分在同一行。
```java
drawSquare(
    x = 10, y = 10,
    width = 100, height = 100,
    fill = true
)
```
### 10 . 链式调用换行
当对链式调用换行时，将 . 字符或者 ?. 操作符放在下一行，并带有单倍缩进：
```java
val anchor = owner
    ?.firstChild!!
    .siblings(forward = true)
    .dropWhile { it is PsiComment || it is PsiWhiteSpace }
```
### 11 . 避免重复结构
一般来说，如果 Kotlin 中的某种语法结构是可选的并且被 IDE 高亮为冗余的，那么应该在代码中省略之。为了清楚起见，不要在代码中保留不必要的语法元素 。
**Unit**<br>
如果函数返回Unit，那么应该省略返回类型：
```java
fun foo() { // 这里省略了“: Unit”

}
```
**分号**<br>
尽可能省略分号

**字符串模板**<br>
将简单变量传入到字符串模版中时不要使用花括号。只有用到更长表达式时才使用花括号。
```java
println("$name has ${children.size} children")
```
### 12 . 语言特性的惯用法





































