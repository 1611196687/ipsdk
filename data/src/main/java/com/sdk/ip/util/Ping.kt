package com.sdk.ip.util

import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Ping命令
 */
class Ping(
    /** 目标主机域名/IP地址  */
    private val host: String,
    /** 数据包个数，默认连续发送  */
    private val count: Int? = null,
    /** 数据包大小，单位bytes，默认为56 bytes  */
    private val packetSize: Int? = null,
    /** 数据包生存时间 */
    private val ttl: Int? = null,
    /** 超时间隔，单位s */
    private val deadline: Int? = null
) {

    /**
     * ## 执行Ping命令
     * 请注意，ping命令在Linux系统下的参数与在Windows系统下有差异，需要区分
     * -c count ping指定次数后停止ping；
     * -s packetsize 指定每次ping发送的数据字节数，默认为“56字节”+“28字节”的ICMP头，一共是84字节；
     */
    fun execute(callback: ExecuteCallback? = null): String {
        val command = toString()
        // 回调输出执行的Ping命令
        callback?.onExecuting("% $command\n")

        val result = StringBuilder()
        var process: Process? = null
        var reader: BufferedReader? = null
        try {
            process = Runtime.getRuntime().exec(command)
            reader = BufferedReader(InputStreamReader(process.inputStream))
            // 读取首行输出内容
            var line = reader.readLine()
            while (line != null) {
                // 回调执行过程的输出内容
                callback?.onExecuting(line)
                // 记录输出行到结果字符串
                result.append(line).append("\n")
                // 读取下一行输出内容
                line = reader.readLine()
            }
            callback?.onCompleted(result.toString())
            reader.close()
            process.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            reader?.close()
            process?.destroy()
        }

        return result.toString()
    }

    /**
     * ## 根据构造字段将实体转换为具体的Ping命令
     * 判断各字段非
     */
    override fun toString(): String {
        val stringBuilder = StringBuilder("ping")
        if (count != null) stringBuilder.append(" -c $count")
        if (packetSize != null) stringBuilder.append(" -s $packetSize")
        if (ttl != null) stringBuilder.append(" -t $ttl")
        if (deadline != null) stringBuilder.append(" -w $deadline")
        stringBuilder.append(" $host")
        return stringBuilder.toString()
    }

}