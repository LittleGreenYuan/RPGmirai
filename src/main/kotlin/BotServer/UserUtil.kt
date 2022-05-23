package org.example.mirai.BotServer


import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.data.MessageChain

class UserUtil {
    val subName = "bb"
    //用于将数据传递给其他类的类（主要是不知道含参调用怎么处理）
    object readData {
        var tempdata=-1
        lateinit var inputmessage:MessageChain
        lateinit var inputsender:Member
        lateinit var inputsenderName:String

        fun getBotdata(message: MessageChain,sender: Member,senderName: String): Triple<MessageChain, Member, String> {
            inputmessage = message
            inputsender = sender
            inputsenderName = senderName
            return Triple(message,sender,senderName)
        }

        fun getdata(input: Int){
            tempdata=input
        }
        operator fun invoke(): Triple<MessageChain, Member, String> {
            return Triple(inputmessage,inputsender,inputsenderName)
        }

    }
}