package org.example.mirai.CommandServer

import java.io.File

object CommandManager {
    fun autoSetup(packageName: String): MutableList<String> {
        //将定义了插件的包导入成为包名返回
        // 判断传入的文件是不是为空
        if (packageName == null) {
            throw NullPointerException()
        }else{
            // 把所有目录、文件放入数组
            val files: Array<File> = File(packageName).listFiles()
            val ktclassName = mutableListOf<String>()
            // 遍历数组每一个元素
            for (f in files) {
                // 判断元素是不是文件夹，是文件夹就重复调用此方法（递归）
                if (f.isDirectory) {
                    //不需要对目录下文件夹中的kt进行遍历
                } else {
                    // 判断文件是不是以.txt结尾的文件，并且count++（注意：文件要显示扩展名）
                    if (f.name.endsWith(".kt")) {
                        ktclassName.add("org.example.mirai.Server."+f.name.toString().replace("\\.kt".toRegex(), ""))
                    }
                }
            }
            return ktclassName
        }
    }


}