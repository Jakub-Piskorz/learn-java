package com.jakubpiskorz.learnjava.logger;

public class LoggerProcessor implements Logger {
  private LoggerServiceInterface loggerService;

  public LoggerProcessor(LoggerServiceInterface service) {
    loggerService = service;
  }

  @Override
  public void quietLog(String msg) {
    loggerService.quietLog(msg);

  }

  @Override
  public void loudLog(String msg) {
    loggerService.loudLog(msg);
  }
}
