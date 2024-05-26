package com.jakubpiskorz.learnjava;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jakubpiskorz.learnjava.logger.LoggerProcessor;
import com.jakubpiskorz.learnjava.logger.Logger;
import com.jakubpiskorz.learnjava.logger.NewLineLoggerService;
import com.jakubpiskorz.learnjava.logger.SimpleLoggerService;

@SpringBootApplication
public class MainApplication {

  public static void main(String[] args) {
    Logger simpleLogger = new LoggerProcessor(new SimpleLoggerService());
    simpleLogger.loudLog("Loud simple log.");
    simpleLogger.quietLog("Quiet simple log.");

    Logger newLineLogger = new LoggerProcessor(new NewLineLoggerService());
    newLineLogger.loudLog("Loud simple log.");
    newLineLogger.quietLog("Quiet simple log.");
  }

}
