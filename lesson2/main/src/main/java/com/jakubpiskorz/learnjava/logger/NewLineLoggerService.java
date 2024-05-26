package com.jakubpiskorz.learnjava.logger;

public class NewLineLoggerService implements LoggerServiceInterface {
  @Override
  public void quietLog(String msg) {
    System.out.println(msg);
  }

  @Override
  public void loudLog(String msg) {
    System.out.println("The message is: " + msg);
  }
}
