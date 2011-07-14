
final class PruneEdgeFlipEdgeLoc {
  private byte[][] distance;

  public int distance(int ef, int el) {
    return distance[ef][el] & 63;
  }

  public boolean solved(int ef, int el) {
    return distance[ef][el] == 64;
  }

  public PruneEdgeFlipEdgeLoc(Transform transform, Turn turn, int eoActive, int eActive, int mask) {
    PackEdgeFlip packEdgeFlip = new PackEdgeFlip(eoActive);
    int efLen = packEdgeFlip.len();
    int efStart = packEdgeFlip.startLen();
    PackEdgeLoc packEdgeLoc = new PackEdgeLoc(eActive);
    int elLen = packEdgeLoc.len();
    int elStart = packEdgeLoc.startLen();
    distance = new byte[efLen][elLen];
    for (int ef = 0; ef < efLen; ef++)
      for (int el = 0; el < elLen; el++)
        distance[ef][el] = 63;
    int max = 0;
    for (int t = 0; t < Turn.A_NUM; t++)
      if (max < turn.lengthA[t])
        max = turn.lengthA[t];
    System.err.println(" Phase A: edge flip - midge location:");
    System.err.println("  Start goals: " + efStart + " x " + elStart + " = " + efStart * elStart);
    for (int ef = 0; ef < efStart; ef++)
      for (int el = 0; el < elStart; el++)
        distance[packEdgeFlip.start(ef)][packEdgeLoc.start(el)] = 64;
    int states = efStart * elStart;
    while (states > 0) {
      states = 0;
      for (int ef = 0; ef < efLen; ef++) {
        for (int el = 0; el < elLen; el++) {
          if ((distance[ef][el] & 63) == 0) {
            for (int t = 0; t < Turn.A_NUM; t++) {
              if ((Turn.turnMaskA[t] & mask) != 0) {
                int nef = transform.edgeFlip.turnA(t, ef);
                int nel = transform.edgeLoc.turnA(t, el);
                if ((distance[nef][nel] & 63) > 0) {
                  distance[nef][nel] = 0;
                  states++;
                }
              }
            }
          }
        }
      }
    }
    for (int ef = 0; ef < efLen; ef++)
      for (int el = 0; el < elLen; el++)
        if ((distance[ef][el] & 63) == 0)
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
        for (int el = 0; el < elLen; el++) {
          int dist = distance[ef][el] & 63;
          if (dist < depth && dist + max >= depth) {
            for (int t = 0; t < Turn.A_NUM; t++) {
              if (dist + turn.lengthA[t] == depth) {
                int nef = transform.edgeFlip.turnA(t, ef);
                int nel = transform.edgeLoc.turnA(t, el);
                if ((distance[nef][nel] & 63) > depth) {
                  distance[nef][nel] = depth;
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
