/**
 * Bottom layer edges ordered placing in the top and bottom layers.
 */
public class PackDsEdgePos {
  // upto N_HE = 1680
  private final Comb comb;
  private int active;

  public PackDsEdgePos(int active) {
    this.active = active;
    comb = new Comb(8, active(active));
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

  public void unpack(int h) {
    comb.varUnpack(h);
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

  public int get(int i) {
    return comb.a[i];
  }

  public void set(int i, int value) {
    comb.a[i] = value;
  }
}
