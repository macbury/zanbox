package test;

import com.badlogic.gdx.math.Vector3;
import org.junit.Assert;

/**
 * Created by macbury on 03.06.14.
 */
public class TestHelper {

  public static float[] vector3ToFloat(Vector3 in) {
    return new float[] { in.x, in.y, in.z };
  }

  public static void assertEqual(Vector3 a, Vector3 b) {
    Assert.assertArrayEquals(vector3ToFloat(b), vector3ToFloat(a), 0.1f);
  }
}
