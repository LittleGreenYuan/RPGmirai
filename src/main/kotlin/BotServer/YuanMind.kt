package org.example.mirai.BotServer

import org.example.mirai.CommandServer.CommandManager

//全局变量存放
object YuanMind {
    //机器人实例
    val bot: YuanBot = YuanBot()
    val serverName = CommandManager.autoSetup(System.getProperty("user.dir")+"\\src\\main\\kotlin\\Server")//将定义了插件的包导入成为包名返回
    val serverMap = CommandManager.autoSetupCommand(serverName)//将所有插件的调用指令与serverName建立map映射
}