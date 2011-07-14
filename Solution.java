//import javax.swing.JOptionPane;
import java.util.ArrayList;
public final class Solution {
  private final class ANode {
    public int restDepth;
    public int turn;
    public int turnList;
    public int symmetry;
    // specific values for Phase A
    public int cornTwist;
    public int edgeFlip;
    public int edgeLoc;
    // specific values for Phase A only
    public int cornPerm;
    public int uEdgePos;
    public int dEdgePos;
    public int midgePos;
  }

  private final class BNode {
    public int restDepth;
    public int turn;
    public int turnList;
    public int symmetry;
    // specific values for Phase B
    public int cornPerm;
    public int edgePerm;
    public int midgePerm;
  }

  private static final String[] turnSymbol = {
    "U", "U2", "U'",
    "D", "D2", "D'",
    "F", "F2", "F'",
    "B", "B2", "B'",
    "L", "L2", "L'",
    "R", "R2", "R'",
    "E", "E2", "E'",
    "S", "S2", "S'",
    "M", "M2", "M'",
  };

  public static final int[][] initMoveGroup = {
    null,
    {Turn.A_U1, Turn.A_U2, Turn.A_U3},
    {Turn.A_D1, Turn.A_D2, Turn.A_D3},
    {Turn.A_F1, Turn.A_F2, Turn.A_F3},
    {Turn.A_B1, Turn.A_B2, Turn.A_B3},
    {Turn.A_L1, Turn.A_L2, Turn.A_L3},
    {Turn.A_R1, Turn.A_R2, Turn.A_R3},
    {Turn.A_E1, Turn.A_E2, Turn.A_E3},
    {Turn.A_S1, Turn.A_S2, Turn.A_S3},
    {Turn.A_M1, Turn.A_M2, Turn.A_M3}
  };

  private Transform transform;
  private Prune prune;
  private Turn turn;
  private TurnList turnList;
  private CubeState state;

  private int attempt; // current attempt - initial move
  private int minLength; // length of the shortest solution
  private int maxBLength; // maximum allowed depth for Phase B
  private ANode[] stackA = new ANode[100];
  private BNode[] stackB = new BNode[100];
  private int stackAn; // current size of the stack for Phase A
  private int stackBn; // current size of the stack for Phase B
  private int metric;
  private boolean findAll; // search for all moves on the current depth
  private boolean findOptimal; // search just for the optimal sequences
  // statistics
  private long sol2, sol2x;
  private long aprn, bprn, apry, bpry;

  public Solution(Transform transform, Prune prune, Turn turn, TurnList turnList) {
    this.transform = transform;
    this.prune = prune;
    this.turn = turn;
    this.turnList = turnList;
    for (int i = 0; i < stackA.length; i++)
      stackA[i] = new ANode();
    for (int i = 0; i < stackB.length; i++)
      stackB[i] = new BNode();
  }

  public void solve(CubeState state, Options options, ArrayList<String> ar) {
    this.state = state;
    metric = options.metric;
    findAll = options.findAll;
    findOptimal = options.findOptimal;
    solveA(ar);
  }

