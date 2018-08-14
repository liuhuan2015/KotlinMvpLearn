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
### 12 . 语言特性的习惯用法
#### 12.1 不可变性
优先使用不可变（而不是可变）数据。初始化后未修改的局部变量与属性，总是将其声明为 val 而不是 var。<br>

总是使用不可变集合接口（Collection, List, Set, Map）来声明无需改变的集合。使用工厂函数创建集合实例时，尽可能选用返回不可变集合类型的函数：<br>
```java
// 不良：使用可变集合类型作为无需改变的值
fun validateValue(actualValue: String, allowedValues: HashSet<String>) { …… }

// 良好：使用不可变集合类型
fun validateValue(actualValue: String, allowedValues: Set<String>) { …… }

// 不良：arrayListOf() 返回 ArrayList<T>，这是一个可变集合类型
val allowedValues = arrayListOf("a", "b", "c")

// 良好：listOf() 返回 List<T>
val allowedValues = listOf("a", "b", "c")
```
#### 12.2 默认参数值
优先声明带有默认参数的函数而不是声明重载函数
```java
// 不良
fun foo() = foo("a")
fun foo(a: String) { …… }

// 良好
fun foo(a: String = "a") { …… }
```
#### 12.3 类型别名
如果有一个在代码库中多次用到的函数类型或者带有类型参数的函数类型，那么最好为它定义一个类型别名：
```java
typealias MouseClickHandler = (Any, MouseEvent) -> Unit
typealias PersonIndex = Map<String, Person>
```
#### 12.4 Lambda表达式参数
在简短、非嵌套的 lambda 表达式中建议使用 it 用法而不是显式声明参数。而在有参数的嵌套 lambda 表达式中，始终应该显式声明参数。

#### 12.5 在 lambda 表达式中返回
避免在 lambda 表达式中使用多个返回的标签。请考虑重新组织这样的 lambda 表达式使其只有单一退出点。 如果这无法做到或者不够清晰，<br>
请考虑将 lambda 表达式转换为匿名函数。<br>

不要在 lambda 表达式的最后一条语句中使用返回的标签。
#### 12.6 命名参数
当一个方法接受多个相同的原生类型参数或者多个 Boolean 类型参数时，请使用命名参数语法， 除非在上下文中的所有参数的含义都已绝对清楚。
```java
drawSquare(x = 10, y = 10, width = 100, height = 100, fill = true)
```
#### 12.7 使用条件语句
优先使用 try 、 if 与 when 的表达形式。例如：
```java
return if (x) foo() else bar()

return when(x) {
    0 -> "zero"
    else -> "nonzero"
}
```
优先使用上述代码而不是：
```java
if (x)
    return foo()
else
    return bar()
    
when(x) {
    0 -> return "zero"
    else -> return "nonzero"
}
```
#### 12.8 if 还是 when
二元条件优先使用 if 而不是 when。不要使用
```java
when (x) {
    null -> ……
    else -> ……
}
```
而应使用 if (x == null) …… else ……<br>

如果有三个或多个选项时优先使用when。
#### 12.9 在条件中使用可空的 Boolean 值
如果需要在条件语句中用到可空的 Boolean, 使用 if (value == true) 或 if (value == false) 检测。
#### 12.10 使用循环
优先使用高阶函数（filter、map 等）而不是循环。例外：forEach（优先使用常规的 for 循环， 除非 forEach 的接收者是可空的或者 forEach 用做长调用链的一部分。）<br>

当在使用多个高阶函数的复杂表达式与循环之间进行选择时，请了解每种情况下所执行操作的开销并且记得考虑性能因素。
#### 12.11 区间上循环
使用 until 函数在一个区间上循环
```java
for (i in 0..n - 1) { …… }  // 不良
for (i in 0 until n) { …… }  // 良好
```
#### 12.12 使用字符串
优先使用字符串模板而不是字符串拼接<br>

优先使用多行字符串而不是将 \n 转义序列嵌入到常规字符串字面值中。<br>

如需在多行字符串中维护缩进，当生成的字符串不需要任何内部缩进时使用 trimIndent，而需要内部缩进时使用 trimMargin：
```java
assertEquals(
    """
    Foo
    Bar
    """.trimIndent(), 
    value
)

val a = """if(a > 1) {
          |    return a
          |}""".trimMargin()
```
#### 12.13 使用作用域函数 apply/with/run/also/let
Kotlin 提供了一系列用来在给定对象上下文中执行代码块的函数。要选择正确的函数。这块不抄了，有点不太理解。
### 库的编码规范
在编写库时，建议遵循一组额外的规则以确保 API 的稳定性：
* 总是显式指定成员的可见性（以避免将声明意外暴露为公有 API ）
* 总是显式指定函数返回类型以及属性类型（以避免当实现改变时意外更改返回类型）
* 为所有公有成员提供 KDoc 注释，不需要任何新文档的覆盖成员除外 （以支持为该库生成文档）






































