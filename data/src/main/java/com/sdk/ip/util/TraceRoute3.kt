package com.sdk.ip.util

import android.util.Log
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * TraceRoute命令
 * <p>
 * 由于Android的非Root设备不支持直接使用命令行工具API执行TraceRoute命令，因此改用执行Ping命令
 * 并通过限定TTL参数(IP包被路由器丢弃之前允许通过的最大网段数)来达到相等效果
 * 路由器地址通过正则表达式匹配从Ping响应内容中截取，
 * 路由耗时通过执行Ping命令前后时间戳对比估算
 */
class TraceRoute3(
    /** 目标主机域名  */
    private val host: String
) {
    var TAG = this::class.java.simpleName

    companion object {
        /** IP包被路由器丢弃之前允许通过的最大网段数  */
        const val MAX_HOP = 30

        /** 正则表达式-路由器IP地址 */
        private const val REGEX_ROUTE_IP = "(?<=From )(?:[0-9]{1,3}\\.){3}[0-9]{1,3}"
        /** 正则表达式-目标主机IP地址 */
        private const val REGEX_HOST_IP = "(?<=from ).*(?=: icmp_seq=1 ttl=)"
        /** 正则表达式-数据包往返时间 */
        private const val REGEX_RRT = "(?<=time=).*?ms"
    }

    /**
     * 执行Ping命令模拟TraceRoute流程
     * -c count ping指定次数后停止ping；
     * -t 设置TTL(Time To Live，生存时间)为指定的值。该字段指定IP包被路由器丢弃之前允许通过的最大网段数；
     */
    fun execute(callback: ExecuteCallback? = null) {
        val command = toString();
        callback?.onExecuting("% $command\n")

        callback?.onExecuting("traceroute to $host, 30 hos max, 40 byte packets\n")

        // 当前跃点数
        var hop = 1
        // 终止标识
        var done = false

        val lineBuilder1 = StringBuilder()
        while (!done && hop <= MAX_HOP) {
            val pingResult = Ping(host, packetSize = 40, count = 1, ttl = hop, deadline = 5).execute()
            Log.d(TAG, "ping host ip: $pingResult \n\n")

            val lineBuilder = StringBuilder()
            lineBuilder.append(hop).append(".")

            // 用正则表达式匹配响应内容行
            val routerIpMatcher = matchRouterIp(pingResult)
            if (routerIpMatcher.find()) {   // 匹配到了路由器IP地址，打印路由器IP地址及到达该路由器的耗时
                val routerIp = subRouteIpString(routerIpMatcher)
                lineBuilder.append("\t\t").append(routerIp)

                val pingResult = Ping(host = routerIp, packetSize = 40, count = 3, deadline = 5).execute()
                Log.d(TAG, "ping route ip: $pingResult \n\n")
                matchAndAppendRTT(pingResult, lineBuilder)
            } else {    // 匹配不到
                val hostIpMatcher = matchHostIp(pingResult)
                if(hostIpMatcher.find()) {
                    val hostIp = hostIpMatcher.group()
                    lineBuilder.append("\t\t").append(hostIp)

                    val pingResult = Ping(host = hostIp, packetSize = 40, count = 3, deadline = 5).execute()
                    Log.d(TAG, "ping host ip: $pingResult \n\n")
                    matchAndAppendRTT(pingResult, lineBuilder)
                    done = true
                } else {
                    lineBuilder.append("\t\t *\t\t*\t\t* \t")
                }
            }

            lineBuilder1.append(lineBuilder.toString()+"\n")
            callback?.onExecuting(lineBuilder.toString())

            hop++
        }
        callback?.onFinifh(lineBuilder1.toString())
    }

    /**
     * 匹配并记录数据包往返时间
     */
    private fun matchAndAppendRTT(pingResult: String, lineBuilder: StringBuilder) {
        val rttMatcher = matchRTT(pingResult)
        lineBuilder.append("\t\t")
        var i = 0
        while(i < 3) {
            if(rttMatcher.find()) {
                val rtt = rttMatcher.group()
                lineBuilder.append(rtt).append("\t\t")
            } else {
                lineBuilder.append("*").append("\t\t")
            }
            i++
        }
        lineBuilder.append("\t")
    }

    /**
     * 匹配路由器IP地址
     */
    private fun matchRouterIp(input: CharSequence) = Pattern.compile(REGEX_ROUTE_IP).matcher(input)

    /**
     * 匹配数据包往返时间
     */
    private fun matchRTT(input: CharSequence) = Pattern.compile(REGEX_RRT).matcher(input)

    /**
     * 匹配目标主机IP地址
     */
    private fun matchHostIp(input: CharSequence) =  Pattern.compile(REGEX_HOST_IP).matcher(input)

    /**
     * 截取路由器IP字符串
     */
    private fun subRouteIpString(matcher: Matcher): String {
        var pingIp = matcher.group()
        val start = pingIp.indexOf('(')
        if (start >= 0) {
            pingIp = pingIp.substring(start + 1)
        }
        return pingIp
    }

    override fun toString(): String {
        return "traceroute $host"
    }
}