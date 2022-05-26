package org.example.mirai.Server

import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import org.example.mirai.BotServer.UserData
import org.example.mirai.BotServer.UserUtil
import java.io.File



class HlepServer {
    companion object MyObject {

        val classConstructors = listOf<() -> Any>(
            ::getCommandName
        )
        fun getCommandName(): String {
            return "/help"
        }
        fun ServerMain(): Int {
            val (message,sender, senderName)= UserUtil.readData()
            if(sender.id == UserData.RootList[2].toLong()){
                val Mapimg = File(System.getProperty("user.dir")+"\\src\\main\\kotlin\\data\\IMG01AT.png")
                UserUtil.readData.uploadfileImage(Mapimg.toExternalResource())
                val outputServer = "AAAAA"
                return 1
            }else{
                val outputServer = "BBBBB"
                return 1
            }
        }

    }
}