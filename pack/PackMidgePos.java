/**
 * Middle layer edges ordered placing.
 */
public class PackMidgePos {
  // upto N_SE = 11880
  private final Comb comb;
  private int active; // which edges are specified

  public PackMidgePos(int active) {
    this.active = active;
    comb = new Comb(12, active(active));
  }

  public int active(int active) {
    int r = 0;
    if ((active & 0x100) == 0) r++; // FR 
    if ((active & 0x200) == 0) r++; // FL
    if ((active & 0x400) == 0) r++; // BR
    if ((active & 0x800) == 0) r++; // BL
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
    if ((active & 0x100) == 0) comb.a[8] = r++; // FR
    if ((active & 0x200) == 0) comb.a[9] = r++; // FL
    if ((active & 0x400) == 0) comb.a[10] = r++; // BR
    if ((active & 0x800) == 0) comb.a[11] = r++; // BL
    return pack();
  }

  public short toMidgePerm(int s) {
    Comb c = new Comb(4, active(active));
    unpack(s);
    c.a[0] = comb.a[8]; // FR
    c.a[1] = comb.a[9]; // FL
    c.a[2] = comb.a[10]; // BR
    c.a[3] = comb.a[11]; // BL
    return (short)c.varPack();
  }

  public void cycle(int i1, int i2, int i3, int i4) {
    comb.cycle(i1, i2, i3, i4);
  }

  public void fill(int[] a) {
    comb.fill(a);
  }
}
