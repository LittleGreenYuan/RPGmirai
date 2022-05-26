package org.example.mirai.Server

class RPGExplore {
    companion object MyObject {

        val classConstructors = listOf<() -> Any>(
            ::getCommandName
        )
        fun getCommandName(): String {
            return "/explore"
        }
        fun ServerMain(): String {



            val outputServer = "dd"
            return outputServer
        }

    }

}
