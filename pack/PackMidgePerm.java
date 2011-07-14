/**
 * Middle layer edge permutations.
 */
public class PackMidgePerm {
  // upto B_N_MP = 24
  private final Comb comb;
  private int active; // which edges are specified

  public PackMidgePerm(int active) {
    this.active = active;
    comb = new Comb(4, active(active));
  }

  private static int active(int active) {
    int r = 0;
    if ((active & 0x100) == 0) r++; // FR
    if ((active & 0x200) == 0) r++; // FL
    if ((active & 0x400) == 0) r++; // BR
    if ((active & 0x800) == 0) r++; // BL
    return r;
  }

  public int pack() {
    // permutation of middle layer edges of the cube < 4!
    return comb.varPack();
  }

  public void unpack(int m) {
    comb.varUnpack(m);
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
    if ((active & 0x100) == 0) comb.a[0] = r++; // FR
    if ((active & 0x200) == 0) comb.a[1] = r++; // FL
    if ((active & 0x400) == 0) comb.a[2] = r++; // BR
    if ((active & 0x800) == 0) comb.a[3] = r++; // BL
    return pack();
  }

  public void cycle(int i1, int i2, int i3, int i4) {
    comb.cycle(i1, i2, i3, i4);
  }

  public void swap(int i1, int i2) {
    comb.swap(i1, i2);
  }
}
