package library.cninjector

import library.cnlog.CnLog

private val log = CnLog("CnInjector")

fun cnInjectorLog(message: Any?) = log.print(message)

fun cnInjectorLog() = log.print()