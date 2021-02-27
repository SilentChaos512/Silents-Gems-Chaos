package net.silentchaos512.gemschaos.data.chaosbuff;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.gemschaos.chaosbuff.IChaosBuffSerializer;
import net.silentchaos512.gemschaos.chaosbuff.ICostCondition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChaosBuffBuilder {
    protected final ResourceLocation buffId;
    protected final int maxLevel;
    private final IChaosBuffSerializer<?> serializer;

    protected ITextComponent displayName;
    private int inactiveCost;
    private final List<Integer> activeCostByLevel = new ArrayList<>();
    private final List<Integer> slotsByLevel = new ArrayList<>();
    private final List<ICostCondition> costConditions = new ArrayList<>();

    public ChaosBuffBuilder(ResourceLocation buffId, int maxLevel, IChaosBuffSerializer<?> serializer) {
        this.buffId = buffId;
        this.maxLevel = maxLevel;
        this.serializer = serializer;
    }

    public static ChaosBuffBuilder builder(ResourceLocation buffId, int maxLevel, IChaosBuffSerializer<?> serializer) {
        return new ChaosBuffBuilder(buffId, maxLevel, serializer);
    }

    public ChaosBuffBuilder withSlots(Integer... slots) {
        Collections.addAll(this.slotsByLevel, slots);
        return this;
    }

    public ChaosBuffBuilder withCosts(int inactiveCost, Integer... activeCosts) {
        this.inactiveCost = inactiveCost;
        Collections.addAll(this.activeCostByLevel, activeCosts);
        return this;
    }

    public ChaosBuffBuilder withCostConditions(ICostCondition... conditions) {
        Collections.addAll(this.costConditions, conditions);
        return this;
    }

    public ChaosBuffBuilder withDisplayName(ITextComponent displayName) {
        this.displayName = displayName;
        return this;
    }

    public JsonObject serialize() {
        JsonObject json = new JsonObject();

        json.addProperty("type", serializer.getName().toString());
        json.addProperty("maxLevel", maxLevel);
        json.add("displayName", ITextComponent.Serializer.toJsonTree(displayName));

        JsonArray slots = new JsonArray();
        slotsByLevel.forEach(slots::add);
        json.add("slots", slots);

        JsonObject cost = new JsonObject();
        cost.addProperty("inactive", inactiveCost);

        JsonArray activeCosts = new JsonArray();
        activeCostByLevel.forEach(activeCosts::add);
        cost.add("active", activeCosts);

        JsonArray conditions = new JsonArray();
        costConditions.forEach(c -> conditions.add(c.getName()));
        cost.add("conditions", conditions);

        json.add("cost", cost);

        return json;
    }
}
