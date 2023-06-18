package com.teamaurora.borealib.api.block.v1.compat;

import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * wooden post compatibility for Quark.
 */
public class PostBlock extends Block implements SimpleWaterloggedBlock {
    private static final VoxelShape SHAPE_X = box(0.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);
    private static final VoxelShape SHAPE_Y = box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private static final VoxelShape SHAPE_Z = box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 16.0D);

    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    private static final BooleanProperty[] CHAINED = new BooleanProperty[] {
            BooleanProperty.create("chain_down"),
            BooleanProperty.create("chain_up"),
            BooleanProperty.create("chain_north"),
            BooleanProperty.create("chain_south"),
            BooleanProperty.create("chain_west"),
            BooleanProperty.create("chain_east")
    };

    private final Supplier<Block> stripped;

    public PostBlock(Supplier<Block> stripped, Properties properties) {
        super(properties);
        this.stripped = stripped;
        BlockState defaultState = stateDefinition.any().setValue(WATERLOGGED, false).setValue(AXIS, Direction.Axis.Y);

        for (BooleanProperty prop : CHAINED)
            defaultState = defaultState.setValue(prop, false);

        this.registerDefaultState(defaultState);
    }

    public PostBlock(Properties properties) {
        this(null, properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(AXIS)) {
            case X -> SHAPE_X;
            case Y -> SHAPE_Y;
            default -> SHAPE_Z;
        };
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return !state.getValue(WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.getRelevantState(context.getLevel(), context.getClickedPos(), context.getClickedFace().getAxis());
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        BlockState newState = this.getRelevantState(worldIn, pos, state.getValue(AXIS));
        if (!newState.equals(state))
            worldIn.setBlockAndUpdate(pos, newState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, AXIS);
        for (BooleanProperty prop : CHAINED)
            builder.add(prop);
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction action, boolean simulate) {
        if (action == ToolActions.AXE_STRIP && this.stripped != null) {
            BlockState blockState = this.stripped.get().defaultBlockState();

            for (BooleanProperty chained : CHAINED)
                blockState.setValue(chained, state.getValue(chained));

            blockState.setValue(WATERLOGGED, state.getValue(WATERLOGGED));
            blockState.setValue(AXIS, state.getValue(AXIS));

            return blockState;
        }

        return super.getToolModifiedState(state, context, action, simulate);
    }

    private BlockState getRelevantState(Level world, BlockPos pos, Direction.Axis axis) {
        BlockState state = this.defaultBlockState().setValue(WATERLOGGED, world.getFluidState(pos).getType() == Fluids.WATER).setValue(AXIS, axis);

        for (Direction direction : Direction.values()) {
            if (direction.getAxis() == axis)
                continue;

            BlockState sideState = world.getBlockState(pos.relative(direction));
            if ((sideState.getBlock() instanceof ChainBlock && sideState.getValue(BlockStateProperties.AXIS) == direction.getAxis())
                    || (direction == Direction.DOWN && sideState.getBlock() instanceof LanternBlock && sideState.getValue(LanternBlock.HANGING)))
                state = state.setValue(CHAINED[direction.ordinal()], true);
        }

        return state;
    }
}
