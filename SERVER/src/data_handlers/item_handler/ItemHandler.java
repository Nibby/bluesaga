package data_handlers.item_handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;

import org.newdawn.slick.Color;

import utils.ServerGameInfo;
import utils.RandomUtils;
import utils.TextFormater;
import creature.Creature;
import creature.Npc;
import data_handlers.Handler;
import data_handlers.ability_handler.Ability;
import data_handlers.ability_handler.AbilityHandler;
import data_handlers.card_handler.CardHandler;
import network.Client;
import network.Server;

public class ItemHandler extends Handler {

	public static void handleData(Client client, String message){
		if(client.playerCharacter != null){
			
			// Handle all events related to inventory and personal chest
			InventoryHandler.handleData(client, message);

			// Handle all events related to equipment
			EquipHandler.handleData(client, message);
			
			// Handle all events related to actionbar
			ActionbarHandler.handleData(client, message);
			
			
			// DROP ITEM ON GROUND
			if(message.startsWith("<drop_item>")){
				//String itemInfo = message.substring(11);

				//int itemId = Integer.parseInt(itemInfo);

				ResultSet dropInfo = Server.userDB.askDB("select Nr from character_item where InventoryPos = 'Mouse' and CharacterId = "+client.playerCharacter.getDBId());
				try {
					if(dropInfo.next()){
						Server.userDB.updateDB("delete from character_item where InventoryPos = 'Mouse' and CharacterId = "+client.playerCharacter.getDBId());

						if(client.playerCharacter.getMouseItem() != null){
							loseItemOnGround(client, client.playerCharacter.getMouseItem());
						}
						client.playerCharacter.setMouseItem(null);
					}
					dropInfo.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if(message.startsWith("<item_info>")){
				String info[] = message.substring(11).split(";");

				String infoType = info[0];

				Item infoItem = null;

				String infoToSend = "";

				int userItemId = 0;
				
				if(infoType.equals("shop")){
					int itemId = Integer.parseInt(info[1]);
					infoItem = new Item(ServerGameInfo.itemDef.get(itemId));
				}else if(infoType.equals("closet")){
					int itemId = Integer.parseInt(info[1]);
					if(itemId == 214){
						infoToSend = "255,234,116;"+"#ui.web.get_more_skins"+"/";
						infoToSend += "0,0,0; /";
						infoToSend += "255,255,255;"+"#ui.web.wide_selection"+"/";
						infoToSend += "255,255,255;"+"#ui.web.customize"+"/";
					}else{
						infoItem = new Item(ServerGameInfo.itemDef.get(itemId));
					}
				}else if(infoType.equals("container")){

					String containerXYZ = info[1];
					String itemXY = info[2];

					if(ContainerHandler.CONTAINERS.containsKey(containerXYZ)){
						infoItem = ContainerHandler.CONTAINERS.get(containerXYZ).getItems().get(itemXY);
					}
				}else if(infoType.equals("personalchest")){
					// PERSONAL CHEST ITEM
					userItemId = Integer.parseInt(info[1]);

					if(userItemId == 0){
						infoToSend = "255,234,116;"+"#ui.web.get_more_space"+"/";
						infoToSend += "0,0,0; /";
						infoToSend += "255,255,255;"+"#ui.web.expand_chest"+"/";
						infoToSend += "255,255,255;"+"#ui.web.keep_safe"+"/";
					}else{
						ResultSet userItemInfo = Server.userDB.askDB("select ItemId, ModifierId, MagicId from user_chest where Id = "+userItemId);

						try {
							if(userItemInfo.next()){

								// CHECK IF PLAYER HAS ITEM
								infoItem = new Item(ServerGameInfo.itemDef.get(userItemInfo.getInt("ItemId")));
								infoItem.setModifierId(userItemInfo.getInt("ModifierId"));
								infoItem.setMagicId(userItemInfo.getInt("MagicId"));
								
							}
							userItemInfo.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}

				}else{
					// INVENTORY ITEM
					
					userItemId = Integer.parseInt(info[1]);
				
					ResultSet userItemInfo = Server.userDB.askDB("select ItemId, ModifierId, MagicId from character_item where Id = "+userItemId);

					try {
						if(userItemInfo.next()){

							// CHECK IF PLAYER HAS ITEM
							infoItem = new Item(ServerGameInfo.itemDef.get(userItemInfo.getInt("ItemId")));
							infoItem.setModifierId(userItemInfo.getInt("ModifierId"));
							infoItem.setMagicId(userItemInfo.getInt("MagicId"));
							
						}
						userItemInfo.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
								

				if(infoItem != null){
					
					
					if(infoItem.getMagicId() > 0){
						String magicName = "0";
						String magicColor = "0";

						ResultSet magicInfo = Server.gameDB.askDB("select Name, Color from item_magic where Id = "+infoItem.getMagicId());
						try {
							if(magicInfo.next()){
								magicName = magicInfo.getString("Name");
								magicColor = magicInfo.getString("Color");
							}
							magicInfo.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					
						infoToSend = magicColor+";"+magicName+"/";
					}
					
					infoToSend += infoItem.getColor()+";"+infoItem.getName()+"/";

					infoToSend += "0,0,0; /";

					infoToSend += "255,234,116;"+infoItem.getType()+" - "+infoItem.getSubType()+"/";
					if(infoItem.isTwoHands()){
						infoToSend += TextFormater.formatInfo("Two Hands");
					}					

					infoToSend += "0,0,0; /";

					if(infoItem.getStatValue("MinDamage") > 0 || infoItem.getStatValue("MaxDamage") > 0){
						infoToSend += TextFormater.formatInfo("Base DMG: "+infoItem.getStatValue("MinDamage")+" - "+infoItem.getStatValue("MaxDamage"));
					}

					infoToSend += TextFormater.formatStatInfo("ARMOR: ",infoItem.getStatValue("ARMOR"));
					infoToSend += TextFormater.formatStatInfo("Fire DEF: ",infoItem.getStatValue("FIRE_DEF"));
					infoToSend += TextFormater.formatStatInfo("Cold DEF: ",infoItem.getStatValue("COLD_DEF"));
					infoToSend += TextFormater.formatStatInfo("Shock DEF: ",infoItem.getStatValue("SHOCK_DEF"));
					infoToSend += TextFormater.formatStatInfo("Chems DEF: ",infoItem.getStatValue("CHEMS_DEF"));
					infoToSend += TextFormater.formatStatInfo("Mind DEF: ",infoItem.getStatValue("MIND_DEF"));

					infoToSend += "0,0,0; /";

					infoToSend += TextFormater.formatBonusInfo("STR: ",infoItem.getStatValue("STRENGTH"));
					infoToSend += TextFormater.formatBonusInfo("INT: ",infoItem.getStatValue("INTELLIGENCE"));
					infoToSend += TextFormater.formatBonusInfo("AGI: ",infoItem.getStatValue("AGILITY"));
					
					infoToSend += TextFormater.formatBonusInfo("SPD: ",infoItem.getStatValue("SPEED"));
					infoToSend += TextFormater.formatBonusInfo("ATK SPD: ",infoItem.getStatValue("ATTACKSPEED"));
					infoToSend += TextFormater.formatBonusInfo("CRIT HIT: ",infoItem.getStatValue("CRITICAL_HIT"));
					infoToSend += TextFormater.formatBonusInfo("ACC: ",infoItem.getStatValue("ACCURACY"));
					
					
					if(infoItem.getType().equals("Potion")){
						infoToSend += TextFormater.formatBonusInfo("Restores Health: ",infoItem.getStatValue("MAX_HEALTH"));
						infoToSend += TextFormater.formatBonusInfo("Restores Mana: ",infoItem.getStatValue("MAX_MANA"));
						infoToSend += TextFormater.formatInfo("Instant effect");
					}else if(infoItem.getType().equals("Eatable")){
						infoToSend += TextFormater.formatBonusInfo("Regain health: ",infoItem.getStatValue("MAX_HEALTH"));
						infoToSend += TextFormater.formatBonusInfo("Regain mana: ",infoItem.getStatValue("MAX_MANA"));
						infoToSend += TextFormater.formatInfo("Regain over time,");
						infoToSend += TextFormater.formatInfo("sleep for faster effect");
					}else{
						infoToSend += TextFormater.formatBonusInfo("MAX HEALTH: ",infoItem.getStatValue("MAX_HEALTH"));
						infoToSend += TextFormater.formatBonusInfo("MAX MANA: ",infoItem.getStatValue("MAX_MANA"));
					}
					
					infoToSend += "0,0,0; /";

					
					infoToSend += TextFormater.formatReqInfo("Req STR: ",infoItem.getRequirement("ReqStrength"),client.playerCharacter.getStat("STRENGTH"));
					infoToSend += TextFormater.formatReqInfo("Req INT: ",infoItem.getRequirement("ReqIntelligence"),client.playerCharacter.getStat("INTELLIGENCE"));
					infoToSend += TextFormater.formatReqInfo("Req AGI: ",infoItem.getRequirement("ReqAgility"),client.playerCharacter.getStat("AGILITY"));
					infoToSend += TextFormater.formatReqInfo("Req LVL: ",infoItem.getRequirement("ReqLevel"),client.playerCharacter.getLevel());
					if(infoItem.getClassId() > 0){
						infoToSend += TextFormater.formatConditionInfo("Req Class: "+ServerGameInfo.classDef.get(infoItem.getClassId()).name,client.playerCharacter.hasClass(infoItem.getClassId()));
					}

					if(infoType.equals("inv") && infoItem.getType().equals("Readable")){
						infoToSend += "255,255,255;"+"#ui.inventory.right_click_to_use"+"/";
					}
					if(infoType.equals("inv") && infoItem.getType().equals("Eatable")){
						infoToSend += "255,255,255;"+"#ui.inventory.right_click_to_eat"+"/";
					}
					if(infoType.equals("inv") && infoItem.getType().equals("Potion")){
						infoToSend += "255,255,255;"+"#ui.inventory.right_click_to_use"+"/";
					}

					if(infoType.equals("shop")){
						infoToSend += "0,0,0; /";
						boolean canAfford = client.playerCharacter.hasCopper(infoItem.getValue());
						if(infoItem.getValue() > 0){
							infoToSend += TextFormater.formatPriceInfo("Price: ",infoItem.getValue(),canAfford);
						}else{
							infoToSend += TextFormater.formatInfo("Free");
						}
					}

					if(infoType.equals("invshop")){
						infoToSend += "0,0,0; /";
						int price = infoItem.getValue();

						if(!infoItem.getType().equals("Money")){
							price = infoItem.getSoldValue();
						}
						if(!infoItem.isSellable()){
							infoToSend += TextFormater.formatInfo("Can't be sold");
						}else {
							infoToSend += TextFormater.formatValueInfo("#ui.shop.sell_value#: ",price);
							infoToSend += TextFormater.formatInfo("#ui.shop.right_click_sell");
						}
					}

				}

				if(infoToSend != ""){
					addOutGoingMessage(client,"item_info",infoType+"@"+infoToSend);
				}
			}


			if(message.startsWith("<use_scroll>")){
				String scrollInfo[] = message.substring(12).split(",");
				int tileX = Integer.parseInt(scrollInfo[0]);
				int tileY = Integer.parseInt(scrollInfo[1]);
				
				int scrollUserItemId = Integer.parseInt(scrollInfo[2]);

				String scrollLocation = scrollInfo[3];
				
				boolean useScrollSuccess = false;
				Item scrollItem = null;
				
				// Get scroll from inventory
				ResultSet itemInfo;
		
				if(scrollLocation.equals("Inventory")){
					// Specific inventory scroll
					itemInfo = Server.userDB.askDB("select ItemId, Nr from character_item where Id = "+scrollUserItemId+" and CharacterId = "+client.playerCharacter.getDBId());
				}else{
					// Similar scroll in inventory as the one clicked in the actionbar
					itemInfo = Server.userDB.askDB("select ItemId, Nr from character_item where ItemId = "+scrollUserItemId+" and CharacterId = "+client.playerCharacter.getDBId());
				}
				
				try {
					if(itemInfo.next()){
						scrollItem = new Item(ServerGameInfo.itemDef.get(itemInfo.getInt("ItemId")));

						// CHECK THAT IT IS A SCROLL AND AN ABILITY
						useScrollSuccess = true;
						
						// REMOVE SCROLL
						if(itemInfo.getInt("Nr") > 1){
							Server.userDB.updateDB("update character_item set Nr = Nr - 1 where ItemId = "+itemInfo.getInt("ItemId")+" and CharacterId = "+client.playerCharacter.getDBId());
						}else{
							Server.userDB.updateDB("delete from character_item where ItemId = "+itemInfo.getInt("ItemId")+" and CharacterId = "+client.playerCharacter.getDBId());
						}

						InventoryHandler.sendInventoryInfo(client);
					}
					itemInfo.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// If no scroll in inventory, then use the one in the actionbar
				if(scrollLocation.equals("Actionbar") && !useScrollSuccess){
					int scrollItemId = scrollUserItemId;
					
					ResultSet actionbarItem = Server.userDB.askDB("select Id, OrderNr, ActionId from character_actionbar where CharacterId = "+client.playerCharacter.getDBId()+" and ActionType = 'Item' and ActionId = "+scrollItemId);
					try {
						if(actionbarItem.next()){
							scrollItem = new Item(ServerGameInfo.itemDef.get(actionbarItem.getInt("ActionId")));
							Server.userDB.updateDB("delete from character_actionbar where Id = "+actionbarItem.getInt("Id"));
						
							// Send remove item from Actionbar
							addOutGoingMessage(client,"remove_actionbar", actionbarItem.getInt("OrderNr")+"");
							useScrollSuccess = true;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				// Use Ability
				if(useScrollSuccess && scrollItem != null){
					Ability scrollAbility = new Ability(ServerGameInfo.abilityDef.get(scrollItem.getScrollUseId()));
					scrollAbility.setCaster(client.playerCharacter);
					scrollAbility.setManaCost(0);
					AbilityHandler.playerUseAbility(client, scrollAbility, tileX, tileY, client.playerCharacter.getZ());
				}
			}


			if(message.startsWith("<useitem>")){
				String itemData = message.substring(9);

				boolean useItemSuccess = false;
				Color useColor = new Color(255,255,255);

				Item usedItem = null;

				if(itemData.startsWith("actionbar")){
					String itemInfo[] = itemData.split(";");
					int itemId = Integer.parseInt(itemInfo[1]);

					usedItem = new Item(ServerGameInfo.itemDef.get(itemId));
					
					if(usedItem != null){
						// Use item from Actionbar
						useItemSuccess = ActionbarHandler.useItem(client, usedItem);
					}
				}else{
					// USE ITEM FROM INVENTORY!!

					String itemInfo[] = itemData.split(";");
					int posX = Integer.parseInt(itemInfo[0]);
					int posY = Integer.parseInt(itemInfo[1]);
					int itemId = Integer.parseInt(itemInfo[2]);

					usedItem = new Item(ServerGameInfo.itemDef.get(itemId));
					
					if(usedItem != null){
						useItemSuccess = InventoryHandler.useItem(client,usedItem, posX, posY);
					}
				}

				if(useItemSuccess){
					client.playerCharacter.saveInfo();

					// SEND STATSCHANGE
					if(usedItem.getType().equals("Eatable")){
						useColor = new Color(202,253,161);
						addOutGoingMessage(client, "stat", "HEALTH_REGAIN;"+client.playerCharacter.getStat("HEALTH_REGAIN")+";"+client.playerCharacter.getSatisfied());
						addOutGoingMessage(client, "stat", "MANA_REGAIN;"+client.playerCharacter.getStat("MANA_REGAIN")+";"+client.playerCharacter.getSatisfied());
					}else if(usedItem.getSubType().equals("HEALTH")){
						useColor = new Color(255,88,88);
					}else if(usedItem.getSubType().equals("MANA")){
						useColor = new Color(91,176,255);
					}
					// SEND USE ITEM TO OTHER CLIENTS ON SAME MAP

					for (Map.Entry<Integer, Client> entry : Server.clients.entrySet()) {
						Client s = entry.getValue();

						if(s.Ready && isVisibleForPlayer(s.playerCharacter,client.playerCharacter.getX(),client.playerCharacter.getY(),client.playerCharacter.getZ())){
							// UserType; UserId; R; G; B
							addOutGoingMessage(s,"useitem",client.playerCharacter.getSmallData()+";"+useColor.getRed()+","+useColor.getGreen()+","+useColor.getBlue()+","+client.playerCharacter.getHealthStatus()+";"+usedItem.getType()+","+usedItem.getSubType());
						}
					}
				}else{
					addOutGoingMessage(client,"useitem","no");
				}
			}		
		}
	}

	public static Vector<Item> dropLoot(Npc TARGET){

		Vector<Item> droppedItems = new Vector<Item>();

		ResultSet rs = Server.gameDB.askDB("select LootItems from creature where Id = "+TARGET.getCreatureId());

		int dropOrNot = 0;
		try {
			if(rs.next()){
				if(!rs.getString("LootItems").equals("None")){

					String allLoot[] = rs.getString("LootItems").split(";");

					for(String loot: allLoot){
						String lootInfo[] = loot.split(",");
						int itemId = Integer.parseInt(lootInfo[0]);
						int dropChance = Integer.parseInt(lootInfo[1]);

						dropOrNot = RandomUtils.getInt(0, 1000);

						if(TARGET.getSpecialType() > 0){
							dropChance *= 4;
						}

						if(TARGET.isElite()){
							dropChance *= 3;
						}else if(TARGET.isTitan()){
							dropChance *= 10;
						}

						if(dropOrNot < dropChance){
							Item droppedItem = new Item(ServerGameInfo.itemDef.get(itemId));

							if(droppedItem.getType().equals("Weapon") 
									|| droppedItem.getType().equals("OffHand") 
									|| droppedItem.getType().equals("Head") 
									|| droppedItem.getType().equals("Amulet") 
									|| droppedItem.getType().equals("Artifact")){
								// CREATE RARE ITEMS

								int rareChance = RandomUtils.getInt(0,10000);

								if(TARGET.isElite()){
									rareChance = Math.round(rareChance / 2.0f);
								}else if(TARGET.isTitan()){
									rareChance = Math.round(rareChance / 10.0f);
								}

								ResultSet modifierInfo = Server.gameDB.askDB("select Id from item_modifier where DropRate >= "+rareChance+" order by RANDOM() limit 1");

								if(modifierInfo.next()){
									droppedItem.setModifierId(modifierInfo.getInt("Id"));
								}

								modifierInfo.close();

								if(TARGET.getSpecialType() > 0){
									int magicChance = RandomUtils.getInt(0,100);

									if(TARGET.isTitan()){
										magicChance -= 20;
									}

									if(magicChance < 20){

										ResultSet magicInfo = Server.gameDB.askDB("select Id from item_magic where Id = "+TARGET.getSpecialType());

										if(magicInfo.next()){
											droppedItem.setMagicId(magicInfo.getInt("Id"));
										}
										magicInfo.close();
									}	
								}
							}


							// DROP ITEM
							droppedItems.add(droppedItem);
						}
					}

				}
			}
			rs.close();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return droppedItems;
	}


	public static int dropMoney(Npc TARGET){

		int droppedCopper = 0;

		int dropOrNot = RandomUtils.getInt(0,100);

		if(TARGET.getSpecialType() > 0 || TARGET.isElite() || TARGET.isTitan()){
			dropOrNot -= 50;
		}

		
		
		if(dropOrNot < 50){
			ResultSet rs = Server.gameDB.askDB("select LootCopper from creature where Id = "+TARGET.getCreatureId());

			try {
				if(rs.next()){
					if(rs.getInt("LootCopper") > 0){
						droppedCopper = RandomUtils.getInt(0,rs.getInt("LootCopper")); 
					}
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(TARGET.getSpecialType() > 0){
			droppedCopper *= 2;
		}

		if(TARGET.isElite()){
			droppedCopper *= 2;
		}
		
		if(TARGET.isTitan()){
			droppedCopper *= 20;
		}
		
		return droppedCopper;
	}	

	public static void loseLootUponDeath(Client client){
		Vector<Item> lostLoot = new Vector<Item>();

		Creature TARGET = client.playerCharacter;
		
		int maxLootBagSize = 6*4;

		EquipHandler.checkRequirements(client);
		
		ResultSet lostItemsInfo = Server.userDB.askDB("select Id, ModifierId, MagicId, ItemId, Nr from character_item where InventoryPos <> 'None' and Equipped = 0 and CharacterId = "+client.playerCharacter.getDBId());

		try {
			while(lostItemsInfo.next()){
				ResultSet itemInfo = Server.gameDB.askDB("select Type from item where Id = "+lostItemsInfo.getInt("ItemId"));
				if(itemInfo.next()){
					if(!itemInfo.getString("Type").equals("Key")){
						if(lostLoot.size() < maxLootBagSize){
							Item lostItem = new Item(ServerGameInfo.itemDef.get(lostItemsInfo.getInt("ItemId")));
							lostItem.setStacked(lostItemsInfo.getInt("Nr"));
							lostItem.setModifierId(lostItemsInfo.getInt("ModifierId"));
							lostItem.setMagicId(lostItemsInfo.getInt("MagicId"));
							Server.userDB.updateDB("delete from character_item where Id = "+lostItemsInfo.getInt("Id"));
							lostLoot.add(lostItem);
						}else{
							break;
						}
					}
				}
				itemInfo.close();
			}
			lostItemsInfo.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// LOSE RANDOM CARD WHEN DYING
		Item lostCard = CardHandler.playerDropCard(client);
		if(lostCard != null){
			lostLoot.add(lostCard);
		}
		
		// SEND LOST ITEMS TO ALL CLIENTS ON SAME MAP
		if(lostLoot.size() > 0){
			for(Item loot: lostLoot){
				Server.WORLD_MAP.getTile(TARGET.getX(), TARGET.getY(), TARGET.getZ()).setObjectId("container/bigbag");
				ContainerHandler.addItemToContainer(loot, TARGET.getX(), TARGET.getY(), TARGET.getZ());
			}
			// SEND LOOT INFO TO ALL CLIENTS IN AREA
			for (Map.Entry<Integer, Client> entry : Server.clients.entrySet()) {
				Client s = entry.getValue();

				if(s.Ready && isVisibleForPlayer(s.playerCharacter,TARGET.getX(), TARGET.getY(), TARGET.getZ())){
					addOutGoingMessage(s,"droploot","container/bigbag,"+TARGET.getX()+","+TARGET.getY()+","+TARGET.getZ());
				}
			}
		}
	}



	public static void loseItemOnGround(Client client, Item droppedItem){
		int tileX = client.playerCharacter.getX();
		int tileY = client.playerCharacter.getY();
		int tileZ = client.playerCharacter.getZ();

		Server.WORLD_MAP.getTile(tileX, tileY, tileZ).setObjectId("container/smallbag");
		ContainerHandler.addItemToContainer(droppedItem, tileX, tileY, tileZ);

		// SEND LOOT INFO TO ALL CLIENTS IN AREA
		for (Map.Entry<Integer, Client> entry : Server.clients.entrySet()) {
			Client s = entry.getValue();

			if(s.Ready && isVisibleForPlayer(s.playerCharacter,tileX,tileY,tileZ)){
				addOutGoingMessage(s,"droploot","container/smallbag,"+tileX+","+tileY+","+tileZ);
			}
		}

		addOutGoingMessage(client,"message",droppedItem.getName()+" #messages.inventory.dropped_on_ground");
	}
}