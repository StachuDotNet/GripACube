public final class TransformEdgePos {
  // max. used memory: 3,844,080 (7,664,400)
  private short[][] turnAU; // max. 27 x 11,880 = 320,760 (641,520)
  private short[][] turnAD; // max. 27 x 11,880 = 320,760 (641,520)
  private short[][] turnAM; // max. 27 x 11,880 = 320,760 (641,520)
  private short[][] udSToPerm; // max. 1,680 x 1,680 = 2,822,400 (5,644,800)
  private short[] uEdgeToUs; // max. 11,880 = 11,880 (23,760)
  private short[] dEdgeToDs; // max. 11,880 = 11,880 (23,760)
  private short[] midgeToPerm; // max. 11,880 = 11,880 (23,760)
  private boolean[] uInB; // max. 11,880 = 11,880  (11,880)
  private boolean[] dInB; // max. 11,880  = 11,880 (11,880)
  private PackUEdgePos packU;
  private PackDEdgePos packD;
  private PackMidgePos packM;

  public TransformEdgePos(int eActive, boolean optimal) {
    System.err.print(" Phase A: u edge location...");
    packU = new PackUEdgePos(eActive);
    int un = packU.len();
    turnAU = new short[Turn.A_NUM][un];
    for (int i = 0; i < un; i++) {
      turnAU[Turn.A_U1][i] = upU(i);
      turnAU[Turn.A_D1][i] = downU(i);
      turnAU[Turn.A_F1][i] = frontU(i);
      turnAU[Turn.A_B1][i] = backU(i);
      turnAU[Turn.A_L1][i] = leftU(i);
      turnAU[Turn.A_R1][i] = rightU(i);
    }
    for (int i = 0; i < un; i++) {
      turnAU[Turn.A_U2][i] = turnAU[Turn.A_U1][turnAU[Turn.A_U1][i]];
      turnAU[Turn.A_D2][i] = turnAU[Turn.A_D1][turnAU[Turn.A_D1][i]];
      turnAU[Turn.A_F2][i] = turnAU[Turn.A_F1][turnAU[Turn.A_F1][i]];
      turnAU[Turn.A_B2][i] = turnAU[Turn.A_B1][turnAU[Turn.A_B1][i]];
      turnAU[Turn.A_L2][i] = turnAU[Turn.A_L1][turnAU[Turn.A_L1][i]];
      turnAU[Turn.A_R2][i] = turnAU[Turn.A_R1][turnAU[Turn.A_R1][i]];
    }
    for (int i = 0; i < un; i++) {
      turnAU[Turn.A_U3][i] = turnAU[Turn.A_U2][turnAU[Turn.A_U1][i]];
      turnAU[Turn.A_D3][i] = turnAU[Turn.A_D2][turnAU[Turn.A_D1][i]];
      turnAU[Turn.A_F3][i] = turnAU[Turn.A_F2][turnAU[Turn.A_F1][i]];
      turnAU[Turn.A_B3][i] = turnAU[Turn.A_B2][turnAU[Turn.A_B1][i]];
      turnAU[Turn.A_L3][i] = turnAU[Turn.A_L2][turnAU[Turn.A_L1][i]];
      turnAU[Turn.A_R3][i] = turnAU[Turn.A_R2][turnAU[Turn.A_R1][i]];
    }
    for (int i = 0; i < un; i++) {
      turnAU[Turn.A_E1][i] = turnAU[Turn.A_D3][turnAU[Turn.A_U1][i]];
      turnAU[Turn.A_E2][i] = turnAU[Turn.A_D2][turnAU[Turn.A_U2][i]];
      turnAU[Turn.A_E3][i] = turnAU[Turn.A_D1][turnAU[Turn.A_U3][i]];
      turnAU[Turn.A_S1][i] = turnAU[Turn.A_B1][turnAU[Turn.A_F3][i]];
      turnAU[Turn.A_S2][i] = turnAU[Turn.A_B2][turnAU[Turn.A_F2][i]];
      turnAU[Turn.A_S3][i] = turnAU[Turn.A_B3][turnAU[Turn.A_F1][i]];
      turnAU[Turn.A_M1][i] = turnAU[Turn.A_R1][turnAU[Turn.A_L3][i]];
      turnAU[Turn.A_M2][i] = turnAU[Turn.A_R2][turnAU[Turn.A_L2][i]];
      turnAU[Turn.A_M3][i] = turnAU[Turn.A_R3][turnAU[Turn.A_L1][i]];
    }
    System.err.println(" " + un + " items per turn.");
    System.err.print(" Phase A: down edge location...");
    packD = new PackDEdgePos(eActive);
    int dn = packD.len();
    turnAD = new short[Turn.A_NUM][dn];
    for (int i = 0; i < dn; i++) {
      turnAD[Turn.A_U1][i] = upD(i);
      turnAD[Turn.A_D1][i] = downD(i);
      turnAD[Turn.A_F1][i] = frontD(i);
      turnAD[Turn.A_B1][i] = backD(i);
      turnAD[Turn.A_L1][i] = leftD(i);
      turnAD[Turn.A_R1][i] = rightD(i);
    }
    for (int i = 0; i < dn; i++) {
      turnAD[Turn.A_U2][i] = turnAD[Turn.A_U1][turnAD[Turn.A_U1][i]];
      turnAD[Turn.A_D2][i] = turnAD[Turn.A_D1][turnAD[Turn.A_D1][i]];
      turnAD[Turn.A_F2][i] = turnAD[Turn.A_F1][turnAD[Turn.A_F1][i]];
      turnAD[Turn.A_B2][i] = turnAD[Turn.A_B1][turnAD[Turn.A_B1][i]];
      turnAD[Turn.A_L2][i] = turnAD[Turn.A_L1][turnAD[Turn.A_L1][i]];
      turnAD[Turn.A_R2][i] = turnAD[Turn.A_R1][turnAD[Turn.A_R1][i]];
    }
    for (int i = 0; i < dn; i++) {
      turnAD[Turn.A_U3][i] = turnAD[Turn.A_U2][turnAD[Turn.A_U1][i]];
      turnAD[Turn.A_D3][i] = turnAD[Turn.A_D2][turnAD[Turn.A_D1][i]];
      turnAD[Turn.A_F3][i] = turnAD[Turn.A_F2][turnAD[Turn.A_F1][i]];
      turnAD[Turn.A_B3][i] = turnAD[Turn.A_B2][turnAD[Turn.A_B1][i]];
      turnAD[Turn.A_L3][i] = turnAD[Turn.A_L2][turnAD[Turn.A_L1][i]];
      turnAD[Turn.A_R3][i] = turnAD[Turn.A_R2][turnAD[Turn.A_R1][i]];
    }
    for (int i = 0; i < dn; i++) {
      turnAD[Turn.A_E1][i] = turnAD[Turn.A_D3][turnAD[Turn.A_U1][i]];
      turnAD[Turn.A_E2][i] = turnAD[Turn.A_D2][turnAD[Turn.A_U2][i]];
      turnAD[Turn.A_E3][i] = turnAD[Turn.A_D1][turnAD[Turn.A_U3][i]];
      turnAD[Turn.A_S1][i] = turnAD[Turn.A_B1][turnAD[Turn.A_F3][i]];
      turnAD[Turn.A_S2][i] = turnAD[Turn.A_B2][turnAD[Turn.A_F2][i]];
      turnAD[Turn.A_S3][i] = turnAD[Turn.A_B3][turnAD[Turn.A_F1][i]];
      turnAD[Turn.A_M1][i] = turnAD[Turn.A_R1][turnAD[Turn.A_L3][i]];
      turnAD[Turn.A_M2][i] = turnAD[Turn.A_R2][turnAD[Turn.A_L2][i]];
      turnAD[Turn.A_M3][i] = turnAD[Turn.A_R3][turnAD[Turn.A_L1][i]];
    }
    System.err.println(" " + dn + " items per turn.");
    System.err.print(" Phase A: midge location...");
    packM = new PackMidgePos(eActive);
    int mn = packM.len();
    turnAM = new short[Turn.A_NUM][mn];
    for (int i = 0; i < mn; i++) {
      turnAM[Turn.A_U1][i] = upM(i);
      turnAM[Turn.A_D1][i] = downM(i);
      turnAM[Turn.A_F1][i] = frontM(i);
      turnAM[Turn.A_B1][i] = backM(i);
      turnAM[Turn.A_L1][i] = leftM(i);
      turnAM[Turn.A_R1][i] = rightM(i);
    }
    for (int i = 0; i < mn; i++) {
      turnAM[Turn.A_U2][i] = turnAM[Turn.A_U1][turnAM[Turn.A_U1][i]];
      turnAM[Turn.A_D2][i] = turnAM[Turn.A_D1][turnAM[Turn.A_D1][i]];
      turnAM[Turn.A_F2][i] = turnAM[Turn.A_F1][turnAM[Turn.A_F1][i]];
      turnAM[Turn.A_B2][i] = turnAM[Turn.A_B1][turnAM[Turn.A_B1][i]];
      turnAM[Turn.A_L2][i] = turnAM[Turn.A_L1][turnAM[Turn.A_L1][i]];
      turnAM[Turn.A_R2][i] = turnAM[Turn.A_R1][turnAM[Turn.A_R1][i]];
    }
    for (int i = 0; i < mn; i++) {
      turnAM[Turn.A_U3][i] = turnAM[Turn.A_U2][turnAM[Turn.A_U1][i]];
      turnAM[Turn.A_D3][i] = turnAM[Turn.A_D2][turnAM[Turn.A_D1][i]];
      turnAM[Turn.A_F3][i] = turnAM[Turn.A_F2][turnAM[Turn.A_F1][i]];
      turnAM[Turn.A_B3][i] = turnAM[Turn.A_B2][turnAM[Turn.A_B1][i]];
      turnAM[Turn.A_L3][i] = turnAM[Turn.A_L2][turnAM[Turn.A_L1][i]];
      turnAM[Turn.A_R3][i] = turnAM[Turn.A_R2][turnAM[Turn.A_R1][i]];
    }
    for (int i = 0; i < mn; i++) {
      turnAM[Turn.A_E1][i] = turnAM[Turn.A_D3][turnAM[Turn.A_U1][i]];
      turnAM[Turn.A_E2][i] = turnAM[Turn.A_D2][turnAM[Turn.A_U2][i]];
      turnAM[Turn.A_E3][i] = turnAM[Turn.A_D1][turnAM[Turn.A_U3][i]];
      turnAM[Turn.A_S1][i] = turnAM[Turn.A_B1][turnAM[Turn.A_F3][i]];
      turnAM[Turn.A_S2][i] = turnAM[Turn.A_B2][turnAM[Turn.A_F2][i]];
      turnAM[Turn.A_S3][i] = turnAM[Turn.A_B3][turnAM[Turn.A_F1][i]];
      turnAM[Turn.A_M1][i] = turnAM[Turn.A_R1][turnAM[Turn.A_L3][i]];
      turnAM[Turn.A_M2][i] = turnAM[Turn.A_R2][turnAM[Turn.A_L2][i]];
      turnAM[Turn.A_M3][i] = turnAM[Turn.A_R3][turnAM[Turn.A_L1][i]];
    }
    System.err.println(" " + mn + " items per turn.");
    if (!optimal) {
      PackUsEdgePos packUs = new PackUsEdgePos(eActive);
      int usn = packUs.len();
      PackDsEdgePos packDs = new PackDsEdgePos(eActive);
      int dsn = packDs.len();
      System.err.print(" Phase A/B: u/d edge transformation...");
      udSToPerm = new short[usn][dsn];
      for (int i = 0; i < usn; i++)
        for (int j = 0; j < dsn; j++)
          udSToPerm[i][j] = packUs.toUDEdgePerm(i, j);
      System.err.println(" " + usn + " x " + dsn + " items.");
      System.err.print(" Phase A/B: u edge transformation...");
      uEdgeToUs = new short[un];
      for (int i = 0; i < un; i++)
        uEdgeToUs[i] = packU.toUSlice(i);
      System.err.println(" " + un + " items.");
      System.err.print(" Phase A/B: d edge transformation...");
      dEdgeToDs = new short[dn];
      for (int i = 0; i < dn; i++)
        dEdgeToDs[i] = packD.toDSlice(i);
      System.err.println(" " + dn + " items.");
      System.err.print(" Phase A/B: midge transformation...");
      midgeToPerm = new short[mn];
      for (int i = 0; i < mn; i++)
        midgeToPerm[i] = packM.toMidgePerm(i);
      System.err.println(" " + mn + " items.");
      System.err.print(" Phase A/B: u edge attribute...");
      uInB = new boolean[un];
      for (int i = 0; i < un; i++)
        uInB[i] = packU.isInB(i);
      System.err.println(" " + un + " items.");
      System.err.print(" Phase A/B: d edge attribute...");
      dInB = new boolean[dn];
      for (int i = 0; i < dn; i++)
        dInB[i] = packD.isInB(i);
      System.err.println(" " + dn + " items.");
    }
  }

  public int turnAU(int turn, int state) {
    return turnAU[turn][state] & 0xFFFF;
  }

  public int turnAD(int turn, int state) {
    return turnAD[turn][state] & 0xFFFF;
  }

  public int turnAM(int turn, int state) {
    return turnAM[turn][state] & 0xFFFF;
  }

  public int midgeToPerm(int m) {
    return midgeToPerm[m] & 0xFFFF;
  }

  public int udSToPerm(int u, int d) {
    // the first param. is coded U edges, the second D edges...
    return udSToPerm[uEdgeToUs[u]][dEdgeToDs[d]] & 0xFFFF;
  }

  public boolean uInB(int s) {
    return uInB[s];
  }

  public boolean dInB(int s) {
    return dInB[s];
  }

  private short upU(int s) {
    packU.unpack(s);
    packU.cycle(1, 2, 3, 0); // UR UB UL UF
    return (short)packU.pack();
  }

  private short frontU(int s) {
    packU.unpack(s);
    packU.cycle(9, 4, 8, 0); // FL DF FR UF
    return (short)packU.pack();
  }

  private short rightU(int s) {
    packU.unpack(s);
    packU.cycle(8, 5, 10, 1); // FR DR BR UR
    return (short)packU.pack();
  }

  private short downU(int s) {
    packU.unpack(s);
    packU.cycle(7, 6, 5, 4); // DL DB DR DF
    return (short)packU.pack();
  }

  private short backU(int s) {
    packU.unpack(s);
    packU.cycle(10, 6, 11, 2); // BR DB BL UB
    return (short)packU.pack();
  }

  private short leftU(int s) {
    packU.unpack(s);
    packU.cycle(11, 7, 9, 3); // BL DL FL UL
    return (short)packU.pack();
  }

  private short upD(int s) {
    packD.unpack(s);
    packD.cycle(1, 2, 3, 0); // UR UB UL UF
    return (short)packD.pack();
  }

  private short frontD(int s) {
    packD.unpack(s);
    packD.cycle(9, 4, 8, 0); // FL DF FR UF
    return (short)packD.pack();
  }

  private short rightD(int s) {
    packD.unpack(s);
    packD.cycle(8, 5, 10, 1); // FR DR BR UR
    return (short)packD.pack();
  }

  private short downD(int s) {
    packD.unpack(s);
    packD.cycle(7, 6, 5, 4); // DL DB DR DF
    return (short)packD.pack();
  }

  private short backD(int s) {
    packD.unpack(s);
    packD.cycle(10, 6, 11, 2); // BR DB BL UB
    return (short)packD.pack();
  }

  private short leftD(int s) {
    packD.unpack(s);
    packD.cycle(11, 7, 9, 3); // BL DL FL UL
    return (short)packD.pack();
  }

  private short upM(int s) {
    packM.unpack(s);
    packM.cycle(1, 2, 3, 0); // UR UB UL UF
    return (short)packM.pack();
  }

  private short frontM(int s) {
    packM.unpack(s);
    packM.cycle(9, 4, 8, 0); // FL DF FR UF
    return (short)packM.pack();
  }

  private short rightM(int s) {
    packM.unpack(s);
    packM.cycle(8, 5, 10, 1); // FR DR BR UR
    return (short)packM.pack();
  }

  private short downM(int s) {
    packM.unpack(s);
    packM.cycle(7, 6, 5, 4); // DL DB DR DF
    return (short)packM.pack();
  }

  private short backM(int s) {
    packM.unpack(s);
    packM.cycle(10, 6, 11, 2); // BR DB BL UB
    return (short)packM.pack();
  }

  private short leftM(int s) {
    packM.unpack(s);
    packM.cycle(11, 7, 9, 3); // BL DL FL UL
    return (short)packM.pack();
  }
}
