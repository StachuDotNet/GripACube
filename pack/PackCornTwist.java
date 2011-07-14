/**
 * Corner twists.
 */
public class PackCornTwist {
  // upto A_N_CT = 2187 / A_M_CT = 20412
  private final Comb comb;
  private int active; // which corners are specified

  public PackCornTwist(int active) {
    this.active = active;
    comb = new Comb(8, active(active));
  }

  /**
   * Counts number of zeroes on the particular positions.
   * 0 - specified position, 1 - unspecified position
   */
  private static int active(int active) {
    int r = 0;
    if ((active & 0x01) == 0) r++; // UFR
    if ((active & 0x02) == 0) r++; // URB
    if ((active & 0x04) == 0) r++; // UBL
    if ((active & 0x08) == 0) r++; // ULF
    if ((active & 0x10) == 0) r++; // DRF
    if ((active & 0x20) == 0) r++; // DFL
    if ((active & 0x40) == 0) r++; // DLB
    if ((active & 0x80) == 0) r++; // DBR
    return r;
  }

  public int pack() {
    // twists of corners < 3^7 and more for incomplete cubes
    int c = 0;
    int l = 1;
    for (int i = 0; i < comb.a.length; i++) {
      if (comb.a[i] != 0) {
        l *= 3;
        c = 3 * c + comb.a[i] - 1;
      }
    }
    if ((active & 0xFF) == 0) { // if all orientations are set then the last one is determined
      l /= 3;
      c /= 3;
    }
    return comb.combPack() * l + c;
  }

  public void unpack(int c) {
    int l = 1;
    for (int i = 0; i < comb.a.length; i++) // count number of different orientations
      if ((~active & 1 << i) != 0)
        l *= 3;
    if ((active & 0xFF) != 0) { // some orientations are not set
      comb.combUnpack(c / l); // unpack combination
      c %= l; // let it be only orientations
      for (int i = comb.a.length - 1; i >= 0; i--) {
        if (comb.a[i] != 0) {
          comb.a[i] = c % 3 + 1;
          c /= 3;
        }
      }
    }
    else {  // all orientations are set
      int t = 0;
      l /= 3; // one corner orientation is given
      comb.combUnpack(c / l);
      c %= l;
      for (int i = comb.a.length - 2; i >= 0; i--) {
        comb.a[i] = c % 3 + 1;
        t += c % 3;
        c /= 3;
      }
      comb.a[comb.a.length - 1] = (3 - (t % 3)) % 3 + 1; // set the last orientation
    }
  }

  public int len() {
    int l = 1;
    for (int i = 0; i < comb.a.length; i++)
      if ((active & 1 << i) == 0)
        l *= 3;
    if ((active & 0xFF) == 0) // all orientations are set
      l /= 3;
    return comb.comb * l;
  }

  /**
   * Returns the number of all solved positions, that can be obtained by start(x).
   */
  public int startLen() {
    int p1 = 0;
    int o1 = 0;
    for (int i = 0; i < comb.a.length; i++) {
      if ((active >> 8 & 1 << i) != 0) {
        p1++;
        if ((active & 1 << i) == 0)
          o1++;
      }
    }
    return comb.comb(p1, o1);
  }

  /**
   * By redefining of start() and startLength(), solving to the specified state can be done.
   * position = 0, 1,..., startLength() - 1
   */
  public int start(int pos) {
    int p1 = 0;
    int o1 = 0;
    for (int i = 0; i < comb.a.length; i++) {
      if ((active >> 8 & 1 << i) != 0) { // unspecified position
        p1++;
        if ((active & 1 << i) == 0) // specified orientation
          o1++;
      }
    }
    Comb c = new Comb(p1, o1);
    c.combUnpack(pos);
    for (int i = 0, j = 0; i < comb.a.length; i++) {
      if ((active >> 8 & 1 << i) != 0) // unspecified position
        comb.a[i] = c.a[j++];
      else if ((active & 1 << i) == 0) // specified position and orientation
        comb.a[i] = 1;
      else // specified position, unspecified orientation
        comb.a[i] = 0;
    }
    return pack();
  }

  public void cycle(int i1, int i2, int i3, int i4) {
    comb.cycle(i1, i2, i3, i4);
  }

  public void twist(int i1, int i2, int i3, int i4) {
    if (comb.a[i1] != 0) comb.a[i1] = (comb.a[i1]) % 3 + 1; // clockwise
    if (comb.a[i2] != 0) comb.a[i2] = (comb.a[i2] + 1) % 3 + 1; // counter-clockwise
    if (comb.a[i3] != 0) comb.a[i3] = (comb.a[i3]) % 3 + 1; // clockwise
    if (comb.a[i4] != 0) comb.a[i4] = (comb.a[i4] + 1) % 3 + 1; // counter-clockwise
  }

  public void fill(int[] a) {
    comb.fill(a);
  }
}
