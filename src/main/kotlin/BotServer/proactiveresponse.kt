package org.example.mirai.BotServer

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.utils.ExternalResource
import java.io.InputStream

object proactiveresponse {
    //这里跟反馈差不多，只不过单独开辟了一部分区域给创建的协程使用
    lateinit var prinputevent: MessageEvent
    lateinit var prinputmessage: MessageChain
    lateinit var prinputsender: Member
    lateinit var prinputsenderName:String

    lateinit var proutputtype: String
    lateinit var proutputmessage:String
    lateinit var proutputfile: ExternalResource
    lateinit var proutputstream: InputStream
    lateinit var proutputmessagemessage: Message

    suspend fun proactiveresponseOfbot(event: MessageEvent): MessageReceipt<Contact>? {

        when (proactiveresponse.proutputtype) {
            "String" -> {
                proactiveresponse.prendoutputtype()
                return event.subject.sendMessage(proutputmessage)
            }
            "ExternalResourceImage" -> {
                val externalResource = event.subject.uploadImage(proactiveresponse.prgetoutputfile())
                return event.subject.sendMessage(externalResource)
            }
            "ExternalResourceAudio" -> {
                return null
            }
            "Message" -> {
                proactiveresponse.prendoutputtype()
                return event.subject.sendMessage(proactiveresponse.proutputmessagemessage)
            }
            "InputStream" -> {
                proactiveresponse.prendoutputtype()
                return event.subject.sendImage(proactiveresponse.proutputstream)
            }
            else -> {
                return null
            }

        }
    }


    fun prgetBotdata(event: MessageEvent, message: MessageChain, sender: Member, senderName: String) {
        prinputevent = event
        prinputmessage = message
        prinputsender = sender
        prinputsenderName = senderName
    }
    fun prgetoutputtype(): String {
        return proutputtype
    }
    fun pruploadmessage(input: String){
        proutputtype="String"
        proutputmessage=input
    }
    fun prgetoutputmessages():  String {
        return proutputmessage
    }

    fun pruploadfileImage(input: ExternalResource){
        proutputtype="ExternalResourceImage"
        proutputfile=input
    }
    fun pruploadfileAudio(input: ExternalResource){
        proutputtype="ExternalResourceAudio"
        proutputfile=input
    }
    fun prgetoutputfile(): ExternalResource {
        return proutputfile
    }

    fun pruploadstream(input: InputStream){
        proutputtype="InputStream"
        proutputstream=input
    }
    fun prgetoutputstream(): InputStream {
        return proutputstream
    }
    fun pruploadoutputmessagemessage(input: Message){
        proutputtype="Message"
        proutputmessagemessage=input
    }
    fun prgetoutputmessagemessage(): Message {
        return proutputmessagemessage
    }


    fun prendoutputtype(){
        proutputtype="End"
    }
}