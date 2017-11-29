package infra

import play.Logger

object SQLDebugger {
  def log(sql: String) = {
    Logger.error("=======================start error SQL=====================")
    Logger.error(s"$sql")
    Logger.error("=======================end error SQL=======================")
  }
}
