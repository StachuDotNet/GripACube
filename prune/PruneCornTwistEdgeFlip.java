
final class PruneCornTwistEdgeFlip {
  private byte[][] distance;

  public int distance(int ct, int ef) {
    return distance[ct][ef] & 63;
  }

  public boolean solved(int ct, int ef) {
    return distance[ct][ef] == 64;
  }

  public PruneCornTwistEdgeFlip(Transform transform, Turn turn, int coActive, int eoActive, int mask) {
    PackCornTwist packCornTwist = new PackCornTwist(coActive);
    int ctLen = packCornTwist.len();
    int ctStart = packCornTwist.startLen();
    PackEdgeFlip packEdgeFlip = new PackEdgeFlip(eoActive);
    int efLen = packEdgeFlip.len();
    int efStart = packEdgeFlip.startLen();
    distance = new byte[ctLen][efLen];
    for (int ct = 0; ct < ctLen; ct++)
      for (int ef = 0; ef < efLen; ef++)
        distance[ct][ef] = 63;
    int max = 0;
    for (int t = 0; t < Turn.A_NUM; t++)
      if (max < turn.lengthA[t])
        max = turn.lengthA[t];
    System.err.println(" Phase A: corner twist - edge flip:");
    System.err.println("  Start goals: " + ctStart + " x " + efStart + " = " + ctStart * efStart);
    for (int ct = 0; ct < ctStart; ct++)
      for (int ef = 0; ef < efStart; ef++)
        distance[packCornTwist.start(ct)][packEdgeFlip.start(ef)] = 64;
    int states = ctStart * efStart;
    while (states > 0) {
      states = 0;
      for (int ct = 0; ct < ctLen; ct++) {
        for (int ef = 0; ef < efLen; ef++) {
          if ((distance[ct][ef] & 63) == 0) {
            for (int t = 0; t < Turn.A_NUM; t++) {
              if ((Turn.turnMaskA[t] & mask) != 0) {
                int nct = transform.cornTwist.turnA(t, ct);
                int nef = transform.edgeFlip.turnA(t, ef);
                if ((distance[nct][nef] & 63) > 0) {
                  distance[nct][nef] = 0;
                  states++;
                }
              }
            }
          }
        }
      }
    }
    for (int ct = 0; ct < ctLen; ct++)
      for (int ef = 0; ef < efLen; ef++)
        if ((distance[ct][ef] & 63) == 0)
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
      for (int ct = 0; ct < ctLen; ct++) {
        for (int ef = 0; ef < efLen; ef++) {
          int dist = distance[ct][ef] & 63;
          if (dist < depth && dist + max >= depth) {
            for (int t = 0; t < Turn.A_NUM; t++) {
              if (dist + turn.lengthA[t] == depth) {
                int nct = transform.cornTwist.turnA(t, ct);
                int nef = transform.edgeFlip.turnA(t, ef);
                if ((distance[nct][nef] & 63) > depth) {
                  distance[nct][nef] = depth;
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
