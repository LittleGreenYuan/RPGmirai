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




            return ""
        }

    }

}
