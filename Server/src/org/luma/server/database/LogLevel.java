package org.luma.server.database;


public enum LogLevel {
    MESSAGE(0), ADMINISTRATION(1), NETWORK(2), WARNING(3), ERROR(4);

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
                return LogLevel.ADMINISTRATION;
            case 2:
                return LogLevel.NETWORK;
            case 3:
                return LogLevel.WARNING;
            case 4:
                return LogLevel.ERROR;
            default:
                return LogLevel.MESSAGE;
        }
    }

    @Override
    public String toString() {
        switch (this.level){
            case 1:
                return "ADM";
            case 2:
                return "NET";
            case 3:
                return "WAR";
            case 4:
                return "ERR";
            default:
                return "MSG";
        }
    }
}
