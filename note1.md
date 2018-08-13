>Kotlin学习笔记-------基础语法<br>

### 1 . 空安全
#### 1.1 安全调用操作符：?.,表示变量可为空
在 Kotlin 中，类型系统区分一个引用可以容纳 null （可空引用）还是不能容纳（非空引用）。 <br>

例如，String 类型的常规变量不能容纳 null：
```java
var a: String = "abc"
a = null // 编译错误
```
如果要使变量允许为空，可以这样写：<br>
```java
 var b: String? = "abc"
```
此时，我们如果要调用 a 的方法或者访问它的属性，它保证不会导致 NPE，我们可以放心地使用：<br>
```java
val l = a.length
```
但是如果想访问 b 的同一个属性，那么这是不安全的，并且编译器会报告一个错误：
```java
val l = b.length // 错误：变量“b”可能为空
```
**Kotlin提供了一个安全调用操作符：?.**<br>
```java
val a = "Kotlin"
val b: String? = null
println(b?.length)
println(a?.length)
```
如果 b 非空，就返回 b.length，否则返回 null，这个表达式的类型是 Int?。<br>

安全调用在链式调用中很有用。例如，如果一个员工 Bob 可能会（或者不会）分配给一个部门， 并且可能有另外一个员工是该部门的负责人，那么获取 Bob 所在部门负责人（如果有的话）的名字，我们写作：<br>
```java
bob?.department?.head?.name
```
如果任意一个属性（环节）为空，这个链式调用就会返回 null。<br>

如果要只对非空值执行某个操作，安全调用操作符可以与 let 一起使用：<br>
```java
val listWithNulls: List<String?> = listOf("Kotlin", null)
for (item in listWithNulls) {
    item?.let { println(it) } // 输出 A 并忽略 null
}
```
安全调用也可以出现在赋值的左侧。这样，如果调用链中的任何一个接收者为空都会跳过赋值，而右侧的表达式根本不会求值：<br>
```java
// 如果 `person` 或者 `person.department` 其中之一为空，都不会调用该函数：
person?.department?.head = managersPool.getManager()
```
#### 1.2 Elvis操作符

当我们有一个可空的引用 r 时，我们可以说“如果 r 非空，我使用它；否则使用某个非空的值 x”：<br>
```java
val l: Int = if (b != null) b.length else -1
```
除了完整的 if-表达式，这还可以通过 Elvis 操作符表达，写作 ?:：<br>
```java
val l = b?.length ?: -1
```
如果 ?: 左侧表达式非空，elvis 操作符就返回其左侧表达式，否则返回右侧表达式。 请注意，当且仅当左侧为空时，才会对右侧表达式求值。<br>

**注意**：因为 throw 和 return 在 Kotlin 中都是表达式，所以它们也可以用在 elvis 操作符右侧。这可能会非常方便，例如，检查函数参数：
```java
fun foo(node: Node): String? {
    val parent = node.getParent() ?: return null
    val name = node.getName() ?: throw IllegalArgumentException("name expected")
    // ……
}
```
#### 1.3 !! 操作符

!! 操作符是为 NPE 爱好者准备的：非空断言运算符（!!）**将任何值转换为非空类型，若该值为空则抛出异常**。我们可以写 b!! ，这会返回一个非空的 b 值 （例如：在我们例子中的 String）或者如果 b 为空，就会抛出一个 NPE 异常：
```java
val l = b!!.length
```
因此，如果你想要一个 NPE，你可以得到它，但是你必须显式要求它，否则它不会不期而至。<br>

#### 1.4 安全的类型转换 as?
如果对象不是目标类型，那么常规类型转换可能会导致 ClassCastException。 另一个选择是使用安全的类型转换，如果尝试转换不成功则返回 null：
```java
val aInt: Int? = a as? Int
```
#### 1.5 可空类型的集合
 如果有一个可空类型元素的集合，并且想要过滤非空元素，可以使用 **filterNotNull** 来实现：
```java
val nullableList: List<Int?> = listOf(1, 2, null, 4)
val intList: List<Int> = nullableList.filterNotNull()
```
### 2 . var 和 val 的区别
var,可变变量
```java
var x = 5 // 自动推断出 `Int` 类型
x += 1
```
val，一次赋值（只读）的变量
```java
val a: Int = 1  // 立即赋值
val b = 2   // 自动推断出 `Int` 类型
val c: Int  // 如果没有初始值类型不能省略
```
### 3 . 使用可空值及null检测
当某个变量的值可以为 null 的时候，必须在声明处的类型后添加 ? 来标识该引用可为空。详见1.空安全<br>

### 4 . 使用类型检测及自动类型转换
is 运算符检测一个表达式是否某类型的一个实例。 如果一个不可变的局部变量或属性已经判断出为某类型，那么检测后的分支中可以直接当作该类型使用，无需显式转换：
```java
fun getStringLength(obj: Any): Int? {
    if (obj is String) {
        // `obj` 在该条件分支内自动转换成 `String`
        return obj.length
    }

    // 在离开类型检测分支后，`obj` 仍然是 `Any` 类型
    return null
}
```
或者<br>
```java
fun getStringLength(obj: Any): Int? {
    // `obj` 在 `&&` 右边自动转换成 `String` 类型
    if (obj is String && obj.length > 0) {
        return obj.length
    }

    return null
}
```
### 5 . 使用for循环
```java
val items = listOf("apple", "banana", "kiwifruit")
for (item in items) {
    println(item)
}
```
或者下面这种，可以拿到索引值的
```java
val items = listOf("apple", "banana", "kiwifruit")
for (index in items.indices) {
    println("item at $index is ${items[index]}")
}
```
### 6 . 使用while循环
```java
val items = listOf("apple", "banana", "kiwifruit")
var index = 0
while (index < items.size) {
    println("item at $index is ${items[index]}")
    index++
}
```
### 7 . 使用when表达式
```java
fun describe(obj: Any): String =
when (obj) {
    1          -> "One"
    "Hello"    -> "Greeting"
    is Long    -> "Long"
    !is String -> "Not a string"
    else       -> "Unknown"
}
```
### 8 . 使用区间（range）
使用 in 运算符来检测某个数字是否在指定区间内：
```java
val x = 10
val y = 9
if (x in 1..y+1) {
    println("fits in range")
}
```
同理，也可以检测是否在区间外。<br>

区间迭代：
```java
for (x in 1..5) {
    print(x)
}
```
数列迭代：
```java
for (x in 1..10 step 2) {
    print(x)
}
println()
for (x in 9 downTo 0 step 3) {
    print(x)
}
```
### 9 . 使用集合
对集合进行迭代：
```java
for (item in items) {
    println(item)
}
```
使用 in 运算符来判断集合内是否包含某实例:
```java
 val items = setOf("apple", "banana", "kiwifruit")
    when {
        "orange" in items -> println("juicy")
        "apple" in items -> println("apple is fine too")
    }
```
使用lambda表达式来过滤（filter）与映射（map）集合：
```java
fun main(args: Array<String>) {
  val fruits = listOf("banana", "avocado", "apple", "kiwifruit")
  fruits
      .filter { it.startsWith("a") }
      .sortedBy { it }
      .map { it.toUpperCase() }
      .forEach { println(it) }
}
```
结果：
```java
APPLE
AVOCADO
```
### 10 . 创建基本类及其实例
```java
val rectangle = Rectangle(5.0, 2.0) // 不需要“new”关键字
val triangle = Triangle(3.0, 4.0, 5.0)
```


































