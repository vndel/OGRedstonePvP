package me.drman.redstonepvp.utils.kitteh.pastegg;

import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.function.Function;
import java.util.zip.GZIPOutputStream;

public class PasteContent {
    private PasteContent.ContentType format;
    private String value;
    private transient String processedValue;

    public PasteContent(PasteContent.ContentType var1, String var2) {
        if (var1 == PasteContent.ContentType.XZ) {
            throw new UnsupportedOperationException("XZ not presently supported");
        } else {
            this.format = var1;
            this.value = (String)var1.getProcessor().apply(var2);
            this.processedValue = var2;
        }
    }

    public String getValue() {
        if (this.processedValue == null) {
        }

        return this.processedValue;
    }

    public static enum ContentType {
        @SerializedName("base64")
        BASE64((var0) -> {
            return Base64.getUrlEncoder().encodeToString(var0.getBytes());
        }),
        @SerializedName("gzip")
        GZIP((var0) -> {
            byte[] var1 = var0.getBytes();

            try {
                ByteArrayOutputStream var2 = new ByteArrayOutputStream(var1.length);

                try {
                    GZIPOutputStream var3 = new GZIPOutputStream(var2);
                    Throwable var4 = null;

                    try {
                        var3.write(var1);
                    } catch (Throwable var22) {
                        var4 = var22;
                        throw var22;
                    } finally {
                        if (var3 != null) {
                            if (var4 != null) {
                                try {
                                    var3.close();
                                } catch (Throwable var21) {
                                    var4.addSuppressed(var21);
                                }
                            } else {
                                var3.close();
                            }
                        }

                    }
                } finally {
                    var2.close();
                }

                return Base64.getUrlEncoder().encodeToString(var2.toByteArray());
            } catch (Exception var25) {
                throw new RuntimeException();
            }
        }),
        @SerializedName("text")
        TEXT((var0) -> {
            return var0;
        }),
        @SerializedName("xz")
        XZ((var0) -> {
            return var0;
        });

        private final Function<String, String> processor;

        private ContentType(Function<String, String> var3) {
            this.processor = var3;
        }

        public Function<String, String> getProcessor() {
            return this.processor;
        }
    }
}