  private void solveA(ArrayList<String> ar) {
    minLength = state.minimum; // the minimal length found
    aprn = bprn = 0; // statistics
    apry = bpry = 0;
    sol2 = sol2x = 0;
    for (attempt = 0; attempt < initMoves(state.freeMove); attempt++) {
      System.err.println("attempt " + (attempt + 1) + "/" + initMoves(state.freeMove) + ":");
      // set the root node to the initial configuration
      ANode node = stackA[0];
      node.cornTwist = state.cornTwist;
      node.edgeFlip = state.edgeFlip;
      node.edgeLoc = state.edgeLoc;
      node.cornPerm = state.cornPerm;
      node.uEdgePos = state.uEdgePos;
      node.dEdgePos = state.dEdgePos;
      node.midgePos = state.midgePos;
      node.turn = -2; // unused
      node.turnList = TurnList.FREE; // all turns allowed
      node.symmetry = state.symmetry; // can be used to rotate cube before solving
      // perform the actions for the current premove
      SliceTurnTransform sliceTransform = new SliceTurnTransform();
      for (int i = attempt, m = state.freeMove; m != 0; i /= 4, m /= 10) {
        if (i % 4 > 0) {
          int turn = initMoveGroup[m % 10][i % 4 - 1];
          node.turnList = turnList.nextA[sliceTransform.turnA(turn, node.symmetry)][node.turnList];
          node.symmetry = sliceTransform.symmetry();
          node.cornTwist = transform.cornTwist.turnA(turn, node.cornTwist);
          node.edgeFlip = transform.edgeFlip.turnA(turn, node.edgeFlip);
          node.edgeLoc = transform.edgeLoc.turnA(turn, node.edgeLoc);
          node.cornPerm = transform.cornPerm.turnA(turn, node.cornPerm);
          node.uEdgePos = transform.edgePos.turnAU(turn, node.uEdgePos);
          node.dEdgePos = transform.edgePos.turnAD(turn, node.dEdgePos);
          node.midgePos = transform.edgePos.turnAM(turn, node.midgePos);
        }
      }
      // IDA* depth-first search
      if (findOptimal) {
        // optimal solving
        int maxDepth = prune.distanceA(node.cornTwist, node.edgeFlip, node.edgeLoc,
         node.cornPerm, node.uEdgePos, node.dEdgePos, node.midgePos);
        if (maxDepth < state.depth)
          maxDepth = state.depth;
        for (int d = maxDepth; findAll ? d <= minLength : d < minLength; d++) {
          System.err.println("depth " + d + "...");
          searchAOptimal(d, ar);
        }
      }
      else {
        // two-phase solving
        int maxDepth = prune.distanceA(node.cornTwist, node.edgeFlip, node.edgeLoc);
        if (maxDepth < state.depth)
          maxDepth = state.depth;
        for (int d = maxDepth; findAll ? d <= minLength : d < minLength; d++) {
          System.err.println("depth " + d + "...");
          maxBLength = findAll ? minLength - d : minLength - d - 1;
          searchA(d, ar);
        }
      }
    }
    if (findOptimal) {
      System.err.println("Done. (" + sol2 + " solutions found)");
      if (aprn > 0) {
        long n = aprn < 10000 ? apry * 100 / aprn : apry / (aprn / 100);
        System.err.println(" (" + n + "% save - " + apry + " of " + aprn + " entries)");
      }
    }
    else {
      System.err.println("Done. (" + sol2 + " entries to Phase B of " + sol2x + " tries)");
      if (aprn > 0) {
        long n = aprn < 10000 ? apry * 100 / aprn : apry / (aprn / 100);
        System.err.println(" (" + n + "% save in Phase A - " + apry + " of " + aprn + " entries)");
      }
      if (bprn > 0) {
        long n = bprn < 10000 ? bpry * 100 / bprn : bpry / (bprn / 100);
        System.err.println(" (" + n + "% save in Phase B - " + bpry + " of " + bprn + " entries)");
      }
    }
  }

