package numbers;

import java.util.*;

public class Main {
    final static Scanner scanner = new Scanner(System.in);
    private static long numOne;
    private static long numTwo;

    private enum Properties {EVEN, ODD, BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY}

    private static Properties propertyNumsOne;
    private static Properties propertyNumsTwo;

    public static void main(String[] args) {
        System.out.println("Welcome to Amazing Numbers!");
        printInstructions();
        boolean repeat = true;
        while (repeat) {
            numOne = -1;
            numTwo = -1;
            String input = scanner.nextLine();
            String[] inputArray = input.split(" ");
            //If a user enters an empty request, print the instructions
            if (input.isEmpty()) printInstructions();
                //If one number is entered, calculate and print the properties of this number
            else if (inputArray.length == 1) {
                int result = CheckingNumbers(input);
                if (result == 0) repeat = false;
                else if (result == 1) propertiesSingleNumber();
            } else if (inputArray.length == 2) {
                int result = CheckingNumbers(input);
                if (result == 0) repeat = false;
                else if (result == 1) propertiesListNumbers(numOne, numTwo);
            } else if (inputArray.length == 3) {
                int result = CheckingNumbers(input);
                if (result == 0) repeat = false;
                else if (result == 1) numbersSpecifiedProperty(numOne, numTwo, propertyNumsOne);
            } else if (inputArray.length == 4) {
                int result = CheckingNumbers(input);
                if (result == 0) repeat = false;
                else if (result == 1) numbersSpecifiedProperty(numOne, numTwo, propertyNumsOne, propertyNumsTwo);
            }
        }
        System.out.println("Goodbye!");
    }

    /**
     * Display the instructions
     */
    public static void printInstructions() {
        System.out.println("Supported requests: "
                + "- enter a natural number to know its properties;" + "\n"
                + "- enter two natural numbers to obtain the properties of the list:" + "\n"
                + "\t" + "* the first parameter represents a starting number;" + "\n"
                + "\t" + "* the second parameters show how many consecutive numbers are to be processed;" + "\n"
                + "- two natural numbers and a property to search for;" + "\n"
                + "- two natural numbers and two  properties to search for;" + "\n"
                +  "- separate the parameters with one space;" + "\n"
                + "- enter 0 to exit." + "\n\n"
                + "Enter a request: ");
    }

