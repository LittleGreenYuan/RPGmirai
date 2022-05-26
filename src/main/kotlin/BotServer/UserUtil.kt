package org.example.mirai.BotServer


import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.utils.ExternalResource
import java.io.InputStream

class UserUtil {
    val subName = "bb"
    //用于将数据传递给其他类的类（主要是不知道含参调用怎么处理）
    object readData {
        var tempdata=-1
        lateinit var inputmessage:MessageChain
        lateinit var inputsender:Member
        lateinit var inputsenderName:String

        lateinit var outputtype: String
        lateinit var outputmessage:String
        lateinit var outputfile: ExternalResource
        lateinit var outputstream: InputStream
        lateinit var outputmessagemessage:Message



        fun getBotdata(message: MessageChain,sender: Member,senderName: String) {
            inputmessage = message
            inputsender = sender
            inputsenderName = senderName
        }
        fun getoutputtype(): String {
            return outputtype
        }
        fun uploadmessage(input: String){
            outputtype="String"
            outputmessage=input
        }
        fun getoutputmessages():  String {
            return outputmessage
        }

        fun uploadfileImage(input: ExternalResource){
            outputtype="ExternalResourceImage"
            outputfile=input
        }
        fun uploadfileAudio(input: ExternalResource){
            outputtype="ExternalResourceAudio"
            outputfile=input
        }
        fun getoutputfile():  ExternalResource {
            return outputfile
        }

        fun uploadstream(input: InputStream){
            outputtype="InputStream"
            outputstream=input
        }
        fun getoutputstream(): InputStream {
            return outputstream
        }
        fun uploadoutputmessagemessage(input: Message){
            outputtype="Message"
            outputmessagemessage=input
        }
        fun getoutputmessagemessage(): Message {
            return outputmessagemessage
        }


        fun endoutputtype(){
            outputtype="End"
        }

        operator fun invoke(): Triple<MessageChain, Member, String> {
            return Triple(inputmessage,inputsender,inputsenderName)
        }

    }
}