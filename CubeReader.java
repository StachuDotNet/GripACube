
/**
 * Class used to get the input cube configuration from the input string.
 * This class can be rewritten and replaced in order to support
 * another way of cube state specification (e.g. graphical).
 */
 import java.util.ArrayList;
public final class CubeReader {
  // leading and trailing move characters
  private static final char[] freeMove = {
    0, 'U', 'D', 'F', 'B', 'L', 'R', 'E', 'S', 'M'
  };
  private static final char[] freeMoveMask = {
    'M', 'S', 'E', 'R', 'L', 'B', 'F', 'D', 'U'
  };
  // textual representation of edges
  private static final String[] edgeString = {
    "UF", "UR", "UB", "UL", "DF", "DR", "DB", "DL", "FR", "FL", "BR", "BL",
    "FU", "RU", "BU", "LU", "FD", "RD", "BD", "LD", "RF", "LF", "RB", "LB"
  };
  // textual representation of corners
  private static final String[] cornString = {
    "UFR", "URB", "UBL", "ULF", "DRF", "DFL", "DLB", "DBR",
    "FRU", "RBU", "BLU", "LFU", "RFD", "FLD", "LBD", "BRD",
    "RUF", "BUR", "LUB", "FUL", "FDR", "LDF", "BDL", "RDB",
    // orientation is determined only by the first character
    "URF", "UBR", "ULB", "UFL", "DFR", "DLF", "DBL", "DRB",
    "FUR", "RUB", "BUL", "LUF", "RDF", "FDL", "LDB", "BDR",
    "RFU", "BRU", "LBU", "FLU", "FRD", "LFD", "BLD", "RBD"
  };

  private int[] e = new int[12]; // locations and orientations of the edges
  private int[] c = new int[8]; // locations and orientations of the corners
  private int[] ep = new int[12]; // locations of the edges
  private int[] cp = new int[8]; // locations of the corners
  private int[] eo = new int[12]; // orientations of the edges
  private int[] co = new int[8]; // orientations of the corners
  private int[] ep1 = new int[12]; // mark of locations of the edges
  private int[] cp1 = new int[8]; // mark of locations of the corners
  private int[] eo1 = new int[12]; // mark of orientations of the edges
  private int[] co1 = new int[8]; // mark of orientations of the corners
  private Solution solution; // phase B solving algorithm
  private CubeState state; // characteristic values of the cube state
  private Options options; // various options

  public String init(Options options, String input) {
    this.options = options;
    state = new CubeState();
    String r = enter(input);
    if (r != "OK")
      return r;
    r = check();
    if (r != "OK")
      return r;
    preprocess();
    return "OK";
  }

  public void solve(ArrayList<String> ar) {
    Transform transform = new Transform(state, options.findOptimal);
    Turn turn = Turn.turn(options.metric);
    Prune prune = new Prune(transform, turn, state, options.findOptimal);
    TurnList turnList = new TurnList(state.turnMask);
    solution = new Solution(transform, prune, turn, turnList);
    solution.solve(state, options, ar);
  }

