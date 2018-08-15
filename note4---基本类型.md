>Kotlin学习笔记-------基本类型<br>

在Kotlin中，所有东西都是对象， 在这个意义上讲我们可以在任何变量上调用成员函数与属性。<br>

一些类型可以有特殊的内部表示——例如，数字、字符以及布尔值可以在运行时表示为原生类型值，但是对于用户来说，它们看起来就像普通的类。<br>

本节将描述Kotlin中使用的基本类型：数字、字符、布尔值、数字与字符串。

### 1 . 数字
Kotlin 处理数字在某种程度上接近 Java，但是并不完全相同。例如，对于数字没有隐式拓宽转换（如 Java 中 int 可以隐式转换为long——译者注)，另外有些情况的字面值略有不同。<br>

Kotlin提供了如下的内置类型来表示数字（与Java很接近）：<br>

* **type**:**Bit width**<br>
* Double :64<br>
* Float	:32<br>
* Long	:64<br>
* Int	:32<br>
* Short	:16<br>
* Byte	:8<br>

**注意**：在Kotlin中字符不是数字
#### 1.1 字面常量
十进制：123<br>

Long类型用大写 L 标记：123L<br>

十六进制：0x0f<br>

二进制：0b00001011<br>

**注意**：不支持八进制<br>

Kotlin同样支持浮点数的常规表示方法：<br>

默认double：123.5、123.5e10<br>

Float用 f 或者 F 标记：123.5f<br>

#### 1.2 数字字面值的下划线（自 1.1 起）
可以使用下划线使数字常量更易读：<br>
```java
    val oneMillion = 1_000_000
    val creditCardNumber = 1234_5678_9012_3456L
    val socialSecurityNumber = 999_99_9999L
    val hexBytes = 0xFF_EC_DE_5E
    val bytes = 0b11010010_01101001_10010100_10010010
```
#### 1.3 表示方式
在 Java 平台数字是物理存储为 JVM 的原生类型，除非我们需要一个可空的引用（如 Int?）或泛型。 后者情况下会把数字装箱。<br>

注意数字装箱不必保留同一性：<br>
```java
    val a: Int = 10000
    println(a === a) // 输出“true”
    val boxedA: Int? = a
    val anotherBoxedA: Int? = a
    println(boxedA === anotherBoxedA) // ！！！输出“false”！！！
```
另一方面，它保留了相等性：<br>
```java
    val a: Int = 10000
    println(a == a) // 输出“true”
    val boxedA: Int? = a
    val anotherBoxedA: Int? = a
    println(boxedA == anotherBoxedA) // 输出“true”
```
#### 1.4 显式转换
由于不同的表示方式，较小类型并不是较大类型的子类型。如果是的话，就会出现下述问题：
```java
    // 假想的代码，实际上并不能编译：
    val a: Int? = 1 // 一个装箱的 Int (java.lang.Integer)
    val b: Long? = a // 隐式转换产生一个装箱的 Long (java.lang.Long)
    print(b == a) // 惊！这将输出“false”鉴于 Long 的 equals() 会检测另一个是否也为 Long
```
这时相等性会在所有地方悄无声息的失去，更别说同一性了。<br>

因此**较小的类型不能隐式转换为较大的类型**。 这意味着在不进行显式转换的情况下我们不能把 Byte 型值赋给一个 Int 变量。
```java
    val b: Byte = 1 // OK, 字面值是静态检测的
    val i: Int = b // 错误
```
我们可以显式转换来拓宽数字：
```java
    val i: Int = b.toInt() // OK：显式拓宽
    print(i)
```
每个数字类型支持如下的转换：
* toByte(): Byte
* toShort(): Short
* toInt(): Int
* toLong(): Long
* toFloat(): Float
* toDouble(): Double
* toChar(): Char

缺乏隐式类型转换并不显著，因为类型会从上下文推断出来，而算术运算会有重载做适当转换，例如：
```java
    val l = 1L + 3 // Long + Int => Long
```
#### 1.5 运算
Kotlin支持数字运算的标准集，运算被定义为相应的类成员（但编译器会将函数调用优化为相应的指令），详见运算符重载<br>

对于位运算，没有特殊字符来表示，而只可用中缀方式调用命名函数，例如:<br>
```java
    val x = (1 shl 2) and 0x000FF000
```
这是完整的位运算列表（只用于 Int 与 Long）：<br>
* shl(bits) – 有符号左移 (Java 的 <<)
* shr(bits) – 有符号右移 (Java 的 >>)
* ushr(bits) – 无符号右移 (Java 的 >>>)
* and(bits) – 位与
* or(bits) – 位或
* xor(bits) – 位异或
* inv() – 位非

#### 1.6 浮点数比较



































