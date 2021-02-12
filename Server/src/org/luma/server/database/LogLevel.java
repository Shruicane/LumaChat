package org.luma.server.database;


public enum LogLevel {
    MSG(0), NET(1), WARN(2), ERR(3);

    private final int level;

    LogLevel(int banlvl){
        level = banlvl;
    }

    public int getLogLvl(){
        return level;
    }

    public static LogLevel resolveLogLvl(int i){
        switch (i){
            case 1:
                return LogLevel.NET;
            case 2:
                return LogLevel.WARN;
            case 3:
                return LogLevel.ERR;
            default:
                return LogLevel.MSG;
        }
    }

    @Override
    public String toString() {
        switch (this.level){
            case 1:
                return "NET";
            case 2:
                return "WARN";
            case 3:
                return "ERR";
            default:
                return "MSG";
        }
    }
}
