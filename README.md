# MultiTextProgressBar
一款漂亮强大的支持多文字显示的progressBar，可用作报表、统计，复杂数据展示，可自定义不同文字的颜色、大小、进度条的完成和未完成部分的颜色。

[![](https://jitpack.io/v/kjt666/MultiTextProgressBar.svg)](https://jitpack.io/#kjt666/MultiTextProgressBar)

使用
-----
要在您的构建中获得Git项目：<br>

步骤1.将JitPack存储库添加到构建文件中<br>

将其添加到存储库末尾的根build.gradle中：<br>

```Java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
步骤2.添加依赖项<br>

```Java
dependencies {
	        implementation 'com.github.kjt666:MultiTextProgressBar:v1.1'
	}
```
演示
-----
![](https://github.com/kjt666/ImageCache/blob/master/MultiTextProgressBar.png)<br>
属性说明
-----
```java
<com.wowo.kjt.mylibrary.MultiTextProgressBar
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:progress="60"
                app:left_text="左标题"
                app:left_text_color="#999"
                app:progress_text="200000"
                app:progress_text_color="#fff"
                app:progressbar_height="20dp"
                app:reach_color="#f99"
                app:reach_text="成交套数"
                app:reach_unreach_text_color="#B3B3B3"
                app:text_size="14sp"
                app:unreach_color="#f2f2f2"
                app:unreach_text="库存:2000000套" />
```
* progressbar_height:<br>
进度条的高度，MultiTextProgressBar的真实高度通过这个值设置（有默认值）
* left_text:<br>
进度条左侧文字
* progress_text:<br>
进度条上的文字
* reach_text:<br>
进度条完成部分的文字
* unreach_text:<br>
进度条未完成部分的文字
* text_size:<br>
left_text和progress_text的大小（有默认值）
* reach_color:<br>
进度条完成部分的颜色（有默认值）
* unreach_color:<br>
进度条未完成部分的颜色（有默认值）
* left_text_color:<br>
进度条左侧文字的颜色（有默认值）
* progress_text_color:<br>
进度条上文字的颜色（有默认值）
* reach_unreach_text_color:<br>
进度条底部完成和未完成文字的颜色（有默认值）<br>

方法说明
-----
* void setProgressBarText(String progressBarText)<br>
设置进度条上显示的文字值，代码动态修改progress_text的值。
* void setLeftText(String leftText)<br>
设置进度条左侧的文字值，代码动态修改left_text的值。
* void setReachText(String reachText)<br>
设置进度条完成部分的文字值，代码动态修改reach_text的值。
* void setUnReachText(String unReachText) <br>
设置进度条未完成部分的文字值，代码动态修改unreach_text的值。
