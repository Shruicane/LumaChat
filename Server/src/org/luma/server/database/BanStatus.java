package org.luma.server.database;

public enum BanStatus {

    NONE(0), WARNED(1), TEMPBAN(2), PERMABAN(3);

    private final int lvl;

    BanStatus(int banlvl){
        lvl = banlvl;
    }

    public int getBanLvl(){
        return lvl;
    }

    public BanStatus resolvebanLvl(int i){
        switch (i){
            case 1:
                return BanStatus.WARNED;
            case 2:
                return BanStatus.TEMPBAN;
            case 3:
                return BanStatus.PERMABAN;
            default:
                return BanStatus.NONE;
        }
    }
}