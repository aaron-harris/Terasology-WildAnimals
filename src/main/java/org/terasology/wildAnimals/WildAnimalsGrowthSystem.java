/*
 * Copyright 2017 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.wildAnimals;

import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.delay.DelayManager;
import org.terasology.logic.delay.DelayedActionTriggeredEvent;
import org.terasology.logic.location.LocationComponent;
import org.terasology.registry.In;
import org.terasology.wildAnimals.component.WildAnimalComponent;
import org.terasology.wildAnimals.component.WildAnimalGrowthComponent;
import org.terasology.wildAnimals.event.AnimalGrowthEvent;

@RegisterSystem(RegisterMode.AUTHORITY)
public class WildAnimalsGrowthSystem extends BaseComponentSystem {
    @In
    private DelayManager delayManager;

    @In
    private EntityManager entityManager;

    private static final String GROWTH_ID = "WildAnimals:Growth";

    @ReceiveEvent(components = {WildAnimalComponent.class})
    public void onGrowthComponentActivated(OnActivatedComponent event, EntityRef entityRef, WildAnimalGrowthComponent wildAnimalGrowthComponent) {
        delayManager.addDelayedAction(entityRef, GROWTH_ID, wildAnimalGrowthComponent.timeToGrowth);
    }

    @ReceiveEvent(components = {WildAnimalComponent.class})
    public void onGrowth(DelayedActionTriggeredEvent event, EntityRef entityRef, WildAnimalGrowthComponent wildAnimalGrowthComponent) {
        LocationComponent locationComponent = entityRef.getComponent(LocationComponent.class);
        entityRef.send(new AnimalGrowthEvent());
        entityRef.destroy();
        entityManager.create(wildAnimalGrowthComponent.nextStagePrefab, locationComponent.getWorldPosition());
    }
}
