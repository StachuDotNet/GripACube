public final class TransformMidgePerm {
  private short[][] turnB; // 15 x 24 = 360 (720)
  private PackMidgePerm pack;

  public TransformMidgePerm(int eActive) {
    System.err.print(" Phase B: midge permutation...");
    pack = new PackMidgePerm(eActive);
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
      turnB[Turn.B_U2][i] = turnB[Turn.B_U1][turnB[Turn.B_U1][i]];
      turnB[Turn.B_D2][i] = turnB[Turn.B_D1][turnB[Turn.B_D1][i]];
    }
    for (int i = 0; i < n; i++) {
      turnB[Turn.B_U3][i] = turnB[Turn.B_U2][turnB[Turn.B_U1][i]];
      turnB[Turn.B_D3][i] = turnB[Turn.B_D2][turnB[Turn.B_D1][i]];
    }
    for (int i = 0; i < n; i++) {
      turnB[Turn.B_E1][i] = turnB[Turn.B_D3][turnB[Turn.B_U1][i]];
      turnB[Turn.B_E2][i] = turnB[Turn.B_D2][turnB[Turn.B_U2][i]];
      turnB[Turn.B_E3][i] = turnB[Turn.B_U3][turnB[Turn.B_D1][i]];
      turnB[Turn.B_S2][i] = turnB[Turn.B_B2][turnB[Turn.B_F2][i]];
      turnB[Turn.B_M2][i] = turnB[Turn.B_L2][turnB[Turn.B_R2][i]];
    }
    System.err.println(" " + n + " items per turn.");
  }

  public int turnB(int turn, int state) {
    return turnB[turn][state] & 0xFFFF;
  }

  private short up(int m) {
    return (short)m;
  }

  private short down(int m) {
    return (short)m;
  }

  private short front2(int m) {
    pack.unpack(m);
    pack.swap(0, 1); // FR FL
    return (short)pack.pack();
  }

  private short back2(int m) {
    pack.unpack(m);
    pack.swap(2, 3); // BR BL
    return (short)pack.pack();
  }

  private short left2(int m) {
    pack.unpack(m);
    pack.swap(1, 3); // FL BL
    return (short)pack.pack();
  }

  private short right2(int m) {
    pack.unpack(m);
    pack.swap(0, 2); // FR BR
    return (short)pack.pack();
  }
}
