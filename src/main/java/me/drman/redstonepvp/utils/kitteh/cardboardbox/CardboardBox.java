package me.drman.redstonepvp.utils.kitteh.cardboardbox;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CardboardBox {
    private static final int OLD_DATA_VERSION = 1343;

    private static Method itemStackFromCompound;

    private static Method itemStackSave;

    private static Method nbtCompressedStreamToolsRead;

    private static Method nbtCompressedStreamToolsWrite;

    private static Method nbtTagCompoundGetInt;

    private static Method nbtTagCompoundSetInt;

    private static Method dataFixerUpdate;

    private static Method nbtAccounterUnlimitedHeap;

    private static Constructor<?> dynamic;

    private static Constructor<?> nbtTagCompoundConstructor;

    private static Constructor<?> itemStackConstructor;

    private static Class<?> craftItemStack;

    private static Field craftItemStackHandle;

    private static Method craftItemStackAsNMSCopy;

    private static Method craftItemStackAsCraftMirror;

    private static Method dynamicGetValue;

    private static Object dynamicOpsNBT;

    private static Object dataConverterTypesItemStack;

    private static Object dataConverterRegistryDataFixer;

    private static int dataVersion;

    private static boolean hasDataVersion = false;

    private static boolean failure = true;

    private static boolean modernPaper = false;

    private static Exception exception;

    private static boolean testPending = true;

    static {
        init();
    }

    private static void init() {
        try {
            ItemStack.class.getDeclaredMethod("deserializeBytes", new Class[] { byte[].class });
            ItemStack.class.getDeclaredMethod("serializeAsBytes", new Class[0]);
            modernPaper = true;
            failure = false;
            return;
        } catch (Exception exception) {
            try {
                String str4, str5, str6, str7, str8, str9;
                Pattern pattern = Pattern.compile("1\\.(\\d{1,2})(?:\\.(\\d{1,2}))?");
                Matcher matcher = pattern.matcher(Bukkit.getVersion());
                if (!matcher.find())
                    throw new RuntimeException("Could not parse version");
                int i = Integer.parseInt(matcher.group(1));
                String str1 = matcher.group(2);
                byte b = (byte) ((str1 == null || str1.isEmpty()) ? 0 : Integer.parseInt(str1));
                int j = i * 100 + b;
                String[] arrayOfString = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
                String str2 = arrayOfString[arrayOfString.length - 1] + '.';
                String str3 = "org.bukkit.craftbukkit." + str2;
                String str10 = "net.minecraft.nbt.NBTReadLimiter";
                if (j < 1700) {
                    String str = "net.minecraft.server." + str2;
                    str4 = str + "ItemStack";
                    str6 = str + "NBTTagCompound";
                    str5 = str + "NBTCompressedStreamTools";
                    str7 = str + "DynamicOpsNBT";
                    str8 = str + "DataConverterTypes";
                    str9 = str + "DataConverterRegistry";
                } else {
                    str4 = "net.minecraft.world.item.ItemStack";
                    str6 = "net.minecraft.nbt.NBTTagCompound";
                    str5 = "net.minecraft.nbt.NBTCompressedStreamTools";
                    str7 = "net.minecraft.nbt.DynamicOpsNBT";
                    str8 = "net.minecraft.util.datafix.fixes.DataConverterTypes";
                    str9 = "net.minecraft.util.datafix.DataConverterRegistry";
                }
                Class<?> clazz1 = Class.forName(str4);
                Class<?> clazz2 = Class.forName(str5);
                Class<?> clazz3 = Class.forName(str6);
                nbtTagCompoundConstructor = clazz3.getConstructor(new Class[0]);
                try {
                    itemStackFromCompound = clazz1.getMethod("fromCompound", new Class[] { clazz3 });
                } catch (NoSuchMethodException noSuchMethodException) {
                    try {
                        itemStackFromCompound = clazz1.getMethod("createStack", new Class[] { clazz3 });
                    } catch (NoSuchMethodException noSuchMethodException1) {
                        try {
                            itemStackFromCompound = clazz1.getMethod("a", new Class[] { clazz3 });
                        } catch (NoSuchMethodException noSuchMethodException2) {
                            itemStackConstructor = clazz1.getConstructor(new Class[] { clazz3 });
                        }
                    }
                }
                itemStackSave = clazz1.getMethod((j >= 1800) ? "b" : "save", new Class[] { clazz3 });
                if (j < 2004) {
                    nbtCompressedStreamToolsRead = clazz2.getMethod("a", new Class[] { InputStream.class });
                } else {
                    Class<?> clazz = Class.forName(str10);
                    nbtCompressedStreamToolsRead = clazz2.getMethod("a", new Class[] { InputStream.class, clazz });
                    nbtAccounterUnlimitedHeap = clazz.getMethod("a", new Class[0]);
                }
                nbtCompressedStreamToolsWrite = clazz2.getMethod("a", new Class[] { clazz3, OutputStream.class });
                nbtTagCompoundGetInt = clazz3.getMethod((j >= 1800) ? "h" : "getInt", new Class[] { String.class });
                nbtTagCompoundSetInt = clazz3.getMethod((j >= 1800) ? "a" : "setInt", new Class[] { String.class, int.class });
                craftItemStack = Class.forName(str3 + "inventory.CraftItemStack");
                Class<?> clazz4 = Class.forName(str3 + "util.CraftMagicNumbers");
                craftItemStackHandle = craftItemStack.getDeclaredField("handle");
                craftItemStackHandle.setAccessible(true);
                Field field = clazz4.getField("INSTANCE");
                craftItemStackAsNMSCopy = craftItemStack.getMethod("asNMSCopy", new Class[] { ItemStack.class });
                craftItemStackAsCraftMirror = craftItemStack.getMethod("asCraftMirror", new Class[] { clazz1 });
                try {
                    clazz4.getMethod("getDataVersion", new Class[0]);
                    dataVersion = ((Integer)clazz4.getMethod("getDataVersion", new Class[0]).invoke(field.get(null), new Object[0])).intValue();
                    Class<?> clazz5 = Class.forName("com.mojang.datafixers.DataFixer");
                    for (Method method : clazz5.getMethods()) {
                        if (method.getName().equals("update") && method.getParameterCount() == 4) {
                            dataFixerUpdate = method;
                            break;
                        }
                    }
                    Class<?> clazz6 = Class.forName(str9);
                    for (Field field1 : clazz6.getDeclaredFields()) {
                        if (clazz5.isAssignableFrom(field1.getType())) {
                            field1.setAccessible(true);
                            dataConverterRegistryDataFixer = field1.get(null);
                            break;
                        }
                    }
                    if (dataConverterRegistryDataFixer == null)
                        throw new IllegalStateException("No sign of data fixer");
                    if (j < 1700) {
                        dataConverterTypesItemStack = Class.forName(str8).getField("ITEM_STACK").get(null);
                    } else {
                        dataConverterTypesItemStack = ("item_stack");
                    }
                    dynamicOpsNBT = Class.forName(str7).getField("a").get(null);
                    Class<?> clazz7 = dataFixerUpdate.getParameterTypes()[1];
                    for (Constructor<?> constructor : clazz7.getConstructors()) {
                        if (constructor.getParameterCount() == 2) {
                            dynamic = constructor;
                            break;
                        }
                    }
                    dynamicGetValue = clazz7.getMethod("getValue", new Class[0]);
                    hasDataVersion = true;
                } catch (Exception exception1) {}
                failure = false;
            } catch (Exception exception1) {
                exception = exception1;
            }
            return;
        }
    }

    public static boolean isModernPaperSupport() {
        return modernPaper;
    }

    public static boolean isReady() {
        if (modernPaper)
            return true;
        if (!failure && testPending) {
            testPending = false;
            try {
                ItemStack itemStack = new ItemStack(Material.DIRT);
                deserializeItem(serializeItem(itemStack));
            } catch (Exception exception) {
                failure = true;
                exception = exception;
            }
        }
        return !failure;
    }

    public static byte[] serializeItem(ItemStack paramItemStack) {
        if (failure)
            throw new IllegalStateException("Cardboard Box failed to initialize. Cannot serialize without risk.", exception);
        if (paramItemStack == null || paramItemStack.getType() == Material.AIR)
            return new byte[] { 0 };
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Object object1;
            if (craftItemStack.isAssignableFrom(paramItemStack.getClass())) {
                object1 = craftItemStackHandle.get(paramItemStack);
            } else {
                object1 = craftItemStackAsNMSCopy.invoke(null, new Object[] { paramItemStack });
            }
            Object object2 = itemStackSave.invoke(object1, new Object[] { nbtTagCompoundConstructor.newInstance(new Object[0]) });
            if (hasDataVersion)
                nbtTagCompoundSetInt.invoke(object2, new Object[] { "DataVersion", Integer.valueOf(dataVersion) });
            nbtCompressedStreamToolsWrite.invoke(null, new Object[] { object2, byteArrayOutputStream });
        } catch (Exception exception) {
            throw new RuntimeException("Failed to serialize item stack\n" + paramItemStack, exception);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static ItemStack deserializeItem(byte[] paramArrayOfbyte) {
        if (failure)
            throw new IllegalStateException("Cardboard Box failed to initialize. Cannot serialize without risk.", exception);
        if (paramArrayOfbyte == null || paramArrayOfbyte.length == 0 || (paramArrayOfbyte.length == 1 && paramArrayOfbyte[0] == 0))
            return new ItemStack(Material.AIR);
        try {
            Object object;
            if (nbtAccounterUnlimitedHeap == null) {
                object = nbtCompressedStreamToolsRead.invoke(null, new Object[] { new ByteArrayInputStream(paramArrayOfbyte) });
            } else {
                object = nbtCompressedStreamToolsRead.invoke(null, new Object[] { new ByteArrayInputStream(paramArrayOfbyte), nbtAccounterUnlimitedHeap.invoke(null, new Object[0]) });
            }
            if (hasDataVersion) {
                int i = ((Integer)nbtTagCompoundGetInt.invoke(object, new Object[] { "DataVersion" })).intValue();
                if (i == 0)
                    i = 1343;
                if (i > dataVersion)
                    throw new IllegalArgumentException("Attempting to load an item of version " + i + " but this server is version " + dataVersion);
                object = dynamicGetValue.invoke(dataFixerUpdate.invoke(dataConverterRegistryDataFixer, new Object[] { dataConverterTypesItemStack, dynamic.newInstance(new Object[] { dynamicOpsNBT, object }), Integer.valueOf(i), Integer.valueOf(dataVersion) }), new Object[0]);
            }
            return (ItemStack)craftItemStackAsCraftMirror.invoke(null, new Object[] { (itemStackFromCompound == null) ? itemStackConstructor.newInstance(new Object[] { object }) : itemStackFromCompound.invoke(null, new Object[] { object }) });
        } catch (Exception exception) {
            throw new RuntimeException("Failed to deserialize item stack\n" + Base64.getEncoder().encodeToString(paramArrayOfbyte), exception);
        }
    }
}
