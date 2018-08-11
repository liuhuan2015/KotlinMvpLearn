笔记
>今天周末了，在家里想把这个项目clone下来，coding一下，结果发现clone速度巨慢，上网搜了好几种方案，最中解决了，记录一下
### 方案一
在hosts文件中添加如下配置，参照文章[GitHub clone 速度过慢的问题](https://blog.csdn.net/sinat_38843093/article/details/79716804)：
```text
151.101.112.249 http://global-ssl.fastly.Net
192.30.253.112 http://github.com
```
但是发现好像没有解决问题
### 方案二 这个需要我们有ss。
参照文章[提高Github Clone速度](https://www.jianshu.com/p/5e74b1042b70)
使用git内置代理，直接走系统中运行的代理工具中转，比如，你的 SS 本地端口是 1080（一般port均为1080），那么可以如下方式走代理：
```text
git config --global http.proxy socks5://127.0.0.1:1080
git config --global https.proxy socks5://127.0.0.1:1080
```
此时，我又重新执行了一下clone命令，速度很快，溜的飞起。
