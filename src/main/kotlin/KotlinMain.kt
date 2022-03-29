package org.example.mirai

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonObject
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.utils.BotConfiguration.MiraiProtocol.*
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


object WithoutConfiguration {
    @JvmStatic
    fun main(args: Array<String>): Unit = runBlocking {

        val bot = BotFactory.newBot(3028159740L, "963852741tsy") {
            fileBasedDeviceInfo() // 使用 device.json 存储设备信息
            //protocol = ANDROID_WATCH
            //protocol = ANDROID_PAD // 切换协议
            protocol = ANDROID_PHONE
        }.alsoLogin()

        val detime = 2//执行备份任务的时间，DAY
        val HelpWord= listOf("/help: 进行操作指南查询。\n/RPG：前往冒险者酒馆进行文字RPG冒险活动。\n/explore：开展探索活动获得随机奖励。\n/operation：开展随机副本探索，挑战随机怪物获得奖励。" +
            "\n/spring：花费20金币进行生命恢复。\n/mission：查看冒险者任务公告。\n/submit：提交相应冒险者任务。\n/raid：查询大型副本状态。\n/raidattack：一定时间内，对RaidBoss发起一次进攻。")

        val UserKeyListPath = "I:\\Jetbrains\\mirai-hello-world\\src\\main\\kotlin\\UserKey.txt"
        val LogUserKeyListPath = "I:\\Jetbrains\\mirai-hello-world\\src\\main\\kotlin\\"
        //按行读取TXT中的文本内容，每一行是一个LIST
        val UserDATA = File(UserKeyListPath).readLines()
        //Map<Long, List<Int>> DataMap = new()
        val Datamap:HashMap<Long,List<Any>> = HashMap<Long,List<Any>>() //define empty hashmap
        val GoodsItem = listOf( "LifePotion", "Bonefragments", "Woods", "Stones", "MeatPieces", "Gels", "Herbals", "Seeds")
        val MonstersItem = listOf( "炼金商人", "骷髅哨兵", "古老树种", "岩石棘虫", "吸血蝙蝠", "彩虹史莱姆", "走路草", "狂暴松鼠")

        //任务面板需求材料，创建对应材料的list里面存储了相应需要提交的材料数量
        val MissionA = mapOf<Int, Int>(0 to 5, 2 to 3, 4 to 5)
        val MissionB = mapOf<Int, Int>(1 to 3, 3 to 5, 5 to 2)
        val MissionC = mapOf<Int, Int>(4 to 2, 6 to 4, 7 to 8)

        //Raid副本的怪物初始化
        val RaidBoss = RaidMoster("绝境亚历山大",40,10,mapOf(0 to 5, 2 to 3, 4 to 5),1)
        var RaidListID = mutableListOf<Long>()





        //data class User(val id:Long, val HP:Int, val ActionStatus:Int,val OpStatus:Int ,val Coins:Int , val LifePotion:Int, val Bonefragments:Int, val Woods:Int, val Stones:Int,
        //               val MeatPieces:Int, val Gels:Int, val Herbals:Int, val Seeds:Int)
        //上面这个是各个参数代表的意思
        for (i in 1..UserDATA.count()){
            //每一个LIST进行按照空格切分，变成单独元素的List
            val strtemp=UserDATA.elementAt(i-1).toString().split(" ")
            //println(strtemp)
            //将最后数据部分整合成一个List
            var Listtemp = mutableListOf <Int>()
            for(i in 1..strtemp.count()-1){
                //Listtemp.add(strtemp.elementAt((i).toInt()))
                Listtemp.add(strtemp.elementAt((i)).toInt())
            }
            //println(Listtemp)
            //将数据与QQ号构建出索引表，Key是QQ号
            Datamap.put(strtemp.elementAt(0).toLong(),Listtemp)
            //println(Datamap.count())


        }
        /*
        class  ExploreTask:Runnable{
            override fun run(){
                runBlocking {
                    subject.sendMessage("冒险探索结束")
                }
            }
        }*/

        val executor = Executors.newScheduledThreadPool(1)//创建一个协程
        executor.scheduleAtFixedRate(Runnable{
            runBlocking {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.BASIC_ISO_DATE
                val formatted = current.format(formatter)

                val fileName = LogUserKeyListPath + "data" + formatted.toString() + ".txt"
                println(fileName)
                val filename = File(fileName)
                if (!filename.exists()) {
                    println("创建文件")
                    filename.createNewFile()
                    val DATAKeyList = ArrayList(Datamap.keys)
                    val DATAvaluesList = ArrayList(Datamap.values)
                    for (i in 1..DATAKeyList.count()) {
                        var Outtext = DATAKeyList[i - 1].toString()
                        for (j in 1..DATAvaluesList[i - 1].count()) {
                            Outtext = Outtext + " " + DATAvaluesList[i - 1][j - 1].toString()
                        }
                        Outtext = Outtext + "\n"
                        File(fileName).appendText(Outtext)
                    }
                }else{
                    File(fileName).delete()//删除文件并重建数据文件，一定有什么方法可以实现覆盖填写的
                    filename.createNewFile()
                    val DATAKeyList=ArrayList(Datamap.keys)
                    val DATAvaluesList=ArrayList(Datamap.values)
                    var Outtextx=""
                    for(i in 1..DATAKeyList.count()) {
                        var Outtext=DATAKeyList[i - 1].toString()
                        for(j in 1..DATAvaluesList[i - 1].count()){
                            Outtext=Outtext+" "+DATAvaluesList[i - 1][j - 1].toString()
                        }
                        Outtextx=Outtextx+Outtext+"\n"


                    }
                    File(fileName).appendText(Outtextx)

                }
            }
        } , detime.toLong(), detime.toLong(),TimeUnit.MINUTES)


        bot.getFriend(17)?.sendMessage("System online!")
        //bot.getGroup(85)?.sendMessage("System online!")
        //bot.eventChannel.subscribeAlways<FriendMessageEvent>
        bot.eventChannel.subscribeAlways<GroupMessageEvent> {
            if(message.contentToString().contentEquals("/save")){
                subject.sendMessage(message.quote() + "Hi, you just said '${message.content}'")
                if(sender.id==17L){
                    val current = LocalDateTime.now()
                    val formatter = DateTimeFormatter.BASIC_ISO_DATE
                    val formatted = current.format(formatter)

                    val fileName = LogUserKeyListPath+"data"+formatted.toString()+".txt"
                    println(fileName)
                    val filename = File(fileName)
                    if (!filename.exists()) {
                        println("创建文件")
                        filename.createNewFile()
                        val DATAKeyList=ArrayList(Datamap.keys)
                        val DATAvaluesList=ArrayList(Datamap.values)
                        for(i in 1..DATAKeyList.count()) {
                            var Outtext=DATAKeyList[i - 1].toString()
                            for(j in 1..DATAvaluesList[i - 1].count()){
                                Outtext=Outtext+" "+DATAvaluesList[i - 1][j - 1].toString()
                            }
                            Outtext=Outtext+"\n"
                            File(fileName).appendText(Outtext)

                        }
                    }else{
                        File(fileName).delete()//删除文件并重建数据文件，一定有什么方法可以实现覆盖填写的
                        filename.createNewFile()
                        val DATAKeyList=ArrayList(Datamap.keys)
                        val DATAvaluesList=ArrayList(Datamap.values)

                        for(i in 1..DATAKeyList.count()) {
                            var Outtext=DATAKeyList[i - 1].toString()
                            for(j in 1..DATAvaluesList[i - 1].count()){
                                Outtext=Outtext+" "+DATAvaluesList[i - 1][j - 1].toString()
                            }
                            Outtext=Outtext+"\n"
                            File(fileName).writeText(Outtext)
                        }
                    }
                }
            }else
                if (message.contentToString().contentEquals("/help")) {
                if(sender.id == 15L){
                    subject.sendMessage(message.quote() + HelpWord+"\n/save：手动保存当前RPG数据DATAMap。")
                }else{
                    subject.sendMessage(message.quote() + HelpWord)
                }
            } else
                if (message.contentToString().startsWith("/RPG")) {
                    //RPG游戏开始与初始角色设定
                    subject.sendMessage("开始进行RPG冒险！用户角色：$senderName")
                    //val flag = Datamap.containsKey(sender.id)
                    if (!Datamap.containsKey(sender.id)) {
                        subject.sendMessage(message.quote() + "$senderName 是新来的冒险者，正在为冒险者创建冒险名片。")
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
                        subject.sendMessage("@$senderName  可以给出冒险指令了！！")

                        Listtemp = mutableListOf<Any>()
                        Listtemp= Datamap[sender.id] as MutableList<Any>
                        Listtemp[1]=1//将行动状态设置为1，代表正在进行冒险模式
                        Datamap.replace(sender.id, Listtemp)//修改数据库中的数据


                    } else {
                        subject.sendMessage("@$senderName  可以给出冒险指令了！！")
                        var Listtemp = mutableListOf<Any>()
                        Listtemp= Datamap[sender.id] as MutableList<Any>
                        Listtemp[1]=1//将行动状态设置为1，代表正在进行冒险模式
                        Datamap.replace(sender.id, Listtemp)//修改数据库中的数据
                    }
                } else
                if (message.contentToString().contentEquals("/explore")) {
                    var Listtemp = mutableListOf<Any>()
                    Listtemp = Datamap[sender.id] as MutableList<Any>
                    if(Listtemp[1]==0){
                        subject.sendMessage("@$senderName 似乎还没有开始准备冒险活动，不如试试输入 /RPG 吧。")
                    }else if(Listtemp[1]==1){
                        val random = Random()
                        val time: Int = 25 + random.nextInt(10)  //线性增加时间
                        Listtemp[1]=2//将行动状态设置为2，代表正在进行探索，1说明在酒馆，2正在随机探索，3正在随机副本然所，4说明玩家处于团本之中,5代表已经对怪物进行攻击
                        Datamap.replace(sender.id, Listtemp)//修改数据库中的数据
                        subject.sendMessage("@$senderName 准备进行冒险探索，预计探索时间为：$time 分钟。如果时间过长，请主动输入 /endexplore 结束此次探索。")
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
                                        Listtemp[GoodsItemtemp+5]=Listtemp[GoodsItemtemp+5].toString().toInt()+1//从第5个开始才是材料，对应物品数量增加1个
                                    }
                                    Listtemp[4]=Listtemp[4].toString().toInt()+Coins//金币数量增加1个
                                    Listtemp[1]=1
                                    Datamap.replace(sender.id, Listtemp)//修改数据库中的数据
                                    subject.sendMessage("@$senderName 冒险探索结束，分别获得了$Coins 个金币与以下$itemNumber 个材料：" +
                                        TextOut)
                                }

                            } } , time.toLong(), TimeUnit.MINUTES)
                    }
                }else
                if(message.contentToString().contentEquals("/endexplore")){
                    var Listtemp = mutableListOf<Any>()
                    Listtemp = Datamap[sender.id] as MutableList<Any>
                    if(Listtemp[1]==2){
                        Listtemp[1]=1
                        Datamap.replace(sender.id, Listtemp)//修改数据库中的数据
                        subject.sendMessage("冒险者$senderName 已返回营地。")
                    }else if(Listtemp[1]==0){
                        subject.sendMessage("冒险者$senderName 似乎在摸鱼。")
                    }
                }else
                if(message.contentToString().contentEquals("/operation")){
                    var Listtemp = mutableListOf<Any>()
                    Listtemp = Datamap[sender.id] as MutableList<Any>
                    if(Listtemp[1]==0){
                        subject.sendMessage("@$senderName 似乎还没有开始准备冒险活动，不如试试输入 /RPG 吧。")
                    } else if(Listtemp[1]==1){
                        val random = Random()
                        val time: Int = 25 + random.nextInt(10)  //线性增加时间
                        Listtemp[1]=3//将行动状态设置为3，代表正在进行随机副本攻击，1说明在酒馆，2正在随机探索，3正在随机副本然所，4说明玩家处于团本之中,5代表已经对怪物进行攻击
                        Datamap.replace(sender.id, Listtemp)//修改数据库中的数据
                        subject.sendMessage("@$senderName 正在前往地下副本，预计探索时间为：$time 分钟。如果时间过长，请主动输入 /endexplore 结束此次探索。")
                        val executor = Executors.newScheduledThreadPool(1)//创建一个协程
                        executor.schedule(Runnable{
                            runBlocking {
                                val random = Random()
                                val Coins: Int = 0 + random.nextInt(2)  //随机金币数目
                                val HPcounter: Int = 1 + random.nextInt(15)  //随机生命损失
                                val Monster: Int = (0..MonstersItem.count()-1).random()//随机怪物选择
                                val itemNumber: Int = 1+random.nextInt(1)//随机奖励数目
                                if(Listtemp[1]==3){
                                    Listtemp[0]=Listtemp[0].toString().toInt()-HPcounter//生命值扣除

                                    if(Listtemp[0].toString().toInt()<=0){
                                        Listtemp[0]=1
                                        Datamap.replace(sender.id, Listtemp)//修改数据库中的数据
                                        subject.sendMessage("冒险者@$senderName 很抱歉，在副本战斗中你不幸阵亡，我们将你送回到了冒险者行会进行治疗。" +
                                                        "\n当前，您的生命值已经恢复到了1点，请合理进行接下来的探索。")
                                    }else{
                                        //随机获得材料
                                        var TextOut=""
                                        TextOut=TextOut+"\n ${GoodsItem[Monster].toString()} x1；"
                                        Listtemp[Monster+4]=Listtemp[Monster+4].toString().toInt()+1//对应物品数量增加1个
                                        for(i in 1..itemNumber){
                                            var GoodsItemtemp=(0..GoodsItem.count()-1).random()
                                            var GoodsName=GoodsItem[GoodsItemtemp].toString()
                                            TextOut=TextOut+"\n $GoodsName x1；"
                                            Listtemp[GoodsItemtemp+4]=Listtemp[GoodsItemtemp+4].toString().toInt()+1//对应物品数量增加1个
                                        }
                                        Listtemp[3]=Listtemp[3].toString().toInt()+Coins//金币数量增加
                                        Listtemp[1]=1
                                        Datamap.replace(sender.id, Listtemp)//修改数据库中的数据
                                        subject.sendMessage("@$senderName 冒险探索顺利结束，遭遇了${MonstersItem[Monster].toString()}，生命受损${HPcounter}\n" +
                                            "获得了$Coins 个金币与以下固定材料与$itemNumber 个额外材料：" +
                                            TextOut)
                                    }

                                }

                            } } , time.toLong(), TimeUnit.MINUTES)
                    }else{
                        subject.sendMessage("@$senderName 正处于别的活动，请活动结束再进行尝试。")
                    }
                }else
                if(message.contentToString().contentEquals("/spring")){
                    var Listtemp = mutableListOf<Any>()
                    Listtemp = Datamap[sender.id] as MutableList<Any>
                    if(Listtemp[1]==0){
                        subject.sendMessage("@$senderName 似乎还没有开始准备冒险活动，不如试试输入 /RPG 吧。")
                    }else if(Listtemp[1]==1){
                        if(Listtemp[3].toString().toInt()<20){
                            subject.sendMessage("冒险者@$senderName 很抱歉你似乎并没有持有足够的金币（20金币一次）。")
                        }else{
                            Listtemp[3]=Listtemp[3].toString().toInt()+20//恢复生命值
                            if(Listtemp[3].toString().toInt()>100){
                                Listtemp[3]=100
                            }
                            Datamap.replace(sender.id, Listtemp)//修改数据库中的数据
                            subject.sendMessage("冒险者@$senderName 扣除20个金币后给你恢复了20点生命值，\n当前生命值：${Listtemp[3].toString().toInt()}。")

                        }
                    }
                }else
                if (message.contentToString().startsWith("/mission")){
                    var MissionText=message.contentToString().toUpperCase().replace("/MISSION","")
                    when (MissionText.trim()) {
                        "A" -> {
                            val MissionTextOut = BulidMission(MissionA,GoodsItem)
                            subject.sendMessage(MissionTextOut.toString())

                        }
                        "B" -> {
                            val MissionTextOut = BulidMission(MissionB,GoodsItem)
                            subject.sendMessage(MissionTextOut.toString())

                        }
                        "C" -> {
                            val MissionTextOut = BulidMission(MissionC,GoodsItem)
                            subject.sendMessage(MissionTextOut.toString())
                        }
                        else ->{
                            subject.sendMessage("欢迎@$senderName 来到冒险者酒馆，目前公告栏上只有A、B和C三个任务可以查看。")

                        }
                    }
                }else
                if(message.contentToString().startsWith("/submit")){
                    var Listtemp = mutableListOf<Any>()
                    Listtemp = Datamap[sender.id] as MutableList<Any>
                    if(Listtemp[1]==1){
                        var MissionText=message.contentToString().toUpperCase().replace("/SUBMIT","")
                        when(MissionText.trim()){
                            "A"->{
                                var FlageMission = MissionSubmit(MissionA,Listtemp)
                                if(FlageMission[0].toInt()==1){
                                    subject.sendMessage("任务已已提交，提交后@${senderName}获得金币${FlageMission[1]}个")
                                    Listtemp[3]=Listtemp[3].toString().toInt()+FlageMission[1].toInt()//金币数量增加
                                    Datamap.replace(sender.id, Listtemp)//修改数据库中的数据
                                }else{
                                    subject.sendMessage("@$senderName 似乎无法提交该任务，请核对背包信息。")
                                }

                            }
                            "B"->{
                                var FlageMission = MissionSubmit(MissionB,Listtemp)
                                if(FlageMission[0].toInt()==1){
                                    subject.sendMessage("任务已已提交，提交后@${senderName}获得金币${FlageMission[1]}个")
                                    Listtemp[3]=Listtemp[3].toString().toInt()+FlageMission[1].toInt()//金币数量增加
                                    Datamap.replace(sender.id, Listtemp)//修改数据库中的数据
                                }else{
                                    subject.sendMessage("@$senderName 似乎无法提交该任务，请核对背包信息。")
                                }

                            }
                            "C"->{
                                var FlageMission = MissionSubmit(MissionC,Listtemp)
                                if(FlageMission[0].toInt()==1){
                                    subject.sendMessage("任务已已提交，提交后@${senderName}获得金币${FlageMission[1]}个")
                                    Listtemp[3]=Listtemp[3].toString().toInt()+FlageMission[1].toInt()//金币数量增加
                                    Datamap.replace(sender.id, Listtemp)//修改数据库中的数据
                                }else{
                                    subject.sendMessage("@$senderName 似乎无法提交该任务，请核对背包信息。")
                                }

                            }
                            else->{subject.sendMessage("亲爱的@$senderName ，冒险者酒馆公告栏上似乎没有发布任何有效任务。")}

                        }

                    }else{ subject.sendMessage("@$senderName 似乎还不在冒险者酒馆之中，请稍后再试。") }




                }else
                if(message.contentToString().contentEquals("/raid")){
                    var TextTemp=""
                    if(RaidBoss.GetFlage()==0)
                    {
                        TextTemp = "当前不可攻略。"
                    }else
                        if(RaidBoss.GetFlage()==1){
                            TextTemp = "正在攻略中。"
                        }else
                            if(RaidBoss.GetFlage()==2||RaidBoss.GetFlage()==3){
                                TextTemp = "本次攻略完成。"
                            }else{
                                TextTemp = "似乎出现了程序错误。"
                            }

                    subject.sendMessage("当前RaidBoss\"${RaidBoss.BossName}\"状态为\n" +
                        "生命值：${RaidBoss.HP}\n" +
                        "初始攻击力：${RaidBoss.Attack}\n" +
                        "随机掉落奖励：" +RaidBoss.GetGoods(GoodsItem).toString()+"\n"+
                        "当前Boss状态："+TextTemp.toString())

                }else
                if(message.contentToString().contentEquals("/raidattack")){

                    var Listtemp = mutableListOf<Any>()
                    Listtemp = Datamap[sender.id] as MutableList<Any>
                    if(RaidBoss.GetFlage()==1&&RaidBoss.GetHP()>0){
                        if(Listtemp[1]==0){
                            subject.sendMessage("@$senderName 似乎还没有开始准备冒险活动，不如试试输入 /RPG 吧。")
                        }else
                            if(Listtemp[1]==1){
                                if(RaidBoss.GetFlage()==0){
                                    subject.sendMessage("${RaidBoss.GetName()} 似乎还处于不可攻略的状态，请等待攻略开放。")
                                }else
                                if(RaidBoss.GetFlage()==1){
                                        Listtemp[1]=4//1说明在酒馆，2正在随机探索，3正在随机副本然所，4说明玩家处于团本之中,5代表已经对怪物进行攻击
                                        Datamap.replace(sender.id, Listtemp)//修改数据库中的数据
                                        if(RaidListID.getOrNull(sender.id.toInt())==null){
                                            RaidListID.add(sender.id.toLong())
                                            if(Listtemp[0].toString().toInt()<=RaidBoss.GetAttack()){
                                                Listtemp[0]=1
                                                Listtemp[1]=1//1说明在酒馆，2正在随机探索，3正在随机副本然所，4说明玩家处于团本之中,5代表已经对怪物进行攻击
                                                Datamap.replace(sender.id, Listtemp)//修改数据库中的数据
                                                subject.sendMessage("冒险者@$senderName 很抱歉，在Raid战斗中你不幸阵亡，我们将你送回到了冒险者行会进行治疗。" +
                                                    "\n当前，您的生命值已经恢复到了1点，请合理进行接下来的探索。")
                                                RaidListID.remove(sender.id)
                                            }else{
                                                RaidBoss.AttackformPlayer(10)
                                                Listtemp[0]=Listtemp[0].toString().toInt()-RaidBoss.GetAttack()
                                                Listtemp[1]=5//1说明在酒馆，2正在随机探索，3正在随机副本然所，4说明玩家处于团本之中,5代表已经对怪物进行攻击
                                                Datamap.replace(sender.id, Listtemp)//修改数据库中的数据
                                            }
                                        }else{
                                            if(Listtemp[0].toString().toInt()<=RaidBoss.GetAttack()){
                                                Listtemp[0]=1
                                                Listtemp[1]=1//1说明在酒馆，2正在随机探索，3正在随机副本然所，4说明玩家处于团本之中,5代表已经对怪物进行攻击
                                                Datamap.replace(sender.id, Listtemp)//修改数据库中的数据
                                                subject.sendMessage("冒险者@$senderName 很抱歉，在Raid战斗中你不幸阵亡，我们将你送回到了冒险者行会进行治疗。" +
                                                    "\n当前，您的生命值已经恢复到了1点，请合理进行接下来的探索。")
                                                RaidListID.remove(sender.id)
                                            }else{
                                                RaidBoss.AttackformPlayer(10)
                                                Listtemp[0]=Listtemp[0].toString().toInt()-RaidBoss.GetAttack()
                                                Listtemp[1]=5//1说明在酒馆，2正在随机探索，3正在随机副本然所，4说明玩家处于团本之中,5代表已经对怪物进行攻击
                                                Datamap.replace(sender.id, Listtemp)//修改数据库中的数据
                                            }
                                        }
                                    }else
                                if(RaidBoss.GetFlage()==2||RaidBoss.GetFlage()==3){
                                        subject.sendMessage("${RaidBoss.GetName()} 已被攻略完毕，请等待攻略再次开放。")
                                    }else{
                                        subject.sendMessage("error: Raid出现Flage错误。")
                                    }
                            }else
                                if(Listtemp[1]==5){
                                    subject.sendMessage("冒险者@$senderName 你已经攻击过怪物了，不如看看其他人的战斗吧。")
                                }else{
                                    subject.sendMessage("冒险者@$senderName 似乎还处于别的任务之中，不如先等待一下吧。")
                                }
                    }

                    if(RaidBoss.GetFlage()==2&&RaidBoss.GetHP()==0){
                        subject.sendMessage("在冒险者们的努力之下，${RaidBoss.GetName()}已经被攻略，正在结算成果。")
                        //这里需要添加一个利用RaidList进行素材添加的函数
                        RaidSubmit(raidListID = RaidListID, goods = RaidBoss.Goods, datamap = Datamap)
                        RaidBoss.EndRaid()
                    }else{
                        subject.sendMessage("Error：Raid出现错误。")
                    }



                }
            //SaveHashMap(LogUserKeyListPath,Datamap)

        }




    }

    private fun SaveHashMap(logUserKeyListPath: String, datamap: HashMap<Long, List<Any>>) {
        /*val Keys=kotlin.collections.ArrayList(datamap.keys).toString()
        val Values=kotlin.collections.ArrayList(datamap.values).toString()
        val maptemp:HashMap<String,Any> = HashMap<String,Any>() //define empty hashmap
        for(i in 0..Keys.count()-1){
            maptemp.put(Keys[i].toString(),Values[i].toString())
        }*/
        //ObjectMapper


    }


    private fun RaidSubmit(raidListID: MutableList<Long>, goods: Map<Int, Int>, datamap: HashMap<Long, List<Any>>) {
        val goodsKeyList=ArrayList(goods.keys)
        val goodsvaluesList=ArrayList(goods.values)
        for (i in 0..raidListID.count()-1){
            var listtemp = datamap[raidListID[i].toInt().toLong()] as MutableList<Any>
            for(j in 0..goodsKeyList.count()-1){
                listtemp[goodsKeyList[j].toInt()+4]=listtemp[goodsKeyList[j].toInt()+4].toString().toInt()+goodsvaluesList[j].toInt()
            }
            listtemp[1]=1
            datamap.replace(raidListID[i].toInt().toLong(), listtemp)//修改数据库中的数据
        }
        println("成功为玩家分配材料。")
    }

    private fun BulidMission(missionA: Map<Int, Int>, goodsItem: List<String>): Any {
        //根据Mission的List确定所需要的材料数目
        var MissionText = "根据冒险者招募公告，目前行会需要以下材料："
        val DATAKeyList=ArrayList(missionA.keys)
        val DATAvaluesList=ArrayList(missionA.values)
        for(i in 0..2){
            MissionText += "\n${goodsItem[DATAKeyList[i].toInt()]}x${DATAvaluesList[i].toString()}"
        }
        return MissionText

    }

    private fun MissionSubmit(missionA: Map<Int, Int>, listtemp: MutableList<Any>): MutableList<Int> {
        //根据Mission的List与角色DataMap确定是否能够提交任务，返回一个List，0号位代表了提交状态，1号位代表提交任务后获得的金币数量
        //0表明所需素材不够，1表明素材提交成功
        val FlageMission=mutableListOf <Int>()
        val DATAKeyList=ArrayList(missionA.keys)
        val DATAvaluesList=ArrayList(missionA.values)
        if((DATAvaluesList[0].toInt() <= listtemp[DATAKeyList[0].toInt()+4].toString().toInt())&&((DATAvaluesList[1].toInt() <= listtemp[DATAKeyList[1].toInt()+4].toString().toInt())) &&((DATAvaluesList[2].toInt() <= listtemp[DATAKeyList[2].toInt()+4].toString().toInt()))){
            val random = Random()
            val Coins: Int = 5 + random.nextInt(3)  //随机金币数目

            listtemp[DATAKeyList[0].toInt()+4]=listtemp[DATAKeyList[0].toInt()+4].toString().toInt()-DATAvaluesList[0].toInt()
            listtemp[DATAKeyList[1].toInt()+4]=listtemp[DATAKeyList[1].toInt()+4].toString().toInt()-DATAvaluesList[1].toInt()
            listtemp[DATAKeyList[2].toInt()+4]=listtemp[DATAKeyList[2].toInt()+4].toString().toInt()-DATAvaluesList[2].toInt()

            FlageMission.add(1)
            FlageMission.add(Coins)
        }else{
            FlageMission.add(0)
            FlageMission.add(0)
        }
        return FlageMission
    }




}

