/**
 * Corner permutations.
 */
public class PackCornPerm {
  // upto B_N_CP = 40320
  private final Comb comb;
  private int active; // which corners are specified

  public PackCornPerm(int active) {
    this.active = active;
    comb = new Comb(8, active(active));
  }

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
    // permutation of the corners of the cube < 8!
    return comb.varPack();
  }

  public void unpack(int c) {
    comb.varUnpack(c);
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
    if ((active & 0x01) == 0) comb.a[0] = r++; // UFR
    if ((active & 0x02) == 0) comb.a[1] = r++; // URB
    if ((active & 0x04) == 0) comb.a[2] = r++; // UBL
    if ((active & 0x08) == 0) comb.a[3] = r++; // ULF
    if ((active & 0x10) == 0) comb.a[4] = r++; // DRF
    if ((active & 0x20) == 0) comb.a[5] = r++; // DFL
    if ((active & 0x40) == 0) comb.a[6] = r++; // DLB
    if ((active & 0x80) == 0) comb.a[7] = r++; // DBR
    return pack();
  }

  public void cycle(int i1, int i2, int i3, int i4) {
    comb.cycle(i1, i2, i3, i4);
  }

  public void fill(int[] a) {
    comb.fill(a);
  }
}
