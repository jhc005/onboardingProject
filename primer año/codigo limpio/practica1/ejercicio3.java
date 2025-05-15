public class ejercicio3 {
    public static void main(String[] args) {
        double grossSalary = 30000.0;
        double netSalary;
        final double taxRate10=0.9;
        final double taxRate15=0.85;
        final double taxRate25=0.75;        
        

        if (grossSalary <= 12000) {
            netSalary = grossSalary * taxRate10;  
        } else if (grossSalary <= 25000) {
            netSalary = grossSalary * taxRate15;  
        } else {
            netSalary = grossSalary * taxRate25;  
        
        System.out.println("Net salary: " + netSalary + " euros");
    }
    }
}
