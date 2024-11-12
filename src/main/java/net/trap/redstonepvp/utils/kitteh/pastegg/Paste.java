package me.drman.redstonepvp.utils.kitteh.pastegg;

import java.util.Optional;

public class Paste {
    private final String id;

    private final String deletion_key;

    private final Visibility visibility;

    public Paste(String paramString) {
        this(paramString, null);
    }

    public Paste(String paramString1, String paramString2) {
        this(paramString1, paramString2, Visibility.PUBLIC);
    }

    public Paste(String paramString1, String paramString2, Visibility paramVisibility) {
        this.id = paramString1;
        this.deletion_key = paramString2;
        this.visibility = paramVisibility;
    }

    public String getId() {
        return this.id;
    }

    public Optional<String> getDeletionKey() {
        return Optional.ofNullable(this.deletion_key);
    }

    public Visibility getVisibility() {
        return this.visibility;
    }
}
