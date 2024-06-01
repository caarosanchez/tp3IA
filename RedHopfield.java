import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RedHopfield {
    private int numNeuronas;
    private double[][] pesos;

    public RedHopfield(int numNeuronas) {
        this.numNeuronas = numNeuronas;
        this.pesos = new double[numNeuronas][numNeuronas];
    }

    // Método de entrenamiento
    public void entrenar(double[][] patrones) {
        for (double[] patron : patrones) {
            for (int i = 0; i < numNeuronas; i++) {
                for (int j = 0; j < numNeuronas; j++) {
                    if (i != j) {
                        pesos[i][j] += patron[i] * patron[j];
                    }
                }
            }
        }
    }

    // Método de recuperación
    public double[] recuperar(double[] entrada) {
        double[] estado = entrada.clone();
        boolean estable;

        do {
            estable = true;
            for (int i = 0; i < numNeuronas; i++) {
                double suma = 0.0;
                for (int j = 0; j < numNeuronas; j++) {
                    suma += pesos[i][j] * estado[j];
                }
                double nuevoEstado = suma >= 0 ? 1.0 : -1.0;
                if (nuevoEstado != estado[i]) {
                    estable = false;
                }
                estado[i] = nuevoEstado;
            }
        } while (!estable);

        return estado;
    }

    // Convertir imagen a matriz binaria
    public static double[] convertirImagenABinario(String rutaImagen) throws IOException {
        BufferedImage imagen = ImageIO.read(new File(rutaImagen));
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        double[] matrizBinaria = new double[ancho * alto];

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int color = imagen.getRGB(x, y);
                int gris = (color & 0xFF);
                matrizBinaria[y * ancho + x] = (gris > 127) ? 1.0 : -1.0;
            }
        }

        return matrizBinaria;
    }

    // Método principal para ejecutar el prototipo
    public static void main(String[] args) {
        // Crear una red de Hopfield con 100 neuronas
        RedHopfield redHopfield = new RedHopfield(100);

        // Ejemplos de patrones (simplificados)
        double[][] patrones = {
            // Aro centrado
            {
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1,  1,  1,  1, -1, -1, -1, -1,
                -1, -1,  1, -1, -1, -1,  1, -1, -1, -1,
                -1,  1, -1, -1, -1, -1, -1,  1, -1, -1,
                -1,  1, -1, -1, -1, -1, -1,  1, -1, -1,
                -1,  1, -1, -1, -1, -1, -1,  1, -1, -1,
                -1, -1,  1, -1, -1, -1,  1, -1, -1, -1,
                -1, -1, -1,  1,  1,  1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            },
            // Aro desplazado
            {
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1,  1,  1,  1, -1, -1, -1,
                -1, -1, -1,  1, -1, -1, -1,  1, -1, -1,
                -1, -1,  1, -1, -1, -1, -1, -1,  1, -1,
                -1,  1, -1, -1, -1, -1, -1, -1,  1, -1,
                -1,  1, -1, -1, -1, -1, -1, -1,  1, -1,
                -1, -1,  1, -1, -1, -1, -1,  1, -1, -1,
                -1, -1, -1,  1,  1,  1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            }
        };

        // Entrenar la red
        redHopfield.entrenar(patrones);

        try {
            // Convertir una imagen ruidosa a matriz binaria
            double[] entradaRuidosa = convertirImagenABinario("C:\\Users\\Caro\\OneDrive\\Escritorio\\motorruido.jpg");

            // Recuperar el patrón
            double[] resultado = redHopfield.recuperar(entradaRuidosa);

            // Imprimir el resultado
            System.out.println("Patrón recuperado:");
            for (int i = 0; i < 100; i++) {
                System.out.print((resultado[i] == 1 ? "1" : "0") + " ");
                if ((i + 1) % 10 == 0) System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
