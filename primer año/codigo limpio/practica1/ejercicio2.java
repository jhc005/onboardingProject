public class ejercicio2{
    public static void main(String[] args) {
        final String firstName = "Anna";
        final String lastName = "Garcia";
        int age = 28;
        final boolean student = true;
        
        // Display user information
        System.out.println("User information:");
        System.out.println("First name: " + firstName);
        System.out.println("Last name: " + lastName);
        System.out.println("Age: " + age);
        System.out.println("Is student: " + (student ? "Yes" : "No"));
        
        // Check if user is an adult
        final int legalAge=18;
        System.out.println(age >= legalAge ? "Is an adult" : "Is a minor");
        
        // Calculate price with discount if student
        double basePrice = 100.0;
        double discountRate= 0.8;
        double price;
        
        System.out.println(student ?price=basePrice*discountRate:basePrice);
        System.out.println("Price to pay: " + price + " euros");
    }
}