package com.susion.rabbit.tracer.transform

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.builder.model.AndroidProject
import com.susion.rabbit.tracer.transform.core.context.RabbitTransformInvocation
import com.susion.rabbit.tracer.transform.core.rxentension.file
import com.susion.rabbit.tracer.transform.utils.RabbitTransformPrinter
import java.util.concurrent.TimeUnit

/**
 * susionwang at 2019-11-12
 */
class RabbitTransform : Transform() {

    override fun getName() = "rabbit-speed-tracer"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> =
        TransformManager.CONTENT_CLASS

    override fun isIncremental() = true

    override fun getScopes(): MutableSet<QualifiedContent.ScopeType> = TransformManager.SCOPE_FULL_PROJECT

    override fun transform(transformInvocation: TransformInvocation?) {

        if (transformInvocation == null) return

        RabbitTransformPrinter.p("rabbit RabbitFirstTransform run ")

        RabbitTransformInvocation(transformInvocation).apply {
            if (isIncremental) {
                onPreTransform(this)
                doIncrementalTransform()
            } else {
                //删除老的构建内容
                buildDir.file(AndroidProject.FD_INTERMEDIATES, "transforms", "dexBuilder")
                    .let { dexBuilder ->
                        if (dexBuilder.exists()) {
                            dexBuilder.deleteRecursively()
                        }
                    }
                outputProvider.deleteAll()
                onPreTransform(this)
                doFullTransform()
            }

            onPostTransform(this)

        }.executor.apply {
            shutdown()
            awaitTermination(1, TimeUnit.MINUTES)
        }
    }

}