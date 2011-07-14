
final class PruneCornPerm {
  private byte[] distance;

  public int distance(int cp) {
    return distance[cp] & 63;
  }

  public boolean solved(int cp) {
    return distance[cp] == 64;
  }

  public PruneCornPerm(Transform transform, Turn turn, int cActive, int mask) {
    PackCornPerm packCornPerm = new PackCornPerm(cActive);
    int cpLen = packCornPerm.len();
    int cpStart = packCornPerm.startLen();
    distance = new byte[cpLen];
    for (int cp = 0; cp < cpLen; cp++)
      distance[cp] = 63;
    int max = 0;
    for (int t = 0; t < Turn.A_NUM; t++)
      if (max < turn.lengthA[t])
        max = turn.lengthA[t];
    System.err.println(" Phase A: corner permutation:");
    System.err.println("  Start goals: " + cpStart);
    for (int cp = 0; cp < cpStart; cp++)
      distance[packCornPerm.start(cp)] = 64;
    int states = cpStart;
    while (states > 0) {
      states = 0;
      for (int cp = 0; cp < cpLen; cp++) {
        if ((distance[cp] & 63) == 0) {
          for (int t = 0; t < Turn.A_NUM; t++) {
            if ((Turn.turnMaskA[t] & mask) != 0) {
              int ncp = transform.cornPerm.turnA(t, cp);
              if ((distance[ncp] & 63) > 0) {
                distance[ncp] = 0;
                states++;
              }
            }
          }
        }
      }
    }
    for (int cp = 0; cp < cpLen; cp++)
      if ((distance[cp] & 63) == 0)
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
      for (int cp = 0; cp < cpLen; cp++) {
        int dist = distance[cp] & 63;
        if (dist < depth && dist + max >= depth) {
          for (int t = 0; t < Turn.A_NUM; t++) {
            if (dist + turn.lengthA[t] == depth) {
              int ncp = transform.cornPerm.turnA(t, cp);
              if ((distance[ncp] & 63) > depth) {
                distance[ncp] = depth;
                states++;
              }
            }
          }
        }
      }
    }
  }
}
