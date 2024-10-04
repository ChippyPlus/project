import data.io.FileDescriptors
import data.memory.InternalMemory
import data.registers.Registers
import debugger.DebugEngine
import debugger.encoding.DebugFile
import engine.execution.Execute
import engine.parser
import engine.v2.Compile
import engine.v2.ExecutionV2
import environment.VMErrors
import internals.Vm
import kotlinx.serialization.json.Json
import optimisations.VarRedundancy
import java.io.File
import kotlin.system.exitProcess

const val STACK_LIMIT = 32
const val MEMORY_LIMIT = 64
val vm = Vm()

val errors = VMErrors()
val fileDescriptors = FileDescriptors()
val register = Registers()
val internalMemory = InternalMemory()
val systemRegisters = register.systemRegisters
val returnRegisters = register.returnRegisters
val generalRegisters = register.generalRegisters
val execute = Execute()
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: mvm <command> [options]")
        exitProcess(1)
    }
    when (args[0]) {
        "irun" -> {
            if (args.size < 2) {
                println("Usage: mvm irun <file.kar>")
                exitProcess(1)
            }
            execute.execute(File(args[1]))
        }

        "crun" -> {
            if (args.size < 2) {
                println("Usage: mvm crun <file.mar>")
                exitProcess(1)
            }
            ExecutionV2().execute(File(args[1]).readText())
        }

        "run" -> {
            if (args.size < 2) {
                println("Usage: mvm run <file.kar>")
                exitProcess(1)
            }
            ExecutionV2().execute(Compile().execute(parser(File(args[1]))))
        }

        "tokenise" -> {
            if (args.size < 2) {
                println("Usage: mvm tokenise <file.kar>")
                exitProcess(1)
            }
            println(parser(File(args[1])))
        }


        "otokenise" -> {
            if (args.size < 2) {
                println("Usage: mvm tokenise <file.kar>")
                exitProcess(1)
            }
            println(VarRedundancy(globalInfo = parser(File(args[1]))).removeRedundancy())
        }

        "compile" -> {
            if (args.size < 2) {
                println("Usage: mvm compile <file.kar>")
                exitProcess(1)
            }
            val optimised = VarRedundancy(globalInfo = parser(File(args[1]))).removeRedundancy()
            val out = Compile().execute(optimised)
            val f = File(args[1].split(".")[0] + ".mar")
            f.createNewFile()
            f.writeText(out)
            println("Compiled with 0 Issues!!!!!")
        }

        "debug" -> {// TODO Make this an option
            if (args.size < 3) {
                println("Usage: mvm debug <debugFile.json> <file.kar>")
                exitProcess(1)
            }
            val debugEngine = DebugEngine(Json.decodeFromString<DebugFile>(File(args[1]).readText()))
            execute.execute(File(args[2]), debugEngine)
        }

        "help" -> {
            println(
                "mvm irun <file.kar> - Runs KAR code in interpreter mode\n" + "mvm compile <file.kar> - Compiles the KAR code into the byte code stored in file.mar\n" + "mvm crun <file.mar> - Runs compiled code\n" + "mvm run <file.kar> - Compiles and runs code without creating a file\n" + "mvm tokenise <file.kar> - Shows the tokenised version of the code to the terminal\n" + "mvm otokenise <file.kar> - Shows the optimised tokenised version of the code to the terminal\n"
            )
        }

        else -> {
            println("Usage: mvm <command> <arguments...>")
            exitProcess(1)
        }
    }
}


