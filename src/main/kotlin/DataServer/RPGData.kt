package org.example.mirai.DataServer

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.User
import org.example.mirai.RaidMoster
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.HashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class RPGData {
    //这里存储了所有RPG需要用到的数据
    companion object {
        val detime = 1//执行备份任务的时间，DAY
        val HelpWord= listOf("/help: 进行操作指南查询。\n/RPG：前往冒险者酒馆进行文字RPG冒险活动。\n/explore：开展探索活动获得随机奖励。\n/operation：开展随机副本探索，挑战随机怪物获得奖励。" +
            "\n/spring：花费20金币进行生命恢复。\n/mission：查看冒险者任务公告。\n/submit：提交相应冒险者任务。\n/raid：查询大型副本状态。\n/raidattack：一定时间内，对RaidBoss发起一次进攻。")

        val UserKeyListPath = System.getProperty("user.dir")+"\\src\\main\\kotlin\\UserKey.txt"
        val LogUserKeyListPath = System.getProperty("user.dir")+"\\src\\main\\kotlin\\"
        //按行读取TXT中的文本内容，每一行是一个LIST
        val UserDATA = File(UserKeyListPath).readLines()
        val Datamap: HashMap<Long, List<Any>> = HashMap<Long,List<Any>>() //define empty hashmap
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

        fun RPGUserDataUpload(){
            //将txt文件中读取到的用户数据转换成Datamp Hash表
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
            //每一次加载数据（相当于重启Bot）后添加一个自动保存线程
            val executor = Executors.newScheduledThreadPool(1)//创建一个协程
            executor.scheduleAtFixedRate(Runnable{
                //println("已经设置了自动保存！")
                runBlocking {
                    val current = LocalDateTime.now()
                    val formatter = DateTimeFormatter.BASIC_ISO_DATE
                    val formatted = current.format(formatter)

                    //val fileName = LogUserKeyListPath + "UserKey.txt"
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
            } , detime.toLong(), detime.toLong(), TimeUnit.MINUTES)

        }

    }
}