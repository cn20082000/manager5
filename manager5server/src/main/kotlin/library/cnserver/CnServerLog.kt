package library.cnserver

import library.cnlog.CnLog

private val log = CnLog("CnServer")

fun cnServerLog(message: Any?) = log.print(message)

fun cnServerLog() = log.print()