package raccoonman.reterraforged.common.level.levelgen.noise.climate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import raccoonman.reterraforged.common.level.levelgen.noise.Noise;
import raccoonman.reterraforged.common.level.levelgen.noise.NoiseUtil;

public record Moisture(Noise source, Noise power) implements Noise {
	public static final Codec<Moisture> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Noise.HOLDER_HELPER_CODEC.fieldOf("source").forGetter(Moisture::source),
		Noise.HOLDER_HELPER_CODEC.fieldOf("power").forGetter(Moisture::power)
	).apply(instance, Moisture::new));
	
//    public Moisture(final int scale, final int power) {
//        this(Source.simplex(scale, 1).clamp(0.125, 0.875).map(0.0, 1.0).freq(0.5, 1.0), power);
//    }
//    
    @Override
    public float compute(float x, float y, int seed) {
        float noise = this.source.compute(x, y, seed);
        float power = this.power.compute(x, y, seed);
        if (power < 2) {
            return noise;
        }
        noise = (noise - 0.5f) * 2.0f;
        float value = NoiseUtil.pow(noise, power);
        value = NoiseUtil.copySign(value, noise);
        return NoiseUtil.map(value, -1.0f, 1.0f, 2.0f);
    }

	@Override
	public Codec<Moisture> codec() {
		return CODEC;
	}

	@Override
	public Noise mapAll(Visitor visitor) {
		return visitor.apply(new Moisture(this.source.mapAll(visitor), this.power.mapAll(visitor)));
	}
}
