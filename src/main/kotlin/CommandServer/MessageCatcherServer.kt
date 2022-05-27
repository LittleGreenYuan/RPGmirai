package org.example.mirai.CommandServer

import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import org.example.mirai.BotServer.UserUtil
import org.example.mirai.BotServer.YuanBot
import org.example.mirai.BotServer.YuanMind
import org.example.mirai.CommandServer.CommandManager.CommandExtraction
import java.io.File
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredFunctions

object MessageManager {
    fun register(bot: Bot) {
        /*bot.eventChannel.subscribeMessages {}*/

        bot.eventChannel.subscribeAlways<GroupMessageEvent> {
            val Command = CommandExtraction(message.contentToString())
            if(Command[Command.size-1] == "null"){

            }else{
                var kclasstname = YuanMind.serverMap.get(Command[Command.size-1])?.let { YuanMind.serverName.get(it.toInt()) }//安全地访问每个指令对应的包，如果没有注册该指令则返回null
                if(kclasstname == null){
                    kclasstname="org.example.mirai.Server.FailedServer"
                }
                val inputcommand="ServerMain"//解析具体执行什么指令
                val personClass = Class.forName(kclasstname.toString()).kotlin
                //val obj = personClass.createInstance()
                val companionObj = personClass.companionObjectInstance
                // 访问companion object的函数
                personClass.companionObject?.declaredFunctions?.forEach {
                    when (it.name) {
                        inputcommand -> {
                            UserUtil.readData.getBotdata(this,message, sender, senderName)
                            it.call(companionObj)

                            when(UserUtil.readData.getoutputtype())
                            {
                                "String" -> {
                                    subject.sendMessage(UserUtil.readData.outputmessage)
                                    UserUtil.readData.endoutputtype()
                                }
                                "ExternalResourceImage" -> {
                                    val externalResource=subject.uploadImage(UserUtil.readData.getoutputfile())
                                    subject.sendMessage(externalResource)
                                    UserUtil.readData.endoutputtype()
                                }
                                "ExternalResourceAudio" -> {
                                    val externalResource=subject.uploadAudio(UserUtil.readData.getoutputfile())
                                    subject.sendMessage(externalResource)
                                    UserUtil.readData.endoutputtype()
                                }
                                "Message" ->{
                                    subject.sendMessage(UserUtil.readData.getoutputmessagemessage())
                                    UserUtil.readData.endoutputtype()
                                }
                                "InputStream"->{
                                    subject.sendImage(UserUtil.readData.getoutputstream())
                                    UserUtil.readData.endoutputtype()
                                }
                                else -> {}

                            }

                        }
                    }
                }

            }

            }
        }

    suspend fun proactiveresponse(event:MessageEvent) {
        event.subject.sendMessage("xxxxxxxxxxxxxxx")

    }

}