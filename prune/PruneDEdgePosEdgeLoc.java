
final class PruneDEdgePosEdgeLoc {
  private byte[][] distance;

  public int distance(int dp, int el) {
    return distance[dp][el] & 63;
  }

  public boolean solved(int dp, int el) {
    return distance[dp][el] == 64;
  }

  public PruneDEdgePosEdgeLoc(Transform transform, Turn turn, int eActive, int mask) {
    PackDEdgePos packDEdgePos = new PackDEdgePos(eActive);
    int dpLen = packDEdgePos.len();
    int dpStart = packDEdgePos.startLen();
    PackEdgeLoc packEdgeLoc = new PackEdgeLoc(eActive);
    int elLen = packEdgeLoc.len();
    int elStart = packEdgeLoc.startLen();
    distance = new byte[dpLen][elLen];
    for (int dp = 0; dp < dpLen; dp++)
      for (int el = 0; el < elLen; el++)
        distance[dp][el] = 63;
    int max = 0;
    for (int t = 0; t < Turn.A_NUM; t++)
      if (max < turn.lengthA[t])
        max = turn.lengthA[t];
    System.err.println(" Phase A: d edge position - midge location:");
    System.err.println("  Start goals: " + dpStart + " x " + elStart + " = " + dpStart * elStart);
    for (int dp = 0; dp < dpStart; dp++)
      for (int el = 0; el < elStart; el++)
        distance[packDEdgePos.start(dp)][packEdgeLoc.start(el)] = 64;
    int states = dpStart * elStart;
    while (states > 0) {
      states = 0;
      for (int dp = 0; dp < dpLen; dp++) {
        for (int el = 0; el < elLen; el++) {
          if ((distance[dp][el] & 63) == 0) {
            for (int t = 0; t < Turn.A_NUM; t++) {
              if ((Turn.turnMaskA[t] & mask) != 0) {
                int ndp = transform.edgePos.turnAD(t, dp);
                int nel = transform.edgeLoc.turnA(t, el);
                if ((distance[ndp][nel] & 63) > 0) {
                  distance[ndp][nel] = 0;
                  states++;
                }
              }
            }
          }
        }
      }
    }
    for (int dp = 0; dp < dpLen; dp++)
      for (int el = 0; el < elLen; el++)
        if ((distance[dp][el] & 63) == 0)
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
      for (int dp = 0; dp < dpLen; dp++) {
        for (int el = 0; el < elLen; el++) {
          int dist = distance[dp][el] & 63;
          if (dist < depth && dist + max >= depth) {
            for (int t = 0; t < Turn.A_NUM; t++) {
              if (dist + turn.lengthA[t] == depth) {
                int ndp = transform.edgePos.turnAD(t, dp);
                int nel = transform.edgeLoc.turnA(t, el);
                if ((distance[ndp][nel] & 63) > depth) {
                  distance[ndp][nel] = depth;
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
