package net.minestom.server.utils.item;

import net.minestom.server.attribute.Attribute;
import net.minestom.server.attribute.AttributeOperation;
import net.minestom.server.chat.ChatParser;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.attribute.AttributeSlot;
import net.minestom.server.item.attribute.ItemAttribute;
import net.minestom.server.network.packet.PacketReader;
import net.minestom.server.potion.PotionType;
import net.minestom.server.registry.Registries;

import java.util.ArrayList;
import java.util.UUID;

public class NbtReaderUtils {

    public static void readItemStackNBT(PacketReader reader, ItemStack item) {

        byte typeId = reader.readByte();

        //System.out.println("DEBUG TYPE: " + typeId);
        switch (typeId) {
            case 0x00: // TAG_End
                // End of item NBT
                return;
            case 0x01: // TAG_Byte

                break;
            case 0x02: // TAG_Short
                String shortName = reader.readShortSizedString();

                // Damage NBT
                if (shortName.equals("Damage")) {
                    short damage = reader.readShort();
                    item.setDamage(damage);
                    readItemStackNBT(reader, item);
                }
                break;
            case 0x03: // TAG_Int
                String intName = reader.readShortSizedString();

                // Damage
                if (intName.equals("Damage")) {
                    int damage = reader.readInteger();
                    //item.setDamage(damage);
                    // TODO short vs int damage
                    readItemStackNBT(reader, item);
                }

                // Unbreakable
                if (intName.equals("Unbreakable")) {
                    int value = reader.readInteger();
                    item.setUnbreakable(value == 1);
                    readItemStackNBT(reader, item);
                }

                if (intName.equals("HideFlags")) {
                    int flag = reader.readInteger();
                    item.setHideFlag(flag);
                    readItemStackNBT(reader, item);
                }

                break;
            case 0x04: // TAG_Long

                break;
            case 0x05: // TAG_Float

                break;
            case 0x06: // TAG_Double

                break;
            case 0x07: // TAG_Byte_Array

                break;
            case 0x08: // TAG_String
                String stringName = reader.readShortSizedString();

                if (stringName.equals("Potion")) {
                    String potionId = reader.readShortSizedString();
                    PotionType potionType = Registries.getPotionType(potionId);

                    item.addPotionType(potionType);

                    readItemStackNBT(reader, item);
                }
                break;
            case 0x09: // TAG_List

                String listName = reader.readShortSizedString();

                final boolean isEnchantment = listName.equals("Enchantments");
                final boolean isStoredEnchantment = listName.equals("StoredEnchantments");
                if (isEnchantment || isStoredEnchantment) {
                    reader.readByte(); // Should be a compound (0x0A)
                    int size = reader.readInteger(); // Enchantments count

                    for (int i = 0; i < size; i++) {
                        reader.readByte(); // Type id (short)
                        reader.readShortSizedString(); // Constant "lvl"
                        short lvl = reader.readShort();

                        reader.readByte(); // Type id (string)
                        reader.readShortSizedString(); // Constant "id"
                        String id = reader.readShortSizedString();

                        // Convert id
                        Enchantment enchantment = Registries.getEnchantment(id);

                        if (isEnchantment) {
                            item.setEnchantment(enchantment, lvl);
                        } else if (isStoredEnchantment) {
                            item.setStoredEnchantment(enchantment, lvl);
                        }
                    }

                    reader.readByte(); // Compound end

                    readItemStackNBT(reader, item);

                }

                if (listName.equals("AttributeModifiers")) {
                    reader.readByte(); // Should be a compound (0x0A);
                    int size = reader.readInteger(); // Attributes count
                    for (int i = 0; i < size; i++) {
                        reader.readByte(); // Type id (long)
                        reader.readShortSizedString(); // Constant "UUIDMost"
                        long uuidMost = reader.readLong();

                        reader.readByte(); // Type id (long)
                        reader.readShortSizedString(); // Constant "UUIDLeast"
                        long uuidLeast = reader.readLong();

                        final UUID uuid = new UUID(uuidMost, uuidLeast);

                        reader.readByte(); // Type id (double)
                        reader.readShortSizedString(); // Constant "Amount"
                        final double value = reader.readDouble();

                        reader.readByte(); // Type id (string)
                        reader.readShortSizedString(); // Constant "Slot"
                        final String slot = reader.readShortSizedString();

                        reader.readByte(); // Type id (string)
                        reader.readShortSizedString(); // Constant "AttributeName"
                        final String attributeName = reader.readShortSizedString();

                        reader.readByte(); // Type id (int)
                        reader.readShortSizedString(); // Constant "Operation"
                        final int operation = reader.readInteger();

                        reader.readByte(); // Type id (string)
                        reader.readShortSizedString(); // Constant "Name"
                        final String name = reader.readShortSizedString();

                        final Attribute attribute = Attribute.fromKey(attributeName);
                        // Wrong attribute name, stop here
                        if (attribute == null)
                            break;
                        final AttributeOperation attributeOperation = AttributeOperation.byId(operation);
                        // Wrong attribute operation, stop here
                        if (attributeOperation == null)
                            break;
                        final AttributeSlot attributeSlot = AttributeSlot.valueOf(slot.toUpperCase());
                        // Wrong attribute slot, stop here
                        if (attributeSlot == null)
                            break;

                        // Add attribute
                        final ItemAttribute itemAttribute =
                                new ItemAttribute(uuid, name, attribute, attributeOperation, value, attributeSlot);
                        item.addAttribute(itemAttribute);
                    }

                    reader.readByte(); // Compound end

                    readItemStackNBT(reader, item);
                }

                break;
            case 0x0A: // TAG_Compound

                String compoundName = reader.readShortSizedString();

                // Display Compound
                if (compoundName.equals("display")) {
                    readItemStackDisplayNBT(reader, item);
                }

                break;
        }
    }

    public static void readItemStackDisplayNBT(PacketReader reader, ItemStack item) {
        byte typeId = reader.readByte();

        switch (typeId) {
            case 0x00: // TAG_End
                // End of the display compound
                readItemStackNBT(reader, item);
                break;
            case 0x08: // TAG_String

                String stringName = reader.readShortSizedString();

                if (stringName.equals("Name")) {
                    String jsonDisplayName = reader.readShortSizedString();
                    ColoredText displayName = ChatParser.toColoredText(jsonDisplayName);

                    item.setDisplayName(displayName);
                    readItemStackDisplayNBT(reader, item);
                }
                break;
            case 0x09: // TAG_List

                String listName = reader.readShortSizedString();

                if (listName.equals("Lore")) {
                    reader.readByte(); // lore type, should always be 0x08 (TAG_String)

                    int size = reader.readInteger();
                    ArrayList<ColoredText> lore = new ArrayList<>(size);
                    for (int i = 0; i < size; i++) {
                        String string = reader.readShortSizedString();
                        ColoredText text = ChatParser.toColoredText(string);

                        lore.add(text);
                        if (lore.size() == size) {
                            item.setLore(lore);
                        }

                        if (i == size - 1) { // Last iteration
                            readItemStackDisplayNBT(reader, item);
                        }
                    }
                }

                break;
        }
    }

}
