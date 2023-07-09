package library.cnnavigation

import library.cnlog.CnLog

private val log = CnLog("CnNavigation")

fun cnNavigationLog(message: Any?) = log.print(message)

fun cnNavigationLog() = log.print()