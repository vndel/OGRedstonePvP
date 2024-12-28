package me.drman.redstonepvp.utils.kitteh.pastegg;

public class PasteFile {
    private String id;

    private String name;

    private PasteContent content;

    public PasteFile(String paramString1, String paramString2, PasteContent paramPasteContent) {
        this.id = paramString1;
        this.name = paramString2;
        this.content = paramPasteContent;
    }

    public PasteFile(String paramString, PasteContent paramPasteContent) {
        this(null, paramString, paramPasteContent);
    }

    public PasteContent getContent() {
        return this.content;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
