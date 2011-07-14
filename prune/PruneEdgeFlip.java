
final class PruneEdgeFlip {
  private byte[] distance;

  public int distance(int ef) {
    return distance[ef] & 63;
  }

  public boolean solved(int ef) {
    return distance[ef] == 64;
  }

  public PruneEdgeFlip(Transform transform, Turn turn, int eoActive, int mask) {
    PackEdgeFlip packEdgeFlip = new PackEdgeFlip(eoActive);
    int efLen = packEdgeFlip.len();
    int efStart = packEdgeFlip.startLen();
    distance = new byte[efLen];
    for (int ef = 0; ef < efLen; ef++)
      distance[ef] = 63;
    int max = 0;
    for (int t = 0; t < Turn.A_NUM; t++)
      if (max < turn.lengthA[t])
        max = turn.lengthA[t];
    System.err.println(" Phase A: edge flip:");
    System.err.println("  Start goals: " + efStart);
    for (int ef = 0; ef < efStart; ef++)
      distance[packEdgeFlip.start(ef)] = 64;
    int states = efStart;
    while (states > 0) {
      states = 0;
      for (int ef = 0; ef < efLen; ef++) {
        if ((distance[ef] & 63) == 0) {
          for (int t = 0; t < Turn.A_NUM; t++) {
            if ((Turn.turnMaskA[t] & mask) != 0) {
              int nef = transform.edgeFlip.turnA(t, ef);
              if ((distance[nef] & 63) > 0) {
                distance[nef] = 0;
                states++;
              }
            }
          }
        }
      }
    }
    for (int ef = 0; ef < efLen; ef++)
      if ((distance[ef] & 63) == 0)
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
      for (int ef = 0; ef < efLen; ef++) {
        int dist = distance[ef] & 63;
        if (dist < depth && dist + max >= depth) {
          for (int t = 0; t < Turn.A_NUM; t++) {
            if (dist + turn.lengthA[t] == depth) {
              int nef = transform.edgeFlip.turnA(t, ef);
              if ((distance[nef] & 63) > depth) {
                distance[nef] = depth;
                states++;
              }
            }
          }
        }
      }
    }
  }
}
