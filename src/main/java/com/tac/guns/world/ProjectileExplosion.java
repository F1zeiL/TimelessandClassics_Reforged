package com.tac.guns.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.tac.guns.Config;
import com.tac.guns.init.ModTags;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.ExplosionContext;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class ProjectileExplosion extends Explosion {
    private static final ExplosionContext DEFAULT_CONTEXT = new ExplosionContext();

    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private final float power;
    private final float radius;
    private final Entity exploder;
    private final ExplosionContext context;

    public ProjectileExplosion(World world, Entity exploder, @Nullable DamageSource source, @Nullable ExplosionContext context, double x, double y, double z, float power, float radius, Mode mode) {
        super(world, exploder, source, context, x, y, z, radius, Config.SERVER.gameplay.explosionCauseFire.get(), mode);
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.power = power;
        this.radius = radius;
        this.exploder = exploder;
        this.context = context == null ? DEFAULT_CONTEXT : context;
    }

    @Override
    public void doExplosionA() {
        Set<BlockPos> set = Sets.newHashSet();

        for (int x = 0; x < 16; ++x) {
            for (int y = 0; y < 16; ++y) {
                for (int z = 0; z < 16; ++z) {
                    if (x == 0 || x == 15 || y == 0 || y == 15 || z == 0 || z == 15) {
                        double d0 = ((float) x / 15.0F * 2.0F - 1.0F);
                        double d1 = ((float) y / 15.0F * 2.0F - 1.0F);
                        double d2 = ((float) z / 15.0F * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 = d0 / d3;
                        d1 = d1 / d3;
                        d2 = d2 / d3;
                        float f = this.radius * (0.7F + this.world.rand.nextFloat() * 0.6F);
                        double blockX = this.x;
                        double blockY = this.y;
                        double blockZ = this.z;

                        for (; f > 0.0F; f -= 0.225F) {
                            BlockPos pos = new BlockPos(blockX, blockY, blockZ);
                            BlockState blockState = this.world.getBlockState(pos);
                            FluidState fluidState = this.world.getFluidState(pos);
                            Optional<Float> optional = this.context.getExplosionResistance(this, this.world, pos, blockState, fluidState);
                            if (optional.isPresent()) {
                                f -= (optional.get() + 0.3F) * 0.3F;
                            }

                            if (f > 0.0F && this.context.canExplosionDestroyBlock(this, this.world, pos, blockState, f)) {
                                set.add(pos);
                            }

                            blockX += d0 * (double) 0.3F;
                            blockY += d1 * (double) 0.3F;
                            blockZ += d2 * (double) 0.3F;
                        }
                    }
                }
            }
        }

        this.getAffectedBlockPositions().addAll(set);
        float radius = this.radius;
        int minX = MathHelper.floor(this.x - (double) radius - 1.0D);
        int maxX = MathHelper.floor(this.x + (double) radius + 1.0D);
        int minY = MathHelper.floor(this.y - (double) radius - 1.0D);
        int maxY = MathHelper.floor(this.y + (double) radius + 1.0D);
        int minZ = MathHelper.floor(this.z - (double) radius - 1.0D);
        int maxZ = MathHelper.floor(this.z + (double) radius + 1.0D);
        radius *= 2;
        List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ));
        net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.world, this, entities, radius);
        Vector3d explosionPos = new Vector3d(this.x, this.y, this.z);

        for (Entity entity : entities) {
            if (entity.isImmuneToExplosions())
                continue;

            AxisAlignedBB boundingBox = entity.getBoundingBox();
            RayTraceResult result;
            double strength;
            double deltaX;
            double deltaY;
            double deltaZ;
            double minDistance = radius;

            Vector3d[] d = new Vector3d[15];

            if (!(entity instanceof LivingEntity)) {
                strength = MathHelper.sqrt(entity.getDistanceSq(explosionPos)) * 2 / radius;
                deltaX = entity.getPosX() - this.x;
                deltaY = (entity instanceof TNTEntity ? entity.getPosY() : entity.getPosYEye()) - this.y;
                deltaZ = entity.getPosZ() - this.z;
            } else {
                deltaX = (boundingBox.maxX + boundingBox.minX) / 2;
                deltaY = (boundingBox.maxY + boundingBox.minY) / 2;
                deltaZ = (boundingBox.maxZ + boundingBox.minZ) / 2;
                d[0] = new Vector3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
                d[1] = new Vector3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
                d[2] = new Vector3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
                d[3] = new Vector3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
                d[4] = new Vector3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
                d[5] = new Vector3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
                d[6] = new Vector3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
                d[7] = new Vector3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
                d[8] = new Vector3d(boundingBox.minX, deltaY, deltaZ);
                d[9] = new Vector3d(boundingBox.maxX, deltaY, deltaZ);
                d[10] = new Vector3d(deltaX, boundingBox.minY, deltaZ);
                d[11] = new Vector3d(deltaX, boundingBox.maxY, deltaZ);
                d[12] = new Vector3d(deltaX, deltaY, boundingBox.minZ);
                d[13] = new Vector3d(deltaX, deltaY, boundingBox.maxZ);
                d[14] = new Vector3d(deltaX, deltaY, deltaZ);
                for (int i = 0; i < 15; i++) {
                    result = rayTraceBlocks(this.world, new RayTraceContext(explosionPos, d[i], RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, null));
                    minDistance = (result.getType() != RayTraceResult.Type.BLOCK) ? Math.min(minDistance, explosionPos.distanceTo(d[i])) : minDistance;
                }
                strength = minDistance * 2 / radius;
                deltaX -= this.x;
                deltaY -= this.y;
                deltaZ -= this.z;
            }

            if (strength > 1.0D)
                continue;

            double distanceToExplosion = MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

            if (distanceToExplosion != 0.0D) {
                deltaX /= distanceToExplosion;
                deltaY /= distanceToExplosion;
                deltaZ /= distanceToExplosion;
            } else {
                // Fixes an issue where explosion exactly on the player would cause no damage
                deltaX = 0.0;
                deltaY = 1.0;
                deltaZ = 0.0;
            }

            double damage = 1.0D - strength;
            entity.attackEntityFrom(this.getDamageSource(), (float) damage * this.power);

            if (entity instanceof LivingEntity) {
                damage = (float) ProtectionEnchantment.getBlastDamageReduction((LivingEntity) entity, damage);
            }

            entity.setMotion(entity.getMotion().add(deltaX * damage * radius / 5, deltaY * damage * radius / 5, deltaZ * damage * radius / 5));
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                if (!player.isSpectator() && (!player.isCreative() || !player.abilities.isFlying)) {
                    this.getPlayerKnockbackMap().put(player, new Vector3d(deltaX * damage * radius / 5, deltaY * damage * radius / 5, deltaZ * damage * radius / 5));
                }
            }
        }
    }

