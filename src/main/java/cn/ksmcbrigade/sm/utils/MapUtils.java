package cn.ksmcbrigade.sm.utils;

import cn.ksmcbrigade.sm.config.mode.MapMode;
import cn.ksmcbrigade.sm.records.PosRecord;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class MapUtils {
    public static void update(Level p_42894_, Entity p_42895_, MapItemSavedData p_42896_, int size,MapMode mode) {
        if (p_42894_.dimension() == p_42896_.dimension && p_42895_ instanceof Player) {
            int i = 1 << p_42896_.scale;
            int j = p_42896_.centerX;
            int k = p_42896_.centerZ;
            int r = size/2;
            int l,i1;
                l = r;
                i1 = r;

            int j1 = size/i;
            /*if (p_42894_.dimensionType().hasCeiling()) {
                j1 /= 2;
            }*/

            MapItemSavedData.HoldingPlayer mapitemsaveddata$holdingplayer = p_42896_.getHoldingPlayer((Player)p_42895_);
            ++mapitemsaveddata$holdingplayer.step;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();
            boolean flag = false;

            for(int k1 = l - j1 + 1; k1 < l + j1; ++k1) {
                if ((k1 & 15) == (mapitemsaveddata$holdingplayer.step & 15) || flag) {
                    flag = false;
                    double d0 = 0.0D;

                    for(int l1 = i1 - j1 - 1; l1 < i1 + j1; ++l1) {
                        if (k1 >= 0 && l1 >= -1 && k1 < size && l1 < size) {
                            int dx = k1 - r;
                            int dz = l1 - r;
                            int distanceSquared = dx * dx + dz * dz;
                            if (mode==MapMode.ROUND && !(distanceSquared <= r*r)){
                                continue;
                            }
                            int i2 = Mth.square(k1 - l) + Mth.square(l1 - i1);
                            boolean flag1 = i2 > (j1 - 2) * (j1 - 2);
                            int j2 = (j / i + k1 - r) * i;
                            int k2 = (k / i + l1 - r) * i;
                            Multiset<MapColor> multiset = LinkedHashMultiset.create();
                            LevelChunk levelchunk = p_42894_.getChunk(SectionPos.blockToSectionCoord(j2), SectionPos.blockToSectionCoord(k2));
                            if (!levelchunk.isEmpty()) {
                                int l2 = 0;
                                double d1 = 0.0D;
                                /*if (p_42894_.dimensionType().hasCeiling()) {
                                    int i3 = j2 + k2 * 231871;
                                    i3 = i3 * i3 * 31287121 + i3 * 11;
                                    if ((i3 >> 20 & 1) == 0) {
                                        multiset.add(Blocks.DIRT.defaultBlockState().getMapColor(p_42894_, BlockPos.ZERO), 10);
                                    } else {
                                        multiset.add(Blocks.STONE.defaultBlockState().getMapColor(p_42894_, BlockPos.ZERO), 100);
                                    }

                                    d1 = 100.0D;
                                } else {*/
                                    for(int i4 = 0; i4 < i; ++i4) {
                                        for(int j3 = 0; j3 < i; ++j3) {
                                            blockpos$mutableblockpos.set(j2 + i4, 0, k2 + j3);
                                            int k3 = levelchunk.getHeight(Heightmap.Types.WORLD_SURFACE, blockpos$mutableblockpos.getX(), blockpos$mutableblockpos.getZ()) + 1;
                                            BlockState blockstate;
                                            if (k3 <= p_42894_.getMinBuildHeight() + 1) {
                                                blockstate = Blocks.BEDROCK.defaultBlockState();
                                            } else {
                                                if(p_42894_.dimensionType().hasCeiling() && p_42895_.getBlockY()<128) k3 = getY(p_42895_.blockPosition(),p_42894_);
                                                do {
                                                    --k3;


                                                        blockpos$mutableblockpos.setY(k3);
                                                    blockstate = levelchunk.getBlockState(blockpos$mutableblockpos);
                                                } while(blockstate.getMapColor(p_42894_, blockpos$mutableblockpos) == MapColor.NONE && k3 > p_42894_.getMinBuildHeight());

                                                if (k3 > p_42894_.getMinBuildHeight() && !blockstate.getFluidState().isEmpty()) {
                                                    int l3 = k3 - 1;
                                                    blockpos$mutableblockpos1.set(blockpos$mutableblockpos);

                                                    BlockState blockstate1;
                                                    do {
                                                            blockpos$mutableblockpos1.setY(l3--);
                                                        blockstate1 = levelchunk.getBlockState(blockpos$mutableblockpos1);
                                                        ++l2;
                                                    } while(l3 > p_42894_.getMinBuildHeight() && !blockstate1.getFluidState().isEmpty());

                                                    blockstate = getCorrectStateForFluidBlock(p_42894_, blockstate, blockpos$mutableblockpos);
                                                }
                                            }

                                            p_42896_.checkBanners(p_42894_, blockpos$mutableblockpos.getX(), blockpos$mutableblockpos.getZ());
                                            d1 += (double)k3 / (double)(i * i);
                                            multiset.add(blockstate.getMapColor(p_42894_, blockpos$mutableblockpos));
                                        }
                                    }
                                //}

                                l2 /= i * i;
                                MapColor mapcolor = Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MapColor.NONE);
                                MapColor.Brightness mapcolor$brightness;
                                if (mapcolor == MapColor.WATER) {
                                    double d2 = (double)l2 * 0.1D + (double)(k1 + l1 & 1) * 0.2D;
                                    if (d2 < 0.5D) {
                                        mapcolor$brightness = MapColor.Brightness.HIGH;
                                    } else if (d2 > 0.9D) {
                                        mapcolor$brightness = MapColor.Brightness.LOW;
                                    } else {
                                        mapcolor$brightness = MapColor.Brightness.NORMAL;
                                    }
                                } else {
                                    double d3 = (d1 - d0) * 4.0D / (double)(i + 4) + ((double)(k1 + l1 & 1) - 0.5D) * 0.4D;
                                    if (d3 > 0.6D) {
                                        mapcolor$brightness = MapColor.Brightness.HIGH;
                                    } else if (d3 < -0.6D) {
                                        mapcolor$brightness = MapColor.Brightness.LOW;
                                    } else {
                                        mapcolor$brightness = MapColor.Brightness.NORMAL;
                                    }
                                }

                                d0 = d1;
                                if (l1 >= 0 && i2 < j1 * j1 && (!flag1 || (k1 + l1 & 1) != 0)) {
                                    flag |= p_42896_.updateColor(k1, l1, mapcolor.getPackedId(mapcolor$brightness));
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    private static BlockState getCorrectStateForFluidBlock(Level p_42901_, BlockState p_42902_, BlockPos p_42903_) {
        FluidState fluidstate = p_42902_.getFluidState();
        return !fluidstate.isEmpty() && !p_42902_.isFaceSturdy(p_42901_, p_42903_, Direction.UP) ? fluidstate.createLegacyBlock() : p_42902_;
    }

    public static PosRecord getMapPos(Entity entity,MapItemSavedData data,int size,MapMode mode){
        int i = 1 << data.scale;
        int j = data.centerX;
        int k = data.centerZ;
        int r = size/2;
        int mapX,mapZ;
        mapX = Mth.floor((entity.getX() - (double)j) / i) + r;
        mapZ = Mth.floor((entity.getZ() - (double)k) / i) + r;
        if(mode==MapMode.ROUND){
            int dx = mapX - r;
            int dz = mapZ - r;
            int distanceSquared = dx * dx + dz * dz;
            int radiusSquared = r * r;
            if (distanceSquared > radiusSquared) {
                mapX = -1;
                mapZ = -1;
            } else {
                if (mapX < 0) mapX = 0;
                if (mapX >= size) mapX = size - 1;
                if (mapZ < 0) mapZ = 0;
                if (mapZ >= size) mapZ = size - 1;
            }
        }
        return new PosRecord(mapX,mapZ);
    }

    private static int getY(BlockPos or, Level getter) {
        int y = getter.dimensionType().logicalHeight()-1;
        int result = y;
        while (y > or.getY()) {
            if(getter.getBlockState(new BlockPos(or.getX(), y, or.getZ())).isAir()){
                result = y;
                //System.out.println(new BlockPos(or.getX(), y, or.getZ()).toShortString());
                break;
            }
            y--;
        }
        return result+1;
    }
}
