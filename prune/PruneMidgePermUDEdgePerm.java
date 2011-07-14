
final class PruneMidgePermUDEdgePerm {
  private byte[][] distance;

  public int distance(int mp, int ep) {
    return distance[mp][ep] & 63;
  }

  public boolean solved(int mp, int ep) {
    return distance[mp][ep] == 64;
  }

  public PruneMidgePermUDEdgePerm(Transform transform, Turn turn, int eActive, int mask) {
    PackMidgePerm packMidgePerm = new PackMidgePerm(eActive);
    int mpLen = packMidgePerm.len();
    int mpStart = packMidgePerm.startLen();
    PackUDEdgePerm packUDEdgePerm = new PackUDEdgePerm(eActive);
    int epLen = packUDEdgePerm.len();
    int epStart = packUDEdgePerm.startLen();
    distance = new byte[mpLen][epLen];
    for (int mp = 0; mp < mpLen; mp++)
      for (int ep = 0; ep < epLen; ep++)
        distance[mp][ep] = 63;
    int max = 0;
    for (int t = 0; t < Turn.B_NUM; t++)
      if (max < turn.lengthB[t])
        max = turn.lengthB[t];
    System.err.println(" Phase B: midge permutation - u/d edge permutation:");
    System.err.println("  Start goals: " + mpStart + " x " + epStart + " = " + mpStart * epStart);
    for (int mp = 0; mp < mpStart; mp++)
      for (int ep = 0; ep < epStart; ep++)
        distance[packMidgePerm.start(mp)][packUDEdgePerm.start(ep)] = 64;
    int states = mpStart * epStart;
    while (states > 0) {
      states = 0;
      for (int mp = 0; mp < mpLen; mp++) {
        for (int ep = 0; ep < epLen; ep++) {
          if ((distance[mp][ep] & 63) == 0) {
            for (int t = 0; t < Turn.B_NUM; t++) {
              if ((Turn.turnMaskB[t] & mask) != 0) {
                int nmp = transform.midgePerm.turnB(t, mp);
                int nep = transform.udEdgePerm.turnB(t, ep);
                if ((distance[nmp][nep] & 63) > 0) {
                  distance[nmp][nep] = 0;
                  states++;
                }
              }
            }
          }
        }
      }
    }
    for (int mp = 0; mp < mpLen; mp++)
      for (int ep = 0; ep < epLen; ep++)
        if ((distance[mp][ep] & 63) == 0)
          states++;
    System.err.println("  All goals: " + states); 
    byte depth = 0;
    int total = 0;
    while (states > 0) {
      total += states;
      System.err.println("   " + CubeReader.padInt(depth, 2) + " " +
       CubeReader.padInt(states, 8) + " " + CubeReader.padInt(total, 10));
      states = 0;
      depth++;
      for (int mp = 0; mp < mpLen; mp++) {
        for (int ep = 0; ep < epLen; ep++) {
          int dist = distance[mp][ep] & 63;
          if (dist < depth && dist + max >= depth) {
            for (int t = 0; t < Turn.B_NUM; t++) {
              if (dist + turn.lengthB[t] == depth) {
                int nmp = transform.midgePerm.turnB(t, mp);
                int nep = transform.udEdgePerm.turnB(t, ep);
                if ((distance[nmp][nep] & 63) > depth) {
                  distance[nmp][nep] = depth;
                  states++;
                }
              }
            }
          }
        }
      }
    }
  }
}
