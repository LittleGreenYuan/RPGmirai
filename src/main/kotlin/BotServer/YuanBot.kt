package org.example.mirai.BotServer

import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.utils.BotConfiguration
import org.example.mirai.BotServer.UserData.Companion.Botload
import org.example.mirai.BotServer.UserData.Companion.RootList
import org.example.mirai.CommandServer.CommandManager
import org.example.mirai.CommandServer.CommandManager.MessageCatcher
import java.util.HashMap

class YuanBot {
    lateinit var bot: Bot


    suspend fun login() {
        Botload()
        println(RootList)
        bot = BotFactory.newBot(UserData.RootList[0].toLong(), UserData.RootList[1].toString()) {
            fileBasedDeviceInfo() // 使用 device.json 存储设备信息
            //protocol = ANDROID_WATCH
            //protocol = ANDROID_PAD // 切换协议
            protocol = BotConfiguration.MiraiProtocol.ANDROID_PHONE
        }
        bot.login()

        UserData.Botload()
        MessageCatcher(bot)

    }
    suspend fun jion(){
        bot.join()

    }


}