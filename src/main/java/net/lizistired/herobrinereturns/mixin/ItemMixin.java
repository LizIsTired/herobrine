package net.lizistired.herobrinereturns.mixin;

import net.lizistired.herobrinereturns.HerobrineReturns;
import net.lizistired.herobrinereturns.HerobrineReturnsServer;
import net.lizistired.herobrinereturns.entities.herobrinetypes.HerobrineBoss;
import net.lizistired.herobrinereturns.utils.registry.RegisterEntities;
import net.lizistired.herobrinereturns.utils.registry.RegisterItems;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.block.BlockPredicate;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemMixin {


    @Shadow
    public abstract Text getName();

    @Shadow public abstract ItemStack getStack();

    @Shadow @Nullable public abstract Entity getOwner();

    @Unique
    private boolean correctStructure = false;

    @Unique
    private static BlockPattern herobrineShrinePattern;
    @Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At(value = "TAIL"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        HerobrineReturns.LOGGER.info(this.getName().toString() + RegisterItems.CURSED_BOOK.getName());
        MinecraftServer minecraftServer = (MinecraftServer) HerobrineReturns.minecraftServer;
        if (this.getStack().getItem() == RegisterItems.CURSED_BOOK.asItem()) {
            if (source.isIn(DamageTypeTags.IS_FIRE)) {
                BlockPattern.Result result = getHerobrineShrinePattern().searchAround((ServerWorld) minecraftServer.getWorld(World.OVERWORLD), ((ItemEntity) (Object) this).getBlockPos());
                if (result == null) {
                } else {
                    CarvedPumpkinBlock.breakPatternBlocks((ServerWorld) minecraftServer.getWorld(World.OVERWORLD), result);
                    HerobrineBoss herobrineBoss = new HerobrineBoss(RegisterEntities.HEROBRINE_BOSS_ENTITY_TYPE, minecraftServer.getOverworld());
                    //get the position of the item entity and set the position of the Herobrine boss to that position
                    herobrineBoss.setPosition(((ItemEntity) (Object) this).getBlockPos().toCenterPos());
                    minecraftServer.getOverworld().spawnEntity(herobrineBoss);
                    ((ItemEntity) (Object) this).discard();
                    CarvedPumpkinBlock.updatePatternBlocks((ServerWorld)minecraftServer.getWorld(World.OVERWORLD), result);
                }
                }
            }
        }

    @Unique
    private static BlockPattern getHerobrineShrinePattern() {
        herobrineShrinePattern = BlockPatternBuilder.start()
                .aisle("   ", " > ", "   ")
                .aisle(" ~ ","~*~"," ~ ")
                .aisle("###", "###", "###")
                .where('>', CachedBlockPosition.matchesBlockState(BlockPredicate.make(Blocks.FIRE)))
                .where('#', CachedBlockPosition.matchesBlockState(BlockPredicate.make(Blocks.GOLD_BLOCK)))
                .where('~', CachedBlockPosition.matchesBlockState(BlockPredicate.make(Blocks.REDSTONE_TORCH)))
                .where('*', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.NETHERRACK)))
                .build();

        if (herobrineShrinePattern == null) {
            return null;
        }
        return herobrineShrinePattern;
    }
}