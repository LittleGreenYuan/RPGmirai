package org.example.mirai

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonObject
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.Audio
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.message.data.PlainText
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
import java.awt.Image as ImageShaper
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO
import javax.sound.sampled.AudioInputStream

object WithoutConfiguration {
    @JvmStatic
    fun main(args: Array<String>): Unit = runBlocking {
        //定义一个用于存放配置数据的list，包含了bot账号，密码以及root权限的各类数据
        val RootList = mutableListOf<String>()
        val RootPath= "I:\\Jetbrains\\mirai-hello-world\\src\\main\\kotlin\\data\\root.txt"
        val RootDATA = File(RootPath).readLines()
        RootList.add(RootDATA[1])//存放bot账号
        RootList.add(RootDATA[3])//存放bot密码
        RootList.add(RootDATA[5])//存放root账号
        //println(RootList)


        val bot = BotFactory.newBot(RootList[0].toLong(), RootList[1].toString()) {
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
        val Datamap:HashMap<Long,List<Any>> = HashMap<Long,List<Any>>() //define empty hashmap
        val GoodsItem = listOf( "生命药剂", "骸骨碎片", "青苔木片", "特供岩石", "蝙蝠肉块", "多彩凝胶", "粘连纤维", "远古种子")
        //val GoodsItem = listOf( "LifePotion", "Bonefragments", "Woods", "Stones", "MeatPieces", "Gels", "Herbals", "Seeds")
        val MonstersItem = listOf( "炼金商人", "骷髅哨兵", "古老树种", "岩石棘虫", "吸血蝙蝠", "彩虹史莱姆", "走路草", "狂暴松鼠")

        //任务面板需求材料，创建对应材料的list里面存储了相应需要提交的材料数量
        val MissionA = mapOf<Int, Int>(0 to 5, 2 to 3, 4 to 5)
        val MissionB = mapOf<Int, Int>(1 to 3, 3 to 5, 5 to 2)
        val MissionC = mapOf<Int, Int>(4 to 2, 6 to 4, 7 to 8)

        //Raid副本的怪物初始化
        val RaidBoss = RaidMoster("绝境亚历山大",150,10,mapOf(0 to 5, 2 to 3, 4 to 5),0)
        var RaidListID = mutableListOf<Long>()
        val NameofGua=listOf("乾卦","坤卦" ,"屯卦" ,"蒙卦" ,"需卦" ,"讼卦" ,"师卦" ,"比卦" ,"小畜卦" ,"履卦" ,"泰卦" ,"否卦" ,"同人卦" ,"大有卦" ,"谦卦" ,"豫卦" ,"随卦" ,"蛊卦" ,"临卦" ,"观卦" ,"噬嗑卦" ,"贲卦" ,"剥卦" ,"复卦"
            ,"无妄卦" ,"大畜卦" ,"颐卦" ,"大过卦" ,"坎卦" ,"离卦" ,"咸卦" ,"恒卦" ,"遁卦" ,"大壮卦" ,"晋卦" ,"明夷卦" ,"家人卦" ,"睽卦" ,"蹇卦" ,"解卦" ,"损卦","益卦" ,"夬卦" ,"姤卦" ,"萃卦" ,"升卦" ,"困卦" ,"井卦"
            ,"革卦" ,"鼎卦" ,"震卦" ,"艮卦" ,"渐卦" ,"归妹卦" ,"丰卦" ,"旅卦" ,"巽卦" ,"泽卦" ,"涣卦" ,"节卦" ,"中孚卦" ,"小过卦" ,"既济卦" ,"未济卦")
        val XiangofGua=listOf("上上","上上","下下","中下","中上","中下","中上","上上","下下","中上","中中","中中","中上","上上","中中","中中","中中","中中","中上","中上","上上","中上","中下","中中","下下","中上"
            ,"上上","中下","下下","中上","中上","中上","下下","中上","中上","中下","下下","下下","下下","中上","下下","上上","上上","上","中上","上上","中上","上上","上上","中下","中上","中下","上上","下下"
            , "上上","下下","中上","上上","下下","上上","下下","中上","中上","中下")
        val JieofGua=listOf("困龙得水好运交，不由喜气上眉梢，一切谋望皆如意，向后时运渐渐高。","肥羊失群入山岗，饿虎逢之把口张，适口充肠心欢喜，卦若占之大吉昌。","风刮乱丝不见头，颠三倒四犯忧愁，慢从款来左顺遂，急促反惹不自由。","卦中爻象犯小耗，君子占之运不高，婚姻合伙有琐碎，做事必然受苦劳。","明珠土埋日久深，无光无亮到如今，忽然大风吹土去，自然显露有重新。","心中有事事难做，恰是二人争路走，雨下俱是要占先，谁肯让谁走一步。","将帅领旨去出征，骑着烈马拉硬弓，百步穿杨去得准，箭中金钱喜气生。","顺风行船撒起帆，上天又助一蓬风，不用费力逍遥去，任意而行大亨通。","苗逢旱天尽焦梢，水想云浓雨不浇，农人仰面长吁气，是从款来莫心高。","凤凰落在西岐山，长鸣几声出圣贤，天降文王开基业，富贵荣华八百年。","学文满腹入场闱，三元及第得意回，从今解去愁和闷，喜庆平地一声雷。","虎落陷坑不堪言，进前容易退后难，谋望不遂自己便，疾病口舌事牵连。","心中有事犯猜疑，谋望从前不着实，幸遇明人来指引，诸般忧闷自消之。","砍树摸雀作事牢，是非口舌自然消，婚姻合伙不费力，若问走失未逃脱。","天赐贫人一封金，不争不抢两平分，彼此分得金到手，一切谋望皆遂心。","太公插下杏黄旗，收妖为徒归西岐，自此青龙得了位，一旦谋望百事宜。","泥里步踏这几年，推车靠崖在眼前，目下就该再使力，扒上崖去发财源。","卦中爻象如推磨，顺当为福反为祸，心中有益且迟迟，凡事尽从忙处错。","君王无道民倒悬，常想拨云见青天，幸逢明主施仁政，重又安居乐自然。","卦遇蓬花旱逢河，生意买卖利息多，婚姻自有人来助，出门永不受折磨。","运拙如同身受饥，幸得送饭又送食，适口充腹心欢喜，忧愁从此渐消移。","钟鼓乐之大吉庆，占者逢之喜临头。","鹊遇天晚宿林中，不知林内先有鹰，虽然同处心生恶，卦若逢之是非轻。","马氏太公不相合，世人占之忧疑多，恩人无义反为怨，是非平地起风波。","飞鸟失机落笼中，纵然奋飞不能腾，目下只宜守本分，妄想扒高万不能。","忧愁常锁两眉头，千头万绪挂心间，从今以后防开阵，任意行而不相干。","太公独钓渭水河，手执丝杆忧愁多，时来又遇文王访，自此永不受折磨。","夜晚梦里梦金银，醒来仍不见一文，目下只宜求本分，思想络是空劳神。","一轮明月照水中，只见影儿不见踪，愚夫当财下去取，摸来摸去一场空。","官人来占主高升，庄农人家产业增，生意买卖利息厚，匠艺占之大亨通。","运去黄金失色，时来棒槌发芽，月令极好无差，且喜心宽意大。","渔翁寻鱼运气好，鱼来撞网跑不了，别人使本挣不来，谁想一到就凑合。","浓云蔽日不光明，劝君且莫出远行，婚姻求财皆不利，提防口舌到门庭。","卦占工师得大木，眼前该着走上路，时来运转多顺当，有事自管放心宽。","锄地锄去苗里草，谁想财帛将人找，一锄锄出银子来，这个运气也算好。","时乖运拙走不着，急忙过河拆了桥，恩人无义反为怨，凡事无功枉受劳。","一朵鲜花镜中开，看着极好取不来，劝君休把镜花恋，卦若逢之主可怪。","此卦占来运气歹，如同太公作买卖，贩猪牛快贩羊迟，猪羊齐贩断了宰。","大雨倾地雪满天，路上行人苦又寒，拖泥带水费尽力，事不遂心且耐烦。","目下月令如过关，千辛万苦受熬煎，时来恰相有人救，任意所为不相干。","时运不至费心多，比作推车受折磨，山路崎岖吊下耳，左插右按按不着。","时来运转吉气发，多年枯木又开花，枝叶重生多茂盛，几人见了几人夸。","蜘蛛脱网赛天军，粘住游蜂翅翎毛，幸有大风吹破网，脱离灾难又逍遥。","他乡遇友喜气欢，须知运气福重添，自今交了顺当运，向后管保不相干。","游鱼戏水被网惊，跳过龙门身化龙，三尺杨柳垂金线，万朵桃花显你能。","士人来占必得名，生意买卖也兴隆，匠艺逢之交易好，农间庄稼亦收成。","时运不来好伤怀，撮上押去把梯抬，一筒虫翼无到手，转了上去下不来。","枯井破费已多年，一朝流泉出来鲜，资生济渴人称羡，时来运转喜自然。","苗逢旱天渐渐衰，幸得天恩降雨来，忧去喜来能变化，求谋干事遂心怀。","莺鹜蛤蜊落沙滩，蛤蜊莺鹜两翅扇，渔人进前双得利，失走行人却自在。","一口金钟在淤泥，人人拿着当玩石，忽然一日钟悬起，响亮一声天下知。","财帛常打心头走，可惜眼前难到手，不如意时且忍耐，逢着闲事休开口。","俊鸟幸得出笼中，脱离灾难显威风，一朝得意福力至，东西南北任意行。","求鱼须当向水中，树上求之不顺情，受尽爬揭难随意，劳而无功运平平。","古镜昏暗好几年，一朝磨明似月圆，君子谋事逢此卦，时来运转喜自然。","飞鸟树上垒窝巢，小人使计举火烧，君占此卦为不吉，一切谋望枉徒劳。","一叶孤舟落沙滩，有篙无水进退难，时逢大雨江湖溢，不用费力任往返。","这个卦象真可取，觉着做事不费力，休要错过这机关，事事觉得随心意。","隔河望见一锭金，欲取岸宽水又深，指望资财难到手，昼思夜想枉费心。","时来运转喜气生，登台封神姜太公，到此诸神皆退位，纵然有祸不成凶。","路上行人色匆匆，急忙无桥过薄冰，小心谨慎过得去，一步错了落水中。","行人路过独木桥，心内惶恐眼里瞧，爽利保你过得去，慢行一定不安牢。","金榜以上题姓名，不负当年苦用功，人逢此卦名吉庆，一切谋望大亨通。","离地着人几丈深，是防偷营劫寨人，后封太岁为凶煞，时加谨慎祸不侵。")
        val Mapimg = ImageIO.read(File("I:\\Jetbrains\\mirai-hello-world\\src\\main\\kotlin\\data\\IMG01AT.png")).getScaledInstance(400, 400, ImageShaper.SCALE_SMOOTH)
        val flagimg = ImageIO.read(File("I:\\Jetbrains\\mirai-hello-world\\src\\main\\kotlin\\data\\flag.png")).getScaledInstance(30, 30, ImageShaper.SCALE_SMOOTH)
        val layerMap = mapOf("M" to Mapimg, "F" to flagimg)



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

        val executor = Executors.newScheduledThreadPool(1)//创建一个协程
        executor.scheduleAtFixedRate(Runnable{
            runBlocking {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.BASIC_ISO_DATE
                val formatted = current.format(formatter)

                val fileName = LogUserKeyListPath + "UserKey.txt"
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
        } , detime.toLong(), detime.toLong(),TimeUnit.HOURS)


        bot.getFriend(RootList[2].toLong())?.sendMessage("System online!")
        //bot.getGroup(857261272)?.sendMessage("System online!")
        bot.eventChannel.subscribeAlways<FriendMessageEvent> {
            if (message.contentToString().startsWith("/tp")) {
                if (sender.id == RootList[2].toLong()) {
                    var MissionText = message.contentToString().toUpperCase().replace("/TP", "")
                    bot.getGroup(1003508545)?.sendMessage(MissionText.trim().toString())
                }
            } else if (message.contentToString().startsWith("/raid1")) {
                if (sender.id == RootList[2].toLong()) {
                    RaidBoss.ActivateRaid()
                    subject.sendMessage("成功激活RaidBoss。")
                }
            } else if (message.contentToString().startsWith("/raid0")) {
                if (sender.id == RootList[2].toLong()) {
                    RaidBoss.EndRaid()
                    val DATAKeyList = ArrayList(Datamap.keys)
                    for (i in 0..DATAKeyList.count() - 1) {
                        var Listtemp = mutableListOf<Any>()
                        Listtemp = Datamap[DATAKeyList[i]] as MutableList<Any>
                        if (Listtemp[1] == 4 || Listtemp[1] == 5) {
                            Listtemp[1] = 1
                            Datamap.replace(DATAKeyList[i], Listtemp)//修改数据库中的数据
                        }
                    }
                    subject.sendMessage("强制关闭RaidBoss。")
                }
            } else if (message.contentToString().startsWith("/raidend")) {
                if (sender.id == RootList[2].toLong()) {
                    RaidSubmit(raidListID = RaidListID, goods = RaidBoss.Goods, datamap = Datamap)
                    RaidBoss.EndRaid()
                    subject.sendMessage("强制结算RaidBoss。")
                }
            } else if (message.contentToString().startsWith("/gg")) {
                if (sender.id == RootList[2].toLong()) {
                    var MissionText = message.contentToString().toUpperCase().replace("/GG", "")
                    bot.getGroup(938548497)?.sendMessage(MissionText.trim().toString())
                }
            }
        }
        //bot.eventChannel.subscribeAlways<GroupMessageEvent>
        bot.eventChannel.subscribeAlways<GroupMessageEvent> {
            if(message.contentToString().contentEquals("/save")){
                //subject.sendMessage(message.quote() + "Hi, you just said '${message.content}'")
                if(sender.id==RootList[2].toLong()){
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
                    subject.sendMessage("保存完成！")
                }
            }else
                if (message.contentToString().contentEquals("/help")) {
                    if(sender.id == RootList[2].toLong()){
                        //subject.sendMessage(message.quote() + HelpWord+"\n/save：手动保存当前RPG数据DATAMap。")
                        subject.sendMessage(HelpWord.toString()+"\n/save：手动保存当前RPG数据DATAMap。")
                    }else{
                        subject.sendMessage(HelpWord.toString())
                    }
                } else
                    if (message.contentToString().startsWith("/RPG")) {
                        //RPG游戏开始与初始角色设定
                        //subject.sendMessage("开始进行RPG冒险！用户角色：$senderName")
                        //val flag = Datamap.containsKey(sender.id)
                        if (!Datamap.containsKey(sender.id)) {
                            subject.sendMessage("$senderName 是新来的冒险者，正在为冒险者创建冒险名片。")
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
                                                Listtemp[GoodsItemtemp+4]=Listtemp[GoodsItemtemp+4].toString().toInt()+1//从第5个开始才是材料，对应物品数量增加1个
                                            }
                                            Listtemp[3]=Listtemp[3].toString().toInt()+Coins//金币数量增加1个
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
                                                val HPcounter: Int = 1 + random.nextInt(9)  //随机生命损失
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
                                                        val RandNumber: Int = 1+random.nextInt(2)//随机数目

                                                        TextOut=TextOut+"\n ${GoodsItem[Monster].toString()} x"+RandNumber.toString()+"；"
                                                        Listtemp[Monster+4]=Listtemp[Monster+4].toString().toInt()+RandNumber//对应物品数量增加1个
                                                        for(i in 1..itemNumber){
                                                            var GoodsItemtemp=(0..GoodsItem.count()-1).random()
                                                            var GoodsName=GoodsItem[GoodsItemtemp].toString()

                                                            TextOut=TextOut+"\n $GoodsName x1;"
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
                                                                                    subject.sendMessage("冒险者@$senderName 成功对"+RaidBoss.GetName()+"造成10点伤害，接下来在Boss击败前将会滞留在此。")
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
                                                                                    subject.sendMessage("冒险者@$senderName 成功对"+RaidBoss.GetName()+"造成10点伤害，接下来在Boss击败前将会滞留在此。")
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
                                                            subject.sendMessage("当前RaidBoss"+RaidBoss.GetName()+"残余生命"+RaidBoss.HP.toString()+"点")
                                                        }
                                                    }else
                                                        if(message.contentToString().contentEquals("/bag")){
                                                            var Listtemp = mutableListOf<Any>()
                                                            Listtemp = Datamap[sender.id] as MutableList<Any>
                                                            var BigOutputString = ""
                                                            BigOutputString = Bigcheck(Listtemp,GoodsItem)
                                                            subject.sendMessage("@${senderName}"+BigOutputString.toString())
                                                        }else
                                                            if(message.contentToString().startsWith("/sg")){
                                                                var Arraylist=Array(6){IntArray(3)}
                                                                var (bengua,biangua,IndexNumber)=BenGua(Arraylist)
                                                                subject.sendMessage("->本卦："+NameofGua[IndexNumber[0]]+" "+XiangofGua[IndexNumber[0]]+"\n"+bengua+JieofGua[IndexNumber[0]]+"\n\n->变卦："+NameofGua[IndexNumber[1]]+" "+XiangofGua[IndexNumber[1]]+"\n"+biangua+JieofGua[IndexNumber[1]]+"\nTips:本卦是对现在某件事的描述，变卦是对这件事结果的描述")
                                                            }else
                                                                if(message.contentToString().startsWith("/img")){
                                                                    //subject.sendImage(.\\data\\IMG01AT.png,IMG01AT.png)
                                                                    val MapArr = listOf("F","M")
                                                                    val drawArr = mutableListOf<DrawItem>()
                                                                    for (layer in MapArr) {
                                                                        drawArr.add(
                                                                            DrawItem(
                                                                                type = layer,
                                                                                x = if (layer == "F") 40 else 0,
                                                                                y = if (layer == "F") 100 else 0,
                                                                                width = if (layer == "F") 30 else 400,
                                                                                height = if (layer == "F") 30 else 400
                                                                            )
                                                                        )
                                                                    }


                                                                    val image = BufferedImage(400, 400, BufferedImage.TYPE_4BYTE_ABGR)
                                                                    val g2d = image.createGraphics()
                                                                    drawArr.reversed().forEach {
                                                                        g2d.drawImage(
                                                                            layerMap[it.type],
                                                                            it.x,
                                                                            it.y,
                                                                            it.width,
                                                                            it.height,
                                                                            null
                                                                        )
                                                                    }
                                                                    g2d.dispose()
                                                                    val os = ByteArrayOutputStream()
                                                                    ImageIO.write(image, "png", os)
                                                                    ImageIO.write(image, "png", File("I:\\Jetbrains\\mirai-hello-world\\src\\main\\kotlin\\xsx.png"))
                                                                    subject.sendImage( ByteArrayInputStream(os.toByteArray()) as InputStream)
                                                                    subject.sendMessage(PlainText("哈哈，笨比不出图吧！"))

                                                                    //subject.sendMessage(net.mamoe.mirai.message.data.Audio())
                                                                    //subject.sendMessage(ByteArrayInputStream(os.toByteArray()) as InputStream)
                                                                }
            //SaveHashMap(LogUserKeyListPath,Datamap)

        }




    }

    data class DrawItem(
        val type: String,
        val x: Int,
        val y: Int,
        val width: Int,
        val height: Int
    )

    private fun BenGua(arraylist: Array<IntArray>):Triple<String, String,IntArray> {
        val randomnumber = listOf<Int>(0,1)
        var bengua=""
        var biangua=""
        val  yang = "_______"
        val  yin =  "___  ___"
        var IndexNumber = IntArray(2)
        for(i in 0..5){
            arraylist[i][0]=randomnumber.random().toInt()
            arraylist[i][1]=randomnumber.random().toInt()
            arraylist[i][2]=randomnumber.random().toInt()
            var sumarray =arraylist[i][0]+arraylist[i][1]+arraylist[i][2]
            if(sumarray==0){
                bengua=bengua+yin+"\n"
                IndexNumber[0]=IndexNumber[0]+0*Math.pow(2.0,i.toDouble()).toInt()
                biangua=biangua+yin+"\n"
                IndexNumber[1]=IndexNumber[1]+0*Math.pow(2.0,i.toDouble()).toInt()

            }else if(sumarray==1){
                bengua=bengua+yang+"\n"
                IndexNumber[0]=IndexNumber[0]+1*Math.pow(2.0,i.toDouble()).toInt()
                biangua=biangua+yang+"\n"
                IndexNumber[1]=IndexNumber[1]+1*Math.pow(2.0,i.toDouble()).toInt()

            }else if(sumarray==2){
                bengua=bengua+yin+"*\n"
                IndexNumber[0]=IndexNumber[0]+0*Math.pow(2.0,i.toDouble()).toInt()
                biangua=biangua+yang+"\n"
                IndexNumber[1]=IndexNumber[1]+1*Math.pow(2.0,i.toDouble()).toInt()
            }else if(sumarray==3){
                bengua=bengua+yang+"*\n"
                IndexNumber[0]=IndexNumber[0]+1*Math.pow(2.0,i.toDouble()).toInt()
                biangua=biangua+yin+"\n"
                IndexNumber[1]=IndexNumber[1]+0*Math.pow(2.0,i.toDouble()).toInt()
            }
        }
        return  Triple(bengua,biangua,IndexNumber)

    }


    private fun Bigcheck(listtemp: MutableList<Any>, goodsItem: List<String>): String {
        var BigOutputString = "您现在生命值为${listtemp[0].toString().toInt()}，金币有${listtemp[3].toString().toInt()}个，"

        for(i in 0..goodsItem.count()-1){
            BigOutputString=BigOutputString+"\n${goodsItem[i]} x ${listtemp[4+i].toString()}"
        }
        return  BigOutputString
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
            val Coins: Int = 5 + random.nextInt(5)  //随机金币数目

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
/*
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
}*/
