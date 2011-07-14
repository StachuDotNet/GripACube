
final class PruneMidgePos {
  private byte[] distance;

  public int distance(int mp) {
    return distance[mp] & 63;
  }

  public boolean solved(int mp) {
    return distance[mp] == 64;
  }

  public PruneMidgePos(Transform transform, Turn turn, int eActive, int mask) {
    PackMidgePos packMidgePos = new PackMidgePos(eActive);
    int mpLen = packMidgePos.len();
    int mpStart = packMidgePos.startLen();
    distance = new byte[mpLen];
    for (int mp = 0; mp < mpLen; mp++)
      distance[mp] = 63;
    int max = 0;
    for (int t = 0; t < Turn.A_NUM; t++)
      if (max < turn.lengthA[t])
        max = turn.lengthA[t];
    System.err.println(" Phase A: midge position:");
    System.err.println("  Start goals: " + mpStart);
    for (int mp = 0; mp < mpStart; mp++)
      distance[packMidgePos.start(mp)] = 64;
    int states = mpStart;
    while (states > 0) {
      states = 0;
      for (int mp = 0; mp < mpLen; mp++) {
        if ((distance[mp] & 63) == 0) {
          for (int t = 0; t < Turn.A_NUM; t++) {
            if ((Turn.turnMaskA[t] & mask) != 0) {
              int nmp = transform.edgePos.turnAM(t, mp);
              if ((distance[nmp] & 63) > 0) {
                distance[nmp] = 0;
                states++;
              }
            }
          }
        }
      }
    }
    for (int mp = 0; mp < mpLen; mp++)
      if ((distance[mp] & 63) == 0)
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
        int dist = distance[mp] & 63;
        if (dist < depth && dist + max >= depth) {
          for (int t = 0; t < Turn.A_NUM; t++) {
            if (dist + turn.lengthA[t] == depth) {
              int nmp = transform.edgePos.turnAM(t, mp);
              if ((distance[nmp] & 63) > depth) {
                distance[nmp] = depth;
                states++;
              }
            }
          }
        }
      }
    }
  }
}
