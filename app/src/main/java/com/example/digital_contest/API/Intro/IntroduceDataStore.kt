package com.example.digital_contest.API.Intro

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class IntroduceDataStore(private val context: Context) {
    companion object{//글쓰기 api호출시 생성되는 자동글을 여기에 작성.
        private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name="product_manager")
        private val INTRO_TEXT_KEY= stringPreferencesKey("introducetext")

        private val PRICE_KEY= intPreferencesKey("price")

    }

    suspend fun savaIntroduceText(introducetext:String){  //이함수에서 저장하고
                context.datastore.edit{preferences ->
                    preferences[INTRO_TEXT_KEY]=introducetext
                Log.i("저장체크","${preferences[INTRO_TEXT_KEY]}")}
    }

    suspend fun getIntroduceText():String?{  //여기서 가져오게 한다.
        val preferences=context.datastore.data.first()
        return preferences[INTRO_TEXT_KEY]

    }
    suspend fun savaIntroduceprice(price:Int){  //이함수에서 저장하고
        context.datastore.edit{preferences ->
            preferences[PRICE_KEY]=price
            Log.i("저장체크","${preferences[PRICE_KEY]}")}
    }

    suspend fun getIntroducePrice():Int{  //여기서 가져오게 한다.
        val preferences=context.datastore.data.first()
        return preferences[PRICE_KEY] ?:0

    }
}
