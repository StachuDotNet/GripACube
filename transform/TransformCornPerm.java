public final class TransformCornPerm {
  private short[][] turnA; // max. 27 x 40,320 = 1,088,640 (2,177,280)
  private PackCornPerm pack;

  public TransformCornPerm(int cActive) {
    System.err.print(" Phase A/B: corner permutation...");
    pack = new PackCornPerm(cActive);
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
      turnA[Turn.A_U2][i] = turnA[Turn.A_U1][turnA[Turn.A_U1][i] & 0xFFFF];
      turnA[Turn.A_D2][i] = turnA[Turn.A_D1][turnA[Turn.A_D1][i] & 0xFFFF];
      turnA[Turn.A_F2][i] = turnA[Turn.A_F1][turnA[Turn.A_F1][i] & 0xFFFF];
      turnA[Turn.A_B2][i] = turnA[Turn.A_B1][turnA[Turn.A_B1][i] & 0xFFFF];
      turnA[Turn.A_L2][i] = turnA[Turn.A_L1][turnA[Turn.A_L1][i] & 0xFFFF];
      turnA[Turn.A_R2][i] = turnA[Turn.A_R1][turnA[Turn.A_R1][i] & 0xFFFF];
    }
    for (int i = 0; i < n; i++) {
      turnA[Turn.A_U3][i] = turnA[Turn.A_U2][turnA[Turn.A_U1][i] & 0xFFFF];
      turnA[Turn.A_D3][i] = turnA[Turn.A_D2][turnA[Turn.A_D1][i] & 0xFFFF];
      turnA[Turn.A_F3][i] = turnA[Turn.A_F2][turnA[Turn.A_F1][i] & 0xFFFF];
      turnA[Turn.A_B3][i] = turnA[Turn.A_B2][turnA[Turn.A_B1][i] & 0xFFFF];
      turnA[Turn.A_L3][i] = turnA[Turn.A_L2][turnA[Turn.A_L1][i] & 0xFFFF];
      turnA[Turn.A_R3][i] = turnA[Turn.A_R2][turnA[Turn.A_R1][i] & 0xFFFF];
    }
    for (int i = 0; i < n; i++) {
      turnA[Turn.A_E1][i] = turnA[Turn.A_D3][turnA[Turn.A_U1][i] & 0xFFFF];
      turnA[Turn.A_E2][i] = turnA[Turn.A_D2][turnA[Turn.A_U2][i] & 0xFFFF];
      turnA[Turn.A_E3][i] = turnA[Turn.A_D1][turnA[Turn.A_U3][i] & 0xFFFF];
      turnA[Turn.A_S1][i] = turnA[Turn.A_B1][turnA[Turn.A_F3][i] & 0xFFFF];
      turnA[Turn.A_S2][i] = turnA[Turn.A_B2][turnA[Turn.A_F2][i] & 0xFFFF];
      turnA[Turn.A_S3][i] = turnA[Turn.A_B3][turnA[Turn.A_F1][i] & 0xFFFF];
      turnA[Turn.A_M1][i] = turnA[Turn.A_R1][turnA[Turn.A_L3][i] & 0xFFFF];
      turnA[Turn.A_M2][i] = turnA[Turn.A_R2][turnA[Turn.A_L2][i] & 0xFFFF];
      turnA[Turn.A_M3][i] = turnA[Turn.A_R3][turnA[Turn.A_L1][i] & 0xFFFF];
    }
    System.err.println(" " + n + " items per turn.");
  }

  public int turnA(int turn, int state) {
    return turnA[turn][state] & 0xFFFF;
  }

  public int turnB(int turn, int state) {
    return turnA[Turn.b2a[turn]][state] & 0xFFFF;
  }

  private short up(int c) {
    pack.unpack(c);
    pack.cycle(1, 2, 3, 0); // URB UBL ULF UFR
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
    return (short)pack.pack();
  }

  private short back(int c) {
    pack.unpack(c);
    pack.cycle(7, 6, 2, 1); // DBR DLB UBL URB
    return (short)pack.pack();
  }

  private short left(int c) {
    pack.unpack(c);
    pack.cycle(6, 5, 3, 2); // DLB DFL ULF UBL
    return (short)pack.pack();
  }

  private short right(int c) {
    pack.unpack(c);
    pack.cycle(4, 7, 1, 0); // DRF DBR URB UFR
    return (short)pack.pack();
  }
}
