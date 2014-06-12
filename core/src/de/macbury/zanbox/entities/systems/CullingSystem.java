package de.macbury.zanbox.entities.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import de.macbury.zanbox.entities.components.*;
import de.macbury.zanbox.level.GameLevel;

/**
 * Created by macbury on 03.06.14.
 */
public class CullingSystem extends EntityProcessingSystem {
  @Mapper
  ComponentMapper<PositionComponent> positionMapper;
  @Mapper
  ComponentMapper<BoundingBoxComponent> boundingBoxComponentMapper;
  @Mapper
  ComponentMapper<VisibleComponent> visibleMapper;
  private GameLevel level;

  public CullingSystem(GameLevel level) {
    super(Aspect.getAspectForAll(PositionComponent.class, VisibleComponent.class, BoundingBoxComponent.class));
    this.level = level;
  }

  @Override
  protected void process(Entity entity) {
    BoundingBoxComponent boxComponent     = boundingBoxComponentMapper.get(entity);
    PositionComponent  positionComponent  = positionMapper.get(entity);
    VisibleComponent   visibleComponent   = visibleMapper.get(entity);

    visibleComponent.visible = level.chunksRenderables.visibleBoundingBox.intersects(boxComponent.getBoundingBox(positionComponent.vector));
  }
}
