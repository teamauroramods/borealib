package com.teamaurora.borealib.api.block.v1.entity.compat;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.base.v1.util.Mods;
import com.teamaurora.borealib.api.block.v1.compat.CabinetBlock;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import com.teamaurora.borealib.core.registry.BorealibBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author vectorwing
 */
public class CabinetBlockEntity extends RandomizableContainerBlockEntity {
	private NonNullList<ItemStack> contents = NonNullList.withSize(27, ItemStack.EMPTY);
	private ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
		protected void onOpen(Level level, BlockPos pos, BlockState state) {
			CabinetBlockEntity.this.playSound(state, getOpenSound());
			CabinetBlockEntity.this.updateBlockState(state, true);
		}

		protected void onClose(Level level, BlockPos pos, BlockState state) {
			CabinetBlockEntity.this.playSound(state, getCloseSound());
			CabinetBlockEntity.this.updateBlockState(state, false);
		}

		protected void openerCountChanged(Level level, BlockPos pos, BlockState sta, int arg1, int arg2) {
		}

		protected boolean isOwnContainer(Player p_155060_) {
			if (p_155060_.containerMenu instanceof ChestMenu) {
				Container container = ((ChestMenu) p_155060_.containerMenu).getContainer();
				return container == CabinetBlockEntity.this;
			} else {
				return false;
			}
		}
	};

	public CabinetBlockEntity(BlockPos pos, BlockState state) {
		super(BorealibBlockEntityTypes.CABINET.get(), pos, state);
	}

	private static SoundEvent getOpenSound() {
		return Platform.isModLoaded(Mods.FARMERS_DELIGHT) ? RegistryWrapper.SOUND_EVENTS.get(new ResourceLocation(Mods.FARMERS_DELIGHT, "block.cabinet.open")) : SoundEvents.BARREL_OPEN;
	}

	private static SoundEvent getCloseSound() {
		return Platform.isModLoaded(Mods.FARMERS_DELIGHT) ? RegistryWrapper.SOUND_EVENTS.get(new ResourceLocation(Mods.FARMERS_DELIGHT, "block.cabinet.close")) : SoundEvents.BARREL_CLOSE;
	}

	private static MutableComponent getTranslation(String key, Object... args) {
		return Component.translatable(Mods.FARMERS_DELIGHT + "." + key, args);
	}

	@Override
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		if (!trySaveLootTable(compound)) {
			ContainerHelper.saveAllItems(compound, contents);
		}
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		contents = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		if (!tryLoadLootTable(compound)) {
			ContainerHelper.loadAllItems(compound, contents);
		}
	}

	@Override
	public int getContainerSize() {
		return 27;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return contents;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> itemsIn) {
		contents = itemsIn;
	}

	@Override
	protected Component getDefaultName() {
		return getTranslation("container.cabinet");
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory player) {
		return ChestMenu.threeRows(id, player, this);
	}

	public void startOpen(Player pPlayer) {
		if (level != null && !this.remove && !pPlayer.isSpectator()) {
			this.openersCounter.incrementOpeners(pPlayer, level, this.getBlockPos(), this.getBlockState());
		}
	}

	public void stopOpen(Player pPlayer) {
		if (level != null && !this.remove && !pPlayer.isSpectator()) {
			this.openersCounter.decrementOpeners(pPlayer, level, this.getBlockPos(), this.getBlockState());
		}
	}

	public void recheckOpen() {
		if (level != null && !this.remove) {
			this.openersCounter.recheckOpeners(level, this.getBlockPos(), this.getBlockState());
		}
	}

	void updateBlockState(BlockState state, boolean open) {
		if (level != null) {
			this.level.setBlock(this.getBlockPos(), state.setValue(CabinetBlock.OPEN, open), 3);
		}
	}

	private void playSound(BlockState state, SoundEvent sound) {
		if (level == null) return;

		Vec3i cabinetFacingVector = state.getValue(CabinetBlock.FACING).getNormal();
		double x = (double) worldPosition.getX() + 0.5D + (double) cabinetFacingVector.getX() / 2.0D;
		double y = (double) worldPosition.getY() + 0.5D + (double) cabinetFacingVector.getY() / 2.0D;
		double z = (double) worldPosition.getZ() + 0.5D + (double) cabinetFacingVector.getZ() / 2.0D;
		level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
	}
}