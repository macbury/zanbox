package de.macbury.zanbox.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import de.macbury.zanbox.level.terrain.tiles.BiomeDefinition;

public class TileLoader extends SynchronousAssetLoader<BiomeDefinition, TileLoader.TileLoaderParameter> {
  private final Json json;

  public TileLoader (FileHandleResolver resolver) {
    super(resolver);
    this.json = new Json();

  }

  @Override
  public BiomeDefinition load (AssetManager assetManager, String fileName, FileHandle file, TileLoaderParameter parameter) {
    String result = null;
    FileHandle fh = resolve(fileName);
    if (fh.exists()) {
      result = fh.readString("utf-8");
    }

    BiomeDefinition tileDefinition = json.fromJson(BiomeDefinition.class, result);
    return tileDefinition;
  }

  @Override
  public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, TileLoaderParameter parameter) {
    return null;
  }

  static public class TileLoaderParameter extends AssetLoaderParameters<BiomeDefinition> {
  }
}