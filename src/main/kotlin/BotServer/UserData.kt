package org.example.mirai.BotServer

import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.utils.BotConfiguration
import org.example.mirai.DataServer.RPGData
import java.io.File


class UserData {
    val temp="这个变量需要创建实例后才可以被访问调用，下面的伴生对象则跟类绑定在一起的，不需要创建实例就可以访问。"
    //定义一个用于存放配置数据的list，包含了bot账号，密码以及root权限的各类数据
    companion object{
        val RootList = mutableListOf<String>()
        val RootPath = System.getProperty("user.dir")+"\\src\\main\\kotlin\\data\\loroot.txt"//定义根信息存储的位置
        val RootDATA = File(RootPath)?.readLines()
        suspend fun Botload(): Bot {
            RootList.add(RootDATA[1])//存放bot账号
            RootList.add(RootDATA[3])//存放bot密码
            RootList.add(RootDATA[5])//存放root账号

            val bot = BotFactory.newBot(RootList[0].toLong(), RootList[1].toString()) {
                fileBasedDeviceInfo() // 使用 device.json 存储设备信息
                //protocol = ANDROID_WATCH
                //protocol = ANDROID_PAD // 切换协议
                protocol = BotConfiguration.MiraiProtocol.ANDROID_PHONE
            }.alsoLogin()
            bot.getFriend(RootList[2].toLong())?.sendMessage("System online!")//加载bot
            RPGData.RPGUserDataUpload()//上传本地RPG的相关数据，同时设定自动保存的协程
            return bot
        }

    }



}