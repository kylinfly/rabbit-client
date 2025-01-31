package com.susion.rabbit.base

import android.app.Activity
import android.view.View
import com.susion.rabbit.base.config.RabbitConfig
import okhttp3.Interceptor

/**
 * susionwang at 2020-01-10
 * 外部直接可使用的API
 */
interface RabbitProtocol {

    fun init(config: RabbitConfig)

    fun reConfig(config: RabbitConfig)

    fun getCurrentActivity():Activity?

    fun isAutoOpen():Boolean

    fun changeAutoOpenStatus(autoOpen:Boolean)

    fun saveCrashLog(e:Throwable)

    fun getNetInterceptor(): Interceptor

    fun open(requestPermission: Boolean = true, activity: Activity)

    fun openPage(pageClass: Class<out View>?, params: Any? = null)

}