/**
 * Top and bottom layer edge permutations.
 */
public class PackUDEdgePerm {
  // upto B_N_EP = 40320
  private final Comb comb;
  private int active; // which edges are specified

  public PackUDEdgePerm(int active) {
    this.active = active;
    comb = new Comb(8, active(active));
  }

  private static int active(int active) {
    int r = 0;
    if ((active & 0x01) == 0) r++; // UF
    if ((active & 0x02) == 0) r++; // UR
    if ((active & 0x04) == 0) r++; // UB
    if ((active & 0x08) == 0) r++; // UL
    if ((active & 0x10) == 0) r++; // DF
    if ((active & 0x20) == 0) r++; // DR
    if ((active & 0x40) == 0) r++; // DB
    if ((active & 0x80) == 0) r++; // DL
    return r;
  }

  public int pack() {
    // permutation of up and down edges of the cube < 8!
    return comb.varPack();
  }

  public void unpack(int e) {
    comb.varUnpack(e);
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
    if ((active & 0x10) == 0) comb.a[4] = r++; // DF
    if ((active & 0x20) == 0) comb.a[5] = r++; // DR
    if ((active & 0x40) == 0) comb.a[6] = r++; // DB
    if ((active & 0x80) == 0) comb.a[7] = r++; // DL
    return pack();
  }

  public void cycle(int i1, int i2, int i3, int i4) {
    comb.cycle(i1, i2, i3, i4);
  }

  public void swap(int i1, int i2) {
    comb.swap(i1, i2);
  }

  public int get(int i) {
    return comb.a[i];
  }

  public void set(int i, int value) {
    comb.a[i] = value;
  }
}
