package com.mycompany.ormestudiantes;
import Entidades.Categoria;
import Entidades.Sucursal;
import javax.swing.SwingUtilities;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main { 
    public static void main(String[] args) {

        // Registrar datos iniciales en la base de datos.
        inicializarDatos();

        // Abrir la ventana principal.
        SwingUtilities.invokeLater(() -> {
            new ProductoForm().setVisible(true);
        });
    }
    private static void inicializarDatos() {

        try (Session sesion = HibernateUtil
                .getSessionFactory().openSession()) {

            Transaction tx = sesion.beginTransaction();

            try {
                Long totalCategorias = sesion.createQuery(
                        "SELECT COUNT(c) FROM Categoria c",
                        Long.class).getSingleResult();

                if (totalCategorias == 0) {

                    sesion.persist(new Categoria(
                            "Bebidas", "Productos para beber"));

                    sesion.persist(new Categoria(
                            "Snacks", "Alimentos y aperitivos"));

                    sesion.persist(new Categoria(
                            "Limpieza", "Productos de limpieza"));

                    sesion.persist(new Categoria(
                            "Lácteos", "Productos derivados de la leche"));

                    sesion.persist(new Categoria(
                            "Cuidado personal", "Productos de higiene"));
                }
                Long totalSucursales = sesion.createQuery(
                        "SELECT COUNT(s) FROM Sucursal s",
                        Long.class).getSingleResult();

                if (totalSucursales == 0) {

                    sesion.persist(new Sucursal(
                            "Sucursal Centro"));

                    sesion.persist(new Sucursal(
                            "Sucursal Norte"));

                    sesion.persist(new Sucursal(
                            "Sucursal Sur"));
                }
                tx.commit();
            } catch (Exception ex) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw ex;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
