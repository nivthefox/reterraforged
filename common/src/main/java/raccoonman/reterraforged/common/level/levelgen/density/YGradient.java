package raccoonman.reterraforged.common.level.levelgen.density;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import raccoonman.reterraforged.common.level.levelgen.noise.NoiseUtil;

//TODO do block scaling here
//TODO don't hardcode min / max values
public record YGradient(DensityFunction y, DensityFunction scale) implements DensityFunction.SimpleFunction {
	public static final Codec<YGradient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		DensityFunction.HOLDER_HELPER_CODEC.fieldOf("y").forGetter(YGradient::y),
		DensityFunction.HOLDER_HELPER_CODEC.fieldOf("scale").forGetter(YGradient::scale)
	).apply(instance, YGradient::new));
	
	@Override
	public double compute(FunctionContext ctx) {
		double blockY = ctx.blockY();
		double scale = this.scale.compute(ctx);
		double noiseY = NoiseUtil.floor((float) this.y.compute(ctx) * scale);
		if(blockY > noiseY) {
			return 0.0F;
		} else {
			return 1.0F;
		}
	}

	@Override
	public DensityFunction mapAll(Visitor visitor) {
		return visitor.apply(new YGradient(this.y.mapAll(visitor), this.scale.mapAll(visitor)));
	}

	@Override
	public double minValue() {
		return -1.0D;
	}

	@Override
	public double maxValue() {
		return 1.0D;
	}

	@Override
	public KeyDispatchDataCodec<YGradient> codec() {
		return new KeyDispatchDataCodec<>(CODEC);
	}
}
