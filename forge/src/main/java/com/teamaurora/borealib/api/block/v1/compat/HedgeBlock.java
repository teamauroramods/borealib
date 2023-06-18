package com.teamaurora.borealib.api.block.v1.compat;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

/**
 * leaf carpet compatibility for Quark.
 */
public class HedgeBlock extends FenceBlock {
    private static final VoxelShape WOOD_SHAPE = box(6.0D, 0.0D, 6.0D, 10.0D, 15.0D, 10.0D);
    private static final VoxelShape HEDGE_CENTER_SHAPE = box(2.0D, 1.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    private static final VoxelShape NORTH_SHAPE = box(2.0D, 1.0D, 0.0D, 14.0D, 16.0D, 2.0D);
    private static final VoxelShape SOUTH_SHAPE = box(2.0D, 1.0D, 14.0D, 14.0D, 16.0D, 15.0D);
    private static final VoxelShape EAST_SHAPE = box(14.0D, 1.0D, 2.0D, 16.0D, 16.0D, 14.0D);
    private static final VoxelShape WEST_SHAPE = box(0.0D, 1.0D, 2.0D, 2.0D, 16.0D, 14.0D);
    private static final VoxelShape EXTEND_SHAPE = box(2.0D, 0.0D, 2.0D, 14.0D, 1.0D, 14.0D);
    private static final BooleanProperty EXTEND = BooleanProperty.create("extend");

    private final Object2IntMap<BlockState> hedgeStateToIndex;
    private final VoxelShape[] hedgeShapes;

    public HedgeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(EXTEND, false));

        this.hedgeStateToIndex = new Object2IntOpenHashMap<>();
        this.hedgeShapes = this.cacheHedgeShapes(stateDefinition.getPossibleStates());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return hedgeShapes[getHedgeAABBIndex(state)];
    }

    private VoxelShape[] cacheHedgeShapes(ImmutableList<BlockState> possibleStates) {
        VoxelShape[] shapes = new VoxelShape[possibleStates.size()];

        for (int i = 0; i < shapes.length; i++) {
            BlockState state = possibleStates.get(i);
            VoxelShape finishedShape = Shapes.or(state.getValue(HedgeBlock.EXTEND) ? EXTEND_SHAPE : WOOD_SHAPE, HEDGE_CENTER_SHAPE);
            int realIndex = getHedgeAABBIndex(state);

            if (state.getValue(FenceBlock.NORTH)) finishedShape = Shapes.or(finishedShape, NORTH_SHAPE);
            if (state.getValue(FenceBlock.SOUTH)) finishedShape = Shapes.or(finishedShape, SOUTH_SHAPE);
            if (state.getValue(FenceBlock.EAST)) finishedShape = Shapes.or(finishedShape, EAST_SHAPE);
            if (state.getValue(FenceBlock.WEST)) finishedShape = Shapes.or(finishedShape, WEST_SHAPE);

            shapes[realIndex] = finishedShape;
        }

        return shapes;
    }

    protected int getHedgeAABBIndex(BlockState curr) {
        return hedgeStateToIndex.computeIntIfAbsent(curr, (state) -> {
            int i = 0;

            if (state.getValue(FenceBlock.NORTH))
                i |= 0b00001;
            if (state.getValue(FenceBlock.SOUTH))
                i |= 0b00010;
            if (state.getValue(FenceBlock.EAST))
                i |= 0b00100;
            if (state.getValue(FenceBlock.WEST))
                i |= 0b01000;
            if (state.getValue(EXTEND))
                i |= 0b10000;

            return i;
        });
    }

    @Override
    public boolean connectsTo(BlockState state, boolean isSideSolid, Direction direction) {
        return false; //state.is(HEDGE BLOCK TAG);
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter getter, BlockPos pos, Direction facing, IPlantable plantable) {
        return facing == Direction.UP && !state.getValue(WATERLOGGED) && plantable.getPlantType(getter, pos) == PlantType.PLAINS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return null; //super.getStateForPlacement(context).setValue(EXTEND, context.getLevel().getBlockState(context.getClickedPos().below()).is(HEDGE BLOCK TAG));
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED))
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

        return null; //facing == Direction.DOWN ? stateIn.setValue(EXTEND, facingState.is(HEDGE BLOCK TAG)) : super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(EXTEND);
    }


}
