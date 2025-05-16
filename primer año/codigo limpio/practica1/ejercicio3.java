public class ejercicio3 {
    public static void main(String[] args) {
        double grossSalary = 30000.0;
        double netSalary = calculateNetSalary(grossSalary);

        System.out.println("Net salary: " + netSalary + " euros");
    }

    private static double calculateNetSalary(double grossSalary) {
        final double LOW_RATE = 0.90;
        final double MID_RATE = 0.85;
        final double HIGH_RATE = 0.75;

        if (grossSalary <= 12000) {
            return grossSalary * LOW_RATE;
        } else if (grossSalary <= 25000) {
            return grossSalary * MID_RATE;
        } else {
            return grossSalary * HIGH_RATE;
        }
    }
}

