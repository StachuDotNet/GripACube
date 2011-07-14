
final class PruneCornTwistEdgeLoc {
  private byte[][] distance;

  public int distance(int ct, int el) {
    return distance[ct][el] & 63;
  }

  public boolean solved(int ct, int el) {
    return distance[ct][el] == 64;
  }

  public PruneCornTwistEdgeLoc(Transform transform, Turn turn, int coActive, int eActive, int mask) {
    PackCornTwist packCornTwist = new PackCornTwist(coActive);
    int ctLen = packCornTwist.len();
    int ctStart = packCornTwist.startLen();
    PackEdgeLoc packEdgeLoc = new PackEdgeLoc(eActive);
    int elLen = packEdgeLoc.len();
    int elStart = packEdgeLoc.startLen();
    distance = new byte[ctLen][elLen];
    for (int ct = 0; ct < ctLen; ct++)
      for (int el = 0; el < elLen; el++)
        distance[ct][el] = 63;
    int max = 0;
    for (int t = 0; t < Turn.A_NUM; t++)
      if (max < turn.lengthA[t])
        max = turn.lengthA[t];
    System.err.println(" Phase A: corner twist - midge location:");
    System.err.println("  Start goals: " + ctStart + " x " + elStart + " = " + ctStart * elStart);
    for (int ct = 0; ct < ctStart; ct++)
      for (int el = 0; el < elStart; el++)
        distance[packCornTwist.start(ct)][packEdgeLoc.start(el)] = 64;
    int states = ctStart * elStart;
    while (states > 0) {
      states = 0;
      for (int ct = 0; ct < ctLen; ct++) {
        for (int el = 0; el < elLen; el++) {
          if ((distance[ct][el] & 63) == 0) {
            for (int t = 0; t < Turn.A_NUM; t++) {
              if ((Turn.turnMaskA[t] & mask) != 0) {
                int nct = transform.cornTwist.turnA(t, ct);
                int nel = transform.edgeLoc.turnA(t, el);
                if ((distance[nct][nel] & 63) > 0) {
                  distance[nct][nel] = 0;
                  states++;
                }
              }
            }
          }
        }
      }
    }
    for (int ct = 0; ct < ctLen; ct++)
      for (int el = 0; el < elLen; el++)
        if ((distance[ct][el] & 63) == 0)
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
        for (int el = 0; el < elLen; el++) {
          int dist = distance[ct][el] & 63;
          if (dist < depth && dist + max >= depth) {
            for (int t = 0; t < Turn.A_NUM; t++) {
              if (dist + turn.lengthA[t] == depth) {
                int nct = transform.cornTwist.turnA(t, ct);
                int nel = transform.edgeLoc.turnA(t, el);
                if ((distance[nct][nel] & 63) > depth) {
                  distance[nct][nel] = depth;
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
