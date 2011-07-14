
final class PruneMidgePermCornPerm {
  private byte[][] distance;

  public int distance(int mp, int cp) {
    return distance[mp][cp] & 63;
  }

  public boolean solved(int mp, int cp) {
    return distance[mp][cp] == 64;
  }

  public PruneMidgePermCornPerm(Transform transform, Turn turn, int eActive, int cActive, int mask) {
    PackMidgePerm packMidgePerm = new PackMidgePerm(eActive);
    int mpLen = packMidgePerm.len();
    int mpStart = packMidgePerm.startLen();
    PackCornPerm packCornPerm = new PackCornPerm(cActive);
    int cpLen = packCornPerm.len();
    int cpStart = packCornPerm.startLen();
    distance = new byte[mpLen][cpLen];
    for (int mp = 0; mp < mpLen; mp++)
      for (int cp = 0; cp < cpLen; cp++)
        distance[mp][cp] = 63;
    int max = 0;
    for (int t = 0; t < Turn.B_NUM; t++)
      if (max < turn.lengthB[t])
        max = turn.lengthB[t];
    System.err.println(" Phase A: midge permutation - corner permutation:");
    System.err.println("  Start goals: " + mpStart + " x " + cpStart + " = " + mpStart * cpStart);
    for (int mp = 0; mp < mpStart; mp++)
      for (int cp = 0; cp < cpStart; cp++)
        distance[packMidgePerm.start(mp)][packCornPerm.start(cp)] = 64;
    int states = mpStart * cpStart;
    while (states > 0) {
      states = 0;
      for (int mp = 0; mp < mpLen; mp++) {
        for (int cp = 0; cp < cpLen; cp++) {
          if ((distance[mp][cp] & 63) == 0) {
            for (int t = 0; t < Turn.B_NUM; t++) {
              if ((Turn.turnMaskB[t] & mask) != 0) {
                int nmp = transform.midgePerm.turnB(t, mp);
                int ncp = transform.cornPerm.turnB(t, cp);
                if ((distance[nmp][ncp] & 63) > 0) {
                  distance[nmp][ncp] = 0;
                  states++;
                }
              }
            }
          }
        }
      }
    }
    for (int mp = 0; mp < mpLen; mp++)
      for (int cp = 0; cp < cpLen; cp++)
        if ((distance[mp][cp] & 63) == 0)
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
        for (int cp = 0; cp < cpLen; cp++) {
          int dist = distance[mp][cp] & 63;
          if (dist < depth && dist + max >= depth) {
            for (int t = 0; t < Turn.B_NUM; t++) {
              if (dist + turn.lengthB[t] == depth) {
                int nmp = transform.midgePerm.turnB(t, mp);
                int ncp = transform.cornPerm.turnB(t, cp);
                if ((distance[nmp][ncp] & 63) > depth) {
                  distance[nmp][ncp] = depth;
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
