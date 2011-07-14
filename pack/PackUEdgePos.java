/**
 * Top layer edges ordered placing.
 */
public class PackUEdgePos {
  // upto N_SE = 11880
  private final Comb comb;
  private int active; // which edges are specified

  public PackUEdgePos(int active) {
    this.active = active;
    comb = new Comb(12, active(active));
  }

  private static int active(int active) {
    int r = 0;
    if ((active & 0x01) == 0) r++; // UF
    if ((active & 0x02) == 0) r++; // UR
    if ((active & 0x04) == 0) r++; // UB
    if ((active & 0x08) == 0) r++; // UL
    return r;
  }

  public int pack() {
    // placing of the 4 edges of the cube --> < 12x11x10x9
    return comb.varPack();
  }

  public void unpack(int s) {
    comb.varUnpack(s);
  }

  public int len() {
    return comb.var;
  }

  public int startLen() {
    return 1;
  }

  public int start(int pos) {
    int r = 1;
    comb.clear();
    if ((active & 0x01) == 0) comb.a[0] = r++; // UF
    if ((active & 0x02) == 0) comb.a[1] = r++; // UR
    if ((active & 0x04) == 0) comb.a[2] = r++; // UB
    if ((active & 0x08) == 0) comb.a[3] = r++; // UL
    return pack();
  }

  public short toUSlice(int s) {
    // converts the placing of the 4 up edges on any positions to
    // the placing of these 4 edges on up and down layer
    unpack(s);
    Comb c = new Comb(8, active(active));
    c.fill(comb.a); // copy U and D edges
    return (short)c.varPack();
  }

  public boolean isInB(int s) {
    unpack(s);
    return (comb.a[8] | comb.a[9] | comb.a[10] | comb.a[11]) == 0;
  }

  public void cycle(int i1, int i2, int i3, int i4) {
    comb.cycle(i1, i2, i3, i4);
  }

  public void fill(int[] a) {
    comb.fill(a);
  }
}
