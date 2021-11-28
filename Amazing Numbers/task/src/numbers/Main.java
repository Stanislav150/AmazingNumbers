package numbers;

import java.util.*;

public class Main {
    final static Scanner scanner = new Scanner(System.in);
    private static long numOne;
    private static long numTwo;
    private static boolean negativeProperty = false;
    private static String incorrectPairs = "";
    private static final List<String> listOfPropertys = new ArrayList<>();

    enum Properties {
        EVEN, ODD, BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY,
        SQUARE, SUNNY, JUMPING, HAPPY, SAD
    }

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
            } else if (inputArray.length >= 3) {
                int result = CheckingNumbers(input);
                if (result == 0) repeat = false;
                else if (result == 1) defineProperties(numOne, numTwo, listOfPropertys);
            }
        }
        System.out.println("Goodbye!");
    }

    /**
     * Display the instructions
     */
    public static void printInstructions() {
        System.out.println("Supported requests: " + "\n"
                + "- enter a natural number to know its properties;" + "\n"
                + "- enter two natural numbers to obtain the properties of the list:" + "\n"
                + "\t" + "* the first parameter represents a starting number;" + "\n"
                + "\t" + "* the second parameters shows how many consecutive numbers are to be processed;" + "\n"
                + "- two natural numbers and a properties to search for;" + "\n"
                + "- separate the parameters with one space;" + "\n"
                + "- a property preceded by minus must not be present in numbers;" + "\n"
                + "- enter 0 to exit." + "\n");
        System.out.print("Enter a request: ");
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
                System.out.println("The second parameter should be a natural number.");
                System.out.print("Enter a request: ");
                return -1;
            }
            if (numTwo < 0) {
                System.out.println("The second parameter should be a natural number.");
                System.out.print("Enter a request: ");
                return -1;
            } else if (numTwo == 0) {
                return 0;
            }
        }
        //Check the correctness of entering the properties of numbers
        BitSet bitSet = new BitSet(16);
        if (nums.length > 2) {
            for (int i = 2; i < nums.length; i++) {
                StringBuilder property = new StringBuilder();
                property.append(nums[i].toUpperCase(Locale.ROOT));
                if (property.charAt(0) == '-') {
                    negativeProperty = true;
                    property.delete(0, 1);
                }
                try {
                    listOfPropertys.add(Properties.valueOf(property.toString()).toString());
                } catch (IllegalArgumentException | NullPointerException exception) {
                    bitSet.set(i - 2);
                }
                if ((negativeProperty) && (bitSet.isEmpty())) {
                    listOfPropertys.set(i - 2, "-" + listOfPropertys.get(i - 2));
                    negativeProperty = false;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        String temp = "";
        if (!bitSet.isEmpty()) {
            for (int i = 0; i <= nums.length - 3; i++) {
                if (bitSet.get(i)) temp = nums[i + 2] + ", ";
                sb.append(temp);
            }
            sb.delete(sb.length() - 2, sb.length());
            if (bitSet.cardinality() == 1)
                System.out.printf("The property [%s] is wrong.\n", sb.toString().toUpperCase(Locale.ROOT));
            else System.out.printf("The properties [%s] are wrong.\n", sb.toString().toUpperCase(Locale.ROOT));
            System.out.println("Available properties: [EVEN, ODD, BUZZ, DUCK, " +
                    "PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY, JUMPING, HAPPY, SAD]");
            System.out.println();
            System.out.print("Enter a request: ");
            bitSet.clear();
            return -1;
        }
        // Check properties for compatibility. Incompatible pairs Even and Odd, Duck and Spy,
        // Sunny and Square, Happy and Sad
        if (checkPairs(listOfPropertys).length() > 0) {
            System.out.printf("The request contains mutually exclusive properties: [%s]%n", incorrectPairs);
            System.out.println("There are no numbers with these properties.");
            System.out.println();
            System.out.print("Enter a request: ");
            listOfPropertys.clear();
            incorrectPairs = "";
            return -1;
        }
        return 1;
    }

    /**
     * @param list - list of all parameters entered by the user
     */
    public static String checkPairs(List<String> list) {
        //Option 1 5 -spy -spy is not implemented
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 1; j < list.size(); j++) {
                if (((list.get(i).startsWith("-") && (list.get(i).substring(1).equals(list.get(j)))))
                        || ((list.get(j).startsWith("-") && (list.get(j).substring(1).equals(list.get(i)))))) {
                    incorrectPairs = list.get(i) + ", " + list.get(j);
                }
            }
        }
        // We check the match of the pairs, but if they were with a minus, then we add it for printing.
        if ((list.contains("EVEN")) && (list.contains("ODD"))) incorrectPairs = "EVEN, ODD";
        else if ((list.contains("-EVEN")) && (list.contains("-ODD"))) incorrectPairs = "-EVEN, -ODD";
        else if ((list.contains("DUCK")) && (list.contains("SPY"))) incorrectPairs = "DUCK, SPY";
        else if ((list.contains("-DUCK")) && (list.contains("-SPY"))) incorrectPairs = "-DUCK, -SPY";
        else if ((list.contains("SUNNY")) && (list.contains("SQUARE"))) incorrectPairs = "SUNNY, SQUARE";
        else if ((list.contains("-SUNNY")) && (list.contains("-SQUARE"))) incorrectPairs = "-SUNNY, -SQUARE";
        else if ((list.contains("HAPPY")) && (list.contains("SAD"))) incorrectPairs = "HAPPY, SAD";
        else if ((list.contains("-HAPPY")) && (list.contains("-SAD"))) incorrectPairs = "HAPPY, SAD";

        return incorrectPairs;
    }

    /**
     * Defining and print the properties of a single number
     */
    public static void propertiesSingleNumber() {
        System.out.println("Properties of " + numOne + "\n"
                + (checkEven(numOne) ? "        even: true" : "        even: false") + "\n"
                + (checkOdd(numOne) ? "         odd: true" : "         odd: false") + "\n"
                + (checkBuzz(numOne) ? "        buzz: true" : "        buzz: false") + "\n"
                + (checkDuck(numOne) ? "        duck: true" : "        duck: false") + "\n"
                + (checkPalindrome(numOne) ? " palindromic: true" : " palindromic: false") + "\n"
                + (checkGapful(numOne) ? "      gapful: true" : "      gapful: false") + "\n"
                + (checkSpy(numOne) ? "       spy: true" : "       spy: false") + "\n"
                + (checkSquare(numOne) ? "    square: true" : "     square: false") + "\n"
                + (checkSunny(numOne) ? "     sunny: true" : "      sunny: false") + "\n"
                + (checkJumping(numOne) ? "    jumping: true" : "    jumping: false") + "\n"
                + (checkHappy(numOne) ? "       happy: true" : "      happy: false") + "\n"
                + (checkSad(numOne) ? "    sad: true" : "    sad: false") + "\n");
        System.out.print("Enter a request: ");
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
                    + (checkEven(num) ? "even" : "")
                    + (checkOdd(num) ? "odd" : "")
                    + (checkBuzz(num) ? ", buzz" : "")
                    + (checkDuck(num) ? ", duck" : "")
                    + (checkPalindrome(num) ? ", palindromic" : "")
                    + (checkGapful(num) ? ", gapful" : "")
                    + (checkSpy(num) ? ", spy" : "")
                    + (checkSquare(num) ? ", square" : "")
                    + (checkSunny(num) ? ", sunny" : "")
                    + (checkJumping(num) ? ",  jumping" : "")
                    + (checkHappy(num) ? ",  happy" : "")
                    + (checkSad(num) ? ",  sad" : "")
            );
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
    public static void defineProperties(long startNum, long repeatNum, List<String> properties) {
        BitSet bitSet = new BitSet(8);
        List<Long> listNums = new ArrayList<>();
        long checknum = startNum;
        int flagAddingNumber = 0;
        boolean skipNumber = false;
        for (String p : properties) if (!p.startsWith("-")) flagAddingNumber++;
        while (listNums.size() < repeatNum) {
            for (int i = 0; i < properties.size(); i++) {
                switch (properties.get(i)) {
                    case "BUZZ":
                        if (checkBuzz(checknum)) bitSet.set(i);
                        break;
                    case "DUCK":
                        if (checkDuck(checknum)) bitSet.set(i);
                        break;
                    case "PALINDROMIC":
                        if (checkPalindrome(checknum)) bitSet.set(i);
                        break;
                    case "GAPFUL":
                        if (checkGapful(checknum)) bitSet.set(i);
                        break;
                    case "SPY":
                        if (checkSpy(checknum)) bitSet.set(i);
                        break;
                    case "EVEN":
                        if (checkEven(checknum)) bitSet.set(i);
                        break;
                    case "ODD":
                        if (checkOdd(checknum)) bitSet.set(i);
                        break;
                    case "SQUARE":
                        if (checkSquare(checknum)) bitSet.set(i);
                        break;
                    case "SUNNY":
                        if (checkSunny(checknum)) bitSet.set(i);
                        break;
                    case "JUMPING":
                        if (checkJumping(checknum)) bitSet.set(i);
                        break;
                    case "HAPPY":
                        if (checkHappy(checknum)) bitSet.set(i);
                        break;
                    case "SAD":
                        if (checkSad(checknum)) {
                            bitSet.set(i);
                            break;
                        }
                    default:
                        if (properties.get(i).startsWith("-")) {
                            skipNumber = checkNegativeProperties(checknum, properties.get(i));
                        }

                }
            }
            if ((bitSet.cardinality() == flagAddingNumber) && !skipNumber) listNums.add(checknum);
            bitSet.clear();
            skipNumber = false;
            checknum += 1;
        }
        for (Long num : listNums) {
            System.out.println(num + " is "
                    + (checkEven(num) ? "even" : "")
                    + (checkOdd(num) ? "odd" : "")
                    + (checkBuzz(num) ? ", buzz" : "")
                    + (checkDuck(num) ? ", duck" : "")
                    + (checkPalindrome(num) ? ", palindromic" : "")
                    + (checkGapful(num) ? ", gapful" : "")
                    + (checkSpy(num) ? ", spy" : "")
                    + (checkSquare(num) ? ", square" : "")
                    + (checkSunny(num) ? ", sunny" : "")
                    + (checkJumping(num) ? ", jumping" : "")
                    + (checkHappy(num) ? ", happy" : "")
                    + (checkSad(num) ? ", sad" : ""));
        }
        listOfPropertys.clear();
        listNums.clear();
        System.out.println();
        System.out.print("Enter a request: ");
    }

    /**
     * Check whether the number has a negative property
     *
     * @param num      numver
     * @param property the property of a number, must begin with a minus
     * @return true if the number has a negative property
     */
    public static boolean checkNegativeProperties(long num, String property) {
        String test = property.substring(1);
        switch (test) {
            case "BUZZ":
                if (checkBuzz(num)) return true;
                break;
            case "DUCK":
                if (checkDuck(num)) return true;
                break;
            case "PALINDROMIC":
                if (checkPalindrome(num)) return true;
                break;
            case "GAPFUL":
                if (checkGapful(num)) return true;
                break;
            case "SPY":
                if (checkSpy(num)) return true;
                break;
            case "EVEN":
                if (checkEven(num)) return true;
                break;
            case "ODD":
                if (checkOdd(num)) return true;
                break;
            case "SQUARE":
                if (checkSquare(num)) return true;
                break;
            case "SUNNY":
                if (checkSunny(num)) return true;
                break;
            case "JUMPING":
                if (checkJumping(num)) return true;
                break;
            case "HAPPY":
                if (checkHappy(num)) return true;
                break;
            case "SAD":
                if (checkSad(num)) return true;
                break;
        }
        return false;
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

    public static boolean checkEven(long num) {
        return num % 2 == 0;
    }

    public static boolean checkOdd(long num) {
        return !(num % 2 == 0);
    }

    /**
     * A number is called a buzz number if it is
     * divisible by 7, or it ends with 7
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
     * A number is called a spy number if the sum of all
     * the digits is equal to the product of the digits.
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

    /**
     * A number is a Jumping number if the adjacent digits
     * inside the number differ by 1. The difference
     * between 9 and 0 is not considered as 1.
     * Single-digit numbers are considered Jumping numbers.
     *
     * @param num a natural number entered by the user
     * @return true if the num is the jumping number
     */
    public static boolean checkJumping(long num) {
        if (num < 10) return true;
        String str = Long.toString(num);
        long[] array = new long[str.length()];
        for (int i = str.length() - 1; i >= 0; i--) {
            array[i] = (int) (num % 10);
            num /= 10;
        }
        int count = 0;
        for (int i = 0; i < array.length - 1; i++) {
            if (Math.abs(array[i + 1] - array[i]) == 1) count++;
        }
        return (count == array.length - 1);
    }

    /**
     * In number theory, a happy number is a number that reaches
     * 1 after a sequence during which the number is replaced
     * by the sum of each digit squares.
     *
     * @param num a natural number entered by the user
     * @return true if the num is the happy number
     */
    public static boolean checkHappy(long num) {
        while (true) {
            String str = Long.toString(num);
            long[] array = new long[str.length()];
            for (int i = str.length() - 1; i >= 0; i--) {
                array[i] = (int) (num % 10);
                num /= 10;
            }
            long sumSquares = 0;
            for (long l : array) {
                sumSquares += Math.pow(l, 2);
            }
            if (sumSquares == 1) return true;
            else if (sumSquares == 4) return false;
            else num = sumSquares;
        }
    }

    public static boolean checkSad(long num) {
        return !checkHappy(num);
    }
}

