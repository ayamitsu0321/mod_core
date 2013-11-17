package ayamitsu.util.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class AbstractInventory implements IInventory {
	public ItemStack[] items;
	private String name;
	private int stackLimit;

	public AbstractInventory(int capacity, String str) {
		this(capacity, str, 64);
	}

	public AbstractInventory(int capacity, String str, int stack) {
		this.items = new ItemStack[capacity];
		this.name = str;
		this.stackLimit = stack;
	}

	private int getInventorySlotContainItem(int id) {
		for (int i = 0; i < this.items.length; ++i) {
			if (this.items[i] != null && this.items[i].itemID == id) {
				return i;
			}
		}

		return -1;
	}

	private int getInventorySlotContainItemAndDamage(int id, int damage) {
		for (int i = 0; i < this.items.length; i++) {
			if (this.items[i] != null && this.items[i].itemID == id
					&& this.items[i].getItemDamage() == damage) {
				return i;
			}
		}

		return -1;
	}

	private int storeItemStack(ItemStack par1ItemStack) {
		for (int var2 = 0; var2 < this.items.length; ++var2) {
			if (this.items[var2] != null
					&& this.items[var2].itemID == par1ItemStack.itemID
					&& this.items[var2].isStackable()
					&& this.items[var2].stackSize < this.items[var2]
							.getMaxStackSize()
					&& this.items[var2].stackSize < this
							.getInventoryStackLimit()
					&& (!this.items[var2].getHasSubtypes() || this.items[var2]
							.getItemDamage() == par1ItemStack.getItemDamage())
					&& ItemStack.areItemStackTagsEqual(this.items[var2],
							par1ItemStack)) {
				return var2;
			}
		}

		return -1;
	}

	public int getFirstEmptyStack() {
		for (int i = 0; i < this.items.length; ++i) {
			if (this.items[i] == null) {
				return i;
			}
		}

		return -1;
	}

	public boolean hasSomeItemStack() {
		for (int i = 0; i < this.items.length; ++i) {
			if (this.items[i] != null) {
				return true;
			}
		}

		return false;
	}

	/**
	 * if id == -1 && damage == -1, clear all items
	 */
	public int clearInventory(int id, int damage) {
		int count = 0;

		for (int i = 0; i < this.items.length; ++i) {
			ItemStack itemStack = this.items[i];

			if (itemStack != null && (id <= -1 || itemStack.itemID == id)
					&& (damage <= -1 && itemStack.getItemDamage() == damage)) {
				count += itemStack.stackSize;
				this.items[i] = null;
			}
		}

		return count;
	}

	private int storePartialItemStack(ItemStack par1ItemStack) {
		int var2 = par1ItemStack.itemID;
		int var3 = par1ItemStack.stackSize;
		int var4;

		if (par1ItemStack.getMaxStackSize() == 1) {
			var4 = this.getFirstEmptyStack();

			if (var4 < 0) {
				return var3;
			} else {
				if (this.items[var4] == null) {
					this.items[var4] = ItemStack.copyItemStack(par1ItemStack);
				}

				return 0;
			}
		} else {
			var4 = this.storeItemStack(par1ItemStack);

			if (var4 < 0) {
				var4 = this.getFirstEmptyStack();
			}

			if (var4 < 0) {
				return var3;
			} else {
				if (this.items[var4] == null) {
					this.items[var4] = new ItemStack(var2, 0,
							par1ItemStack.getItemDamage());

					if (par1ItemStack.hasTagCompound()) {
						this.items[var4]
								.setTagCompound((NBTTagCompound) par1ItemStack
										.getTagCompound().copy());
					}
				}

				int var5 = var3;

				if (var3 > this.items[var4].getMaxStackSize()
						- this.items[var4].stackSize) {
					var5 = this.items[var4].getMaxStackSize()
							- this.items[var4].stackSize;
				}

				if (var5 > this.getInventoryStackLimit()
						- this.items[var4].stackSize) {
					var5 = this.getInventoryStackLimit()
							- this.items[var4].stackSize;
				}

				if (var5 == 0) {
					return var3;
				} else {
					var3 -= var5;
					this.items[var4].stackSize += var5;
					this.items[var4].animationsToGo = 5;
					return var3;
				}
			}
		}
	}

	public boolean consumeInventoryItem(int id) {
		int location = this.getInventorySlotContainItem(id);

		if (location < 0) {
			return false;
		} else {
			if (--this.items[location].stackSize <= 0) {
				this.items[location] = null;
			}

			return true;
		}
	}

	public boolean hasItem(int id) {
		return this.getInventorySlotContainItem(id) >= 0;
	}

	public boolean addItemStackToInventory(ItemStack par1ItemStack) {
		int var2;

		if (par1ItemStack.isItemDamaged()) {
			var2 = this.getFirstEmptyStack();

			if (var2 >= 0) {
				this.items[var2] = ItemStack.copyItemStack(par1ItemStack);
				this.items[var2].animationsToGo = 5;
				par1ItemStack.stackSize = 0;
				return true;
			}
			/*
			 * else if (this.player.capabilities.isCreativeMode) {
			 * par1ItemStack.stackSize = 0; return true; }
			 */
			else {
				return false;
			}
		} else {
			do {
				var2 = par1ItemStack.stackSize;
				par1ItemStack.stackSize = this
						.storePartialItemStack(par1ItemStack);
			} while (par1ItemStack.stackSize > 0
					&& par1ItemStack.stackSize < var2);

			/*
			 * if (par1ItemStack.stackSize == var2 &&
			 * this.player.capabilities.isCreativeMode) {
			 * par1ItemStack.stackSize = 0; return true; }
			 */
			// else
			// {
			return par1ItemStack.stackSize < var2;
			// }
		}
	}

	public ItemStack decrStackSize(int location, int stackSize) {
		if (this.items[location] != null) {
			ItemStack itemStack;

			if (this.items[location].stackSize <= stackSize) {
				itemStack = this.items[location];
				this.items[location] = null;
				return itemStack;
			} else {
				itemStack = this.items[location].splitStack(stackSize);

				if (this.items[location].stackSize == 0) {
					this.items[location] = null;
				}

				return itemStack;
			}
		} else {
			return null;
		}
	}

	public ItemStack getStackInSlotOnClosing(int location) {
		if (this.items[location] != null) {
			ItemStack itemStack = this.items[location];
			this.items[location] = null;
			return itemStack;
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int location, ItemStack itemStack) {
		this.items[location] = itemStack;
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		NBTTagList nbttaglist = new NBTTagList();
		NBTTagCompound content;

		for (int i = 0; i < this.items.length; i++) {
			if (this.items[i] != null) {
				content = new NBTTagCompound();
				content.setByte("Slot", (byte) i);
				this.items[i].writeToNBT(content);
				nbttaglist.appendTag(content);
			}
		}

		nbttagcompound.setTag(this.name, nbttaglist);
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		NBTTagList nbttaglist = nbttagcompound.getTagList(this.name);

		if (nbttaglist != null) {
			NBTTagCompound content;

			for (int i = 0; i < nbttaglist.tagCount(); i++) {
				content = (NBTTagCompound) nbttaglist.tagAt(i);
				int location = content.getByte("Slot") & 255;
				ItemStack itemStack = ItemStack.loadItemStackFromNBT(content);

				if (itemStack != null) {
					this.items[location] = itemStack;
				}
			}
		}
	}

	public int getSizeInventory() {
		return this.items.length;
	}

	public ItemStack getStackInSlot(int location) {
		return this.items[location];
	}

	public String getInvName() {
		return this.name;
	}

	public int getInventoryStackLimit() {
		return this.stackLimit;
	}

	public boolean hasItemStack(ItemStack itemStack) {
		for (int i = 0; i < this.items.length; i++) {
			if (this.items[i] != null && this.items[i].isItemEqual(itemStack)) {
				return true;
			}
		}

		return false;
	}

	public void openChest() {}

	public void closeChest() {}

	public void onInventoryChanged() {}

	/**
	 * for custom name
	 */
	public boolean isInvNameLocalized() {
		return false;
	}

	/**
	 * for hopper
	 */
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}
}