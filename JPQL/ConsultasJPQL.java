package JPQL;
import com.mycompany.ormestudiantes.HibernateUtil;
import Entidades.Producto;
import java.util.List;
import org.hibernate.Session;

public class ConsultasJPQL {

    // CONSULTA 1: Buscar productos por categoría.
    public List<Producto> buscarPorCategoria(
            String nombreCategoria, int pagina, int tamanioPagina) {

        try (Session sesion = HibernateUtil
                .getSessionFactory().openSession()) {

            return sesion.createQuery(
                    "SELECT p FROM Producto p "
                    + "WHERE p.categoria.nombre = :categoria "
                    + "ORDER BY p.nombre ASC",
                    Producto.class)
                    .setParameter("categoria", nombreCategoria)
                    .setFirstResult((pagina - 1) * tamanioPagina)
                    .setMaxResults(tamanioPagina)
                    .getResultList();
        }
    }

    // CONSULTA 2: Buscar productos con stock bajo.
    public List<Producto> buscarStockBajo(
            int limiteStock, int pagina, int tamanioPagina) {

        try (Session sesion = HibernateUtil
                .getSessionFactory().openSession()) {

            return sesion.createQuery(
                    "SELECT p FROM Producto p "
                    + "WHERE p.stock <= :limite "
                    + "ORDER BY p.stock ASC, p.nombre ASC",
                    Producto.class)
                    .setParameter("limite", limiteStock)
                    .setFirstResult((pagina - 1) * tamanioPagina)
                    .setMaxResults(tamanioPagina)
                    .getResultList();
        }
    }
    // CONSULTA 3: Buscar productos por rango de precios.
    public List<Producto> buscarPorRangoPrecio(
            double precioMinimo, double precioMaximo,
            int pagina, int tamanioPagina) {

        try (Session sesion = HibernateUtil
                .getSessionFactory().openSession()) {

            return sesion.createQuery(
                    "SELECT p FROM Producto p "
                    + "WHERE p.precio BETWEEN :minimo AND :maximo "
                    + "ORDER BY p.precio DESC",
                    Producto.class)
                    .setParameter("minimo", precioMinimo)
                    .setParameter("maximo", precioMaximo)
                    .setFirstResult((pagina - 1) * tamanioPagina)
                    .setMaxResults(tamanioPagina)
                    .getResultList();
        }
    }
}

