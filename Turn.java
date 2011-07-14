
public final class Turn {
  public static final int QUARTER_METRIC = 0;
  public static final int FACE_METRIC = 1;
  public static final int SLICE_METRIC = 2;

  public static final int A_U1 = 0;
  public static final int A_U2 = 1;
  public static final int A_U3 = 2;
  public static final int A_D1 = 3;
  public static final int A_D2 = 4;
  public static final int A_D3 = 5;
  public static final int A_F1 = 6;
  public static final int A_F2 = 7;
  public static final int A_F3 = 8;
  public static final int A_B1 = 9;
  public static final int A_B2 = 10;
  public static final int A_B3 = 11;
  public static final int A_L1 = 12;
  public static final int A_L2 = 13;
  public static final int A_L3 = 14;
  public static final int A_R1 = 15;
  public static final int A_R2 = 16;
  public static final int A_R3 = 17;
  public static final int A_E1 = 18;
  public static final int A_E2 = 19;
  public static final int A_E3 = 20;
  public static final int A_S1 = 21;
  public static final int A_S2 = 22;
  public static final int A_S3 = 23;
  public static final int A_M1 = 24;
  public static final int A_M2 = 25;
  public static final int A_M3 = 26;
  public static final int A_NUM = 27;

  public static final int B_U1 = 0;
  public static final int B_U2 = 1;
  public static final int B_U3 = 2;
  public static final int B_D1 = 3;
  public static final int B_D2 = 4;
  public static final int B_D3 = 5;
  public static final int B_F2 = 6;
  public static final int B_B2 = 7;
  public static final int B_L2 = 8;
  public static final int B_R2 = 9;
  public static final int B_E1 = 10;
  public static final int B_E2 = 11;
  public static final int B_E3 = 12;
  public static final int B_S2 = 13;
  public static final int B_M2 = 14;
  public static final int B_NUM = 15;

  public static final int MASK_U = 256;
  public static final int MASK_D = 128;
  public static final int MASK_F = 64;
  public static final int MASK_B = 32;
  public static final int MASK_L = 16;
  public static final int MASK_R = 8;
  public static final int MASK_E = 4;
  public static final int MASK_S = 2;
  public static final int MASK_M = 1;
  public static final int MASK_U2 = 131072;
  public static final int MASK_D2 = 65536;
  public static final int MASK_F2 = 32768;
  public static final int MASK_B2 = 16384;
  public static final int MASK_L2 = 8192;
  public static final int MASK_R2 = 4096;
  public static final int MASK_E2 = 2048;
  public static final int MASK_S2 = 1024;
  public static final int MASK_M2 = 512;

  public final int[] lengthA = new int[A_NUM];
  public final int[] lengthB = new int[B_NUM];

  public static final int[] turnMaskA = {
    MASK_U, MASK_U | MASK_U2, MASK_U,
    MASK_D, MASK_D | MASK_D2, MASK_D,
    MASK_F, MASK_F | MASK_F2, MASK_F,
    MASK_B, MASK_B | MASK_B2, MASK_B,
    MASK_L, MASK_L | MASK_L2, MASK_L,
    MASK_R, MASK_R | MASK_R2, MASK_R,
    MASK_E, MASK_E | MASK_E2, MASK_E,
    MASK_S, MASK_S | MASK_S2, MASK_S,
    MASK_M, MASK_M | MASK_M2, MASK_M,
  };
  public static final int[] turnMaskB = {
    MASK_U, MASK_U | MASK_U2, MASK_U,
    MASK_D, MASK_D | MASK_D2, MASK_D,
    MASK_F,
    MASK_B,
    MASK_L,
    MASK_R,
    MASK_E, MASK_E | MASK_E2, MASK_E,
    MASK_S,
    MASK_M,
  };
  public static final int[] b2a = {
    A_U1, A_U2, A_U3,
    A_D1, A_D2, A_D3,
    A_F2,
    A_B2,
    A_L2,
    A_R2,
    A_E1, A_E2, A_E3,
    A_S2,
    A_M2
  };
  public static final int[] a2b = {
    B_U1, B_U2, B_U3,
    B_D1, B_D2, B_D3,
    -1, B_F2, -1,
    -1, B_B2, -1,
    -1, B_L2, -1,
    -1, B_R2, -1,
    B_E1, B_E2, B_E3,
    -1, B_S2, -1,
    -1, B_M2, -1
  };
  public static final boolean[] inB = {
    true, true, true,
    true, true, true,
    false, true, false,
    false, true, false,
    false, true, false,
    false, true, false,
    true, true, true,
    false, true, false,
    false, true, false,
  };
  public static final int[] quarter = {
    A_U1, A_U3, A_D1, A_D3, A_F1, A_F3, A_B1, A_B3, A_L1, A_L3, A_R1, A_R3
  };
  public static final int[] face = {
    A_U2, A_D2, A_F2, A_B2, A_L2, A_R2
  };
  public static final int[] slice = {
    A_E1, A_E3, A_S1, A_S3, A_M1, A_M3
  };
  public static final int[] slice2 = {
    A_E2, A_S2, A_M2
  };

  public static final String[] metricString = {
    "Quarter", "Face", "Slice"
  };

  private static final Turn turnQuarter = new Turn(QUARTER_METRIC);
  private static final Turn turnFace = new Turn(FACE_METRIC);
  private static final Turn turnSlice = new Turn(SLICE_METRIC);

  private Turn(int metric) {
    for (int i = 0; i < A_NUM; i++) {
      lengthA[i] = 1;
    }
    if (metric == QUARTER_METRIC) {
      for (int i = 0; i < face.length; i++)
        lengthA[face[i]] = 2;
      for (int i = 0; i < slice.length; i++)
        lengthA[slice[i]] = 2;
      for (int i = 0; i < slice2.length; i++)
        lengthA[slice2[i]] = 4;
    }
    else if (metric == FACE_METRIC) {
      for (int i = 0; i < slice.length; i++)
        lengthA[slice[i]] = 2;
      for (int i = 0; i < slice2.length; i++)
        lengthA[slice2[i]] = 2;
    }
    for (int i = 0; i < b2a.length; i++) {
      lengthB[i] = lengthA[b2a[i]];
    }
  }

  public static Turn turn(int metric) {
    switch (metric) {
     case QUARTER_METRIC:
      return turnQuarter;
     case FACE_METRIC:
      return turnFace;
     case SLICE_METRIC:
      return turnSlice;
     default:
      throw new IllegalArgumentException("Invalid metric");
    }
  }
}
