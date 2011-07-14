
public final class SliceTurnTransform {
  public static final int ID = 0;
  public static final int US = 1;
  public static final int FS = 2;
  public static final int RS = 3;
  public static final int US2 = 4;
  public static final int FS2 = 5;
  public static final int RS2 = 6;
  public static final int DS = 7;
  public static final int BS = 8;
  public static final int LS = 9;
  public static final int US_FS = 10;
  public static final int US_RS = 11;
  public static final int FS_RS = 12;
  public static final int RS_US = 13;
  public static final int US_FS2 = 14;
  public static final int US_RS2 = 15;
  public static final int FS_US2 = 16;
  public static final int FS_RS2 = 17;
  public static final int RS_US2 = 18;
  public static final int RS_FS2 = 19;
  public static final int US_LS = 20;
  public static final int FS_DS = 21;
  public static final int RS_BS = 22;
  public static final int DS_BS = 23;
  public static final int SYM_NUM = 24;

  public static final int S_E1 = 0;
  public static final int S_E2 = 1;
  public static final int S_E3 = 2;
  public static final int S_S1 = 3;
  public static final int S_S2 = 4;
  public static final int S_S3 = 5;
  public static final int S_M1 = 6;
  public static final int S_M2 = 7;
  public static final int S_M3 = 8;
  public static final int S_NUM = 9;

  private final int[][] output = new int[SYM_NUM][Turn.A_NUM];
  private final int[][] transition = new int[SYM_NUM][Turn.A_NUM];
  private int sym;
  private static final int[] s2a = {
    Turn.A_E1, Turn.A_E2, Turn.A_E3,
    Turn.A_S1, Turn.A_S2, Turn.A_S3,
    Turn.A_M1, Turn.A_M2, Turn.A_M3
  };
  private static final int[] symUs = {
    US, US2, US_RS, RS_US, DS, US_RS2, US_FS2, ID,
    US_LS, US_FS, RS_FS2, FS_US2, RS, RS_US2, FS2, RS2,
    FS_DS, RS_BS, FS_RS, DS_BS, FS_RS2, FS, BS, LS
  };
  private static final int[] symFs = {
    FS, US_FS, FS2, US_RS, FS_RS2, BS, FS_US2, FS_RS,
    ID, FS_DS, US_FS2, RS_FS2, US_RS2, US, RS_US, DS_BS,
    US2, RS2, US_LS, RS_BS, LS, RS_US2, RS, DS
  };
  private static final int[] symRs = {
    RS, US_RS, FS_RS, RS2, RS_FS2, RS_US2, LS, RS_BS,
    RS_US, ID, FS, US_RS2, FS_RS2, FS_US2, FS_DS, US_LS,
    DS_BS, US_FS, US2, FS2, US, DS, US_FS2, BS
  };
  private static final int[] turnUs = {
    Turn.A_U1, Turn.A_U2, Turn.A_U3,
    Turn.A_D1, Turn.A_D2, Turn.A_D3,
    Turn.A_R1, Turn.A_R2, Turn.A_R3,
    Turn.A_L1, Turn.A_L2, Turn.A_L3,
    Turn.A_F1, Turn.A_F2, Turn.A_F3,
    Turn.A_B1, Turn.A_B2, Turn.A_B3,
    Turn.A_E1, Turn.A_E2, Turn.A_E3,
    Turn.A_M3, Turn.A_M2, Turn.A_M1,
    Turn.A_S1, Turn.A_S2, Turn.A_S3,
  };
  private static final int[] turnFs = {
    Turn.A_L1, Turn.A_L2, Turn.A_L3,
    Turn.A_R1, Turn.A_R2, Turn.A_R3,
    Turn.A_F1, Turn.A_F2, Turn.A_F3,
    Turn.A_B1, Turn.A_B2, Turn.A_B3,
    Turn.A_D1, Turn.A_D2, Turn.A_D3,
    Turn.A_U1, Turn.A_U2, Turn.A_U3,
    Turn.A_M3, Turn.A_M2, Turn.A_M1,
    Turn.A_S1, Turn.A_S2, Turn.A_S3,
    Turn.A_E1, Turn.A_E2, Turn.A_E3,
  };
  private static final int[] turnRs = {
    Turn.A_F1, Turn.A_F2, Turn.A_F3,
    Turn.A_B1, Turn.A_B2, Turn.A_B3,
    Turn.A_D1, Turn.A_D2, Turn.A_D3,
    Turn.A_U1, Turn.A_U2, Turn.A_U3,
    Turn.A_L1, Turn.A_L2, Turn.A_L3,
    Turn.A_R1, Turn.A_R2, Turn.A_R3,
    Turn.A_S3, Turn.A_S2, Turn.A_S1,
    Turn.A_E1, Turn.A_E2, Turn.A_E3,
    Turn.A_M1, Turn.A_M2, Turn.A_M3,
  };

