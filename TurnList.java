
public final class TurnList {
  public static final int INVALID = 0;
  public static final int FREE = 1;
  public static final int U1 = 2;
  public static final int U2 = 3;
  public static final int U3 = 4;
  public static final int D1 = 5;
  public static final int D2 = 6;
  public static final int D3 = 7;
  public static final int F1 = 8;
  public static final int F2 = 9;
  public static final int F3 = 10;
  public static final int B1 = 11;
  public static final int B2 = 12;
  public static final int B3 = 13;
  public static final int L1 = 14;
  public static final int L2 = 15;
  public static final int L3 = 16;
  public static final int R1 = 17;
  public static final int R2 = 18;
  public static final int R3 = 19;
  public static final int UD = 20;
  public static final int FB = 21;
  public static final int LR = 22;
  public static final int UDE = 23;
  public static final int FBS = 24;
  public static final int LRM = 25;
  public static final int NUM = 26;

  public final int[][] nextA = new int[Turn.A_NUM][NUM];

  public TurnList(int mask) {
    if (mask == 0)
      mask = 0777777;
    System.err.println("Turn mask: " + Integer.toOctalString(mask));
    for (int i = 0; i < NUM; i++) {
      nextA[Turn.A_U1][i] = (mask & Turn.MASK_U) != 0 ? U1 : INVALID;
      nextA[Turn.A_U2][i] = (mask & Turn.MASK_U | mask & Turn.MASK_U2) != 0 ? U2 : INVALID;
      nextA[Turn.A_U3][i] = (mask & Turn.MASK_U) != 0 ? U3 : INVALID;
      nextA[Turn.A_D1][i] = (mask & Turn.MASK_D) != 0 ? D1 : INVALID;
      nextA[Turn.A_D2][i] = (mask & Turn.MASK_D | mask & Turn.MASK_D2) != 0 ? D2 : INVALID;
      nextA[Turn.A_D3][i] = (mask & Turn.MASK_D) != 0 ? D3 : INVALID;
      nextA[Turn.A_F1][i] = (mask & Turn.MASK_F) != 0 ? F1 : INVALID;
      nextA[Turn.A_F2][i] = (mask & Turn.MASK_F | mask & Turn.MASK_F2) != 0 ? F2 : INVALID;
      nextA[Turn.A_F3][i] = (mask & Turn.MASK_F) != 0 ? F3 : INVALID;
      nextA[Turn.A_B1][i] = (mask & Turn.MASK_B) != 0 ? B1 : INVALID;
      nextA[Turn.A_B2][i] = (mask & Turn.MASK_B | mask & Turn.MASK_B2) != 0 ? B2 : INVALID;
      nextA[Turn.A_B3][i] = (mask & Turn.MASK_B) != 0 ? B3 : INVALID;
      nextA[Turn.A_L1][i] = (mask & Turn.MASK_L) != 0 ? L1 : INVALID;
      nextA[Turn.A_L2][i] = (mask & Turn.MASK_L | mask & Turn.MASK_L2) != 0 ? L2 : INVALID;
      nextA[Turn.A_L3][i] = (mask & Turn.MASK_L) != 0 ? L3 : INVALID;
      nextA[Turn.A_R1][i] = (mask & Turn.MASK_R) != 0 ? R1 : INVALID;
      nextA[Turn.A_R2][i] = (mask & Turn.MASK_R | mask & Turn.MASK_R2) != 0 ? R2 : INVALID;
      nextA[Turn.A_R3][i] = (mask & Turn.MASK_R) != 0 ? R3 : INVALID;
      nextA[Turn.A_E1][i] = (mask & Turn.MASK_E) != 0 ? UDE : INVALID;
      nextA[Turn.A_E2][i] = (mask & Turn.MASK_E | mask & Turn.MASK_E2) != 0 ? UDE : INVALID;
      nextA[Turn.A_E3][i] = (mask & Turn.MASK_E) != 0 ? UDE : INVALID;
      nextA[Turn.A_S1][i] = (mask & Turn.MASK_S) != 0 ? FBS : INVALID;
      nextA[Turn.A_S2][i] = (mask & Turn.MASK_S | mask & Turn.MASK_S2) != 0 ? FBS : INVALID;
      nextA[Turn.A_S3][i] = (mask & Turn.MASK_S) != 0 ? FBS : INVALID;
      nextA[Turn.A_M1][i] = (mask & Turn.MASK_M) != 0 ? LRM : INVALID;
      nextA[Turn.A_M2][i] = (mask & Turn.MASK_M | mask & Turn.MASK_M2) != 0 ? LRM : INVALID;
      nextA[Turn.A_M3][i] = (mask & Turn.MASK_M) != 0 ? LRM : INVALID;
    }
    for (int i = 0; i < Turn.A_NUM; i++)
      nextA[i][INVALID] = INVALID;
    nextA[Turn.A_U1][U1] = nextA[Turn.A_U1][U2] = nextA[Turn.A_U1][U3] =
    nextA[Turn.A_U1][D1] = nextA[Turn.A_U1][D2] = nextA[Turn.A_U1][D3] =
    nextA[Turn.A_U1][UD] = nextA[Turn.A_U1][UDE] = INVALID;
    nextA[Turn.A_U2][U1] = nextA[Turn.A_U2][U2] = nextA[Turn.A_U2][U3] =
    nextA[Turn.A_U2][D1] = nextA[Turn.A_U2][D2] = nextA[Turn.A_U2][D3] =
    nextA[Turn.A_U2][UD] = nextA[Turn.A_U2][UDE] = INVALID;
    nextA[Turn.A_U3][U1] = nextA[Turn.A_U3][U2] = nextA[Turn.A_U3][U3] =
    nextA[Turn.A_U3][D1] = nextA[Turn.A_U3][D2] = nextA[Turn.A_U3][D3] =
    nextA[Turn.A_U3][UD] = nextA[Turn.A_U3][UDE] = INVALID;

    nextA[Turn.A_D1][D1] = nextA[Turn.A_D1][D2] = nextA[Turn.A_D1][D3] =
    nextA[Turn.A_D1][UD] = nextA[Turn.A_D1][UDE] = INVALID;
    nextA[Turn.A_D1][U1] = nextA[Turn.A_D1][U2] = nextA[Turn.A_D1][U3] =
    (mask & Turn.MASK_D) != 0 ? UD : INVALID;
    nextA[Turn.A_D2][D1] = nextA[Turn.A_D2][D2] = nextA[Turn.A_D2][D3] =
    nextA[Turn.A_D2][UD] = nextA[Turn.A_D2][UDE] = INVALID;
    nextA[Turn.A_D2][U1] = nextA[Turn.A_D2][U2] = nextA[Turn.A_D2][U3] =
    (mask & Turn.MASK_D | mask & Turn.MASK_D2) != 0 ? UD : INVALID;
    nextA[Turn.A_D3][D1] = nextA[Turn.A_D3][D2] = nextA[Turn.A_D3][D3] =
    nextA[Turn.A_D3][UD] = nextA[Turn.A_D3][UDE] = INVALID;
    nextA[Turn.A_D3][U1] = nextA[Turn.A_D3][U2] = nextA[Turn.A_D3][U3] =
    (mask & Turn.MASK_D) != 0 ? UD : INVALID;

    nextA[Turn.A_F1][F1] = nextA[Turn.A_F1][F2] = nextA[Turn.A_F1][F3] =
    nextA[Turn.A_F1][B1] = nextA[Turn.A_F1][B2] = nextA[Turn.A_F1][B3] =
    nextA[Turn.A_F1][FB] = nextA[Turn.A_F1][FBS] = INVALID;
    nextA[Turn.A_F2][F1] = nextA[Turn.A_F2][F2] = nextA[Turn.A_F2][F3] =
    nextA[Turn.A_F2][B1] = nextA[Turn.A_F2][B2] = nextA[Turn.A_F2][B3] =
    nextA[Turn.A_F2][FB] = nextA[Turn.A_F2][FBS] = INVALID;
    nextA[Turn.A_F3][F1] = nextA[Turn.A_F3][F2] = nextA[Turn.A_F3][F3] =
    nextA[Turn.A_F3][B1] = nextA[Turn.A_F3][B2] = nextA[Turn.A_F3][B3] =
    nextA[Turn.A_F3][FB] = nextA[Turn.A_F3][FBS] = INVALID;

    nextA[Turn.A_B1][B1] = nextA[Turn.A_B1][B2] = nextA[Turn.A_B1][B3] =
    nextA[Turn.A_B1][FB] = nextA[Turn.A_B1][FBS] = INVALID;
    nextA[Turn.A_B1][F1] = nextA[Turn.A_B1][F2] = nextA[Turn.A_B1][F3] =
    (mask & Turn.MASK_B) != 0 ? FB : INVALID;
    nextA[Turn.A_B2][B1] = nextA[Turn.A_B2][B2] = nextA[Turn.A_B2][B3] =
    nextA[Turn.A_B2][FB] = nextA[Turn.A_B2][FBS] = INVALID;
    nextA[Turn.A_B2][F1] = nextA[Turn.A_B2][F2] = nextA[Turn.A_B2][F3] =
    (mask & Turn.MASK_B | mask & Turn.MASK_B2) != 0 ? FB : INVALID;
    nextA[Turn.A_B3][B1] = nextA[Turn.A_B3][B2] = nextA[Turn.A_B3][B3] =
    nextA[Turn.A_B3][FB] = nextA[Turn.A_B3][FBS] = INVALID;
    nextA[Turn.A_B3][F1] = nextA[Turn.A_B3][F2] = nextA[Turn.A_B3][F3] =
    (mask & Turn.MASK_B) != 0 ? FB : INVALID;

    nextA[Turn.A_L1][L1] = nextA[Turn.A_L1][L2] = nextA[Turn.A_L1][L3] =
    nextA[Turn.A_L1][R1] = nextA[Turn.A_L1][R2] = nextA[Turn.A_L1][R3] =
    nextA[Turn.A_L1][LR] = nextA[Turn.A_L1][LRM] = INVALID;
    nextA[Turn.A_L2][L1] = nextA[Turn.A_L2][L2] = nextA[Turn.A_L2][L3] =
    nextA[Turn.A_L2][R1] = nextA[Turn.A_L2][R2] = nextA[Turn.A_L2][R3] =
    nextA[Turn.A_L2][LR] = nextA[Turn.A_L2][LRM] = INVALID;
    nextA[Turn.A_L3][L1] = nextA[Turn.A_L3][L2] = nextA[Turn.A_L3][L3] =
    nextA[Turn.A_L3][R1] = nextA[Turn.A_L3][R2] = nextA[Turn.A_L3][R3] =
    nextA[Turn.A_L3][LR] = nextA[Turn.A_L3][LRM] = INVALID;

    nextA[Turn.A_R1][R1] = nextA[Turn.A_R1][R2] = nextA[Turn.A_R1][R3] =
    nextA[Turn.A_R1][LR] = nextA[Turn.A_R1][LRM] = INVALID;
    nextA[Turn.A_R1][L1] = nextA[Turn.A_R1][L2] = nextA[Turn.A_R1][L3] =
    (mask & Turn.MASK_R) != 0 ? LR : INVALID;
    nextA[Turn.A_R2][R1] = nextA[Turn.A_R2][R2] = nextA[Turn.A_R2][R3] =
    nextA[Turn.A_R2][LR] = nextA[Turn.A_R2][LRM] = INVALID;
    nextA[Turn.A_R2][L1] = nextA[Turn.A_R2][L2] = nextA[Turn.A_R2][L3] =
    (mask & Turn.MASK_R | mask & Turn.MASK_R2) != 0 ? LR : INVALID;
    nextA[Turn.A_R3][R1] = nextA[Turn.A_R3][R2] = nextA[Turn.A_R3][R3] =
    nextA[Turn.A_R3][LR] = nextA[Turn.A_R3][LRM] = INVALID;
    nextA[Turn.A_R3][L1] = nextA[Turn.A_R3][L2] = nextA[Turn.A_R3][L3] =
    (mask & Turn.MASK_R) != 0 ? LR : INVALID;

    nextA[Turn.A_E1][UDE] = INVALID;
    nextA[Turn.A_E1][U1] = nextA[Turn.A_E1][U2] = nextA[Turn.A_E1][U3] =
    nextA[Turn.A_E1][D1] = nextA[Turn.A_E1][D2] = nextA[Turn.A_E1][D3] =
    nextA[Turn.A_E1][UD] = (mask & Turn.MASK_E) != 0 ? UDE : INVALID;
    nextA[Turn.A_E2][UDE] = INVALID;
    nextA[Turn.A_E2][U1] = nextA[Turn.A_E2][U2] = nextA[Turn.A_E2][U3] =
    nextA[Turn.A_E2][D1] = nextA[Turn.A_E2][D2] = nextA[Turn.A_E2][D3] =
    nextA[Turn.A_E2][UD] = (mask & Turn.MASK_E | mask & Turn.MASK_E2) != 0 ? UDE : INVALID;
    nextA[Turn.A_E3][UDE] = INVALID;
    nextA[Turn.A_E3][U1] = nextA[Turn.A_E3][U2] = nextA[Turn.A_E3][U3] =
    nextA[Turn.A_E3][D1] = nextA[Turn.A_E3][D2] = nextA[Turn.A_E3][D3] =
    nextA[Turn.A_E3][UD] = (mask & Turn.MASK_E) != 0 ? UDE : INVALID;

    nextA[Turn.A_S1][FBS] = INVALID;
    nextA[Turn.A_S1][F1] = nextA[Turn.A_S1][F2] = nextA[Turn.A_S1][F3] =
    nextA[Turn.A_S1][B1] = nextA[Turn.A_S1][B2] = nextA[Turn.A_S1][B3] =
    nextA[Turn.A_S1][FB] = (mask & Turn.MASK_S) != 0 ? FBS : INVALID;
    nextA[Turn.A_S2][FBS] = INVALID;
    nextA[Turn.A_S2][F1] = nextA[Turn.A_S2][F2] = nextA[Turn.A_S2][F3] =
    nextA[Turn.A_S2][B1] = nextA[Turn.A_S2][B2] = nextA[Turn.A_S2][B3] =
    nextA[Turn.A_S2][FB] = (mask & Turn.MASK_S | mask & Turn.MASK_S2) != 0 ? FBS : INVALID;
    nextA[Turn.A_S3][FBS] = INVALID;
    nextA[Turn.A_S3][F1] = nextA[Turn.A_S3][F2] = nextA[Turn.A_S3][F3] =
    nextA[Turn.A_S3][B1] = nextA[Turn.A_S3][B2] = nextA[Turn.A_S3][B3] =
    nextA[Turn.A_S3][FB] = (mask & Turn.MASK_S) != 0 ? FBS : INVALID;

    nextA[Turn.A_M1][LRM] = INVALID;
    nextA[Turn.A_M1][L1] = nextA[Turn.A_M1][L2] = nextA[Turn.A_M1][L3] =
    nextA[Turn.A_M1][R1] = nextA[Turn.A_M1][R2] = nextA[Turn.A_M1][R3] =
    nextA[Turn.A_M1][LR] = (mask & Turn.MASK_M) != 0 ? LRM : INVALID;
    nextA[Turn.A_M2][LRM] = INVALID;
    nextA[Turn.A_M2][L1] = nextA[Turn.A_M2][L2] = nextA[Turn.A_M2][L3] =
    nextA[Turn.A_M2][R1] = nextA[Turn.A_M2][R2] = nextA[Turn.A_M2][R3] =
    nextA[Turn.A_M2][LR] = (mask & Turn.MASK_M | mask & Turn.MASK_M2) != 0 ? LRM : INVALID;
    nextA[Turn.A_M3][LRM] = INVALID;
    nextA[Turn.A_M3][L1] = nextA[Turn.A_M3][L2] = nextA[Turn.A_M3][L3] =
    nextA[Turn.A_M3][R1] = nextA[Turn.A_M3][R2] = nextA[Turn.A_M3][R3] =
    nextA[Turn.A_M3][LR] = (mask & Turn.MASK_M) != 0 ? LRM : INVALID;
  }
}
