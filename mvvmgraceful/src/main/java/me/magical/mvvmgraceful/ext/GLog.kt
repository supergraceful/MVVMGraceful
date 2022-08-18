package me.magical.mvvmgraceful.ext

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


object GLog {

    @JvmStatic
    var isShow = true

    private const val DEFAULT = "default"
    private val LINE_SEPARATOR = System.getProperty("line.separator")
    private const val JSON_INDENT = 4

    @JvmStatic
    fun v(tag: String? = null, msg: Any = DEFAULT) {
        printLog(TYPE.V, tag, msg)
    }

    @JvmStatic
    fun d(tag: String? = null, msg: Any = DEFAULT) {
        printLog(TYPE.D, tag, msg)
    }

    @JvmStatic
    fun i(tag: String? = null, msg: Any = DEFAULT) {
        printLog(TYPE.I, tag, msg)
    }

    @JvmStatic
    fun w(tag: String? = null, msg: Any = DEFAULT) {
        printLog(TYPE.W, tag, msg)
    }

    @JvmStatic
    fun e(tag: String? = null, msg: Any = DEFAULT) {
        printLog(TYPE.E, tag, msg)
    }

    @JvmStatic
    fun a(tag: String? = null, msg: Any = DEFAULT) {
        printLog(TYPE.A, tag, msg)
    }

    @JvmStatic
    fun json(msg: Any, tag: String? = null) {
        printLog(TYPE.JSON, tag, msg)
    }

    private fun printLog(type: TYPE, tag: String?, msg: Any?) {
        if (!isShow) return
        val stackTrace = Thread.currentThread().stackTrace
        val index = 4
        var mMsg = ""

        val fileName = stackTrace[index].fileName
        var methodName = stackTrace[index].methodName
        val lineNumber = stackTrace[index].lineNumber

        val mTag = tag ?: fileName
        methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1)
        val stringBuild = StringBuilder()
        stringBuild.apply {
            append("[ (")
            append(fileName)
            append(":")
            append(lineNumber)
            append(")#")
            append(methodName)
            append(" ] ")
        }
        mMsg = msg?.run {
            this.toString()
        } ?: let {
            "Log msg is null"
        }

        if (type != TYPE.JSON) {
            stringBuild.append(mMsg)
        }

        val lastMsg = stringBuild.toString()

        when (type) {
            TYPE.V -> Log.v(mTag, lastMsg)
            TYPE.D -> Log.d(mTag, lastMsg)
            TYPE.I -> Log.i(mTag, lastMsg)
            TYPE.W -> Log.w(mTag, lastMsg)
            TYPE.E -> Log.e(mTag, lastMsg)
            TYPE.A -> Log.wtf(mTag, lastMsg)
            TYPE.JSON -> {
                msg ?: also {
                    Log.d(mTag, "json is null")
                    return
                }

                var message: String? = null

                try {
                    if (mMsg.startsWith("{")) {
                        val jsonObject: JSONObject = JSONObject(mMsg)
                        message = jsonObject.toString(JSON_INDENT)
                    } else if (mMsg.startsWith("[")) {
                        val jsonArray = JSONArray(msg)
                        message = jsonArray.toString(JSON_INDENT)
                    }
                } catch (e: JSONException) {
                    e("""${e.cause!!.message}$msg""".trimIndent(), mTag)
                    return
                }

                printLine(mTag, true)
                message = lastMsg + LINE_SEPARATOR + message
                val lines: Array<String> =
                    message.split(LINE_SEPARATOR.toRegex())
                        .toTypedArray()
                val jsonContent = java.lang.StringBuilder()
                for (line in lines) {
                    jsonContent.append("║ ").append(line)
                        .append(LINE_SEPARATOR)
                }
                //Log.i(tag, jsonContent.toString());

                //Log.i(tag, jsonContent.toString());
                if (jsonContent.toString().length > 3200) {
                    Log.w(tag, "jsonContent.length = " + jsonContent.toString().length)
                    val chunkCount = jsonContent.toString().length / 3200
                    for (i in 0..chunkCount) {
                        val max = 3200 * (i + 1)
                        if (max >= jsonContent.toString().length) {
                            Log.w(tag, jsonContent.toString().substring(3200 * i))
                        } else {
                            Log.w(tag, jsonContent.toString().substring(3200 * i, max))
                        }
                    }
                } else {
                    Log.w(tag, jsonContent.toString())
                }
                printLine(mTag, false)
            }
        }

    }

    private fun printLine(tag: String, isTop: Boolean) {
        if (isTop) {
            Log.w(
                tag,
                "╔═══════════════════════════════════════════════════════════════════════════════════════"
            )
        } else {
            Log.w(
                tag,
                "╚═══════════════════════════════════════════════════════════════════════════════════════"
            )
        }
    }


    private enum class TYPE {
        V, D, I, W, E, A, JSON
    }
}