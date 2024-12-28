package me.drman.redstonepvp.utils;

import me.drman.redstonepvp.utils.kitteh.cardboardbox.CardboardBox;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class InventoryUtils {
    public static String toStorage(Inventory inventory, String target) {
        try {
            return Base64Coder.encodeLines(writeInventory(inventory.getContents()));
        } catch (Exception e) {
           throw new IllegalStateException("Failed to save items for " + target, e);
        }
    }

    public static ItemStack[] fromStorage(String data) {
        if (data == null || data.isEmpty()) {
            ItemStack[] i = new ItemStack[6 * 9];
            for (int x = 0; x < i.length; x++) {
                i[x] = new ItemStack(Material.AIR);
            }
            return i;
        }
        try {
            return readInventory(Base64Coder.decodeLines(data));
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] writeInventory(ItemStack[] contents) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytes);
        out.writeInt(contents.length);
        for (ItemStack content : contents) {
            byte[] item = CardboardBox.serializeItem(content);
            out.writeInt(item.length);
            out.write(item);
        }
        out.close();
        return bytes.toByteArray();
    }

    private static ItemStack[] readInventory(byte[] data) throws IOException {
        DataInputStream input = new DataInputStream(new ByteArrayInputStream(data));
        ItemStack[] contents = new ItemStack[input.readInt()];
        for (int i = 0; i < contents.length; i++) {
            int len = input.readInt();
            byte[] itemBytes = new byte[len];
            input.readFully(itemBytes);
            contents[i] = CardboardBox.deserializeItem(itemBytes);
        }
        return contents;
    }
}