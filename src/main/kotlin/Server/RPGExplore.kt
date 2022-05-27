package org.example.mirai.Server

import kotlinx.coroutines.runBlocking
import org.example.mirai.BotServer.UserUtil
import org.example.mirai.BotServer.YuanBot
import org.example.mirai.BotServer.YuanMind.bot
import org.example.mirai.BotServer.proactiveresponse
import org.example.mirai.CommandServer.MessageManager
import org.example.mirai.DataServer.RPGData.Companion.Datamap
import org.example.mirai.DataServer.RPGData.Companion.GoodsItem
import org.example.mirai.DataServer.RPGData.Companion.UserKeyListPath
import java.io.File
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class RPGExplore {
    companion object MyObject {

        val classConstructors = listOf<() -> Any>(
            ::getCommandName
        )
        fun getCommandName(): String {
            return "/explore"
        }
        fun ServerMain(){
            var (pairnumber,sender,senderName)=UserUtil.readData()
            var (event,message)=pairnumber

            //RPG游戏开始与初始角色设定
            //subject.sendMessage("开始进行RPG冒险！用户角色：$senderName")
            //val flag = Datamap.containsKey(sender.id)
            if (!Datamap.containsKey(sender.id)) {
                println("$senderName 是尚未注册的用户，正在为其注册账户")
                var Listtemp = mutableListOf<Any>()
                //初始化各个参数并存储在txt里
                var ListMap = mutableListOf<Any>()
                ListMap = Datamap[173799675] as MutableList<Any>//模仿前面的样本进行角色创建

                Listtemp.add("100")
                var TextString=" 100"
                for (i in 1..ListMap.count()-1) {
                    Listtemp.add("0")
                    TextString = TextString +" 0"
                }
                Datamap.put(sender.id, Listtemp)
                File(UserKeyListPath).appendText(sender.id.toString() +TextString+ "\n")
                //subject.sendMessage("@$senderName  可以给出冒险指令了！！")

                Listtemp = mutableListOf<Any>()
                Listtemp= Datamap[sender.id] as MutableList<Any>
                Listtemp[1]=1//将行动状态设置为1，代表正在进行冒险模式
                Datamap.replace(sender.id, Listtemp)//修改数据库中的数据


            } else {
                //subject.sendMessage("@$senderName  可以给出冒险指令了！！")
                var Listtemp = mutableListOf<Any>()
                Listtemp= Datamap[sender.id] as MutableList<Any>
                Listtemp[1]=1//将行动状态设置为1，代表正在进行冒险模式
                Datamap.replace(sender.id, Listtemp)//修改数据库中的数据
            }


            var Listtemp = mutableListOf<Any>()
            Listtemp = Datamap[sender.id] as MutableList<Any>
            if(Listtemp[1]==0){
                println("@$senderName 似乎还没有开始准备冒险活动，不如试试输入 /RPG 吧。")
            }else if(Listtemp[1]==1){
                val random = Random()
                val time: Int = 1 + random.nextInt(1)  //线性增加时间
                Listtemp[1]=2//将行动状态设置为2，代表正在进行探索，1说明在酒馆，2正在随机探索，3正在随机副本然所，4说明玩家处于团本之中,5代表已经对怪物进行攻击
                Datamap.replace(sender.id, Listtemp)//修改数据库中的数据

                val Outputmessage = "@$senderName 准备进行冒险探索，预计探索时间为：$time 分钟。如果时间过长，请主动输入 /endexplore 结束此次探索。"
                UserUtil.readData.uploadmessage(Outputmessage)//将文本数据回传

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
                                var GoodsItemtemp=(0..GoodsItem.count()-1).random()
                                var GoodsName=GoodsItem[GoodsItemtemp].toString()
                                TextOut=TextOut+"\n $GoodsName x1；"
                                Listtemp[GoodsItemtemp+4]=Listtemp[GoodsItemtemp+4].toString().toInt()+1//从第5个开始才是材料，对应物品数量增加1个
                            }
                            Listtemp[3]=Listtemp[3].toString().toInt()+Coins//金币数量增加1个
                            Listtemp[1]=1
                            Datamap.replace(sender.id, Listtemp)//修改数据库中的数据

                            proactiveresponse.prgetBotdata(event,message,sender,senderName)
                            proactiveresponse.pruploadmessage("@$senderName 冒险探索结束，分别获得了$Coins 个金币与以下$itemNumber 个材料：\n $TextOut")
                            proactiveresponse.proactiveresponseOfbot(event)

                            //bot.proactiveresponse(1554596642L,"@$senderName 冒险探索结束，分别获得了$Coins 个金币与以下$itemNumber 个材料：" +
                            //    TextOut)
                        }

                    } } , time.toLong(), TimeUnit.MINUTES)
            }

        }

    }

}
