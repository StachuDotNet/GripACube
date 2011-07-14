/**
 * Edge flips.
 */
public class PackEdgeFlip {
  // upto A_N_EF = 2048 / A_M_EF = 126720
  private final Comb comb;
  private int active; // which edges are specified

  public PackEdgeFlip(int active) {
    this.active = active;
    comb = new Comb(12, active(active));
  }
  
  private static int active(int active) {
    int r = 0;
    if ((active & 0x001) == 0) r++; // UF
    if ((active & 0x002) == 0) r++; // UR
    if ((active & 0x004) == 0) r++; // UB
    if ((active & 0x008) == 0) r++; // UL
    if ((active & 0x010) == 0) r++; // DF
    if ((active & 0x020) == 0) r++; // DR
    if ((active & 0x040) == 0) r++; // DB
    if ((active & 0x080) == 0) r++; // DL
    if ((active & 0x100) == 0) r++; // FR
    if ((active & 0x200) == 0) r++; // FL
    if ((active & 0x400) == 0) r++; // BR
    if ((active & 0x800) == 0) r++; // BL
    return r;
  }

  public int pack() {
    // flips of edges < 2^11 and even more for incomplete cubes
    int e = 0;
    int l = 1;
    for (int i = 0; i < comb.a.length; i++)
      if (comb.a[i] != 0) {
        l *= 2;
        e = 2 * e + comb.a[i] - 1;
      }
    if ((active & 0xFFF) == 0) {
      l /= 2;
      e /= 2;
    }
    return comb.combPack() * l + e;
  }

  public void unpack(int e) {
    int l = 1;
    for (int i = 0; i < comb.a.length; i++)
      if ((active & 1 << i) == 0)
        l *= 2;
    if ((active & 0xFFF) != 0) {
      comb.combUnpack(e / l);
      e %= l;
      for (int i = comb.a.length - 1; i >= 0; i--) {
        if (comb.a[i] != 0) {
          comb.a[i] = e % 2 + 1;
          e /= 2;
        }
      }
    }
    else {
      int f = 0;
      l /= 2;
      comb.combUnpack(e / l);
      e %= l;
      for (int i = comb.a.length - 2; i >= 0; i--) {
        comb.a[i] = e % 2 + 1;
        f += e % 2;
        e /= 2;
      }
      comb.a[comb.a.length - 1] = (2 - f % 2) % 2 + 1;
    }
  }

  public int len() {
    int l = 1;
    for (int i = 0; i < comb.a.length; i++)
      if ((active & 1 << i) == 0)
        l *= 2;
    if ((active & 0xFFF) == 0)
      l /= 2;
    return comb.comb * l;
  }

  public int startLen() {
    int p1 = 0;
    int o1 = 0;
    for (int i = 0; i < comb.a.length; i++) {
      if ((active >> 12 & 1 << i) != 0) {
        p1++;
        if ((active & 1 << i) == 0)
          o1++;
      }
    }
    return comb.comb(p1, o1);
  }

  public int start(int pos) {
    int p1 = 0;
    int o1 = 0;
    for (int i = 0; i < comb.a.length; i++) {
      if ((active >> 12 & 1 << i) != 0) {
        p1++;
        if ((active & 1 << i) == 0)
          o1++;
      }
    }
    Comb c = new Comb(p1, o1);
    c.combUnpack(pos);
    for (int i = 0, j = 0; i < comb.a.length; i++) {
      if ((active >> 12 & 1 << i) != 0)
        comb.a[i] = c.a[j++];
      else if ((active & 1 << i) == 0)
        comb.a[i] = 1;
      else
        comb.a[i] = 0;
    }
    return pack();
  }

  public void cycle(int i1, int i2, int i3, int i4) {
    comb.cycle(i1, i2, i3, i4);
  }

  public void flip(int i1, int i2, int i3, int i4) {
    if (comb.a[i1] != 0) comb.a[i1] = 3 - comb.a[i1];
    if (comb.a[i2] != 0) comb.a[i2] = 3 - comb.a[i2];
    if (comb.a[i3] != 0) comb.a[i3] = 3 - comb.a[i3];
    if (comb.a[i4] != 0) comb.a[i4] = 3 - comb.a[i4];
  }

  public void fill(int[] a) {
    comb.fill(a);
  }
}