  public int turnA(int turn, int symmetry) {
    sym = symmetry;
    int t = output[sym][turn];
    sym = transition[sym][t];
    return t;
  }

  public int turnBA(int turn, int symmetry) {
    return turnA(Turn.b2a[turn], symmetry);
  }

  public int symmetry() {
    return sym;
  }

  public SliceTurnTransform() {
    sym = ID;
    int[][] turnSym = new int[SYM_NUM][S_NUM];
    for (int i = 0; i < SYM_NUM; i++) {
      turnSym[i][S_E1] = symUs[i];
      turnSym[i][S_S3] = symFs[i];
      turnSym[i][S_M1] = symRs[i];
    }
    for (int i = 0; i < SYM_NUM; i++) {
      turnSym[i][S_E2] = turnSym[turnSym[i][S_E1]][S_E1];
      turnSym[i][S_S2] = turnSym[turnSym[i][S_S3]][S_S3];
      turnSym[i][S_M2] = turnSym[turnSym[i][S_M1]][S_M1];
    }
    for (int i = 0; i < SYM_NUM; i++) {
      turnSym[i][S_E3] = turnSym[turnSym[i][S_E2]][S_E1];
      turnSym[i][S_S1] = turnSym[turnSym[i][S_S2]][S_S3];
      turnSym[i][S_M3] = turnSym[turnSym[i][S_M2]][S_M1];
    }
    for (int i = 0; i < SYM_NUM; i++) {
      for (int j = 0; j < Turn.A_NUM; j++)
        transition[i][j] = i;
      for (int j = 0; j < S_NUM; j++)
        transition[i][s2a[j]] = turnSym[i][j];
    }
    for (int j = 0; j < Turn.A_NUM; j++) {
      output[ID][j] = j;
      output[US][j] = turnUs[j];
      output[FS][j] = turnFs[j];
      output[RS][j] = turnRs[j];
      output[US2][j] = turnUs[turnUs[j]];
      output[FS2][j] = turnFs[turnFs[j]];
      output[RS2][j] = turnRs[turnRs[j]];
      output[DS][j] = turnUs[turnUs[turnUs[j]]];
      output[BS][j] = turnFs[turnFs[turnFs[j]]];
      output[LS][j] = turnRs[turnRs[turnRs[j]]];
      output[US_FS][j] = turnFs[turnUs[j]];
      output[US_RS][j] = turnRs[turnUs[j]];
      output[FS_RS][j] = turnRs[turnFs[j]];
      output[RS_US][j] = turnUs[turnRs[j]];
      output[US_FS2][j] = turnFs[turnFs[turnUs[j]]];
      output[US_RS2][j] = turnRs[turnRs[turnUs[j]]];
      output[FS_US2][j] = turnUs[turnUs[turnFs[j]]];
      output[FS_RS2][j] = turnRs[turnRs[turnFs[j]]];
      output[RS_US2][j] = turnUs[turnUs[turnRs[j]]];
      output[RS_FS2][j] = turnFs[turnFs[turnRs[j]]];
      output[US_LS][j] = turnRs[turnRs[turnRs[turnUs[j]]]];
      output[FS_DS][j] = turnUs[turnUs[turnUs[turnFs[j]]]];
      output[RS_BS][j] = turnFs[turnFs[turnFs[turnRs[j]]]];
      output[DS_BS][j] = turnFs[turnFs[turnFs[turnUs[turnUs[turnUs[j]]]]]];
    }
  }
}