    /**
     * the function of determining the validity of the entered numbers.
     * Whether they are natural.
     *
     * @param input - the string entered by the user
     * @return -1, if the number is not natural, 0 - exit from the program, 1 - natural number
     */
    public static int CheckingNumbers(String input) {
        String[] nums = input.split(" ");
        try {
            numOne = Long.parseLong(nums[0]);
        } catch (NumberFormatException nfe) {
            System.out.println("The first parameter should be a natural number or zero.");
            System.out.print("Enter a request: ");
            return -1;
        }
        if (numOne < 0) {
            System.out.println("The first parameter should be a natural number or zero.");
            System.out.print("Enter a request: ");
            return -1;
        } else if (numOne == 0) {
            return 0;
        }
        if (nums.length >= 2) {
            try {
                numTwo = Long.parseLong(nums[1]);
            } catch (NumberFormatException nfe) {
                System.out.println("The second parameter should be a natural number or zero.");
                System.out.print("Enter a request: ");
                return -1;
            }
            if (numTwo < 0) {
                System.out.println("The second parameter should be a natural number or zero.");
                System.out.print("Enter a request: ");
                return -1;
            } else if (numTwo == 0) {
                return 0;
            }
        }
        if (nums.length == 3) {
            String property = nums[2].toUpperCase(Locale.ROOT);
            try {
                propertyNumsOne = Properties.valueOf(property);
            } catch (IllegalArgumentException | NullPointerException exception) {
                System.out.printf("The property [%s] is wrong.\n", property.toUpperCase(Locale.ROOT));
                System.out.print("Available properties: [BUZZ, DUCK, PALINDROMIC, " +
                        "GAPFUL, SPY, EVEN, ODD, SQUARE, SUNNY]");
                System.out.println();
                System.out.print("Enter a request: ");
                return -1;
            }
        }
        // тут должно быть три варианта первое число не правильно, второе или оба
        BitSet bs = new BitSet(8);
        if (nums.length == 4) {

            String propertyOne = nums[2].toUpperCase(Locale.ROOT);
            try {
                propertyNumsOne = Properties.valueOf(propertyOne);
            } catch (IllegalArgumentException | NullPointerException exception) {
                bs.set(0);
            }

            String propertyTwo = nums[3].toUpperCase(Locale.ROOT);
            try {
                propertyNumsTwo = Properties.valueOf(propertyTwo);
            } catch (IllegalArgumentException | NullPointerException exception) {
                bs.set(1);
            }

            if (bs.get(0) && bs.get(1)) {
                System.out.printf("The properties [%s, %s] are wrong.\n",
                        propertyOne.toUpperCase(Locale.ROOT), propertyTwo.toUpperCase(Locale.ROOT));
                System.out.println("Available properties: [EVEN, ODD, BUZZ, DUCK, " +
                        "PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY]");
                System.out.println();
                System.out.print("Enter a request: ");
                bs.clear();
                return -1;
            } else if (bs.get(0) && !bs.get(1)) {
                System.out.printf("The property [%s] is wrong.\n", propertyOne.toUpperCase(Locale.ROOT));
                System.out.print("Available properties: [BUZZ, DUCK, PALINDROMIC, " +
                        "GAPFUL, SPY, EVEN, ODD, SQUARE, SUNNY]");
                System.out.println();
                System.out.print("Enter a request: ");
                bs.clear();
                return -1;
            } else if (!bs.get(0) && bs.get(1)) {
                System.out.printf("The property [%s] is wrong.\n", propertyTwo.toUpperCase(Locale.ROOT));
                System.out.print("Available properties: [BUZZ, DUCK, PALINDROMIC, " +
                        "GAPFUL, SPY, EVEN, ODD, SQUARE, SUNNY]");
                System.out.println();
                System.out.print("Enter a request: ");
                bs.clear();
                return -1;
            }
        }
        // Incompatible pairs Even and Odd, Duck and Spy, Sunny and Square
        if (((propertyNumsOne == Properties.EVEN) && (propertyNumsTwo == Properties.ODD)) ||
                ((propertyNumsOne == Properties.ODD) && (propertyNumsTwo == Properties.EVEN))) {
            System.out.println("The request contains mutually exclusive properties: [ODD, EVEN]");
            System.out.println("There are no numbers with these properties.");
            System.out.println();
            System.out.print("Enter a request: ");
            return -1;
        } else if (((propertyNumsOne == Properties.DUCK) && (propertyNumsTwo == Properties.SPY)) ||
                ((propertyNumsOne == Properties.SPY) && (propertyNumsTwo == Properties.DUCK))) {
            System.out.println("The request contains mutually exclusive properties: [DUCK, SPY]");
            System.out.println("There are no numbers with these properties.");
            System.out.println();
            System.out.print("Enter a request: ");
            return -1;
        } else if (((propertyNumsOne == Properties.SUNNY) && (propertyNumsTwo == Properties.SQUARE)) ||
                ((propertyNumsOne == Properties.SQUARE) && (propertyNumsTwo == Properties.SUNNY))) {
            System.out.println("The request contains mutually exclusive properties: [SUNNY, SQUARE]");
            System.out.println("There are no numbers with these properties.");
            System.out.println();
            System.out.print("Enter a request: ");
            return -1;
        }


        return 1;

    }

    /**
     * Defining and print the properties of a single number
     */
    public static void propertiesSingleNumber() {
        System.out.println("Properties of " + numOne + "\n"
                + (checkEvenOdd(numOne) ? "        even: true" : "        even: false" + "\n"
                + (checkEvenOdd(numOne) ? "         odd: false" : "         odd: true") + "\n"
                + (checkBuzz(numOne) ? "        buzz: true" : "        buzz: false") + "\n"
                + (checkDuck(numOne) ? "        duck: true" : "        duck: false") + "\n"
                + (checkPalindrome(numOne) ? " palindromic: true" : " palindromic: false") + "\n"
                + (checkGapful(numOne) ? "      gapful: true" : "      gapful: false") + "\n"
                + (checkSpy(numOne) ? "       spy: true" : "       spy: false") + "\n"
                + (checkSquare(numOne) ? "    square: true" : "     square: false") + "\n"
                + (checkSunny(numOne) ? "     sunny: true" : "      sunny: false") + "\n"
                + ("Enter a request: ")));
    }

    /**
     * Prints the properties of numbers in the format
     * 140 is even, buzz, duck, gapful
     * 141 is odd, palindromic
     *
     * @param startNum  - starting number of the list
     * @param repeatNum - number of digits in the list
     */
    public static void propertiesListNumbers(long startNum, long repeatNum) {
        List<Long> nums = new ArrayList<>();
        long bound = startNum + repeatNum;
        for (long l = startNum; l < bound; l++) {
            nums.add(l);
        }
        for (Long num : nums) {
            //140 is even, buzz, duck, gapful
            System.out.println(num + " is "
                    + (checkEvenOdd(num) ? "even" : "odd")
                    + (checkBuzz(num) ? ", buzz" : "")
                    + (checkDuck(num) ? ", duck" : "")
                    + (checkPalindrome(num) ? ", palindromic" : "")
                    + (checkGapful(num) ? ", gapful" : "")
                    + (checkSpy(num) ? ", spy" : "")
                    + (checkSquare(num) ? ", square" : "")
                    + (checkSunny(num) ? ", sunny" : ""));
        }
        System.out.println();
        System.out.print("Enter a request: ");
    }

