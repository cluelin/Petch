package com.sideproject.petch.Pet

import java.util.prefs.Preferences

class Pet {
    var status : Int = 0
    var level : Int = 0
    var energy : Int = 0

    private val dataStore: DataStore<Preferences>

    companion object{
        const val LOW_ENERGY_STATUS = 0
        const val NORMAL_STATUS = 1
        const val FULL_ENERGY = 2
    }

}