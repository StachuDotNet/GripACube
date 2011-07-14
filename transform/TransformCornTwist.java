public final class TransformCornTwist {
  private short[][] turnA; // max. 27 x 20,412 = 551,124 (1,102,248)
  private PackCornTwist pack;

  public TransformCornTwist(int coActive) {
    System.err.print(" Phase A: corner twist...");
    pack = new PackCornTwist(coActive);
    int n = pack.len();
    turnA = new short[Turn.A_NUM][n];
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
    return turnA[turn][state] & 0xFFFF;
  }

  private short up(int c) {
    pack.unpack(c);
    pack.cycle(0, 1, 2, 3); // UFR URB UBL ULF
    return (short)pack.pack();
  }

  private short down(int c) {
    pack.unpack(c);
    pack.cycle(5, 6, 7, 4); // DFL DLB DBR DRF
    return (short)pack.pack();
  }

  private short front(int c) {
    pack.unpack(c);
    pack.cycle(5, 4, 0, 3); // DFL DRF UFR ULF
    pack.twist(5, 4, 0, 3);
    return (short)pack.pack();
  }

  private short back(int c) {
    pack.unpack(c);
    pack.cycle(7, 6, 2, 1); // DBR DLB UBL URB
    pack.twist(7, 6, 2, 1);
    return (short)pack.pack();
  }

  private short left(int c) {
    pack.unpack(c);
    pack.cycle(6, 5, 3, 2); // DLB DFL ULF UBL
    pack.twist(6, 5, 3, 2);
    return (short)pack.pack();
  }

  private short right(int c) {
    pack.unpack(c);
    pack.cycle(4, 7, 1, 0); // DRF DBR URB UFR
    pack.twist(4, 7, 1, 0);
    return (short)pack.pack();
  }
}