  private void searchAOptimal(int i, ArrayList<String> ar) {
    SliceTurnTransform sliceTransform = new SliceTurnTransform();
    stackAn = 0;
    stackA[0].restDepth = i; // the rest depth is the largest possible
    stackA[1].turn = -1;
    while (stackAn >= 0) {
      if (stackA[stackAn].restDepth == 0) { // solution found ?
        if (testA(stackA[stackAn])) {
          display(ar);
          minLength = stackA[0].restDepth; // new minimum
          sol2++;
          if (!findAll)
            return;
        }
        stackAn--;
      }
      else {
        ANode s = stackA[stackAn];
        ANode ns = stackA[stackAn + 1];
        int t = ns.turn + 1;
        while (t < Turn.A_NUM) {
          int tl = turnList.nextA[sliceTransform.turnA(t, s.symmetry)][s.turnList];
          if (tl == TurnList.INVALID) { // illegal turn
            t++;
            continue;
          }
          ns.restDepth = s.restDepth - turn.lengthA[t]; // decrease the rest depth
          if (ns.restDepth < 0) {
            t++;
            continue;
          }
          ns.cornTwist = transform.cornTwist.turnA(t, s.cornTwist);
          ns.edgeFlip = transform.edgeFlip.turnA(t, s.edgeFlip);
          ns.edgeLoc = transform.edgeLoc.turnA(t, s.edgeLoc);
          ns.cornPerm = transform.cornPerm.turnA(t, s.cornPerm);
          ns.uEdgePos = transform.edgePos.turnAU(t, s.uEdgePos);
          ns.dEdgePos = transform.edgePos.turnAD(t, s.dEdgePos);
          ns.midgePos = transform.edgePos.turnAM(t, s.midgePos);
          aprn++;
          if (prune.overA(ns.cornTwist, ns.edgeFlip, ns.edgeLoc, ns.cornPerm,
               ns.uEdgePos, ns.dEdgePos, ns.midgePos,
               ns.restDepth)) {
            apry++;
            t++;
            continue;
          }
          ns.turn = t; // save the last turn
          ns.turnList = tl;
          ns.symmetry = sliceTransform.symmetry();
          break;
        }
        if (t == Turn.A_NUM)
          stackAn--; // return to the previous depth
        else {
          stackAn++; // go to the next depth
          stackA[stackAn + 1].turn = -1;
        }
      }
    }
  }

  void searchA(int i, ArrayList<String> ar) {
    SliceTurnTransform sliceTransform = new SliceTurnTransform();
    stackAn = 0;
    stackA[0].restDepth = i;
    stackA[1].turn = -1;
    while (stackAn >= 0) {
      if (stackA[stackAn].restDepth == 0) {
        solveB(ar);
        stackAn--;
      }
      else {
        ANode s = stackA[stackAn];
        ANode ns = stackA[stackAn + 1];
        int t = ns.turn + 1;
        while (t < Turn.A_NUM) {
          int tl = turnList.nextA[sliceTransform.turnA(t, s.symmetry)][s.turnList];
          if (tl == TurnList.INVALID) { // illegal turn
            t++;
            continue;
          }
          ns.restDepth = s.restDepth - turn.lengthA[t];
          if (ns.restDepth < 0) {
            t++;
            continue;
          }
          ns.cornTwist = transform.cornTwist.turnA(t, s.cornTwist);
          ns.edgeFlip = transform.edgeFlip.turnA(t, s.edgeFlip);
          ns.edgeLoc = transform.edgeLoc.turnA(t, s.edgeLoc);
          aprn++;
          if (prune.overA(ns.cornTwist, ns.edgeFlip, ns.edgeLoc, ns.restDepth)) {
            apry++;
            t++;
            continue;
          }
          ns.turn = t;
          ns.turnList = tl;
          ns.symmetry = sliceTransform.symmetry();
          break;
        }
        if (t == Turn.A_NUM)
          stackAn--;
        else {
          stackAn++;
          stackA[stackAn + 1].turn = -1;
        }
      }
    }
  }

