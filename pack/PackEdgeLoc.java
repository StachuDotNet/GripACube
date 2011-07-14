/**
 * Middle layer edge locations.
 */
public class PackEdgeLoc {
  // upto A_N_EL = 495
  private final Comb comb;
  private int active; // which edges are specified

  public PackEdgeLoc(int active) {
    this.active = active;
    comb = new Comb(12, active(active));
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
    // placing of the 4 middle layer edge (unordered) < 12x11x10x9/4!
    return comb.combPack();
  }

  public void unpack(int e) {
    comb.combUnpack(e);
  }

  public int len() {
    return comb.comb;
  }

  public int startLen() {
    return comb.comb(4, active(active));
  }

  public int start(int pos) {
    Comb c = new Comb(4, active(active));
    c.combUnpack(pos);
    comb.clear();
    comb.a[8] = c.a[0]; // FR
    comb.a[9] = c.a[1]; // FL
    comb.a[10] = c.a[2]; // BR
    comb.a[11] = c.a[3]; // BL
    return pack();
  }

  public void cycle(int i1, int i2, int i3, int i4) {
    comb.cycle(i1, i2, i3, i4);
  }

  public void fill(int[] a) {
    comb.fill(a);
  }
}
