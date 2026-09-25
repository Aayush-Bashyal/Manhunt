package net.bikash.manhunt.item;
import net.bikash.manhunt.Manhunt;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

 import net.minecraft.world.item.Item;
 public class ModItems {
     public static final ResourceKey<Item> MANHUNT_COMPASS_KEY = ResourceKey.create(
             BuiltInRegistries.ITEM.key(),
             Identifier.fromNamespaceAndPath(Manhunt.MOD_ID,
                     "manhunt_compass") );
     public static final Item MANHUNT_COMPASS = register(
             MANHUNT_COMPASS_KEY, new Item.Properties() );
     private static Item register(
             ResourceKey<Item> key, Item.Properties properties ) {
         Item item = new Item(properties.setId(key));
         Registry.register( BuiltInRegistries.ITEM, key, item );
         return item;
     } public static void initialize() {

     } }