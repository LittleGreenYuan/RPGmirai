package org.example.mirai

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.getMember
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.sendTo
import net.mamoe.mirai.utils.BotConfiguration.MiraiProtocol.*
import org.example.mirai.BotServer.UserData
import org.example.mirai.BotServer.UserUtil
import org.example.mirai.CommandServer.CommandManager
import org.example.mirai.CommandServer.CommandManager.CommandExtraction
import org.example.mirai.DataServer.RPGData
import java.io.File
import java.util.*
import java.util.regex.Pattern
import javax.imageio.ImageIO
import kotlin.reflect.jvm.jvmName
import kotlin.reflect.jvm.reflect
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.reflect.full.*
import java.awt.Image as ImageShaper



object WithoutConfigurationTest {

    @JvmStatic
    fun main(args: Array<String>): Unit = runBlocking {


        val bot = UserData.Botload()//回传生成的的bot，同时内置了上传本地玩家数据的RPGData.RPGUserDataUpload()部分
        //RPGData.RPGUserDataUpload()//上传本地RPG的相关数据，同时设定自动保存的协程

        val serverName = CommandManager.autoSetup(System.getProperty("user.dir")+"\\src\\main\\kotlin\\Server")//将定义了插件的包导入成为包名返回
        val serverMap = CommandManager.autoSetupCommand(serverName)//将所有插件的调用指令与serverName建立map映射

        bot.eventChannel.subscribeAlways<GroupMessageEvent> {
            val Command = CommandExtraction(message.contentToString())
            if(Command[Command.size-1] == "null"){

            }else{
                UserUtil.readData.getBotdata(message, sender, senderName)
                var kclasstname = serverMap.get(Command[Command.size-1])?.let { serverName.get(it.toInt()) }//安全地访问每个指令对应的包，如果没有注册该指令则返回null
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
                            if(it.call(companionObj) is String){
                                val outmessage = it.call(companionObj).toString()
                                subject.sendMessage(outmessage)
                            }

                        }
                    }
                }
            }




        }


