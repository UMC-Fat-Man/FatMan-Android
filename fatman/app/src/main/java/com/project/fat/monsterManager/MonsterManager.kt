package com.project.fat.monsterManager

import com.project.fat.data.monster.Monster
import kotlin.random.Random

object MonsterManager {
    val monsterList : List<Monster>  = listOf() // 임시
    fun getRandomMonster() : Monster {
        val randomIndex = Random.nextInt(0, monsterList.lastIndex)
        return monsterList[randomIndex]
    }
}