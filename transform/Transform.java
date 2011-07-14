public final class Transform {
  // max. used memory: 25,866,738
  public final TransformCornTwist cornTwist;
  public final TransformEdgeFlip edgeFlip;
  public final TransformEdgeLoc edgeLoc;
  public final TransformCornPerm cornPerm;
  public final TransformUDEdgePerm udEdgePerm;
  public final TransformMidgePerm midgePerm;
  public final TransformEdgePos edgePos;

  public Transform(CubeState state, boolean optimal) {
    System.err.println("Transformation tables...");
    cornTwist = new TransformCornTwist(state.coActive);
    edgeFlip = new TransformEdgeFlip(state.eoActive);
    edgeLoc = new TransformEdgeLoc(state.eActive);
    cornPerm = new TransformCornPerm(state.cActive);
    if (!optimal) {
      udEdgePerm = new TransformUDEdgePerm(state.eActive);
      midgePerm = new TransformMidgePerm(state.eActive);
    }
    else {
      udEdgePerm = null;
      midgePerm = null;
    }
    edgePos = new TransformEdgePos(state.eActive, optimal);
    System.err.println("Transformation tables done.");
  }
}
