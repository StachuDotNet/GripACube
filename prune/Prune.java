
public final class Prune {
  public final PruneEdgeFlip edgeFlip;
  public final PruneCornTwistEdgeFlip cornTwistEdgeFlip;
  public final PruneCornTwistEdgeLoc cornTwistEdgeLoc;
  public final PruneEdgeFlipEdgeLoc edgeFlipEdgeLoc;
  public final PruneCornPerm cornPerm;
  public final PruneUEdgePosEdgeLoc uEdgePosEdgeLoc;
  public final PruneDEdgePosEdgeLoc dEdgePosEdgeLoc;
  public final PruneMidgePos midgePos;
  public final PruneMidgePermCornPerm midgePermCornPerm;
  public final PruneMidgePermUDEdgePerm midgePermUDEdgePerm;

  private final boolean cornTwistLarge;
  private final boolean edgeFlipLarge;

  private int max(int a, int b) {
    return a > b ? a : b;
  }

  private int max(int a, int b, int c) {
    return max(max(a, b), c);
  }

  private int max(int a, int b, int c, int d) {
    return max(max(a, b), max(c, d));
  }

  public int distanceA(int ct, int ef, int el) {
    if (edgeFlipLarge)
      return max(cornTwistEdgeLoc.distance(ct, el),
                 edgeFlip.distance(ef));
    if (cornTwistLarge)
      return max(cornTwistEdgeLoc.distance(ct, el),
                 edgeFlipEdgeLoc.distance(ef, el));
    return max(cornTwistEdgeLoc.distance(ct, el),
               edgeFlipEdgeLoc.distance(ef, el),
               cornTwistEdgeFlip.distance(ct, ef));
  }

  public int distanceA(int ct, int ef, int el, int cp, int up, int dp, int mp) {
    return max(distanceA(ct, ef, el),
               max(cornPerm.distance(cp),
                   midgePos.distance(mp),
                   uEdgePosEdgeLoc.distance(up, el),
                   dEdgePosEdgeLoc.distance(dp, el)));
  }

  public int distanceB(int cp, int ep, int mp) {
    return max(midgePermCornPerm.distance(mp, cp),
               midgePermUDEdgePerm.distance(mp, ep));
  }

  public boolean overA(int ct, int ef, int el, int d) {
    if (cornTwistEdgeLoc.distance(ct, el) > d)
      return true;
    if (edgeFlipLarge)
      return edgeFlip.distance(ef) > d;
    if (edgeFlipEdgeLoc.distance(ef, el) > d)
      return true;
    if (cornTwistLarge)
      return false;
    return cornTwistEdgeFlip.distance(ct, ef) > d;
  }

  public boolean overA(int ct, int ef, int el,
                       int cp, int up, int dp, int mp, int d) {
    if (overA(ct, ef, el, d))
      return true;
    if (cornPerm.distance(cp) > d)
      return true;
    if (uEdgePosEdgeLoc.distance(up, el) > d)
      return true;
    if (dEdgePosEdgeLoc.distance(dp, el) > d)
      return true;
    return midgePos.distance(mp) > d;
  }

  public boolean overB(int cp, int ep, int mp, int d) {
    if (midgePermCornPerm.distance(mp, cp) > d)
      return true;
    return midgePermUDEdgePerm.distance(mp, ep) > d;
  }

  public boolean solvedA(int ct, int ef, int el) {
    return cornTwistEdgeLoc.solved(ct, el) &&
           (edgeFlipLarge ?
             edgeFlip.solved(ef) :
             edgeFlipEdgeLoc.solved(ef, el) &&
             (cornTwistLarge || cornTwistEdgeFlip.solved(ct, ef)));
  }

  public boolean solvedA(int ct, int ef, int el,
                         int cp, int up, int dp, int mp) {
    return solvedA(ct, ef, el) &&
      cornPerm.solved(cp) &&
      uEdgePosEdgeLoc.solved(up, el) &&
      dEdgePosEdgeLoc.solved(dp, el) &&
      midgePos.solved(mp);
  }

  public boolean solvedB(int cp, int ep, int mp) {
    return midgePermCornPerm.solved(mp, cp) &&
           midgePermUDEdgePerm.solved(mp, ep);
  }

  public Prune(Transform transform, Turn turn, CubeState state, boolean optimal) {
    System.err.println("Pruning tables...");
    PackEdgeFlip packEdgeFlip = new PackEdgeFlip(state.eoActive);
    PackCornTwist packCornTwist = new PackCornTwist(state.coActive);
    edgeFlipLarge = packEdgeFlip.len() > 2048;
    cornTwistLarge = packCornTwist.len() > 2187;
    System.err.println(" Edge flip number: " + (edgeFlipLarge ? "large" : "small"));
    System.err.println(" Corn twist number: " + (cornTwistLarge ? "large" : "small"));
    cornTwistEdgeLoc = new PruneCornTwistEdgeLoc(transform, turn, state.coActive, state.eActive, state.freeMoveMask);
    if (edgeFlipLarge) {
      edgeFlip = new PruneEdgeFlip(transform, turn, state.eoActive, state.freeMoveMask);
      System.err.println(" Phase A: edge flip - midge location: none");
      edgeFlipEdgeLoc = null;
      System.err.println(" Phase A: corner twist - edge flip: none");
      cornTwistEdgeFlip = null;
    }
    else {
      System.err.println(" Phase A: edge flip: none");
      edgeFlip = null;
      edgeFlipEdgeLoc = new PruneEdgeFlipEdgeLoc(transform, turn, state.eoActive, state.eActive, state.freeMoveMask);
      if (cornTwistLarge) {
        System.err.println(" Phase A: corner twist - edge flip: none");
        cornTwistEdgeFlip = null;
      }
      else
        cornTwistEdgeFlip = new PruneCornTwistEdgeFlip(transform, turn, state.coActive, state.eoActive, state.freeMoveMask);
    }
    if (optimal) { // do not use phase B
      cornPerm = new PruneCornPerm(transform, turn, state.cActive, state.freeMoveMask);
      uEdgePosEdgeLoc = new PruneUEdgePosEdgeLoc(transform, turn, state.eActive, state.freeMoveMask);
      dEdgePosEdgeLoc = new PruneDEdgePosEdgeLoc(transform, turn, state.eActive, state.freeMoveMask);
      midgePos = new PruneMidgePos(transform, turn, state.eActive, state.freeMoveMask);
      System.err.println(" Phase B: midge permutation - corner permutation: none");
      midgePermCornPerm = null;
      System.err.println(" Phase B: midge permutation - u/d edge permutation: none");
      midgePermUDEdgePerm = null;
    }
    else {
      System.err.println(" Phase A: corner permutation: none");
      cornPerm = null;
      System.err.println(" Phase A: u edge position - midge location: none");
      uEdgePosEdgeLoc = null;
      System.err.println(" Phase A: d edge position - midge location: none");
      dEdgePosEdgeLoc = null;
      System.err.println(" Phase A: midge position: none");
      midgePos = null;
      midgePermCornPerm = new PruneMidgePermCornPerm(transform, turn, state.eActive, state.cActive, state.freeMoveMask);
      midgePermUDEdgePerm = new PruneMidgePermUDEdgePerm(transform, turn, state.eActive, state.freeMoveMask);
    }
    System.err.println("Pruning tables done.");
  }
}