    /**
     * Searches for numbers that have a certain property
     *
     * @param startNum   - starting number of the list
     * @param repeatNum  - number of digits in the list
     * @param properties - the specified properties
     */
    public static void numbersSpecifiedProperty(long startNum, long repeatNum, Properties... properties) {
        BitSet bitSet = new BitSet(8);
        List<Long> listNums = new ArrayList<>();
        long checknum = startNum;
        while (listNums.size() < repeatNum) {
            for (int i = 0; i < properties.length; i++) {
                switch (properties[i]) {
                    case BUZZ:
                        if (checkBuzz(checknum)) bitSet.set(i);
                        break;
                    case DUCK:
                        if (checkDuck(checknum)) bitSet.set(i);
                        break;
                    case PALINDROMIC:
                        if (checkPalindrome(checknum)) bitSet.set(i);
                        break;
                    case GAPFUL:
                        if (checkGapful(checknum)) bitSet.set(i);
                        break;
                    case SPY:
                        if (checkSpy(checknum)) bitSet.set(i);
                        break;
                    case EVEN:
                        if (checkEvenOdd(checknum)) bitSet.set(i);
                        break;
                    case ODD:
                        if (!checkEvenOdd(checknum)) bitSet.set(i);
                        break;
                    case SQUARE:
                        if (checkSquare(checknum)) bitSet.set(i);
                        break;
                    case SUNNY:
                        if (checkSunny(checknum)) bitSet.set(i);
                        break;
                    default:
                        System.out.println("I can't process this property");
                }
            }
            if (bitSet.cardinality() == properties.length) listNums.add(checknum);
            bitSet.clear();
            checknum += 1;
        }
        for (Long num : listNums) {
            System.out.println(num + " is "
                    + (checkEvenOdd(num) ? "even" : "odd")
                    + (checkBuzz(num) ? ", buzz" : "")
                    + (checkDuck(num) ? ", duck" : "")
                    + (checkPalindrome(num) ? ", palindromic" : "")
                    + (checkGapful(num) ? ", gapful" : "")
                    + (checkSpy(num) ? ", spy" : "")
                    + (checkSquare(num) ? ", square" : "")
                    + (checkSunny(num) ? ", sunny" : ""));
        }
        System.out.println();
        System.out.print("Enter a request: ");
    }

    /**
     * We define the duck number, it must contain zero, but not start from zero
     *
     * @param num, a natural number entered by the user
     * @return true if num is the duck number
     */
    public static boolean checkDuck(long num) {
        String strNum = Long.toString(num);
        return strNum.contains("0") && !strNum.startsWith("0");
    }

    public static boolean checkEvenOdd(long num) {
        return num % 2 == 0;
    }

    /**
     * A number is called a buzz number if it is
     * divisible by 7 or it ends with 7
     *
     * @param num num a natural number entered by the user
     * @return true if num is the buzz number
     */
    public static boolean checkBuzz(long num) {
        return (num % 10 == 7) || (num % 7 == 0);
    }

    public static boolean checkPalindrome(long num) {
        String strNum = Long.toString(num);
        StringBuilder sb = new StringBuilder(strNum);
        sb.reverse();
        return (strNum.equals(sb.toString()));
    }

    /**
     * Numbers   (positive integers expressed in base ten)
     * that are (evenly) divisible by the number formed by the first
     * and last digit are known as gapful numbers.
     *
     * @param num a natural number entered by the user
     * @return true if num is the gupful number
     */
    public static boolean checkGapful(long num) {
        String strNum = Long.toString(num);
        if (strNum.length() < 3) return false;
        char theFirstChar = strNum.charAt(0);  // 'H' has the index 0
        char theLastChar = strNum.charAt(strNum.length() - 1);
        String concatenation = new String(new char[]{theFirstChar, theLastChar});
        int divider = Integer.parseInt(concatenation.trim());
        return num % divider == 0;
    }

    /**
     * A number is said to be Spy if the sum of all digits
     * is equal to the product of all digits.
     *
     * @param num - a natural number entered by the user
     * @return true if num is the spy number
     */
    public static boolean checkSpy(long num) {
        String str = Long.toString(num);
        int[] array = new int[str.length()];
        for (int i = str.length() - 1; i >= 0; i--) {
            array[i] = (int) (num % 10);
            num /= 10;
        }
        int sum = 0;
        int product = 1;
        for (int i : array) {
            sum += i;
            product *= i;
        }
        return sum == product;
    }

    /**
     * Checking that the number has a square root
     *
     * @param num a natural number entered by the user
     * @return true if the number has a square root
     */
    public static boolean checkSquare(long num) {
        return Math.sqrt(num) % 1 == 0;
    }

    /**
     * N is a sunny number if N+1 is a perfect square number.
     *
     * @param num a natural number entered by the user
     * @return true if the num is the sunny number
     */
    public static boolean checkSunny(long num) {
        return Math.sqrt(num + 1) % 1 == 0;
    }
}

