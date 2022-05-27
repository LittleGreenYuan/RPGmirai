package org.example.mirai.Server

import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import org.example.mirai.BotServer.UserData
import org.example.mirai.BotServer.UserUtil
import org.example.mirai.CommandServer.MessageManager
import java.io.File



class HlepServer {
    companion object MyObject {

        val classConstructors = listOf<() -> Any>(
            ::getCommandName
        )
        fun getCommandName(): String {
            return "/help"
        }
        private suspend fun ServerMain(){
            var (pairnumber,sender,senderName)=UserUtil.readData()
            var (event,message)=pairnumber

            if(sender.id == UserData.RootList[2].toLong()){
                /*val Mapimg = File(System.getProperty("user.dir")+"\\src\\main\\kotlin\\data\\IMG01AT.png")
                UserUtil.readData.uploadfileImage(Mapimg.toExternalResource())*/

                //这里也可以进行自动化查询每个Server中预先保存的帮助文本
                val outputServer = "/help: 进行操作指南查询。\n/RPG：前往冒险者酒馆进行文字RPG冒险活动。\n/explore：开展探索活动获得随机奖励。\n/operation：开展随机副本探索，挑战随机怪物获得奖励。" +
                    "\n/spring：花费20金币进行生命恢复。\n/mission：查看冒险者任务公告。\n/submit：提交相应冒险者任务。\n/raid：查询大型副本状态。\n/raidattack：一定时间内，对RaidBoss发起一次进攻。"+
                    "\n/bag：查询当前背包。\n/r6：进行彩虹六号的地图的查询，“/r6 地图名 子地图名”或“/r6 地图名。”\n/sg：进行金钱卦的占卜。\n/save：手动保存当前RPG游戏数据。"
                //event.subject.sendMessage(outputServer)
                //MessageManager.proactiveresponse(event)
                UserUtil.readData.uploadmessage(outputServer)

            }else{
                val outputServer = "/help: 进行操作指南查询。\n/RPG：前往冒险者酒馆进行文字RPG冒险活动。\n/explore：开展探索活动获得随机奖励。\n/operation：开展随机副本探索，挑战随机怪物获得奖励。" +
                    "\n/spring：花费20金币进行生命恢复。\n/mission：查看冒险者任务公告。\n/submit：提交相应冒险者任务。\n/raid：查询大型副本状态。\n/raidattack：一定时间内，对RaidBoss发起一次进攻。"+
                    "\n/bag：查询当前背包。\n/r6：进行彩虹六号的地图的查询，“/r6 地图名 子地图名”或“/r6 地图名。”\n/sg：进行金钱卦的占卜。"
                UserUtil.readData.uploadmessage(outputServer)

            }
        }

    }
}