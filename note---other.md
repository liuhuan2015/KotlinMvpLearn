琐碎知识点笔记
### 1 . Animation
alpha标签——调节透明度

android:fromAlpha   动画开始的透明度，从0.0 --1.0 ，0.0表示全透明，1.0表示完全不透明

android:toAlpha     动画结束时的透明度，也是从0.0 --1.0 ，0.0表示全透明，1.0表示完全不透明

在 Android 动画中，缩放动画（scale标签）和旋转动画（rotate标签）均有 android:pivotX 和 android:pivotY 这两个属性，其值可以有多种形式:

50 : 属性值为数值时，表示在当前 View 的左上角，即原点处加上 50px，作为起始点

50%/0.5 : 属性值为百分数时，比如 50%，表示在当前控件的左上角加上自己宽度的 50% （即自身宽度中心）作为起始点

50%p : 属性值是50%p（字母 p 是 parent 的意思）时，取值的基数是父控件，50%p 就是表示在当前的左上角加上父控件宽度的 50% 作为起始点 x 轴坐标。

### 2 . Activity的过渡动画
Activity的过渡动画，一般情况下直接使用：
```java
overridePendingTransition(enterAnim, exitAnim);
```
谷歌在Android 5.0 之后提供了另外一种 Activity 的过渡动画——ActivityOptions，并且提供了兼容包——ActivityOptionsCompat。

ActivityOptionsCompat是一个静态类，提供了为数不多的几个方法。

#### 2.1 makeCustomAnimation方法
这个方法和overridePendingTransition非常类似，在实现效果上和overridePendingTransition也是相同的
```java
   public static ActivityOptionsCompat makeCustomAnimation(@NonNull Context context,
            int enterResId, int exitResId) {
    ...
}
```
#### 2.2 makeScaleUpAnimation方法
这个方法用途还是很广的，效果就是不断的放大一个View，进而进行Activity的过渡。
```java
  public static ActivityOptionsCompat makeScaleUpAnimation(@NonNull View source,
            int startX, int startY, int startWidth, int startHeight) {
    ...
}
```
#### 2.3 makeSceneTransitionAnimation方法
指定多个View进行协作，比如说两个activity参加动画有一个ImageView和一个TextView，来实现过渡动画时它们的协同。
```java
    public static ActivityOptionsCompat makeSceneTransitionAnimation(@NonNull Activity activity,
            Pair<View, String>... sharedElements) {
    ...
}
```
#### 2.4 还有一些其它的方法，这里就不写了