  private String enter(String input) {
    // read input from a user
    this.input = input;
    inputPos = 0;
    state.turnMask = 0;
    state.freeMove = 0;
    state.freeMoveMask = 0;
    state.symmetry = SliceTurnTransform.ID;
    // reading the textual representation of 12 edges
    String[] edgeStr = new String[12];
    for (int i = 0; i < 12; i++) {
      edgeStr[i] = getInputToken();
      if (edgeStr[i].equals("Q") || edgeStr[i].equals("q"))
        return "QUIT";
      if (edgeStr[i].equals("N") || edgeStr[i].equals("n"))
        return "NEW";
      if (edgeStr[i].equals(""))
        return "Insufficient number of edges";
      if (edgeStr[i].charAt(0) == '$' && edgeStr[i].length() == 2) {
        int sym = edgeStr[i].charAt(1) - 'A';
        if (sym < 0 || sym > 23)
          return "Invalid symmetry format";
        state.symmetry = sym;
        i--;
      }
      else if (edgeStr[i].charAt(0) == '~' && edgeStr[i].length() > 1) {
        int rev = 0;
        for (int j = 1; j < edgeStr[i].length(); j++) {
          for (int k = 0; k < freeMove.length; k++) {
            if (freeMove[k] == edgeStr[i].charAt(j)) {
              rev = rev * 10 + k;
              break;
            }
            if (k == freeMove.length - 1)
              return "Invalid character '" + edgeStr[i].charAt(j) + "' in initial move: " + edgeStr[i];
          }
        }
        while (rev != 0) {
          state.freeMove = state.freeMove * 10 + rev % 10;
          rev /= 10;
        }
        for (int j = 1; j < edgeStr[i].length(); j++) {
          for (int k = 0; k < freeMoveMask.length; k++) {
            if (freeMoveMask[k] == edgeStr[i].charAt(j)) {
              state.freeMoveMask = state.freeMoveMask | (1 << k);
              break;
            }
            if (k == freeMoveMask.length - 1)
              return "Invalid character '" + edgeStr[i].charAt(j) + "' in end move: " + edgeStr[i];
          }
        }
        i--;
      }
      else if (edgeStr[i].charAt(0) >= '0' && edgeStr[i].charAt(0) <= '7') {
        state.turnMask = state.turnMask & 0777077 | ((edgeStr[i].charAt(0) - '0') << 6);
        if (edgeStr[i].length() > 1 && edgeStr[i].charAt(1) >= '0' && edgeStr[i].charAt(1) <= '7')
          state.turnMask = state.turnMask & 0777707 | ((edgeStr[i].charAt(1) - '0') << 3);
        if (edgeStr[i].length() > 2 && edgeStr[i].charAt(2) >= '0' && edgeStr[i].charAt(2) <= '7')
          state.turnMask = state.turnMask & 0777770 | (edgeStr[i].charAt(2) - '0');
        if (edgeStr[i].length() > 3 && edgeStr[i].charAt(3) >= '0' && edgeStr[i].charAt(3) <= '7')
          state.turnMask = state.turnMask & 0077777 | ((edgeStr[i].charAt(3) - '0') << 15);
        if (edgeStr[i].length() > 4 && edgeStr[i].charAt(4) >= '0' && edgeStr[i].charAt(4) <= '7')
          state.turnMask = state.turnMask & 0707777 | ((edgeStr[i].charAt(4) - '0') << 12);
        if (edgeStr[i].length() > 5 && edgeStr[i].charAt(5) >= '0' && edgeStr[i].charAt(5) <= '7')
          state.turnMask = state.turnMask & 0770777 | ((edgeStr[i].charAt(5) - '0') << 9);
        i--;
      }
      else if (edgeStr[i].equals("!!"))
        for (; i < 12; i++)
          edgeStr[i] = "!";
      else if (edgeStr[i].equals("@!!"))
        for (; i < 12; i++)
          edgeStr[i] = "@!";
      else if (edgeStr[i].equals("??"))
        for (; i < 12; i++)
          edgeStr[i] = "?";
      else if (edgeStr[i].equals("@??"))
        for (; i < 12; i++)
          edgeStr[i] = "@?";
    }
    // reading the textual representation of 8 corners
    String[] cornStr = new String[8];
    for (int i = 0; i < 8; i++) {
      cornStr[i] = getInputToken();
      if (cornStr[i].equals("Q") || cornStr[i].equals("q"))
        return "QUIT";
      if (cornStr[i].equals("N") || cornStr[i].equals("n"))
        return "NEW";
      if (cornStr[i].equals(""))
        return "Insufficient number of corners";
      if (cornStr[i].equals("!!"))
        for (; i < 8; i++)
          cornStr[i] = "!";
      else if (cornStr[i].equals("@!!"))
        for (; i < 8; i++)
          cornStr[i] = "@!";
      else if (cornStr[i].equals("??"))
        for (; i < 8; i++)
          cornStr[i] = "?";
      else if (cornStr[i].equals("@??"))
        for (; i < 8; i++)
          cornStr[i] = "@?";
    }
    // filling the edge locations and orientations to e[12], eo*[12], ep*[12]
    // eo1[] is not completely filled (we do not know which edge is '@?')
    // we fill locations that are not set later
    for (int i = 0; i < 12; i++) {
      e[i] = 0;
      ep[i] = 0;
      eo[i] = 0;
      ep1[i] = 1; // ok for unknown
      eo1[i] = -1; // fixed later for unknown
    }
    for (int i = 0; i < 12; i++) {
      if (edgeStr[i].equals("!")) {
        e[i] = i + 1;
        ep[i] = i + 1;
        eo[i] = 1;
        ep1[i] = 0;
        eo1[i] = 0;
      }
      else if (edgeStr[i].equals("@!")) {
        e[i] = i + 1;
        ep[i] = i + 1;
        ep1[i] = 0;
        eo1[i] = 1;
      }
      else if (edgeStr[i].equals("-!")) {
        e[i] = i + 12 + 1;
        ep[i] = i + 1;
        eo[i] = 2;
        ep1[i] = 0;
        eo1[i] = 0;
      }
      else if (edgeStr[i].equals("?")) {
        eo[i] = 1;
      }
      else if (edgeStr[i].equals("@?")) {
      }
      else if (edgeStr[i].equals("-?")) {
        eo[i] = 2;
      }
      else if (edgeStr[i].charAt(0) == '@') {
        boolean found = false;
        for (int j = 0; j < edgeString.length; j++) {
          if (edgeStr[i].substring(1).equals(edgeString[j])) {
            e[i] = j % 12 + 1;
            ep[i] = (e[i] - 1) % 12 + 1;
            ep1[ep[i] - 1] = 0;
            eo1[ep[i] - 1] = 1;
            found = true;
            break;
          }
        }
        if (!found)
          return "Improper edge: " + edgeStr[i];
      }
      else if (edgeStr[i].charAt(0) == '-') {
        boolean found = false;
        for (int j = 0; j < edgeString.length; j++) {
          if (edgeStr[i].substring(1).equals(edgeString[j])) {
            e[i] = (j + 12) % 24 + 1;
            ep[i] = (e[i] - 1) % 12 + 1;
            eo[i] = (e[i] - 1) / 12 + 1;
            ep1[ep[i] - 1] = 0;
            eo1[ep[i] - 1] = 0;
            found = true;
            break;
          }
        }
        if (!found)
          return "Improper edge: " + edgeStr[i];
      }
      else {
        boolean found = false;
        for (int j = 0; j < edgeString.length; j++) {
          if (edgeStr[i].equals(edgeString[j])) {
            e[i] = j + 1;
            ep[i] = (e[i] - 1) % 12 + 1;
            eo[i] = (e[i] - 1) / 12 + 1;
            ep1[ep[i] - 1] = 0;
            eo1[ep[i] - 1] = 0;
            found = true;
            break;
          }
        }
        if (!found)
          return "Improper edge: " + edgeStr[i];
      }
    }
    // fix the rest of eo1[]
    // it is not really correct, since it represents only one of many states
    // however, it is handled correctly in the solving algorithm
    for (int i = 0; i < 12; i++) {
      if (ep[i] == 0) { // where we the index to eo1[] is not known
        if (eo[i] != 0) { // and the edge is oriented
          boolean found = false;
          for (int j = 0; j < 12; j++) {
            if (eo1[j] == -1) {
              eo1[j] = 0;
              found = true;
              break;
            }
          }
          if (!found)
            return "Bad edge format";
        }
        else { // the edge is not oriented
          boolean found = false;
          for (int j = 0; j < 12; j++) {
            if (eo1[j] == -1) {
              eo1[j] = 1;
              found = true;
              break;
            }
          }
          if (!found)
            return "Bad edge format";
        }
      }
    }
    for (int i = 0; i < 12; i++)
      if (eo1[i] == -1)
        return "Bad edge format";
    // filling the corner locations and orientations to c[12], co*[12], cp*[12]
    // co1[] is not completely filled (we do not know which corner is '@?')
    // we fill locations that are not set later
    for (int i = 0; i < 8; i++) {
      c[i] = 0;
      cp[i] = 0;
      co[i] = 0;
      cp1[i] = 1; // ok for unknown
      co1[i] = -1; // fixed later for unknown
    }
    for (int i = 0; i < 8; i++) {
      if (cornStr[i].equals("!")) {
        c[i] = i + 1;
        cp[i] = i + 1;
        co[i] = 1;
        cp1[i] = 0;
        co1[i] = 0;
      }
      else if (cornStr[i].equals("@!")) {
        c[i] = i + 1;
        cp[i] = i + 1;
        cp1[i] = 0;
        co1[i] = 1;
      }
      else if (cornStr[i].equals("+!")) {
        c[i] = i + 8 + 1;
        cp[i] = i + 1;
        co[i] = 2;
        cp1[i] = 0;
        co1[i] = 0;
      }
      else if (cornStr[i].equals("-!")) {
        c[i] = i + 16 + 1;
        cp[i] = i + 1;
        co[i] = 3;
        cp1[i] = 0;
        co1[i] = 0;
      }
      else if (cornStr[i].equals("?")) {
        co[i] = 1;
      }
      else if (cornStr[i].equals("@?")) {
      }
      else if (cornStr[i].equals("+?")) {
        co[i] = 2;
      }
      else if (cornStr[i].equals("-?")) {
        co[i] = 3;
      }
      else if (cornStr[i].charAt(0) == '@') {
        boolean found = false;
        for (int j = 0; j < cornString.length; j++) {
          if (cornStr[i].substring(1).equals(cornString[j])) {
            c[i] = j % 24 % 8 + 1;
            cp[i] = (c[i] - 1) % 8 + 1;
            cp1[cp[i] - 1] = 0;
            co1[cp[i] - 1] = 1;
            found = true;
            break;
          }
        }
        if (!found)
          return "Improper corner: " + cornStr[i];
      }
      else if (cornStr[i].charAt(0) == '+') {
        boolean found = false;
        for (int j = 0; j < cornString.length; j++) {
          if (cornStr[i].substring(1).equals(cornString[j])) {
            c[i] = (j + 8) % 24 + 1;
            cp[i] = (c[i] - 1) % 8 + 1;
            co[i] = (c[i] - 1) / 8 + 1;
            cp1[cp[i] - 1] = 0;
            co1[cp[i] - 1] = 0;
            found = true;
            break;
          }
        }
        if (!found)
          return "Improper corner: " + cornStr[i];
      }
      else if (cornStr[i].charAt(0) == '-') {
        boolean found = false;
        for (int j = 0; j < cornString.length; j++) {
          if (cornStr[i].substring(1).equals(cornString[j])) {
            c[i] = (j + 16) % 24 + 1;
            cp[i] = (c[i] - 1) % 8 + 1;
            co[i] = (c[i] - 1) / 8 + 1;
            cp1[cp[i] - 1] = 0;
            co1[cp[i] - 1] = 0;
            found = true;
            break;
          }
        }
        if (!found)
          return "Improper corner: " + cornStr[i];
      }
      else {
        boolean found = false;
        for (int j = 0; j < cornString.length; j++) {
          if (cornStr[i].equals(cornString[j])) {
            c[i] = j % 24 + 1;
            cp[i] = (c[i] - 1) % 8 + 1;
            co[i] = (c[i] - 1) / 8 + 1;
            cp1[cp[i] - 1] = 0;
            co1[cp[i] - 1] = 0;
            found = true;
            break;
          }
        }
        if (!found)
          return "Improper corner: " + cornStr[i];
      }
    }
    // fix the rest of co1[]
    // it is not really correct, since it represents only one of many states
    // however, it is handled correctly in the solving algorithm
    for (int i = 0; i < 8; i++) {
      if (cp[i] == 0) {  // where we didn't know the index to the co1[]
        if (co[i] != 0) { // and the corner is oriented
          boolean found = false;
          for (int j = 0; j < 8; j++) {
            if (co1[j] == -1) {
              co1[j] = 0;
              found = true;
              break;
            }
          }
          if (!found)
            return "Bad corner format";
        }
        else { // where the corner is not oriented
          boolean found = false;
          for (int j = 0; j < 8; j++) {
            if (co1[j] == -1) {
              co1[j] = 1;
              found = true;
              break;
            }
          }
          if (!found)
            return "Bad corner format";
        }
      }
    }
    for (int i = 0; i < 8; i++)
      if (co1[i] == -1)
        return "Bad corner format";
    return "OK";
  }

