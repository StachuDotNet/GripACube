
final class PruneUEdgePosEdgeLoc {
  private byte[][] distance;

  public int distance(int up, int el) {
    return distance[up][el] & 63;
  }

  public boolean solved(int up, int el) {
    return distance[up][el] == 64;
  }

  public PruneUEdgePosEdgeLoc(Transform transform, Turn turn, int eActive, int mask) {
    PackUEdgePos packUEdgePos = new PackUEdgePos(eActive);
    int upLen = packUEdgePos.len();
    int upStart = packUEdgePos.startLen();
    PackEdgeLoc packEdgeLoc = new PackEdgeLoc(eActive);
    int elLen = packEdgeLoc.len();
    int elStart = packEdgeLoc.startLen();
    distance = new byte[upLen][elLen];
    for (int up = 0; up < upLen; up++)
      for (int el = 0; el < elLen; el++)
        distance[up][el] = 63;
    int max = 0;
    for (int t = 0; t < Turn.A_NUM; t++)
      if (max < turn.lengthA[t])
        max = turn.lengthA[t];
    System.err.println(" Phase A: u edge position - midge location:");
    System.err.println("  Start goals: " + upStart + " x " + elStart + " = " + upStart * elStart);
    for (int up = 0; up < upStart; up++)
      for (int el = 0; el < elStart; el++)
        distance[packUEdgePos.start(up)][packEdgeLoc.start(el)] = 64;
    int states = upStart * elStart;
    while (states > 0) {
      states = 0;
      for (int up = 0; up < upLen; up++) {
        for (int el = 0; el < elLen; el++) {
          if ((distance[up][el] & 63) == 0) {
            for (int t = 0; t < Turn.A_NUM; t++) {
              if ((Turn.turnMaskA[t] & mask) != 0) {
                int nup = transform.edgePos.turnAU(t, up);
                int nel = transform.edgeLoc.turnA(t, el);
                if ((distance[nup][nel] & 63) > 0) {
                  distance[nup][nel] = 0;
                  states++;
                }
              }
            }
          }
        }
      }
    }
    for (int up = 0; up < upLen; up++)
      for (int el = 0; el < elLen; el++)
        if ((distance[up][el] & 63) == 0)
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
      for (int up = 0; up < upLen; up++) {
        for (int el = 0; el < elLen; el ++) {
          int dist = distance[up][el] & 63;
          if (dist < depth && dist + max >= depth) {
            for (int t = 0; t < Turn.A_NUM; t++) {
              if (dist + turn.lengthA[t] == depth) {
                int nup = transform.edgePos.turnAU(t, up);
                int nel = transform.edgeLoc.turnA(t, el);
                if ((distance[nup][nel] & 63) > depth) {
                  distance[nup][nel] = depth;
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
