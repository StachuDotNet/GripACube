/**
 * Bottom layer edges ordered placing.
 */
public class PackDEdgePos {
  // upto N_SE = 11880
  private final Comb comb;
  private int active; // which edges are specified

  public PackDEdgePos(int active) {
    this.active = active;
    comb = new Comb(12, active(active));
  }

  private static int active(int active) {
    int r = 0;
    if ((active & 0x10) == 0) r++; // DF
    if ((active & 0x20) == 0) r++; // DR
    if ((active & 0x40) == 0) r++; // DB
    if ((active & 0x80) == 0) r++; // DL
    return r;
  }

  public int pack() {
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
    if ((active & 0x10) == 0) comb.a[4] = r++; // DF
    if ((active & 0x20) == 0) comb.a[5] = r++; // DR
    if ((active & 0x40) == 0) comb.a[6] = r++; // DB
    if ((active & 0x80) == 0) comb.a[7] = r++; // DL
    return pack();
  }

  public short toDSlice(int s) {
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