  private String check() {
    Comb edgeComb = new Comb(12);
    edgeComb.fill(ep);
    if (!edgeComb.checkVar())
      return "Bad edges";
    Comb cornComb = new Comb(8);
    cornComb.fill(cp);
    if (!cornComb.checkVar())
      return "Bad corners";
    int cornParity = cornComb.varParity(); // parity of corner permutation (0, 1, -1, -2)
    int edgeParity = edgeComb.varParity(); // parity of edge permutation (0, 1, -1, -2)
    // for the fully specified state the parities must be the same
    // else must not depend on positions of min. 2 cubies of the same type
    if (cornParity == -1)
      return "Specify the position of the last corner";
    if (edgeParity == -1)
      return "Specify the position of the last edge";
    if (cornParity >= 0 && edgeParity >= 0 && cornParity != edgeParity)
      return "Swap any two edges or any two corners";
    int flip = 0;
    for (int i = 0; i < 12; i++) { // number of flips of edges
      if (eo[i] == 0) {
        flip = -1;
        break;
      }
      flip += eo[i] - 1;
    }
    if (flip >= 0 && flip % 2 != 0) // must be even (edges are fliped in pairs)
      return "Flip any edge";
    int twist = 0;
    for (int i = 0; i < 8; i++) { // number of clockwise twists of corners
      if (co[i] == 0) {
        twist = -1;
        break;
      }
      twist += co[i] - 1;
    }
    if (twist >= 0 && twist % 3 != 0) // twist mod 3 must be 0
      return "Twist any corner " + (twist % 3 == 1 ? "counter " : "") + "clock-wise";
    return "OK";
  }