/*
    @Override
    public void doExplosionB(boolean spawnParticles) {
        if (this.world.isRemote) {
            this.world.playSound(this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F, false);
        }

        boolean flag = this.mode != Explosion.Mode.NONE;
        if (spawnParticles) {
            if (!(this.radius < 2.0F) && flag) {
                this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
            } else {
                this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
            }
        }

        if (flag) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();
            Collections.shuffle(this.affectedBlockPositions, this.world.rand);

            for(BlockPos blockpos : this.affectedBlockPositions) {
                BlockState blockstate = this.world.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                if (!blockstate.isAir(this.world, blockpos)) {
                    BlockPos blockpos1 = blockpos.toImmutable();
                    this.world.getProfiler().startSection("explosion_blocks");
                    if (blockstate.canDropFromExplosion(this.world, blockpos, this) && this.world instanceof ServerWorld) {
                        TileEntity tileentity = blockstate.hasTileEntity() ? this.world.getTileEntity(blockpos) : null;
                        LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld)this.world)).withRandom(this.world.rand).withParameter(LootParameters.field_237457_g_, Vector3d.copyCentered(blockpos)).withParameter(LootParameters.TOOL, ItemStack.EMPTY).withNullableParameter(LootParameters.BLOCK_ENTITY, tileentity).withNullableParameter(LootParameters.THIS_ENTITY, this.exploder);
                        if (this.mode == Explosion.Mode.DESTROY) {
                            lootcontext$builder.withParameter(LootParameters.EXPLOSION_RADIUS, this.radius);
                        }

                        blockstate.getDrops(lootcontext$builder).forEach((stack) -> {
                            handleExplosionDrops(objectarraylist, stack, blockpos1);
                        });
                    }

                    blockstate.onBlockExploded(this.world, blockpos, this);
                    this.world.getProfiler().endSection();
                }
            }

            for(Pair<ItemStack, BlockPos> pair : objectarraylist) {
                Block.spawnAsEntity(this.world, pair.getSecond(), pair.getFirst());
            }
        }

        if (Config.COMMON.gameplay.explosionCauseFire.get()) {
            for(BlockPos blockpos2 : this.affectedBlockPositions) {
                if (this.random.nextInt(3) == 0 && this.world.getBlockState(blockpos2).isAir() && this.world.getBlockState(blockpos2.down()).isOpaqueCube(this.world, blockpos2.down())) {
                    this.world.setBlockState(blockpos2, AbstractFireBlock.getFireForPlacement(this.world, blockpos2));
                }
            }
        }

    }

    private static void handleExplosionDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> dropPositionArray, ItemStack stack, BlockPos pos) {
        int i = dropPositionArray.size();

        for(int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = dropPositionArray.get(j);
            ItemStack itemstack = pair.getFirst();
            if (ItemEntity.canMergeStacks(itemstack, stack)) {
                ItemStack itemstack1 = ItemEntity.mergeStacks(itemstack, stack, 16);
                dropPositionArray.set(j, Pair.of(itemstack1, pair.getSecond()));
                if (stack.isEmpty()) {
                    return;
                }
            }
        }

        dropPositionArray.add(Pair.of(stack, pos));
    }*/

    private static BlockRayTraceResult rayTraceBlocks(World world, RayTraceContext context) {
        return performRayTrace(context, (rayTraceContext, blockPos) -> {
            BlockState blockState = world.getBlockState(blockPos);
            Block block = blockState.getBlock();
            boolean pass = block.isIn(ModTags.bullet_ignore);
            if(pass)return null;

            return getBlockRayTraceResult(world, rayTraceContext, blockPos, blockState);
        }, (rayTraceContext) -> {
            Vector3d Vector3d = rayTraceContext.getStartVec().subtract(rayTraceContext.getEndVec());
            return BlockRayTraceResult.createMiss(rayTraceContext.getEndVec(), Direction.getFacingFromVector(Vector3d.x, Vector3d.y, Vector3d.z), new BlockPos(rayTraceContext.getEndVec()));
        });
    }

    @Nullable
    private static BlockRayTraceResult getBlockRayTraceResult(World world, RayTraceContext rayTraceContext, BlockPos blockPos, BlockState blockState) {
        FluidState fluidState = world.getFluidState(blockPos);
        Vector3d startVec = rayTraceContext.getStartVec();
        Vector3d endVec = rayTraceContext.getEndVec();
        VoxelShape blockShape = rayTraceContext.getBlockShape(blockState, world, blockPos);
        BlockRayTraceResult blockResult = world.rayTraceBlocks(startVec, endVec, blockPos, blockShape, blockState);
        VoxelShape fluidShape = rayTraceContext.getFluidShape(fluidState, world, blockPos);
        BlockRayTraceResult fluidResult = fluidShape.rayTrace(startVec, endVec, blockPos);
        double blockDistance = blockResult == null ? Double.MAX_VALUE : rayTraceContext.getStartVec().squareDistanceTo(blockResult.getHitVec());
        double fluidDistance = fluidResult == null ? Double.MAX_VALUE : rayTraceContext.getStartVec().squareDistanceTo(fluidResult.getHitVec());
        return blockDistance <= fluidDistance ? blockResult : fluidResult;
    }

    private static <T> T performRayTrace(RayTraceContext context, BiFunction<RayTraceContext, BlockPos, T> hitFunction, Function<RayTraceContext, T> missFactory) {
        Vector3d startVec = context.getStartVec();
        Vector3d endVec = context.getEndVec();
        if (startVec.equals(endVec)) {
            return missFactory.apply(context);
        } else {
            double startX = MathHelper.lerp(-0.0000001, endVec.x, startVec.x);
            double startY = MathHelper.lerp(-0.0000001, endVec.y, startVec.y);
            double startZ = MathHelper.lerp(-0.0000001, endVec.z, startVec.z);
            double endX = MathHelper.lerp(-0.0000001, startVec.x, endVec.x);
            double endY = MathHelper.lerp(-0.0000001, startVec.y, endVec.y);
            double endZ = MathHelper.lerp(-0.0000001, startVec.z, endVec.z);
            int blockX = MathHelper.floor(endX);
            int blockY = MathHelper.floor(endY);
            int blockZ = MathHelper.floor(endZ);
            BlockPos.Mutable mutablePos = new BlockPos.Mutable(blockX, blockY, blockZ);
            T t = hitFunction.apply(context, mutablePos);
            if (t != null) {
                return t;
            }

            double deltaX = startX - endX;
            double deltaY = startY - endY;
            double deltaZ = startZ - endZ;
            int signX = MathHelper.signum(deltaX);
            int signY = MathHelper.signum(deltaY);
            int signZ = MathHelper.signum(deltaZ);
            double d9 = signX == 0 ? Double.MAX_VALUE : (double) signX / deltaX;
            double d10 = signY == 0 ? Double.MAX_VALUE : (double) signY / deltaY;
            double d11 = signZ == 0 ? Double.MAX_VALUE : (double) signZ / deltaZ;
            double d12 = d9 * (signX > 0 ? 1.0D - MathHelper.frac(endX) : MathHelper.frac(endX));
            double d13 = d10 * (signY > 0 ? 1.0D - MathHelper.frac(endY) : MathHelper.frac(endY));
            double d14 = d11 * (signZ > 0 ? 1.0D - MathHelper.frac(endZ) : MathHelper.frac(endZ));

            while (d12 <= 1.0D || d13 <= 1.0D || d14 <= 1.0D) {
                if (d12 < d13) {
                    if (d12 < d14) {
                        blockX += signX;
                        d12 += d9;
                    } else {
                        blockZ += signZ;
                        d14 += d11;
                    }
                } else if (d13 < d14) {
                    blockY += signY;
                    d13 += d10;
                } else {
                    blockZ += signZ;
                    d14 += d11;
                }

                T t1 = hitFunction.apply(context, mutablePos.setPos(blockX, blockY, blockZ));
                if (t1 != null) {
                    return t1;
                }
            }

            return missFactory.apply(context);
        }
    }
}