  private void solveB(ArrayList<String> ar) {
    sol2x++;
    int cornPerm = stackA[0].cornPerm;
    int midgePos = stackA[0].midgePos; // these values were not computed in Phase A
    int uEdgePos = stackA[0].uEdgePos; // but we have enough information for computing them
    int dEdgePos = stackA[0].dEdgePos; // from the initial state and Phase A moves
    for (int i = 1; i <= stackAn; i++) {
      int t = stackA[i].turn;
      cornPerm = transform.cornPerm.turnA(t, cornPerm);
      midgePos = transform.edgePos.turnAM(t, midgePos);
      uEdgePos = transform.edgePos.turnAU(t, uEdgePos);
      dEdgePos = transform.edgePos.turnAD(t, dEdgePos);
    }
    // entry to the phase B need not be correct if midges are
    // not completely defined and some U or D edges are in the ring
    if (!transform.edgePos.uInB(uEdgePos) || !transform.edgePos.dInB(dEdgePos))
      return;
    sol2++;
    stackB[0].cornPerm = cornPerm;
    stackB[0].edgePerm = transform.edgePos.udSToPerm(uEdgePos, dEdgePos);
    stackB[0].midgePerm = transform.edgePos.midgeToPerm(midgePos);
    int maxBDepth = prune.distanceB(cornPerm, stackB[0].edgePerm, stackB[0].midgePerm);
    bprn++;
    if (maxBDepth > maxBLength) {
      bpry++;
      return;
    }
    stackB[0].turn = -2; // unused
    stackB[0].turnList = stackA[stackAn].turnList;
    stackB[0].symmetry = stackA[stackAn].symmetry;
    // iterative deepening depth-first search for Phase B
    for (int i = maxBDepth; i <= maxBLength; i++)
      searchB(i, ar);
  }

  private void searchB(int i, ArrayList<String> ar) {
    SliceTurnTransform sliceTransform = new SliceTurnTransform();
    stackBn = 0;
    stackB[0].restDepth = i;
    stackB[1].turn = -1;
    while (stackBn >= 0) {
      if (stackB[stackBn].restDepth == 0) {
        if (testB(stackB[stackBn])) {
          display(ar);
          minLength = stackA[0].restDepth + stackB[0].restDepth;
          if (!findAll) {
            maxBLength = stackB[0].restDepth - 1;
            return;
          }
          maxBLength = stackB[0].restDepth;
        }
        stackBn--;
      }
      else {
        BNode s = stackB[stackBn];
        BNode ns = stackB[stackBn + 1];
        int t = ns.turn + 1;
        while (t < Turn.B_NUM) {
          int tl = turnList.nextA[sliceTransform.turnBA(t, s.symmetry)][s.turnList];
          if (tl == TurnList.INVALID) {
            t++;
            continue;
          }
          ns.restDepth = s.restDepth - turn.lengthB[t];
          if (ns.restDepth < 0) {
            t++;
            continue;
          }
          ns.midgePerm = transform.midgePerm.turnB(t, s.midgePerm);
          ns.cornPerm = transform.cornPerm.turnB(t, s.cornPerm);
          ns.edgePerm = transform.udEdgePerm.turnB(t, s.edgePerm);
          bprn++;
          if (prune.overB(ns.cornPerm, ns.edgePerm, ns.midgePerm,
               ns.restDepth)) {
            bpry++;
            t++;
            continue;
          }
          ns.turn = t;
          ns.turnList = tl;
          ns.symmetry = sliceTransform.symmetry();
          break;
        }
        if (t == Turn.B_NUM)
          stackBn--;
        else {
          stackBn++;
          stackB[stackBn + 1].turn = -1;
        }
      }
    }
  }

  private boolean testA(ANode node) {
    for (int i = 0, n = initMoves(state.freeMove); i < n; i++) {
      int cornTwist = node.cornTwist;
      int edgeFlip = node.edgeFlip;
      int edgeLoc = node.edgeLoc;
      int cornPerm = node.cornPerm;
      int uEdgePos = node.uEdgePos;
      int dEdgePos = node.dEdgePos;
      int midgePos = node.midgePos;
      for (int j = i, m = state.freeMove; m != 0; j /= 4, m /= 10) {
        if (j % 4 > 0) {
          int turn = initMoveGroup[m % 10][j % 4 - 1];
          cornTwist = transform.cornTwist.turnA(turn, cornTwist);
          edgeFlip = transform.edgeFlip.turnA(turn, edgeFlip);
          edgeLoc = transform.edgeLoc.turnA(turn, edgeLoc);
          cornPerm = transform.cornPerm.turnA(turn, cornPerm);
          uEdgePos = transform.edgePos.turnAU(turn, uEdgePos);
          dEdgePos = transform.edgePos.turnAD(turn, dEdgePos);
          midgePos = transform.edgePos.turnAM(turn, midgePos);
        }
      }
      if (prune.solvedA(cornTwist, edgeFlip, edgeLoc, cornPerm, uEdgePos, dEdgePos, midgePos))
        return true;
    }
    return false;
  }

