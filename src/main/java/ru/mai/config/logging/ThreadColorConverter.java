package ru.mai.config.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class ThreadColorConverter extends ClassicConverter {

    // ANSI-коды цветов
    private static final String RESET = "\u001B[0m";
    private static final String BLACK_WHITE = "\u001B[97;40m"; // white text, black background
    private static final String BLACK_PURPLE = "\u001B[95;40m"; // white text, black background
    private static final String BLACK_BLUE = "\u001B[94;40m"; // white text, black background

    @Override
    public String convert(ILoggingEvent event) {
        String threadName = event.getThreadName();

        if (threadName.matches(".*[Ww]orkshopScheduledExecutor.*")) {
            return BLACK_WHITE + threadName + RESET;
        } else if (threadName.matches(".*[Rr]epairingScheduledExecutor.*")) {
            return BLACK_PURPLE + threadName + RESET;
        } else if (threadName.matches(".*[Rr]epairingExecutor.*")) {
            return BLACK_BLUE + threadName + RESET;
        } else {
            return threadName;
        }
    }

}
