public final class TransformEdgeFlip {
  private int[][] turnA; // max. 27 x 126,720 = 3,421,440 (13,685,760)
  private PackEdgeFlip pack;

  public TransformEdgeFlip(int eoActive) {
    System.err.print(" Pahse A: edge flip...");
    pack = new PackEdgeFlip(eoActive);
    int n = pack.len();
    turnA = new int[Turn.A_NUM][n];
    for (int i = 0; i < n; i++) {
      turnA[Turn.A_U1][i] = up(i);
      turnA[Turn.A_D1][i] = down(i);
      turnA[Turn.A_F1][i] = front(i);
      turnA[Turn.A_B1][i] = back(i);
      turnA[Turn.A_L1][i] = left(i);
      turnA[Turn.A_R1][i] = right(i);
    }
    for (int i = 0; i < n; i++) {
      turnA[Turn.A_U2][i] = turnA[Turn.A_U1][turnA[Turn.A_U1][i]];
      turnA[Turn.A_D2][i] = turnA[Turn.A_D1][turnA[Turn.A_D1][i]];
      turnA[Turn.A_F2][i] = turnA[Turn.A_F1][turnA[Turn.A_F1][i]];
      turnA[Turn.A_B2][i] = turnA[Turn.A_B1][turnA[Turn.A_B1][i]];
      turnA[Turn.A_L2][i] = turnA[Turn.A_L1][turnA[Turn.A_L1][i]];
      turnA[Turn.A_R2][i] = turnA[Turn.A_R1][turnA[Turn.A_R1][i]];
    }
    for (int i = 0; i < n; i++) {
      turnA[Turn.A_U3][i] = turnA[Turn.A_U2][turnA[Turn.A_U1][i]];
      turnA[Turn.A_D3][i] = turnA[Turn.A_D2][turnA[Turn.A_D1][i]];
      turnA[Turn.A_F3][i] = turnA[Turn.A_F2][turnA[Turn.A_F1][i]];
      turnA[Turn.A_B3][i] = turnA[Turn.A_B2][turnA[Turn.A_B1][i]];
      turnA[Turn.A_L3][i] = turnA[Turn.A_L2][turnA[Turn.A_L1][i]];
      turnA[Turn.A_R3][i] = turnA[Turn.A_R2][turnA[Turn.A_R1][i]];
    }
    for (int i = 0; i < n; i++) {
      turnA[Turn.A_E1][i] = turnA[Turn.A_D3][turnA[Turn.A_U1][i]];
      turnA[Turn.A_E2][i] = turnA[Turn.A_D2][turnA[Turn.A_U2][i]];
      turnA[Turn.A_E3][i] = turnA[Turn.A_D1][turnA[Turn.A_U3][i]];
      turnA[Turn.A_S1][i] = turnA[Turn.A_B1][turnA[Turn.A_F3][i]];
      turnA[Turn.A_S2][i] = turnA[Turn.A_B2][turnA[Turn.A_F2][i]];
      turnA[Turn.A_S3][i] = turnA[Turn.A_B3][turnA[Turn.A_F1][i]];
      turnA[Turn.A_M1][i] = turnA[Turn.A_R1][turnA[Turn.A_L3][i]];
      turnA[Turn.A_M2][i] = turnA[Turn.A_R2][turnA[Turn.A_L2][i]];
      turnA[Turn.A_M3][i] = turnA[Turn.A_R3][turnA[Turn.A_L1][i]];
    }
    System.err.println(" " + n + " items per turn.");
  }

  public int turnA(int turn, int state) {
    return turnA[turn][state];
  }

  private int up(int e) {
    pack.unpack(e);
    pack.cycle(1, 2, 3, 0); // UR UB UL UF
    return pack.pack();
  }

  private int down(int e) {
    pack.unpack(e);
    pack.cycle(7, 6, 5, 4); // DL DB DR DF
    return pack.pack();
  }

  private int front(int e) {
    pack.unpack(e);
    pack.cycle(9, 4, 8, 0); // FL DF FR UF
    pack.flip(9, 4, 8, 0);
    return pack.pack();
  }

  private int back(int e) {
    pack.unpack(e);
    pack.cycle(10, 6, 11, 2); // BR DB BL UB
    pack.flip(10, 6, 11, 2);
    return pack.pack();
  }

  private int left(int e) {
    pack.unpack(e);
    pack.cycle(11, 7, 9, 3); // BL DL FL UL
    return pack.pack();
  }

  private int right(int e) {
    pack.unpack(e);
    pack.cycle(8, 5, 10, 1); // FR DR BR UR
    return pack.pack();
  }
}
