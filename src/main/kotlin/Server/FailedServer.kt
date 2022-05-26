package org.example.mirai.Server

import org.example.mirai.BotServer.UserData
import org.example.mirai.BotServer.UserUtil

class FailedServer {
    companion object MyObject {

        val classConstructors = listOf<() -> Any>(
            ::getCommandName
        )
        fun getCommandName(): String {
            return "/0216null"
        }
        fun ServerMain(): String {
            val outputServer = "FFFF"
            return outputServer
        }

    }

}