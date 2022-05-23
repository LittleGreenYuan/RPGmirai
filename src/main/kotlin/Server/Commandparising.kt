package org.example.mirai.Server

import org.example.mirai.BotServer.UserUtil

/*
//main函数中调用
//废弃的指令解析测试代码，通过类里的classConstructors实现调用，但使用map对myclass赋值将无法执行
        val mykclass = org.example.mirai.testserver.Commandparising.MyObject
        println(mykclass)
        println("kclass == ${mykclass.getName()}")//类的名称
        val AT = mykclass.classConstructors
        UserUtil.readData.getdata(100)
        println(AT[3]())
*/


class Commandparising {
    companion object MyObject{
        val classConstructors = listOf<() -> Any>(
            ::getCommandName,
            ::gettest,
            ::ClassC,
            ::execute
        )
        fun getCommandName(): String {
            return "/ABC"
        }
        fun gettest(): String {
            return "AA"
        }
        fun ClassC():String {
            return "$(c+a).toString()"
        }
        fun execute(): String {
            //var inputdata= UserUtil.readData()
            //return "${inputdata}(c+a).toString()"
            return "tt"
        }
    }

}