/*
        //RPG游戏开始与初始角色设定
        //subject.sendMessage("开始进行RPG冒险！用户角色：$senderName")
        //val flag = Datamap.containsKey(sender.id)
        if (!RPGData.Datamap.containsKey(senderinput.id)) {
            var Listtemp = mutableListOf<Any>()
            //初始化各个参数并存储在txt里
            var ListMap = mutableListOf<Any>()
            ListMap = RPGData.Datamap[173799675] as MutableList<Any>//模仿前面的样本进行角色创建
            Listtemp.add("100")
            var TextString=" 100"
            for (i in 1..ListMap.count()-1) {
                Listtemp.add("0")
                TextString = TextString +" 0"
            }
            RPGData.Datamap.put(senderinput.id, Listtemp)
            File(RPGData.UserKeyListPath).appendText(senderinput.id.toString() +TextString+ "\n")
            //subject.sendMessage("@$senderName  可以给出冒险指令了！！")

            Listtemp = mutableListOf<Any>()
            Listtemp= RPGData.Datamap[senderinput.id] as MutableList<Any>
            Listtemp[1]=1//将行动状态设置为1，代表正在进行冒险模式
            RPGData.Datamap.replace(senderinput.id, Listtemp)//修改数据库中的数据


        } else {
            //subject.sendMessage("@$senderName  可以给出冒险指令了！！")
            var Listtemp = mutableListOf<Any>()
            Listtemp= RPGData.Datamap[senderinput.id] as MutableList<Any>
            Listtemp[1]=1//将行动状态设置为1，代表正在进行冒险模式
            RPGData.Datamap.replace(senderinput.id, Listtemp)//修改数据库中的数据
        }


        var Listtemp = mutableListOf<Any>()
        Listtemp = RPGData.Datamap[senderinput.id] as MutableList<Any>
        if(Listtemp[1]==0){
            //subject.sendMessage("@$senderName 似乎还没有开始准备冒险活动，不如试试输入 /RPG 吧。")
        }else if(Listtemp[1]==1){
            val random = Random()
            val time: Int = 0 + random.nextInt(1)  //线性增加时间
            Listtemp[1]=2//将行动状态设置为2，代表正在进行探索，1说明在酒馆，2正在随机探索，3正在随机副本然所，4说明玩家处于团本之中,5代表已经对怪物进行攻击
            RPGData.Datamap.replace(senderinput.id, Listtemp)//修改数据库中的数据
            //subject.sendMessage("@$senderName 准备进行冒险探索，预计探索时间为：$time 分钟。如果时间过长，请主动输入 /endexplore 结束此次探索。")
            println(("准备进行冒险探索，预计探索时间为：$time 分钟。如果时间过长，请主动输入 /endexplore 结束此次探索。"))
            val executor = Executors.newScheduledThreadPool(1)//创建一个协程
            executor.schedule(Runnable{
                runBlocking {
                    val random = Random()
                    val Coins: Int = 0 + random.nextInt(2)  //随机金币数目
                    val itemNumber: Int = 1+random.nextInt(2)//随机奖励数目

                    if(Listtemp[1]==2){
                        //随机获得材料
                        var TextOut=""
                        for(i in 1..itemNumber){
                            var GoodsItemtemp=(0..RPGData.GoodsItem.count()-1).random()
                            var GoodsName=RPGData.GoodsItem[GoodsItemtemp].toString()
                            TextOut=TextOut+"\n $GoodsName x1；"
                            Listtemp[GoodsItemtemp+4]=Listtemp[GoodsItemtemp+4].toString().toInt()+1//从第5个开始才是材料，对应物品数量增加1个
                        }
                        Listtemp[3]=Listtemp[3].toString().toInt()+Coins//金币数量增加1个
                        Listtemp[1]=1
                        RPGData.Datamap.replace(senderinput.id, Listtemp)//修改数据库中的数据
                        //subject.sendMessage("@$senderName 冒险探索结束，分别获得了$Coins 个金币与以下$itemNumber 个材料：" + TextOut)
                        println(("冒险探索结束，分别获得了$Coins 个金币与以下$itemNumber 个材料：" + TextOut))
                    }

                } } , time.toLong(), TimeUnit.MINUTES)
        }












        val Mapimg = ImageIO.read(File("I:\\Jetbrains\\mirai-hello-world\\src\\main\\kotlin\\data\\IMG01AT.png")).getScaledInstance(400, 400, ImageShaper.SCALE_SMOOTH)
        val flagimg = ImageIO.read(File("I:\\Jetbrains\\mirai-hello-world\\src\\main\\kotlin\\data\\flag.png")).getScaledInstance(30, 30, ImageShaper.SCALE_SMOOTH)
        val layerMap = mapOf("M" to Mapimg, "F" to flagimg)

        val InputString="tp018测试 采样设备 -r 楼下"
        val mseeage = CommandManager.CommandExtraction(InputString)
        println(mseeage)
        println(mseeage.get(mseeage.size-1))



        val MapOfClass =mapOf( 0 to "org.example.mirai.Server.Commandparising", 1 to "org.example.mirai.Server.DivinationServer")

        val kclasstname = MapOfClass[1]
        var inputcommand="getCommandName"//解析具体执行什么指令
        var datalist=100//需要使用的用户数据进行取值后上传

        val personClass = Class.forName(kclasstname.toString()).kotlin
        //val obj = personClass.createInstance()
        val companionObj = personClass.companionObjectInstance
        // 访问companion object的函数
        personClass.companionObject?.declaredFunctions?.forEach {
            println(it.name)//打印每个方法的名字
            when (it.name) {
                inputcommand -> {
                    UserUtil.readData.getdata(datalist)
                    println(it.call(companionObj))
                }
            }
        }*/



/*
        bot.getFriend(RootList[2].toLong())?.sendMessage("System online!")
        bot.eventChannel.subscribeAlways<FriendMessageEvent> {
            if (message.contentToString().startsWith("/mtp")) {
                if (sender.id == RootList[2].toLong()) {
                    var MissionText = message.contentToString().toUpperCase().replace("/MTP", "")
                    var Messagelist: List<String> = MissionText.trim().split("\\s+".toRegex())
                    //println(Messagelist)
                    bot.getGroup(Messagelist[0].toLong())?.sendMessage(Messagelist[1].trim().toString())
                }
            }
        }
        //bot.eventChannel.subscribeAlways<GroupMessageEvent>
        bot.eventChannel.subscribeAlways<GroupMessageEvent> {
            if(message.contentToString().contentEquals("/help")) {
                    if(sender.id == RootList[2].toLong()){
                        //subject.sendMessage(message.quote() + HelpWord+"\n/save：手动保存当前RPG数据DATAMap。")
                        subject.sendMessage(HelpWord.toString()+"\n/save：手动保存当前RPG数据DATAMap。")
                    }else{
                        subject.sendMessage(HelpWord.toString())
                    }
                }
        }*/




    }

}



