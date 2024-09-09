package org.example.kvmInternals.classes

import org.example.data.registers.enumIdenifiers.SuperRegisterType
import org.example.helpers.fullRegisterRead
import org.example.kvmInternals.systemCalls.calls.*


/**
 * **System Call Instruction:**
 *
 * * `SYSCALL` - Execute a system call (requires system call number and arguments).
 */
class SystemCall {
    fun execute(callId: SuperRegisterType, s2: SuperRegisterType, s3: SuperRegisterType, s4: SuperRegisterType) {
        when (fullRegisterRead(callId)) {
            1 -> readFile(s2,s3,s4)
            3 -> openFile(s2,s3)
            6 -> exit(s2)
            25 -> writeIo(s2)
            else -> throw RuntimeException("Invalid system call ID: $callId")
        }

    }
}