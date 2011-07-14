public final class TransformUDEdgePerm {
  private short[][] turnB; // max. 15 x 40,320 = 640,800 (1,209,600)
  private PackUDEdgePerm pack;

  public TransformUDEdgePerm(int eActive) {
    System.err.print(" Phase B: u/d edge permutation...");
    pack = new PackUDEdgePerm(eActive);
    int n = pack.len();
    turnB = new short[Turn.B_NUM][n];
    for (int i = 0; i < n; i++) {
      turnB[Turn.B_U1][i] = up(i);
      turnB[Turn.B_D1][i] = down(i);
      turnB[Turn.B_F2][i] = front2(i);
      turnB[Turn.B_B2][i] = back2(i);
      turnB[Turn.B_L2][i] = left2(i);
      turnB[Turn.B_R2][i] = right2(i);
    }
    for (int i = 0; i < n; i++) {
      turnB[Turn.B_U2][i] = turnB[Turn.B_U1][turnB[Turn.B_U1][i] & 0xFFFF];
      turnB[Turn.B_D2][i] = turnB[Turn.B_D1][turnB[Turn.B_D1][i] & 0xFFFF];
    }
    for (int i = 0; i < n; i++) {
      turnB[Turn.B_U3][i] = turnB[Turn.B_U2][turnB[Turn.B_U1][i] & 0xFFFF];
      turnB[Turn.B_D3][i] = turnB[Turn.B_D2][turnB[Turn.B_D1][i] & 0xFFFF];
    }
    for (int i = 0; i < n; i++) {
      turnB[Turn.B_E1][i] = turnB[Turn.B_D3][turnB[Turn.B_U1][i] & 0xFFFF];
      turnB[Turn.B_E2][i] = turnB[Turn.B_D2][turnB[Turn.B_U2][i] & 0xFFFF];
      turnB[Turn.B_E3][i] = turnB[Turn.B_U3][turnB[Turn.B_D1][i] & 0xFFFF];
      turnB[Turn.B_S2][i] = turnB[Turn.B_B2][turnB[Turn.B_F2][i] & 0xFFFF];
      turnB[Turn.B_M2][i] = turnB[Turn.B_L2][turnB[Turn.B_R2][i] & 0xFFFF];
    }
    System.err.println(" " + n + " items per turn.");
  }

  public int turnB(int turn, int state) {
    return turnB[turn][state] & 0xFFFF;
  }

  private short up(int e) {
    pack.unpack(e);
    pack.cycle(1, 2, 3, 0); // UR UB UL UF
    return (short)pack.pack();
  }

  private short down(int e) {
    pack.unpack(e);
    pack.cycle(7, 6, 5, 4); // DL DB DR DF
    return (short)pack.pack();
  }

  private short front2(int e) {
    pack.unpack(e);
    pack.swap(0, 4); // UF DF
    return (short)pack.pack();
  }

  private short back2(int e) {
    pack.unpack(e);
    pack.swap(2, 6); // UB DB
    return (short)pack.pack();
  }

  private short left2(int e) {
    pack.unpack(e);
    pack.swap(3, 7); // UL DL
    return (short)pack.pack();
  }

  private short right2(int e) {
    pack.unpack(e);
    pack.swap(1, 5); // UR DR
    return (short)pack.pack();
  }
}