public class RaidMoster constructor(Namei: String,HPi: Int,Attacki: Int, Goodsi: Map<Int,Int>, Flagei: Int){
    var BossName: String =Namei//Boss名称
    var HP : Int = HPi//生命值
    var Attack : Int = Attacki//攻击力
    var Goods: Map<Int, Int> = Goodsi//掉落奖励
    var Flage: Int = Flagei//备用Flage，0代表尚未激活；1代表已经激活；2代表攻略即将完成，还未结算；3代表攻略完成且结算完毕。

    init {
        println("系统默认构建了RaidBoss\"${this.BossName}\"\n生命值：${this.HP}\n初始攻击力：${this.Attack}\n随机掉落奖励：${this.Goods}\n当前Boss状态：${this.Flage}")
    }

    fun AttackformPlayer(attack: Int){
        if(this.Flage==1){
            this.HP = this.HP-attack
            if(this.HP<=0){
                this.HP=0
                this.Flage = 2
            }

        }
    }

    fun GetName(): String{
        return this.BossName
    }
    fun GetHP(): Int{
        return this.HP
    }
    fun GetAttack(): Int{
        return this.Attack
    }
    fun GetFlage(): Int{
        return this.Flage
    }
    fun GetGoods(goodsItem: List<String>): String{
        val DATAKeyList=ArrayList(Goods.keys)
        val DATAvaluesList=ArrayList(Goods.values)
        var MissionText=""
        for(i in 0..DATAKeyList.count()-1){
            MissionText += "\n\t${goodsItem[DATAKeyList[i].toInt()]}x${DATAvaluesList[i].toString()}"
        }
        return MissionText
    }
    fun ActivateRaid(){
        if(this.Flage==0){
            this.Flage=1
        }
    }
    fun EndRaid(){
        if(this.Flage==1||this.Flage==2){
            this.Flage=0
        }
    }


}


object WithConfiguration {
    @JvmStatic
    fun main(args: Array<String>): Unit = runBlocking {
        // 使用自定义配置
        val bot = BotFactory.newBot(315L, "74") {
            fileBasedDeviceInfo() // 使用 device.json 存储设备信息
            protocol = ANDROID_PAD // 切换协议
            //protocol = ANDROID_PHONE
        }.alsoLogin()
        println("WithConfiguration下方的程序被运行了。")

        /*bot.getFriend(3028159740)?.sendMessage("Hello, World!")
        bot.eventChannel.subscribeAlways<FriendMessageEvent> {
            if (sender.id == 3028159740L) {
                subject.sendMessage(message.quote() + "Hi, you just said '${message.content}'")
            }
        }*/


    }
}
