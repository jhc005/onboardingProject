public class ejercicio4 {

    public static void main(String[] args) {
        imprimirAreaRectangulo(1, 5.0, 3.0);
        imprimirAreaRectangulo(2, 7.0, 2.0);
        imprimirAreaRectangulo(3, 10.0, 4.0);
    }
    private static double calculateArea(double ancho, double alto) {
        return ancho * alto;
    }
    private static void imprimirAreaRectangulo(int numero, double ancho, double alto) {
        double area = calculateArea(ancho, alto);
        System.out.println("Área del rectángulo " + numero + ": " + area);
    }

    
}

