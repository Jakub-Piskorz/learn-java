package com.jakubpiskorz.learnjava.logger;

public class SimpleLoggerService implements LoggerServiceInterface {
  @Override

  public void quietLog(String msg) {
    System.out.print(msg);
  }

  @Override
  public void loudLog(String msg) {
    System.out.print("The message is: " + msg);
  }
}
