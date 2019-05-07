# coree
MVP内容项目，可以直接用来开发。
原项目：https://github.com/TheseYears/CoreLibs

2018.06更新项目内核，内容没有更新。

添加到库
https://jitpack.io/#mppy1/coree/v1.0

**gradle 引用**

Step 1

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
Step 2

dependencies {
    	implementation 'com.github.mppy1.coree:CoreLibs:v1.0'
}