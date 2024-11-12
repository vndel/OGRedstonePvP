package me.drman.redstonepvp.utils.kitteh.pastegg;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PasteBuilder {
    public static class PasteResult {
        private String status;

        private Paste result;

        private String message;

        public Optional<Paste> getPaste() {
            return Optional.ofNullable(this.result);
        }

        public Optional<String> getMessage() {
            return Optional.ofNullable(this.message);
        }
    }

    private Visibility visibility = Visibility.getDefault();

    private String name;

    private List<PasteFile> files = new LinkedList<>();

    private String expires;

    public PasteBuilder name(String paramString) {
        this.name = paramString;
        return this;
    }

    public PasteBuilder expires(ZonedDateTime paramZonedDateTime) {
        this.expires = (paramZonedDateTime == null) ? null : paramZonedDateTime.format(DateTimeFormatter.ISO_INSTANT);
        return this;
    }

    public PasteBuilder visibility(Visibility paramVisibility) {
        this.visibility = paramVisibility;
        return this;
    }

    public PasteBuilder addFile(PasteFile paramPasteFile) {
        this.files.add(paramPasteFile);
        return this;
    }

    public PasteResult build() {
        String str = GsonProviderLol.GSON.toJson(this);
        String str1 = ConnectionProvider.processPasteRequest(str);
        return (PasteResult)GsonProviderLol.GSON.fromJson(str1, PasteResult.class);
    }
}
