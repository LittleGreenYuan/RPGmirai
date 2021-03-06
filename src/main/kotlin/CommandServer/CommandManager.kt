package org.example.mirai.CommandServer

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import org.example.mirai.BotServer.UserData.Companion.RootList
import java.io.File
import java.util.HashMap
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredFunctions

object CommandManager {
    fun MessageCatcher(bot: Bot){
        /*
        bot.getFriend(RootList[2].toLong())?.sendMessage("System online!")//加载bot
        bot.eventChannel.subscribeAlways<GroupMessageEvent> {

        }*/
        MessageManager.register(bot)//消息捕捉
    }
    suspend fun MessageOutput(event:MessageEvent){
        MessageManager.proactiveresponse(event)


    }
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
    fun autoSetupCommand(serverName: MutableList<String>): HashMap<String,Int> {
        //从每一个server中提取指令，所有指令都将被转换成大写
        val kcalssMap : HashMap<String, Int> = HashMap<String,Int>() //define empty hashmap
        for (i in 0..serverName.size-1){
            var setupCommandName = serverName[i].toString()
            val personClass = Class.forName(setupCommandName).kotlin
            //val obj = personClass.createInstance()
            val companionObj = personClass.companionObjectInstance
            // 访问companion object的函数
            personClass.companionObject?.declaredFunctions?.forEach {
                when (it.name) {
                    "getCommandName" -> {
                        val commandname= it.call(companionObj).toString()
                        println("已经从${setupCommandName}注册指令：->"+commandname)//打印注册的指令名称
                        kcalssMap.put(commandname.toUpperCase(),i)
                    }
                }
            }
        }
        return kcalssMap
    }
    fun CommandExtraction(messagestring: String): MutableList<String> {
        //list将按照空格解析message，倒数第一个存放解析到的指令，倒数第二个保存了原始message
        var messageout = mutableListOf<String>()
        val messagelist = messagestring.toString().toUpperCase().trim().split("\\s+".toRegex()) as MutableList<String>
        messagelist.forEach {
            messageout.add(it.toString())
        }

        var commandString = messagelist[0].replace("\\d".toRegex(), "")

        val myregex= "^\\/.*[A-Z]".toRegex()
        val command = myregex.find(commandString)?.value

        if(command==null){
            messageout.add("null")
        }else{
            messageout.add(command.toString())
        }
        return messageout
    }


}