  private boolean testB(BNode node) {
    for (int i = 0, n = initMoves(state.freeMove); i < n; i++) {
      int midgePerm = node.midgePerm;
      int cornPerm = node.cornPerm;
      int edgePerm = node.edgePerm;
      int m = state.freeMove;
      for (int j = i; m != 0; j /= 4, m /= 10) {
        if (j % 4 > 0) {
          int turn = Turn.a2b[initMoveGroup[m % 10][j % 4 - 1]];
          if (turn < 0)
            break;
          cornPerm = transform.cornPerm.turnB(turn, cornPerm);
          edgePerm = transform.udEdgePerm.turnB(turn, edgePerm);
          midgePerm = transform.midgePerm.turnB(turn, midgePerm);
        }
      }
      if (m == 0 && prune.solvedB(cornPerm, edgePerm, midgePerm))
        return true;
    }
    return false;
  }

  private static int initMoves(int freeMove) {
    int n = 1;
    for (; freeMove != 0; freeMove /= 10)
      n *= 4;
    return n;
  }

  private static String initMoveString(int attempt, int freeMove) {
    String s = "";
    for (boolean f = false; freeMove != 0; attempt /= 4, freeMove /= 10) {
      if (attempt % 4 > 0) {
        int turn = initMoveGroup[freeMove % 10][attempt % 4 - 1];
        if (f)
          s += " ";
        else
          f = true;
        s += turnSymbol[turn];
      }
    }
    return s;
  }

  private void display(ArrayList<String> ar) {
  	StringBuffer solutionToAdd = new StringBuffer ("");
    String ims = initMoveString(attempt, state.freeMove);
    if (!ims.equals(""))
      solutionToAdd.append("(" + ims + ") ");
    Turn turnQ = Turn.turn(Turn.QUARTER_METRIC);
    Turn turnF = Turn.turn(Turn.FACE_METRIC);
    Turn turnS = Turn.turn(Turn.SLICE_METRIC);
    SliceTurnTransform sliceTransform = new SliceTurnTransform();
    int ql = 0;
    int fl = 0;
    int sl = 0;
    for (int i = 1; i <= stackAn; i++) { // print out the phase A part
      int t = stackA[i].turn;
      int s = stackA[i - 1].symmetry;
      ql += turnQ.lengthA[t];
      fl += turnF.lengthA[t];
      sl += turnS.lengthA[t];
      t = sliceTransform.turnA(t, s); // symmetry transformation for slice-turn
      solutionToAdd.append("" + turnSymbol[t] + " ");
    }
    // print out the lengths in different metrics
    if (findOptimal) {
      switch (metric) {
       case Turn.QUARTER_METRIC:
        System.out.println("(" + ql + "q*, " + fl + "f, " + sl + "s)");
        break;
       case Turn.FACE_METRIC:
        System.out.println("(" + ql + "q, " + fl + "f*, " + sl + "s)");
        break;
       case Turn.SLICE_METRIC:
        System.out.println("(" + ql + "q, " + fl + "f, " + sl + "s*)");
        break;
      }
    }
    else {
      System.out.print(". ");
      for (int i = 1; i <= stackBn; i++) { // print out Phase B part
        int t = stackB[i].turn;
        int s = stackB[i - 1].symmetry;
        ql += turnQ.lengthB[t];
        fl += turnF.lengthB[t];
        sl += turnS.lengthB[t];
        t = sliceTransform.turnBA(t, s);
        solutionToAdd.append("" + turnSymbol[t] + " ");
      }
      System.out.println("(" + ql + "q, " + fl + "f, " + sl + "s)");
    }

    String JUSTNOWREALLY = solutionToAdd.toString();
    ar.add(JUSTNOWREALLY);
  }
}
