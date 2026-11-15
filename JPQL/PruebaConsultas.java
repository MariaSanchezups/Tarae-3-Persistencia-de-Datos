package JPQL;
import JPQL.ConsultasJPQL;
import com.mycompany.ormestudiantes.HibernateUtil;
import Entidades.Producto;
import java.util.List;

public class PruebaConsultas {

    public static void main(String[] args) {

        ConsultasJPQL consultas = new ConsultasJPQL();

        // Primera consulta: productos de la categoría Bebidas.
        System.out.println("PRODUCTOS POR CATEGORIA");

        List<Producto> porCategoria =
                consultas.buscarPorCategoria("Bebidas", 1, 5);

        for (Producto p : porCategoria) {
            System.out.println(
                    p.getNombre() + " - " + p.getCategoria().getNombre());
        }

        // Segunda consulta: productos con stock de 30 o menos.
        System.out.println("PRODUCTOS CON STOCK BAJO");

        List<Producto> stockBajo =
                consultas.buscarStockBajo(30, 1, 5);

        for (Producto p : stockBajo) {
            System.out.println(
                    p.getNombre() + " - Stock: " + p.getStock());
        }

        // Tercera consulta: productos entre 0 y 1 dólar.
        System.out.println("PRODUCTOS POR RANGO DE PRECIO");

        List<Producto> porPrecio =
                consultas.buscarPorRangoPrecio(0, 1, 1, 5);

        for (Producto p : porPrecio) {
            System.out.println(
                    p.getNombre() + " - Precio: $" + p.getPrecio());
        }

        HibernateUtil.cerrar();
    }
}
