package com.susion.rabbit.ui.global.entities

/**
 * susionwang at 2020-01-15
 */
class RabbitGlobalModePreInfo(
    var recordStartTime:String = "",
    var duration:String = "",
    var avgFps: Int = 60,
    var avgJVMMemory: Long = 0,
    var appColdStartTime: Long =0,
    var applicationCreateTime:Long = 0,
    var pageAvgInflateTime: Long = 0,
    var totalPageNumbe:Int = 0,
    var blockCount: Int = 0,
    var slowMethodCount: Int = 0,
    var isRunning:Boolean =false,
    var smoothEvaluateInfo: RabbitAppSmoothEvaluateInfo = RabbitAppSmoothEvaluateInfo()
)