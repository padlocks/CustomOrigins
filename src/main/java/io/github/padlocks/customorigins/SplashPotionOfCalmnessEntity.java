package io.github.padlocks.customorigins;
/* package io.github.padlocks.customorigins.items;

import io.github.padlocks.customorigins.effect.EffectRegistry;
import io.github.padlocks.customorigins.networking.Packets;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

@SuppressWarnings("EntityConstructor")
public class SplashPotionOfCalmnessEntity extends ThrownItemEntity
{
	public SplashPotionOfCalmnessEntity(EntityType<? extends SplashPotionOfCalmnessEntity> entityType, World world)
	{
		super(entityType, world);
	}
	
	private SplashPotionOfCalmnessEntity(World worldIn, LivingEntity throwerIn)
	{
		super(RegistryObjects.getSplashPotionOfCalmnessEntityType(), throwerIn, worldIn);
	}
	
	public static SplashPotionOfCalmnessEntity asThrownEntity(World worldIn, LivingEntity throwerIn)
	{
		return new SplashPotionOfCalmnessEntity(worldIn, throwerIn);
	}

	@Override
	protected Item getDefaultItem()
	{
		return RegistryObjects.getSplashPotionOfCalmnessItem();
	}

	@Override
	protected float getGravity()
	{
		return 0.07F;
	}

	@Override
	protected void onCollision(HitResult result)
	{
		if (!this.world.isClient)
		{
			this.world.syncGlobalEvent(2002, this.getBlockPos(), 0x7cb2Ae);
			//WorldUtil.spawnAngryBees(this.world, result.getPos());
		}

		this.remove();
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult)
	{
		if (!this.world.isClient)
		{
			LivingEntity entity = (LivingEntity) entityHitResult.getEntity();
			entity.addStatusEffect(new StatusEffectInstance(EffectRegistry.CALMNESS, 3000, 1, false, false));
		}

		this.remove();
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return Packets.newSpawnPacket(this);
	}
} */