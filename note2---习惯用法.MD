>Kotlin学习笔记-------习惯用法<br>
### 1 . 创建数据实体类（DTOs/POJOs/POCOs）
```java
data class Customer(val name: String, val email: String)
```
会为Customer类提供以下功能：<br>
* 所有属性的 getters （对于 var 定义的还有 setters）
* equals()
* hashCode()
* toString()
* copy()
* 所有属性的 component1()、 component2()……等等（详见数据类）

### 2 . 函数的默认参数
```java
fun foo(a: Int = 0, b: String = "") { …… }
```
### 3 . 过滤list
```java
val positives = list.filter { x -> x > 0 }
```
或者可以更短：
```java
val positives = list.filter { it > 0 }
```
### 4 . String内插
```java
println("Name $name")
```
### 5 . 类型判断
```java
when (x) {
    is Foo //-> ……
    is Bar //-> ……
    else   //-> ……
}
```
### 6 . 遍历map/pair型list
```java
for ((k, v) in map) {
    println("$k -> $v")
}
```
k，v可以改成任意名字
### 7 . 使用区间
```java
for (i in 1..100) { …… }  // 闭区间：包含 100
for (i in 1 until 100) { …… } // 半开区间：不包含 100
for (x in 2..10 step 2) { …… } //等差数列
for (x in 10 downTo 1) { …… }
if (x in 1..10) { …… }
```
### 8 . 只读list
```java
val list = listOf("a", "b", "c")
```
### 9 . 只读map
```java
val map = mapOf("a" to 1, "b" to 2, "c" to 3)
```
### 10 . 访问map（这个地方不太理解，感觉这样好像是不对的，因为map是只读的，但是如果脱离前面的 9 ，好像它也是正确的）
```java
println(map["key"])
map["key"] = value
```
### 11 . 延迟属性
```java
val p: String by lazy {
    // 计算该字符串
}
```
### 12 . 扩展函数
```java
fun String.spaceToCamelCase() { …… }

"Convert this to camelcase".spaceToCamelCase()
```
### 13 . 创建单例
```java
object Resource {
    val name = "Name"
}
```
### 14 . if not null 缩写
```java
val files = File("Test").listFiles()

println(files?.size)
```
### 15 . if not null and else 缩写
```java
val files = File("Test").listFiles()

println(files?.size ?: "empty")
```
### 16 . if null 执行一个语句
```java
val values = ……
val email = values["email"] ?: throw IllegalStateException("Email is missing!")
```
### 17 . 在可能为空的集合中取第一元素
```java
val emails = ... // 可能会是空集合
val mainEmail = emails.firstOrNull() ?: ""
```
### 18 . if not null 执行代码，?.配合let使用
```java
val value = ……

value?.let {
    …… // 代码会执行到此处, 假如data不为null
}
```
### 19 . 映射可空值（如果非空的话）
```java
val value = ……

val mapped = value?.let { transformValue(it) } ?: defaultValueIfValueIsNull
```
如果 ?: 左侧表达式非空，elvis 操作符就返回其左侧表达式，否则返回右侧表达式。 请注意，当且仅当左侧为空时，才会对右侧表达式求值。<br>
### 20 . 返回 when 表达式
```java
fun transform(color: String): Int {
    return when (color) {
        "Red" -> 0
        "Green" -> 1
        "Blue" -> 2
        else -> throw IllegalArgumentException("Invalid color param value")
    }
}
```
### 21 . “try/catch”表达式
```java
fun test() {
    val result = try {
        count()
    } catch (e: ArithmeticException) {
        throw IllegalStateException(e)
    }

    // 使用 result
}
```
### 22 . "if"表达式
```java
fun foo(param: Int) {
    val result = if (param == 1) {
        "one"
    } else if (param == 2) {
        "two"
    } else {
        "three"
    }
}
```
### 23 . 返回类型为 Unit 的方法的 Builder 风格用法
```java
fun arrayOfMinusOnes(size: Int): IntArray {
    return IntArray(size).apply { fill(-1) }
}
```
### 24 . 单表达式函数
```java
fun theAnswer() = 42
```
等价于
```java
fun theAnswer():Int{
    return 42
}
```
单表达式函数与其它习惯用法一起使用可以简化代码，例如和when表达式一起使用：
```java
fun transform(color: String): Int = when (color) {
    "Red" -> 0
    "Green" -> 1
    "Blue" -> 2
    else -> throw IllegalArgumentException("Invalid color param value")
}
```
### 25 . 对一个对象实例调用多个方法（with）
```java
class Turtle {
    fun penDown()
    fun penUp()
    fun turn(degrees: Double)
    fun forward(pixels: Double)
}

val myTurtle = Turtle()
with(myTurtle) { // 画一个 100 像素的正方形
    penDown()
    for(i in 1..4) {
        forward(100.0)
        turn(90.0)
    }
    penUp()
}
```
### 26 . 使用可空布尔
```java
val b: Boolean? = ……
if (b == true) {
    ……
} else {
    // `b` 是 false 或者 null
}
```


