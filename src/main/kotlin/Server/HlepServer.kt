package org.example.mirai.Server

import org.example.mirai.BotServer.UserData
import org.example.mirai.BotServer.UserUtil

class HlepServer {
    companion object MyObject {

        val classConstructors = listOf<() -> Any>(
            ::getCommandName
        )
        fun getCommandName(): String {
            return "/help"
        }
        fun ServerMain(): String {
            val (message,sender,senderName)= UserUtil.readData()
            if(sender.id == UserData.RootList[2].toLong()){
                return "AAAAA"
            }else{
                return "BBBB"
            }



        }

    }
}