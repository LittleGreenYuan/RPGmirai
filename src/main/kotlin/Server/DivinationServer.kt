package org.example.mirai.Server

import org.example.mirai.BotServer.UserUtil
import org.example.mirai.WithoutConfiguration

class DivinationServer {

    val NameofGua=listOf("乾卦","坤卦" ,"屯卦" ,"蒙卦" ,"需卦" ,"讼卦" ,"师卦" ,"比卦" ,"小畜卦" ,"履卦" ,"泰卦" ,"否卦" ,"同人卦" ,"大有卦" ,"谦卦" ,"豫卦" ,"随卦" ,"蛊卦" ,"临卦" ,"观卦" ,"噬嗑卦" ,"贲卦" ,"剥卦" ,"复卦"
        ,"无妄卦" ,"大畜卦" ,"颐卦" ,"大过卦" ,"坎卦" ,"离卦" ,"咸卦" ,"恒卦" ,"遁卦" ,"大壮卦" ,"晋卦" ,"明夷卦" ,"家人卦" ,"睽卦" ,"蹇卦" ,"解卦" ,"损卦","益卦" ,"夬卦" ,"姤卦" ,"萃卦" ,"升卦" ,"困卦" ,"井卦"
        ,"革卦" ,"鼎卦" ,"震卦" ,"艮卦" ,"渐卦" ,"归妹卦" ,"丰卦" ,"旅卦" ,"巽卦" ,"泽卦" ,"涣卦" ,"节卦" ,"中孚卦" ,"小过卦" ,"既济卦" ,"未济卦")
    val XiangofGua=listOf("上上","上上","下下","中下","中上","中下","中上","上上","下下","中上","中中","中中","中上","上上","中中","中中","中中","中中","中上","中上","上上","中上","中下","中中","下下","中上"
        ,"上上","中下","下下","中上","中上","中上","下下","中上","中上","中下","下下","下下","下下","中上","下下","上上","上上","上","中上","上上","中上","上上","上上","中下","中上","中下","上上","下下"
        , "上上","下下","中上","上上","下下","上上","下下","中上","中上","中下")
    val JieofGua=listOf("困龙得水好运交，不由喜气上眉梢，一切谋望皆如意，向后时运渐渐高。","肥羊失群入山岗，饿虎逢之把口张，适口充肠心欢喜，卦若占之大吉昌。","风刮乱丝不见头，颠三倒四犯忧愁，慢从款来左顺遂，急促反惹不自由。","卦中爻象犯小耗，君子占之运不高，婚姻合伙有琐碎，做事必然受苦劳。","明珠土埋日久深，无光无亮到如今，忽然大风吹土去，自然显露有重新。","心中有事事难做，恰是二人争路走，雨下俱是要占先，谁肯让谁走一步。","将帅领旨去出征，骑着烈马拉硬弓，百步穿杨去得准，箭中金钱喜气生。","顺风行船撒起帆，上天又助一蓬风，不用费力逍遥去，任意而行大亨通。","苗逢旱天尽焦梢，水想云浓雨不浇，农人仰面长吁气，是从款来莫心高。","凤凰落在西岐山，长鸣几声出圣贤，天降文王开基业，富贵荣华八百年。","学文满腹入场闱，三元及第得意回，从今解去愁和闷，喜庆平地一声雷。","虎落陷坑不堪言，进前容易退后难，谋望不遂自己便，疾病口舌事牵连。","心中有事犯猜疑，谋望从前不着实，幸遇明人来指引，诸般忧闷自消之。","砍树摸雀作事牢，是非口舌自然消，婚姻合伙不费力，若问走失未逃脱。","天赐贫人一封金，不争不抢两平分，彼此分得金到手，一切谋望皆遂心。","太公插下杏黄旗，收妖为徒归西岐，自此青龙得了位，一旦谋望百事宜。","泥里步踏这几年，推车靠崖在眼前，目下就该再使力，扒上崖去发财源。","卦中爻象如推磨，顺当为福反为祸，心中有益且迟迟，凡事尽从忙处错。","君王无道民倒悬，常想拨云见青天，幸逢明主施仁政，重又安居乐自然。","卦遇蓬花旱逢河，生意买卖利息多，婚姻自有人来助，出门永不受折磨。","运拙如同身受饥，幸得送饭又送食，适口充腹心欢喜，忧愁从此渐消移。","钟鼓乐之大吉庆，占者逢之喜临头。","鹊遇天晚宿林中，不知林内先有鹰，虽然同处心生恶，卦若逢之是非轻。","马氏太公不相合，世人占之忧疑多，恩人无义反为怨，是非平地起风波。","飞鸟失机落笼中，纵然奋飞不能腾，目下只宜守本分，妄想扒高万不能。","忧愁常锁两眉头，千头万绪挂心间，从今以后防开阵，任意行而不相干。","太公独钓渭水河，手执丝杆忧愁多，时来又遇文王访，自此永不受折磨。","夜晚梦里梦金银，醒来仍不见一文，目下只宜求本分，思想络是空劳神。","一轮明月照水中，只见影儿不见踪，愚夫当财下去取，摸来摸去一场空。","官人来占主高升，庄农人家产业增，生意买卖利息厚，匠艺占之大亨通。","运去黄金失色，时来棒槌发芽，月令极好无差，且喜心宽意大。","渔翁寻鱼运气好，鱼来撞网跑不了，别人使本挣不来，谁想一到就凑合。","浓云蔽日不光明，劝君且莫出远行，婚姻求财皆不利，提防口舌到门庭。","卦占工师得大木，眼前该着走上路，时来运转多顺当，有事自管放心宽。","锄地锄去苗里草，谁想财帛将人找，一锄锄出银子来，这个运气也算好。","时乖运拙走不着，急忙过河拆了桥，恩人无义反为怨，凡事无功枉受劳。","一朵鲜花镜中开，看着极好取不来，劝君休把镜花恋，卦若逢之主可怪。","此卦占来运气歹，如同太公作买卖，贩猪牛快贩羊迟，猪羊齐贩断了宰。","大雨倾地雪满天，路上行人苦又寒，拖泥带水费尽力，事不遂心且耐烦。","目下月令如过关，千辛万苦受熬煎，时来恰相有人救，任意所为不相干。","时运不至费心多，比作推车受折磨，山路崎岖吊下耳，左插右按按不着。","时来运转吉气发，多年枯木又开花，枝叶重生多茂盛，几人见了几人夸。","蜘蛛脱网赛天军，粘住游蜂翅翎毛，幸有大风吹破网，脱离灾难又逍遥。","他乡遇友喜气欢，须知运气福重添，自今交了顺当运，向后管保不相干。","游鱼戏水被网惊，跳过龙门身化龙，三尺杨柳垂金线，万朵桃花显你能。","士人来占必得名，生意买卖也兴隆，匠艺逢之交易好，农间庄稼亦收成。","时运不来好伤怀，撮上押去把梯抬，一筒虫翼无到手，转了上去下不来。","枯井破费已多年，一朝流泉出来鲜，资生济渴人称羡，时来运转喜自然。","苗逢旱天渐渐衰，幸得天恩降雨来，忧去喜来能变化，求谋干事遂心怀。","莺鹜蛤蜊落沙滩，蛤蜊莺鹜两翅扇，渔人进前双得利，失走行人却自在。","一口金钟在淤泥，人人拿着当玩石，忽然一日钟悬起，响亮一声天下知。","财帛常打心头走，可惜眼前难到手，不如意时且忍耐，逢着闲事休开口。","俊鸟幸得出笼中，脱离灾难显威风，一朝得意福力至，东西南北任意行。","求鱼须当向水中，树上求之不顺情，受尽爬揭难随意，劳而无功运平平。","古镜昏暗好几年，一朝磨明似月圆，君子谋事逢此卦，时来运转喜自然。","飞鸟树上垒窝巢，小人使计举火烧，君占此卦为不吉，一切谋望枉徒劳。","一叶孤舟落沙滩，有篙无水进退难，时逢大雨江湖溢，不用费力任往返。","这个卦象真可取，觉着做事不费力，休要错过这机关，事事觉得随心意。","隔河望见一锭金，欲取岸宽水又深，指望资财难到手，昼思夜想枉费心。","时来运转喜气生，登台封神姜太公，到此诸神皆退位，纵然有祸不成凶。","路上行人色匆匆，急忙无桥过薄冰，小心谨慎过得去，一步错了落水中。","行人路过独木桥，心内惶恐眼里瞧，爽利保你过得去，慢行一定不安牢。","金榜以上题姓名，不负当年苦用功，人逢此卦名吉庆，一切谋望大亨通。","离地着人几丈深，是防偷营劫寨人，后封太岁为凶煞，时加谨慎祸不侵。")

    fun BenGua(arraylist: Array<IntArray>):Triple<String, String,IntArray> {
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

    companion object MyObject {

        val classConstructors = listOf<() -> Any>(
            ::getCommandName
        )
        fun getCommandName(): String {
            return "/sg"
        }
        fun ServerMain(){
            val DivinationServer=DivinationServer()
            var Arraylist=Array(6){IntArray(3)}
            var (bengua,biangua,IndexNumber)= DivinationServer.BenGua(Arraylist)
            val serveroutput ="->本卦："+DivinationServer.NameofGua[IndexNumber[0]]+" "+DivinationServer.XiangofGua[IndexNumber[0]]+"\n"+bengua+DivinationServer.JieofGua[IndexNumber[0]]+"\n\n->变卦："+DivinationServer.NameofGua[IndexNumber[1]]+" "+DivinationServer.XiangofGua[IndexNumber[1]]+"\n"+biangua+DivinationServer.JieofGua[IndexNumber[1]]+"\nTips:本卦是对现在某件事的描述，变卦是对这件事结果的描述"
            UserUtil.readData.uploadmessage(serveroutput)

        }



    }

}