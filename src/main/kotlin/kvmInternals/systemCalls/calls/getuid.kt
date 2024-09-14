package org.example.kvmInternals.systemCalls.calls

import org.example.data.registers.enumIdenifiers.SuperRegisterType
import org.example.helpers.fullRegisterWrite
import org.example.kvmInternals.systemCalls.SystemCall
import java.io.BufferedReader
import java.io.InputStreamReader

fun SystemCall.getUid() {
    val p: Process = Runtime.getRuntime().exec("id -u")
    val reader = BufferedReader(InputStreamReader(p.inputStream))
    val line: String = reader.readLine()
    val uid = line.toInt()
    fullRegisterWrite(SuperRegisterType.R2, uid)
}