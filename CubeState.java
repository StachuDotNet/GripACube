
public final class CubeState {
  public int coActive; // wich corners are oriented
  public int eoActive; // wich edges are oriented
  public int cActive; // which corners are placed
  public int eActive; // which edges are placed
  public int cornTwist; // corner orientations for phase A
  public int edgeFlip; // edge orientations for phase A
  public int edgeLoc; // unordered placements of the middle-layer edges for phase A
  public int cornPerm; // corner permutation for phase B
  public int midgePos; // placements of the middle edges for computing the middle edges permutation for phase B
  public int uEdgePos; // placements of U edges for UD edge permutation for phase B
  public int dEdgePos; // placements of D edges for UD edge permutation for phase B
  public int turnMask; // 18-bit mask of allowed turns (U D F B L R E S M  U2 D2 F2 B2 L2 R2 E2 S2 M2)

  public int minimum;
  //public int length;
  public int depth;
  public int symmetry;
  public int freeMove;
  public int freeMoveMask;
  //public int[] turn = new int[100]; // proven upper limit = 29 (for all face turn generators)
}
