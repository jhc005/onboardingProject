public class ejercicio2 {
    public static void main(String[] args) {
        String firstName = "Anna";
        String lastName = "Garcia";
        int age = 28;
        boolean isStudent = true;
        double basePrice = 100.0;

        printUserInfo(firstName, lastName, age, isStudent);
        printAdultStatus(age);

        double finalPrice = calculateDiscountedPrice(basePrice, isStudent);
        System.out.println("Price to pay: " + finalPrice + " euros");
    }

    private static void printUserInfo(String firstName, String lastName, int age, boolean isStudent) {
        System.out.println("User information:");
        System.out.println("First name: " + firstName);
        System.out.println("Last name: " + lastName);
        System.out.println("Age: " + age);
        System.out.println("Is student: " + (isStudent ? "Yes" : "No"));
    }

    private static void printAdultStatus(int age) {
        System.out.println(age >= 18 ? "Is an adult" : "Is a minor");
    }

    private static double calculateDiscountedPrice(double price, boolean isStudent) {
        final double STUDENT_DISCOUNT = 0.20;
        return isStudent ? price * (1 - STUDENT_DISCOUNT) : price;
    }
}
