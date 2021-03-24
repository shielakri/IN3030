import java.util.Arrays;

class RadixSort {

  // useBits:
  // The number of bits used to represent a single digit (or one position).
  // Or: the number of bits one position is composed of.
  // This variable will determine our radix
  int useBits;
  // a: array to be sorted b: the output of counting sort
  int[] a, b;

  RadixSort(int useBits) {
    this.useBits = useBits;
  }

  int[] radixSort(int[] unsortedArray) {

    a = unsortedArray;
    b = new int[a.length];

    // STEP A : Find max

    int max = a[0];

    for (int num : a)
      if (num > max)
        max = num;


    // Find number of positions required to represent max
    // -> gives us the number of times we need to perform the counting sort

    // check of many bits is needed to represent max
    int numBitsMax = 1;
    // Or ceil(log2(max))
    while (max >= (1L << numBitsMax))
      numBitsMax++;

    // find num of positions
    int numOfPositions = numBitsMax / useBits;
    if (numBitsMax % useBits != 0) numOfPositions++; // including the leftover bits

    // save space
    if (numBitsMax < useBits) useBits = numBitsMax;

    // both mask and shift are used to extract the digits
    // shift ensures that the digits extracted will always be
    // below the radix
    int mask = (1 << useBits) - 1;
    int shift = 0;

    // Perform counting sort on each position
    for (int i = 0; i < numOfPositions; i++) {

      countingSort(mask, shift);
      shift += useBits;

      // switching so that a will always be the one we sort
      int[] temp = a;
      a = b;
      b = temp;

    }

    return a;

  }

  void countingSort(int mask, int shift) {

    // Radix: the number of unique digits
    // Or 2^useBits
    // Or 1 << usebits
    int radix = mask + 1;


    // STEP B : count the frequencies of each digit
    int[] digitFrequency = new int[radix];

    for (int num : a) {

      // extract the digit
      int digit = (num >> shift) & mask;

      // increment the counter of that digit
      digitFrequency[digit]++;
    }


    // STEP C : calculate the pointers

    int[] digitPointers = new int[radix];

    for (int i = 0; i < digitFrequency.length - 1; i++) {
      // the startpoint of the previous digit and the frequency of the previous digit
      // is the startpoint of the next digit
      digitPointers[i + 1] = digitPointers[i] + digitFrequency[i];
    }


    // STEP D : place the numbers inside array b

    for (int num : a) {

      // extracting the digit
      int digit = (num >> shift) & mask;

      // getting the pointer for that digit (its index in array b)
      int pointer = digitPointers[digit];

      // placing the number in the correct position in b
      b[pointer] = num;

      // incrementing the pointer
      digitPointers[digit]++;
    }

  }

  public static void main(String[] args) {

    int n = Integer.parseInt(args[0]);
    int seed = Integer.parseInt(args[1]);
    int useBits = Integer.parseInt(args[2]);

    RadixSort rs = new RadixSort(useBits);
    int[] a = Oblig4Precode.generateArray(n, seed);
    a = rs.radixSort(a);


    // a simple check to see if array is sorted
    int[] arraysort = Oblig4Precode.generateArray(n, seed);
    Arrays.sort(arraysort);
    System.out.println("Arrays are equal: " + Arrays.equals(a, arraysort));
  }
}
