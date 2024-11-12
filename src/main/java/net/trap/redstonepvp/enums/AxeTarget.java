package me.drman.redstonepvp.enums;

public enum AxeTarget {
    ATTACKER,DEFENDER;

    public static AxeTarget getByName(String s){
        if (s.equalsIgnoreCase("ATTACKER")){
            return ATTACKER;
        }else if(s.equalsIgnoreCase("DEFENDER")){
            return DEFENDER;
        }else{
            return null;
        }
    }

}