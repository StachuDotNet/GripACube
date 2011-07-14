/**
 * Top layer edges ordered placing in the top and bottom layers.
 */
public class PackUsEdgePos {
  // upto N_HE = 1680
  private final Comb comb;
  private int active;

  public PackUsEdgePos(int active) {
    this.active = active;
    comb = new Comb(8, active(active));
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
    // placing of the up edges on up and down layers < 4!
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
    if ((active & 0x01) == 0) comb.a[0] = r++; // UF
    if ((active & 0x02) == 0) comb.a[1] = r++; // UR
    if ((active & 0x04) == 0) comb.a[2] = r++; // UB
    if ((active & 0x08) == 0) comb.a[3] = r++; // UL
    return pack();
  }

  public short toUDEdgePerm(int uh, int dh) {
    PackDsEdgePos pd = new PackDsEdgePos(active);
    PackUDEdgePerm pud = new PackUDEdgePerm(active);
    unpack(uh);
    int max = 0;
    for (int i = 0; i < 8; i++) {
      pud.set(i, comb.a[i]);
      if (max < comb.a[i])
        max = comb.a[i];
    }
    pd.unpack(dh);
    for (int i = 0; i < 8; i++) {
      if (pd.get(i) != 0)
        pud.set(i, pd.get(i) + max);
    }
    return (short)pud.pack();
  }
}
