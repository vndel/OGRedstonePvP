package me.drman.redstonepvp.utils.kitteh.pastegg;

public enum Visibility {
    PRIVATE, PUBLIC, UNLISTED;

    public static Visibility getDefault() {
        return UNLISTED;
    }
}
