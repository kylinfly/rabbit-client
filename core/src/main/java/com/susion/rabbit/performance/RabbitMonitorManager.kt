package com.susion.rabbit.performance

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import com.susion.rabbit.RabbitLog
import com.susion.rabbit.config.RabbitConfig
import com.susion.rabbit.config.RabbitSettings
import com.susion.rabbit.performance.core.RabbitMonitor
import com.susion.rabbit.performance.monitor.*

/**
 * susionwang at 2019-10-18
 * 所有监控的管理者
 */
internal object RabbitMonitorManager {

    private val TAG = "rabbit-monitor"
    private var mContext: Application? = null
    private var initStatus = false
    private var mConfig: RabbitConfig.MonitorConfig = RabbitConfig.MonitorConfig()
    private val monitorMap = HashMap<String, RabbitMonitor>()

    fun init(context: Application, config: RabbitConfig.MonitorConfig) {
        //只运行在主进程
        if (!isMainProcess(context) || initStatus) return

        mConfig = config
        mContext = context
        initStatus = true

        initMonitor()

        mConfig.autoOpenMonitors.forEach {
            RabbitSettings.setAutoOpenFlag(context, it, true)
        }

        monitorMap.values.forEach {
            val autoOpen = RabbitSettings.autoOpen(context, it.getMonitorInfo().name)
            if (autoOpen) {
                it.open(context)
                RabbitLog.d(TAG, "monitor auto open : ${it.getMonitorInfo().name} ")
            }
        }

    }

    private fun initMonitor() {
        monitorMap.apply {
            put(RabbitMonitor.APP_SPEED.name, RabbitAppSpeedMonitor())
            put(RabbitMonitor.FPS.name, RabbitFPSMonitor())
            put(RabbitMonitor.BLOCK.name, RabbitBlockMonitor())
            put(RabbitMonitor.MEMORY.name, RabbitMemoryMonitor())
//        put(RabbitMonitor.TRAFFIC.name, RabbitTrafficMonitor())
        }
    }

    fun openMonitor(name: String) {
        assertInit()
        monitorMap[name]?.open(mContext!!)
    }

    fun closeMonitor(name: String) {
        assertInit()
        monitorMap[name]?.close()
    }

    fun isOpen(name: String): Boolean {
        return monitorMap[name]?.isOpen() ?: false
    }

    private fun assertInit() {
        if (!initStatus) {
            throw RuntimeException("RabbitMonitorManager not call open!")
        }
    }

    fun monitorRequest(requestUrl: String): Boolean {
        val appSpeedMonitor = monitorMap[RabbitMonitor.APP_SPEED.name]
        if (appSpeedMonitor is RabbitAppSpeedMonitor) {
            return appSpeedMonitor.monitorRequest(requestUrl)
        }
        return false
    }

    fun markRequestFinish(requestUrl: String, costTime: Long) {
        val appSpeedMonitor = monitorMap[RabbitMonitor.APP_SPEED.name]
        if (appSpeedMonitor is RabbitAppSpeedMonitor) {
            appSpeedMonitor.markRequestFinish(requestUrl, costTime)
        }
    }

    private fun isMainProcess(context: Context): Boolean {
        return context.packageName == getCurrentProcessName(
            context
        )
    }

    private fun getCurrentProcessName(context: Context): String {
        val pid = android.os.Process.myPid()
        var processName = ""
        val manager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (process in manager.runningAppProcesses) {
            if (process.pid == pid) {
                processName = process.processName
            }
        }
        return processName
    }

    fun isAutoOpen(monitor: RabbitMonitor): Boolean {
        if (mContext == null) return false
        return RabbitSettings.autoOpen(mContext!!, monitor.getMonitorInfo().name)
    }

    fun getMonitorList() = monitorMap.values

}