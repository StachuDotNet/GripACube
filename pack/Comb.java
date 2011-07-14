/**
 * This class contains common algorithms for packing and unpacking
 * permutations, variations and combinations of int arrays
 * in order to make the representation as compact and efficient as possible.
 */
public final class Comb {
  public final int[] a;

  private final int n; // number of items
  private final int k; // number of chosen items

  // precomputed values (constants)
  public final int facN;
  public final int facK;
  public final int comb;
  public final int var;

  /**
   * Creates new object for variations and combinations.
   * @param n number of all items.
   * @param k number of active items.
   */
  public Comb(int n, int k) {
    this.n = n;
    this.k = k;
    facN = fac(n);
    facK = fac(k);
    comb = comb(n, k);
    var = var(n, k);
    a = new int[n];
  }

  /**
   * Creates new object for permutations.
   * @param n number of items.
   */
  public Comb(int n) {
    this(n, n);
  }

  /**
   * Fills the inner value array with the contents of the given one.
   */
  public void fill(int[] a) {
    for (int i = 0; i < n; i++)
      this.a[i] = a[i];
  }

  /**
   * Clears the inner value array to zeroes.
   */
  public void clear() {
    for (int i = 0; i < n; i++)
      a[i] = 0;
  }

  /**
   * Computes factorial of n (n!).
   */
  public static int fac(int n) {
    int x = 1;
    for (int i = n; i > 1; i--)
      x *= i;
    return x;
  }

  /**
   * Computes combinational number n over k (n! / ((n - k)! * k!).
   */
  public static int comb(int n, int k) {
    int x = 1, y = 1;
    for (int i = n, j = k; j > 0; i--, j--) {
      x *= i;
      y *= j;
    }
    return x / y;
  }

  /**
   * Computes variational number n over k (n! / (n - k)!).
   */
  public static int var(int n, int k) {
    int x = 1;
    for (int i = n, j = k; j > 0; i--, j--)
      x *= i;
    return x;
  }

  /**
   * Maps int array to the permutation order.
   * The least number: 0123...; the largest one: ...3210
   */
  public int permPack() {
    int x = 0;
    for (int i = 0; i < n - 1; i++) {
      for (int j = i + 1; j < n; j++)
        if (a[j] < a[i])
          x++;
      x *= n - i - 1;
    }
    return x;
  }

  /**
   * Maps order of permutation to the perm-array of numbers 0, 1, to n - 1.
   */
  public void permUnpack(int x) {
    a[n - 1] = 0;
    for (int i = n - 2; i >= 0; i--) {
      a[i] = x % (n - i);
      x /= n - i;
      for (int j = n - 1; j > i; j--)
        if (a[j] >= a[i])
          a[j]++;
    }
  }

  /**
   * Similar to <tt>permPack()</tt>, packs only non-zero numbers.
   */
  public int mixPermPack() {
    int x = 0;
    for (int i = 0; i < n - 1; i++) {
      if (a[i] > 0) {
        int m = 0;
        for (int j = i + 1; j < n; j++) {
          if (a[j] != 0) {
            m++;
            if (a[j] < a[i])
              x++;
          }
        }
        if (m <= 1) // only one item remains and it does not affect the result
          return x;
        x *= m;
      }
    }
    return 0; // if we have only zeroes in a[i]
  }

  /**
   * Similar to <tt>permUnpack()</tt>, depacks only to non-zero items of a[].
   */
  public void mixPermUnpack(int x) {
    int m = 0;
    for (int i = n - 1; i >= 0; i--) {
      if (a[i] > 0) {
        m++;
        a[i] = x % m + 1;
        x /= m;
        for (int j = n - 1; j > i; j--)
          if (a[j] > 0 && a[j] >= a[i])
            a[j]++;
      }
    }
  }

  /**
   * Packs the binary array with k 1s to its order (C(n, k)).
   * The least number: 00..01..11; the largest one: 11..10..00.
   */
  public int combPack() {
    int x = 0;
    for (int i = 0, j = k; i < n - j; i++)
      if (a[i] > 0)
        x += comb(n - i - 1, j--);
    return x;
  }

  /**
   * Depacks order of combination to the binary array with k ones.
   */
  public void combUnpack(int x) {
    for (int i = 0, j = k; i < n; i++) {
      int c = comb(n - i - 1, j);
      if (c <= x) {
        x -= c;
        a[i] = 1;
        j--;
      }
      else
        a[i] = 0;
    }
  }

  /**
   * Packs variation of k numbers 0 to k - 1 to its order.
   * The least number: ..00123.., .., ..00..321; the largest one: ..32100..
   */
  public int varPack() {
    return combPack() * facK + mixPermPack(); // comb x k! + (sub)perm
  }

  /**
   * Depacks order of variation V(n, k) to the array a.
   */
  public void varUnpack(int x) {
    combUnpack(x / facK); // order of comb
    mixPermUnpack(x % facK); // order of (sub)perm
  }

  /**
   * Checks whether a[] contains proper permutation of the numbers 0 to n - 1.
   * true: valid permutation, false: invalid permutation.
   */
  public boolean checkPerm() {
    int[] c = new int[n];
    for (int i = 0; i < n; i++) {
      if (a[i] < 0 || a[i] >= n) // the numbers must be in this range
        return false;
      c[a[i]]++;
    }
    for (int i = 0; i < n; i++) // each number can appear exactly once
      if (c[i] != 1)
        return false;
    return true;
  }

  /**
   * Checks whether a[] contains proper variation of the numbers > 0.
   * @return true for a valid variation, false for an invalid variation.
   */
  public boolean checkVar() {
    int[] c = new int[n];
    for (int i = 0; i < n; i++) {
      if (a[i] < 0 || a[i] > n)
        return false;
      if (a[i] > 0)
        c[a[i] - 1]++;
    }
    for (int i = 0; i < n; i++) // each number can appear at most once
      if (c[i] > 1)
        return false;
    return true;
  }

  /**
   * Returns the number of swaps in the permutations (parity).
   * @return 0 for even number of swaps, 1 for odd.
   */
  public int permParity() {
    int p = 0;
    for (int i = 0; i < n - 1; i++) // how many pairs are not in order mod 2
      for (int j = i + 1; j < n; j++)
        if (a[i] > a[j])
          p = 1 - p;
    return p;
  }

  /**
   * Returns the number of swaps in the variation (parity).
   * @return 0 for even, 1 for odd, -1 for invalid, -2 for any.
   */
  public int varParity() {
    int p = 0;
    for (int i = 0; i < n; i++)
      if (a[i] == 0)
        if (p == -1)
          return -2;
        else
          p = -1;
    if (p < 0)
      return p;
    return permParity();
  }

  /**
   * Swaps items at the positions: i1 &lt;-> i2.
   */
  public void swap(int i1, int i2) {
    int t = a[i1];
    a[i1] = a[i2];
    a[i2] = t;
  }

  /**
   * Cycles items at the positions: i1 -> i4 -> i3 -> i2 -> i1.
   */
  public void cycle(int i1, int i2, int i3, int i4) {
    int t = a[i1];
    a[i1] = a[i2];
    a[i2] = a[i3];
    a[i3] = a[i4];
    a[i4] = t;
  }

  public String toString() {
    String s = "" + n + "/" + k + " [";
    for (int i = 0; i < a.length; i++)
      s += (i == 0 ? "" : " ") + a[i];
    return s + "]";
  }
}
