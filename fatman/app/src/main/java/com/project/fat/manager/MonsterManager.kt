package com.project.fat.manager

import android.util.Log
import com.project.fat.data.marker.Marker
import com.project.fat.data.monster.Monster
import kotlin.random.Random

object MonsterManager {
    private val MONSTERLIST : List<Monster>  = listOf(
        Monster(1,"몬스터1", com.project.fat.R.drawable.monster1, "model/fatcell.glb"),
        Monster(2, "몬스터2", com.project.fat.R.drawable.monster2, "model/cell_test.glb"),
        Monster(3, "몬스터3", com.project.fat.R.drawable.monster3, "model/blue_cell.glb")
    )

    private const val MAXINDEXREADYMONSTERLIST = Marker.MAXNUM_MARKER
    private var readyMonsterList : List<Monster> = listOf()

    fun deleteMonster(monster: Monster){
        readyMonsterList = readyMonsterList.drop(monster.id)
    }

    fun getReadyMonsterList() = readyMonsterList

    fun getReadyMonster(index : Int) = readyMonsterList[index]

    fun setRandomMonster() {
        if(readyMonsterList.lastIndex == MAXINDEXREADYMONSTERLIST)
            return
        for(i in 0..MAXINDEXREADYMONSTERLIST){
            val randomIndex = Random.nextInt(0, MONSTERLIST.size)
            Log.d("setRandomMonster", "randomIndex = $randomIndex")
            readyMonsterList = readyMonsterList.plus(MONSTERLIST[randomIndex])
        }
    }
}