  private void preprocess() {
    state.minimum = 101;
    state.depth = 0;
    // corner-twist processing
    int cornOri = 0;
    int cornPos = 0;
    for (int i = 0; i < 8; i++) {
      cornOri |= co1[i] << i;
      cornPos |= cp1[i] << i;
    }
    cornOri |= cornPos << 8; // we have to specify which oriented corners are not positioned
    state.coActive = cornOri;
    state.cActive = cornPos;
    PackCornTwist cornTwist = new PackCornTwist(cornOri);
    cornTwist.fill(co);
    state.cornTwist = cornTwist.pack();
    // edge-flip processing
    int edgeOri = 0;
    int edgePos = 0;
    for (int i = 0; i < 12; i++) {
      edgeOri |= eo1[i] << i;
      edgePos |= ep1[i] << i;
    }
    edgeOri |= edgePos << 12; // we have to specify which oriented edges are not positioned
    state.eoActive = edgeOri;
    state.eActive = edgePos;
    PackEdgeFlip edgeFlip = new PackEdgeFlip(edgeOri);
    edgeFlip.fill(eo);
    state.edgeFlip = edgeFlip.pack();
    // midge-location processing
    int[] edgeTemp = new int[12];
    for (int i = 0; i < 12; i++)
      edgeTemp[i] = (ep[i] <= 8 ? 0 : 1); // 0 - Ux Dx, 1 - FR FL BR BL
    PackEdgeLoc edgeLoc = new PackEdgeLoc(edgePos);
    edgeLoc.fill(edgeTemp);
    state.edgeLoc = edgeLoc.pack();
    // corner-permutation processing
    PackCornPerm cornPerm = new PackCornPerm(cornPos);
    cornPerm.fill(cp);
    state.cornPerm = cornPerm.pack();
    // middle-slice-edge-permutation processing
    for (int i = 0; i < 12; i++) {
      switch (ep[i] - 1) {
       case 8: // FR
        edgeTemp[i] = 1;
        break;
       case 9: // FL
        edgeTemp[i] = 2 - (edgePos >> 8 & 1); // FR
        break;
       case 10: // BR
        edgeTemp[i] = 3 - (edgePos >> 8 & 1) - (edgePos >> 9 & 1); // FR FL
        break;
       case 11: // BL
        edgeTemp[i] = 4 - (edgePos >> 8 & 1) - (edgePos >> 9 & 1) - (edgePos >> 10 & 1); // FR FL BR
        break;
       default:
        edgeTemp[i] = 0;
      }
    }
    PackMidgePos midgePos = new PackMidgePos(edgePos);
    midgePos.fill(edgeTemp);
    state.midgePos = midgePos.pack();
    // up-slice-edge-permutation processing
    for (int i = 0; i < 12; i++) {
      switch (ep[i] - 1) {
       case 0: // UF
        edgeTemp[i] = 1;
        break;
       case 1: // UR
        edgeTemp[i] = 2 - (edgePos & 1); // UF
        break;
       case 2: // UB
        edgeTemp[i] = 3 - (edgePos & 1) - (edgePos >> 1 & 1); // UF UR
        break;
       case 3: // UL
        edgeTemp[i] = 4 - (edgePos & 1) - (edgePos >> 1 & 1) - (edgePos >> 2 & 1); // UF UR UB
        break;
       default:
        edgeTemp[i] = 0;
      }
    }
    PackUEdgePos uEdgePos = new PackUEdgePos(edgePos);
    uEdgePos.fill(edgeTemp);
    state.uEdgePos = uEdgePos.pack();
    // down-slice-edge-permutation processing
    for (int i = 0; i < 12; i++) {
      switch (ep[i] - 1) {
       case 4: // DF
        edgeTemp[i] = 1;
        break;
       case 5: // DR
        edgeTemp[i] = 2 - (edgePos >> 4 & 1); // DF
        break;
       case 6: // DB
        edgeTemp[i] = 3 - (edgePos >> 4 & 1) - (edgePos >> 5 & 1); // DF DR
        break;
       case 7: // DL
        edgeTemp[i] = 4 - (edgePos >> 4 & 1) - (edgePos >> 5 & 1) - (edgePos >> 6 & 1); // DF DR DB
        break;
       default:
        edgeTemp[i] = 0;
      }
    }
    PackDEdgePos dEdgePos = new PackDEdgePos(edgePos);
    dEdgePos.fill(edgeTemp);
    state.dEdgePos = dEdgePos.pack();
  }

  private String input;
  private int inputPos;

  private String getInputToken() {
    String s = "";
    while (inputPos < input.length() && input.charAt(inputPos) <= 32)
      inputPos++;
    while (inputPos < input.length() && input.charAt(inputPos) > 32) {
      s += input.charAt(inputPos);
      inputPos++;
    }
    return s;
  }

  public static String padInt(int number, int length) {
    String s = "" + number;
    while (s.length() < length)
      s = " " + s;
    return s;
  }
}
