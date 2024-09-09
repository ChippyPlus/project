package org.example.kvmInternals.systemCalls.calls

import org.example.data.registers.enumIdenifiers.SuperRegisterType
import org.example.fileDescriptors
import org.example.helpers.*
import org.example.kvmInternals.classes.SystemCall
import java.io.File

fun SystemCall.openFile(registerPath: SuperRegisterType, flag: SuperRegisterType) {
    val path = readRegisterString(registerPath)
    val f = File(path)
    val fd = fileDescriptors.addFileDescriptor(VMFile(f, fileFlags[fullRegisterRead(flag)]!!))
    fullRegisterWrite(SuperRegisterType.R2, fd)

}