package com.susion.rabbit.tracer.transform.speed

import com.google.auto.service.AutoService
import com.susion.rabbit.tracer.AppStartTracer
import com.susion.rabbit.tracer.transform.core.RabbitClassTransformer
import com.susion.rabbit.tracer.transform.core.context.ArtifactManager
import com.susion.rabbit.tracer.transform.core.context.TransformContext
import com.susion.rabbit.tracer.transform.core.rxentension.className
import com.susion.rabbit.tracer.transform.core.rxentension.find
import com.susion.rabbit.tracer.transform.utils.ComponentHandler
import com.susion.rabbit.tracer.transform.utils.RabbitTransformPrinter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import javax.xml.parsers.SAXParserFactory

/**
 * susionwang at 2019-11-12
 *
 * 应用启动测速
 *
 * start time : Application Construct
 * end time : onCreate() end
 */
@AutoService(RabbitClassTransformer::class)
class AppStartSpeedMeasureTransform : RabbitClassTransformer {

    private val METHOD_ATTACH_CONTEXT_NAME = "attachBaseContext"
    private val METHOD_ATTACH_CONTEXT_DESC = "(Landroid/content/Context;)V"

    private val METHOD_ON_CREATE_NAME = "onCreate"
    private val METHOD_ONCREATE_DESC = "()V"

    private val applications = mutableSetOf<String>()

    override fun onPreTransform(context: TransformContext) {
        val parser = SAXParserFactory.newInstance().newSAXParser()
        context.artifacts.get(ArtifactManager.MERGED_MANIFESTS).forEach { manifest ->
            val handler = ComponentHandler()
            parser.parse(manifest, handler)
            applications.addAll(handler.applications)
        }

    }

    override fun transform(context: TransformContext, klass: ClassNode): ClassNode {

        if (!this.applications.contains(klass.className)) {
            return klass
        }

        insertApplicationStartRecordCode(klass)

        insertApplicationEndRecordCode(klass)

        return klass

    }

    private fun insertApplicationEndRecordCode(klass: ClassNode) {
        var onCreateMethod = klass.methods?.find {
            "${it.name}${it.desc}" == "$METHOD_ON_CREATE_NAME$METHOD_ONCREATE_DESC"
        }

        if (onCreateMethod == null) {
            onCreateMethod = getOnCreateMethod(klass)
            klass.methods.add(onCreateMethod)
        }

        onCreateMethod.instructions?.find(Opcodes.RETURN)?.apply {

            RabbitTransformPrinter.p("insert code to  ${onCreateMethod.name} --- ${this.opcode}")

            onCreateMethod.instructions?.insertBefore(
                this,
                MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    AppStartTracer.CLASS_PATH,
                    AppStartTracer.METHOD_RECORD_APPLICATION_CREATE_END,
                    "()V",
                    false
                )
            )
        }
    }

    private fun insertApplicationStartRecordCode(klass: ClassNode) {
        var attachMethod = klass.methods?.find {
            "${it.name}${it.desc}" == "$METHOD_ATTACH_CONTEXT_NAME$METHOD_ATTACH_CONTEXT_DESC"
        }

        if (attachMethod == null) {
            attachMethod = getAttachBaseContextMethod(klass)
            klass.methods.add(attachMethod)
        }

        attachMethod.instructions?.find(Opcodes.ALOAD)?.apply {

            RabbitTransformPrinter.p("insert code to  ${attachMethod.name} --- ${klass.name}")

            attachMethod.instructions?.insertBefore(
                this,
                MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    AppStartTracer.CLASS_PATH,
                    AppStartTracer.METHOD_RECORD_APPLICATION_CREATE_START,
                    "()V",
                    false
                )
            )
        }
    }


    private fun getAttachBaseContextMethod(klass: ClassNode): MethodNode {
        RabbitTransformPrinter.p("new Attach Method --> super class name : ${klass.superName}  --->")
        return MethodNode(
            Opcodes.ACC_PROTECTED,
            METHOD_ATTACH_CONTEXT_NAME,
            METHOD_ATTACH_CONTEXT_DESC,
            null,
            null
        ).apply {
            instructions.add(InsnList().apply {
                add(VarInsnNode(Opcodes.ALOAD, 0))
                add(VarInsnNode(Opcodes.ALOAD, 1))
                add(
                    MethodInsnNode(
                        Opcodes.INVOKESPECIAL,
                        klass.superName,
                        METHOD_ATTACH_CONTEXT_NAME,
                        METHOD_ATTACH_CONTEXT_DESC,
                        false
                    )
                )
                add(InsnNode(Opcodes.RETURN))
            })
            maxStack = 1
        }
    }

    private fun getOnCreateMethod(klass: ClassNode): MethodNode {
        RabbitTransformPrinter.p("new getOnCreateMethod Method --> super class name : ${klass.superName}  --->")
        return MethodNode(
            Opcodes.ACC_PUBLIC,
            METHOD_ON_CREATE_NAME,
            METHOD_ONCREATE_DESC,
            null,
            null
        ).apply {
            instructions.add(InsnList().apply {
                add(VarInsnNode(Opcodes.ALOAD, 0))
                add(
                    MethodInsnNode(
                        Opcodes.INVOKESPECIAL,
                        klass.superName,
                        METHOD_ON_CREATE_NAME,
                        METHOD_ONCREATE_DESC,
                        false
                    )
                )
                add(InsnNode(Opcodes.RETURN))
            })
            maxStack = 1
        }
    }

}
