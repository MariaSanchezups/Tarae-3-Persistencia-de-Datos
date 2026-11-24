package com.mycompany.ormestudiantes;
import Entidades.DetallePedido;
import Entidades.Pedido;
import Entidades.Producto;
import Entidades.Categoria;
import Entidades.Sucursal;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.util.List;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class ProductoForm extends JFrame {


    // COMPONENTES DE PRODUCTOS

    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JComboBox<Categoria> cmbCategoria = new JComboBox<>();
    private final JTextField txtPrecio = new JTextField();
    private final JTextField txtStock = new JTextField();
    private final JComboBox<Sucursal> cmbSucursal = new JComboBox<>();
    private final JTextField txtBuscar = new JTextField(15);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Código", "Nombre", "Categoría",
                "Precio", "Stock", "Sucursal"}, 0) {

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    private final JTable tabla = new JTable(modelo);

    // COMPONENTES DE PEDIDOS
    private final JComboBox<Producto> cmbProductoPedido =
            new JComboBox<>();

    private final JTextField txtCantidadPedido = new JTextField();
    private final JTextField txtSubtotalPedido = new JTextField();
    private final JTextField txtTotalPedido = new JTextField();

    private final DefaultTableModel modeloDetalles =
            new DefaultTableModel(
                    new String[]{"Producto", "Precio", "Cantidad", "Subtotal"}, 0) {

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    private final JTable tablaDetalles = new JTable(modeloDetalles);

    // Lista que relaciona las filas con sus productos
    private final List<Producto> productosDetalles = new ArrayList<>();

    // COMPONENTES DEL HISTORIAL

    private final DefaultTableModel modeloHistorial =
            new DefaultTableModel(
                    new String[]{"ID", "Fecha", "Total"}, 0) {

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    private final JTable tablaHistorial = new JTable(modeloHistorial);

    private final DefaultTableModel modeloHistorialDetalles =
            new DefaultTableModel(
                    new String[]{"Producto", "Cantidad", "Subtotal"}, 0) {

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    private final JTable tablaHistorialDetalles =
            new JTable(modeloHistorialDetalles);

    // CONSTRUCTOR

    public ProductoForm() {

        setTitle("TUTI - Gestión de productos y pedidos");
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        crearInterfaz();

        cargarCategorias();
        cargarSucursales();
        cargarProductos();
        cargarProductosPedido();
        cargarHistorialPedidos();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                HibernateUtil.cerrar();
                dispose();
            }
        });
    }

    // INTERFAZ PRINCIPAL

    private void crearInterfaz() {

        JTabbedPane pestanas = new JTabbedPane();

        pestanas.addTab("Productos", crearPanelProductos());

        pestanas.addTab("Pedidos", crearPanelPedidos());

        pestanas.addTab("Historial de pedidos",
                crearPanelHistorial());

        add(pestanas, BorderLayout.CENTER);
    }

    // PANEL DE PRODUCTOS

    private JPanel crearPanelProductos() {

        JPanel panelPrincipal = new JPanel(new BorderLayout(8, 8));

        JPanel formulario = new JPanel(new GridLayout(6, 2, 8, 8));
        formulario.setBorder(
                BorderFactory.createTitledBorder("Datos del producto"));

        formulario.add(new JLabel("Código:"));
        formulario.add(txtCodigo);

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("Categoría:"));
        formulario.add(cmbCategoria);

        formulario.add(new JLabel("Precio ($):"));
        formulario.add(txtPrecio);

        formulario.add(new JLabel("Stock:"));
        formulario.add(txtStock);

        formulario.add(new JLabel("Sucursal:"));
        formulario.add(cmbSucursal);

        JButton btnCrear = new JButton("Crear");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        btnCrear.addActionListener(e -> crearProducto());
        btnActualizar.addActionListener(e -> actualizarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        JPanel botones = new JPanel(new FlowLayout());

        botones.add(btnCrear);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        JButton btnBuscar = new JButton("Buscar");
        JButton btnMostrar = new JButton("Mostrar todos");

        btnBuscar.addActionListener(e -> buscarProductos());
        btnMostrar.addActionListener(e -> cargarProductos());

        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));

        busqueda.add(new JLabel("Buscar por nombre:"));
        busqueda.add(txtBuscar);
        busqueda.add(btnBuscar);
        busqueda.add(btnMostrar);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabla.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && tabla.getSelectedRow() >= 0) {

                int fila = tabla.getSelectedRow();

                txtCodigo.setText(modelo.getValueAt(fila, 1).toString());
                txtNombre.setText(modelo.getValueAt(fila, 2).toString());

                seleccionarCategoriaPorNombre(
                        modelo.getValueAt(fila, 3).toString());

                txtPrecio.setText(modelo.getValueAt(fila, 4).toString());
                txtStock.setText(modelo.getValueAt(fila, 5).toString());

                seleccionarSucursalPorNombre(
                        modelo.getValueAt(fila, 6).toString());
            }
        });

        JPanel superior = new JPanel(new BorderLayout(8, 8));

        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);

        panelPrincipal.add(superior, BorderLayout.NORTH);
        panelPrincipal.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panelPrincipal.add(busqueda, BorderLayout.SOUTH);

        return panelPrincipal;
    }

    // PANEL DE PEDIDOS

    private JPanel crearPanelPedidos() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        panel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Información general
        JPanel formulario = new JPanel(new GridLayout(2, 2, 10, 10));

        formulario.setBorder(
                BorderFactory.createTitledBorder("Información del pedido"));

        formulario.add(new JLabel("Fecha del pedido:"));
        formulario.add(new JLabel(java.time.LocalDate.now().toString()));

        // Datos del detalle
        JPanel datosDetalle = new JPanel(new GridLayout(3, 2, 10, 10));

        datosDetalle.setBorder(
                BorderFactory.createTitledBorder("Detalle del pedido"));

        txtSubtotalPedido.setEditable(false);
        txtTotalPedido.setEditable(false);

        datosDetalle.add(new JLabel("Seleccionar producto:"));
        datosDetalle.add(cmbProductoPedido);

        datosDetalle.add(new JLabel("Cantidad:"));
        datosDetalle.add(txtCantidadPedido);

        datosDetalle.add(new JLabel("Subtotal ($):"));
        datosDetalle.add(txtSubtotalPedido);

        // Botones
        JButton btnAgregarDetalle = new JButton("Agregar detalle");
        JButton btnEliminarDetalle = new JButton("Eliminar detalle");
        JButton btnGuardarPedido = new JButton("Guardar pedido");

        btnAgregarDetalle.addActionListener(e -> agregarDetalle());
        btnEliminarDetalle.addActionListener(e -> eliminarDetalle());
        btnGuardarPedido.addActionListener(e -> guardarPedido());

        JPanel botones = new JPanel(new FlowLayout());

        botones.add(btnAgregarDetalle);
        botones.add(btnEliminarDetalle);
        botones.add(btnGuardarPedido);

        // Tabla
        tablaDetalles.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        JPanel tablaPanel = new JPanel(new BorderLayout(5, 5));

        tablaPanel.setBorder(
                BorderFactory.createTitledBorder("Productos del pedido"));

        tablaPanel.add(
                new JScrollPane(tablaDetalles),
                BorderLayout.CENTER);

        // Total
        JPanel totalPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT));

        totalPanel.add(new JLabel("Total del pedido ($):"));

        txtTotalPedido.setColumns(10);

        totalPanel.add(txtTotalPedido);

        JPanel inferior = new JPanel(new BorderLayout());

        inferior.add(botones, BorderLayout.CENTER);
        inferior.add(totalPanel, BorderLayout.SOUTH);

        JPanel superior = new JPanel(new BorderLayout(10, 10));

        superior.add(formulario, BorderLayout.NORTH);
        superior.add(datosDetalle, BorderLayout.CENTER);

        panel.add(superior, BorderLayout.NORTH);
        panel.add(tablaPanel, BorderLayout.CENTER);
        panel.add(inferior, BorderLayout.SOUTH);

        // Calcular subtotal automáticamente
        cmbProductoPedido.addActionListener(e -> calcularSubtotal());

        txtCantidadPedido.addKeyListener(
                new java.awt.event.KeyAdapter() {

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                calcularSubtotal();
            }
        });

        return panel;
    }
    
// PANEL DEL HISTORIAL

private JPanel crearPanelHistorial() {

    JPanel panel = new JPanel(new BorderLayout(10, 10));

    panel.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15));

    // Tabla de pedidos
    JPanel panelPedidos = new JPanel(new BorderLayout(5, 5));

    panelPedidos.setBorder(
            BorderFactory.createTitledBorder("Pedidos registrados"));

    tablaHistorial.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION);

    panelPedidos.add(
            new JScrollPane(tablaHistorial),
            BorderLayout.CENTER);

    // Tabla de detalles
    JPanel panelDetallesHistorial =
            new JPanel(new BorderLayout(5, 5));

    panelDetallesHistorial.setBorder(
            BorderFactory.createTitledBorder(
                    "Productos del pedido seleccionado"));

    panelDetallesHistorial.add(
            new JScrollPane(tablaHistorialDetalles),
            BorderLayout.CENTER);

    // Botón actualizar historial
    JButton btnActualizarHistorial =
            new JButton("Actualizar historial");

    btnActualizarHistorial.addActionListener(
            e -> cargarHistorialPedidos());

    // Botón eliminar pedido
    JButton btnEliminarPedido =
            new JButton("Eliminar pedido");

    btnEliminarPedido.addActionListener(
            e -> eliminarPedido());

    // Panel de botones
    JPanel panelBoton = new JPanel(
            new FlowLayout(FlowLayout.RIGHT));

    panelBoton.add(btnActualizarHistorial);
    panelBoton.add(btnEliminarPedido);

    // Seleccionar pedido
    tablaHistorial.getSelectionModel()
            .addListSelectionListener(e -> {

        if (!e.getValueIsAdjusting()
                && tablaHistorial.getSelectedRow() >= 0) {

            int fila = tablaHistorial.getSelectedRow();

            Long idPedido = Long.valueOf(
                    modeloHistorial.getValueAt(fila, 0).toString());

            cargarDetallesHistorial(idPedido);
        }
    });

    // Dividir las dos tablas
    JSplitPane division = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            panelPedidos,
            panelDetallesHistorial);

    division.setResizeWeight(0.5);

    panel.add(division, BorderLayout.CENTER);
    panel.add(panelBoton, BorderLayout.SOUTH);

    return panel;
}

    // MÉTODOS DE PRODUCTOS

    private Producto leerFormulario() {

        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty()
                || txtPrecio.getText().trim().isEmpty()
                || txtStock.getText().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Completa todos los campos del producto.");
        }

        double precio = Double.parseDouble(
                txtPrecio.getText().trim());

        int stock = Integer.parseInt(
                txtStock.getText().trim());

        if (precio < 0 || stock < 0) {
            throw new IllegalArgumentException(
                    "El precio y el stock no pueden ser negativos.");
        }

        Categoria categoria =
                (Categoria) cmbCategoria.getSelectedItem();

        Sucursal sucursal =
                (Sucursal) cmbSucursal.getSelectedItem();

        if (categoria == null || sucursal == null) {
            throw new IllegalArgumentException(
                    "Debes seleccionar una categoría y una sucursal.");
        }

        return new Producto(
                codigo, nombre, precio, stock, categoria, sucursal);
    }

    private void crearProducto() {

        try {

            Producto datos = leerFormulario();

            try (Session sesion = HibernateUtil
                    .getSessionFactory().openSession()) {

                Transaction tx = sesion.beginTransaction();

                try {

                    Categoria categoria = sesion.find(
                            Categoria.class,
                            datos.getCategoria().getId());

                    Sucursal sucursal = sesion.find(
                            Sucursal.class,
                            datos.getSucursal().getId());

                    datos.setCategoria(categoria);
                    datos.setSucursal(sucursal);

                    sesion.persist(datos);

                    tx.commit();

                } catch (Exception ex) {

                    if (tx.isActive()) {
                        tx.rollback();
                    }

                    throw ex;
                }
            }

            cargarProductos();
            cargarProductosPedido();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Producto registrado correctamente.");

        } catch (NumberFormatException ex) {

            mensajeError("Ingresa un precio y un stock válidos.");

        } catch (Exception ex) {

            mensajeError(ex.getMessage());
        }
    }

    private void cargarProductos() {

        try (Session sesion = HibernateUtil
                .getSessionFactory().openSession()) {

            List<Producto> productos = sesion.createQuery(
                    "FROM Producto ORDER BY id",
                    Producto.class).getResultList();

            mostrarProductos(productos);

        } catch (Exception ex) {

            mensajeError(ex.getMessage());
        }
    }

    private void buscarProductos() {

        try (Session sesion = HibernateUtil
                .getSessionFactory().openSession()) {

            List<Producto> productos = sesion.createQuery(
                    "FROM Producto WHERE LOWER(nombre) "
                    + "LIKE :nombre ORDER BY id",
                    Producto.class)
                    .setParameter(
                            "nombre",
                            "%" + txtBuscar.getText()
                                    .trim().toLowerCase() + "%")
                    .getResultList();

            mostrarProductos(productos);

        } catch (Exception ex) {

            mensajeError(ex.getMessage());
        }
    }

    private void actualizarProducto() {

        int fila = tabla.getSelectedRow();

        if (fila < 0) {
            mensajeError("Selecciona un producto de la tabla.");
            return;
        }

        try {

            Long id = Long.valueOf(
                    modelo.getValueAt(fila, 0).toString());

            Producto datos = leerFormulario();

            try (Session sesion = HibernateUtil
                    .getSessionFactory().openSession()) {

                Transaction tx = sesion.beginTransaction();

                try {

                    Producto producto = sesion.find(
                            Producto.class, id);

                    if (producto == null) {
                        throw new IllegalArgumentException(
                                "El producto ya no existe.");
                    }

                    Categoria categoria = sesion.find(
                            Categoria.class,
                            datos.getCategoria().getId());

                    Sucursal sucursal = sesion.find(
                            Sucursal.class,
                            datos.getSucursal().getId());

                    producto.setCodigo(datos.getCodigo());
                    producto.setNombre(datos.getNombre());
                    producto.setCategoria(categoria);
                    producto.setPrecio(datos.getPrecio());
                    producto.setStock(datos.getStock());
                    producto.setSucursal(sucursal);

                    tx.commit();

                } catch (Exception ex) {

                    if (tx.isActive()) {
                        tx.rollback();
                    }

                    throw ex;
                }
            }

            cargarProductos();
            cargarProductosPedido();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Producto actualizado correctamente.");

        } catch (NumberFormatException ex) {

            mensajeError("Revisa el precio y el stock.");

        } catch (Exception ex) {

            mensajeError(ex.getMessage());
        }
    }

    private void eliminarProducto() {

        int fila = tabla.getSelectedRow();

        if (fila < 0) {
            mensajeError("Selecciona un producto de la tabla.");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas eliminar el producto seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            Long id = Long.valueOf(
                    modelo.getValueAt(fila, 0).toString());

            try (Session sesion = HibernateUtil
                    .getSessionFactory().openSession()) {

                Transaction tx = sesion.beginTransaction();

                try {

                    Producto producto = sesion.find(
                            Producto.class, id);

                    if (producto != null) {
                        sesion.remove(producto);
                    }

                    tx.commit();

                } catch (Exception ex) {

                    if (tx.isActive()) {
                        tx.rollback();
                    }

                    throw ex;
                }
            }

            cargarProductos();
            cargarProductosPedido();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Producto eliminado correctamente.");

        } catch (Exception ex) {

            mensajeError(ex.getMessage());
        }
    }

    private void mostrarProductos(List<Producto> productos) {

        modelo.setRowCount(0);

        for (Producto p : productos) {

            modelo.addRow(new Object[]{
                p.getId(),
                p.getCodigo(),
                p.getNombre(),
                p.getCategoria() != null
                        ? p.getCategoria().getNombre() : "",
                p.getPrecio(),
                p.getStock(),
                p.getSucursal() != null
                        ? p.getSucursal().getNombre() : ""
            });
        }
    }

    private void seleccionarCategoriaPorNombre(String nombre) {

        for (int i = 0; i < cmbCategoria.getItemCount(); i++) {

            Categoria categoria = cmbCategoria.getItemAt(i);

            if (categoria.getNombre().equals(nombre)) {
                cmbCategoria.setSelectedIndex(i);
                return;
            }
        }

        cmbCategoria.setSelectedIndex(-1);
    }

    private void seleccionarSucursalPorNombre(String nombre) {

        for (int i = 0; i < cmbSucursal.getItemCount(); i++) {

            Sucursal sucursal = cmbSucursal.getItemAt(i);

            if (sucursal.getNombre().equals(nombre)) {
                cmbSucursal.setSelectedIndex(i);
                return;
            }
        }

        cmbSucursal.setSelectedIndex(-1);
    }

    private void limpiarCampos() {

        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");

        cmbCategoria.setSelectedIndex(-1);
        cmbSucursal.setSelectedIndex(-1);

        tabla.clearSelection();
    }
    // CARGAR CATEGORÍAS Y SUCURSALES

    private void cargarCategorias() {

        try (Session sesion = HibernateUtil
                .getSessionFactory().openSession()) {

            List<Categoria> categorias = sesion.createQuery(
                    "FROM Categoria ORDER BY nombre",
                    Categoria.class).getResultList();

            cmbCategoria.removeAllItems();

            for (Categoria c : categorias) {
                cmbCategoria.addItem(c);
            }

        } catch (Exception ex) {

            mensajeError(ex.getMessage());
        }
    }

    private void cargarSucursales() {

        try (Session sesion = HibernateUtil
                .getSessionFactory().openSession()) {

            List<Sucursal> sucursales = sesion.createQuery(
                    "FROM Sucursal ORDER BY nombre",
                    Sucursal.class).getResultList();

            cmbSucursal.removeAllItems();

            for (Sucursal s : sucursales) {
                cmbSucursal.addItem(s);
            }

        } catch (Exception ex) {

            mensajeError(ex.getMessage());
        }
    }

    // CARGAR PRODUCTOS PARA PEDIDOS

    private void cargarProductosPedido() {

        try (Session sesion = HibernateUtil
                .getSessionFactory().openSession()) {

            List<Producto> productos = sesion.createQuery(
                    "FROM Producto ORDER BY nombre",
                    Producto.class).getResultList();

            cmbProductoPedido.removeAllItems();

            for (Producto producto : productos) {
                cmbProductoPedido.addItem(producto);
            }

        } catch (Exception ex) {

            mensajeError(ex.getMessage());
        }
    }

    // MÉTODOS DE PEDIDOS Y DETALLES
    private void calcularSubtotal() {

        Producto producto =
                (Producto) cmbProductoPedido.getSelectedItem();

        String cantidadTexto =
                txtCantidadPedido.getText().trim();

        if (producto == null || cantidadTexto.isEmpty()) {
            txtSubtotalPedido.setText("");
            return;
        }

        try {

            int cantidad = Integer.parseInt(cantidadTexto);

            if (cantidad <= 0) {
                txtSubtotalPedido.setText("");
                return;
            }

            double subtotal = producto.getPrecio() * cantidad;

            txtSubtotalPedido.setText(
                    String.format(
                            java.util.Locale.US,
                            "%.2f",
                            subtotal));

        } catch (NumberFormatException ex) {

            txtSubtotalPedido.setText("");
        }
    }

    private void agregarDetalle() {

        Producto producto =
                (Producto) cmbProductoPedido.getSelectedItem();

        if (producto == null) {
            mensajeError("Selecciona un producto.");
            return;
        }

        try {

            int cantidad = Integer.parseInt(
                    txtCantidadPedido.getText().trim());

            if (cantidad <= 0) {
                mensajeError("La cantidad debe ser mayor que cero.");
                return;
            }

            // Comprobar el stock acumulado del producto
            int cantidadAcumulada = cantidad;

            for (int i = 0; i < productosDetalles.size(); i++) {

                if (productosDetalles.get(i).getId()
                        .equals(producto.getId())) {

                    cantidadAcumulada += Integer.parseInt(
                            modeloDetalles.getValueAt(i, 2).toString());
                }
            }

            if (cantidadAcumulada > producto.getStock()) {
                mensajeError("No hay suficiente stock disponible.");
                return;
            }

            double subtotal = producto.getPrecio() * cantidad;

            modeloDetalles.addRow(new Object[]{
                producto.getNombre(),
                producto.getPrecio(),
                cantidad,
                subtotal
            });

            productosDetalles.add(producto);

            calcularTotalPedido();

            txtCantidadPedido.setText("");
            txtSubtotalPedido.setText("");

        } catch (NumberFormatException ex) {

            mensajeError("Ingresa una cantidad válida.");
        }
    }

    private void calcularTotalPedido() {

        double total = 0;

        for (int i = 0; i < modeloDetalles.getRowCount(); i++) {

            total += Double.parseDouble(
                    modeloDetalles.getValueAt(i, 3).toString());
        }

        txtTotalPedido.setText(
                String.format(
                        java.util.Locale.US,
                        "%.2f",
                        total));
    }

    private void eliminarDetalle() {

        int fila = tablaDetalles.getSelectedRow();

        if (fila < 0) {
            mensajeError("Selecciona un detalle para eliminar.");
            return;
        }

        modeloDetalles.removeRow(fila);
        productosDetalles.remove(fila);

        calcularTotalPedido();
    }

private void guardarPedido() {

    if (modeloDetalles.getRowCount() == 0) {
        mensajeError("Agrega al menos un producto al pedido.");
        return;
    }

    try (Session sesion = HibernateUtil
            .getSessionFactory().openSession()) {

        Transaction tx = sesion.beginTransaction();

        try {

            double total = Double.parseDouble(
                    txtTotalPedido.getText());

            // Crear el pedido principal
            Pedido pedido = new Pedido(
                    java.time.LocalDate.now(), total);

            sesion.persist(pedido);

            // Registrar los productos del pedido
            for (int i = 0; i < modeloDetalles.getRowCount(); i++) {

                Long idProducto = productosDetalles.get(i).getId();

                Producto producto = sesion.find(
                        Producto.class, idProducto);

                if (producto == null) {
                    throw new IllegalArgumentException(
                            "Uno de los productos ya no existe.");
                }

                int cantidad = Integer.parseInt(
                        modeloDetalles.getValueAt(i, 2).toString());

                double subtotal = Double.parseDouble(
                        modeloDetalles.getValueAt(i, 3).toString());

                // Verificar stock acumulado
                int cantidadAcumulada = 0;

                for (int j = 0; j < modeloDetalles.getRowCount(); j++) {

                    if (productosDetalles.get(j).getId()
                            .equals(idProducto)) {

                        cantidadAcumulada += Integer.parseInt(
                                modeloDetalles.getValueAt(j, 2).toString());
                    }
                }

                if (cantidadAcumulada > producto.getStock()) {
                    throw new IllegalArgumentException(
                            "El stock de " + producto.getNombre()
                            + " ya no es suficiente.");
                }

                // Crear y guardar el detalle
                DetallePedido detalle = new DetallePedido(
                        cantidad, subtotal, pedido, producto);

                pedido.getDetalles().add(detalle);

                sesion.persist(detalle);
            }

            // Confirmar el registro del pedido y sus detalles
            tx.commit();

            // Limpiar el formulario
            modeloDetalles.setRowCount(0);
            productosDetalles.clear();

            txtTotalPedido.setText("");
            txtCantidadPedido.setText("");
            txtSubtotalPedido.setText("");

            // Actualizar el historial
            cargarHistorialPedidos();

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido registrado correctamente.");

        } catch (Exception ex) {

            if (tx.isActive()) {
                tx.rollback();
            }

            throw ex;
        }

    } catch (Exception ex) {

        mensajeError(ex.getMessage());
    }
}
    // MÉTODOS DEL HISTORIAL

    private void cargarHistorialPedidos() {

        try (Session sesion = HibernateUtil
                .getSessionFactory().openSession()) {

            List<Pedido> pedidos = sesion.createQuery(
                    "FROM Pedido p ORDER BY p.id DESC",
                    Pedido.class).getResultList();

            modeloHistorial.setRowCount(0);
            modeloHistorialDetalles.setRowCount(0);

            for (Pedido pedido : pedidos) {

                modeloHistorial.addRow(new Object[]{
                    pedido.getId(),
                    pedido.getFecha(),
                    pedido.getTotal()
                });
            }

        } catch (Exception ex) {

            mensajeError(ex.getMessage());
        }
    }

    private void cargarDetallesHistorial(Long idPedido) {

        try (Session sesion = HibernateUtil
                .getSessionFactory().openSession()) {

            List<DetallePedido> detalles = sesion.createQuery(
                    "FROM DetallePedido d "
                    + "JOIN FETCH d.producto "
                    + "WHERE d.pedido.id = :id "
                    + "ORDER BY d.id",
                    DetallePedido.class)
                    .setParameter("id", idPedido)
                    .getResultList();

            modeloHistorialDetalles.setRowCount(0);

            for (DetallePedido detalle : detalles) {

                modeloHistorialDetalles.addRow(new Object[]{
                    detalle.getProducto().getNombre(),
                    detalle.getCantidad(),
                    detalle.getSubtotal()
                });
            }

        } catch (Exception ex) {

            mensajeError(ex.getMessage());
        }
    }
    // MENSAJES

    private void mensajeError(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje == null ? "Ocurrió un error." : mensaje,
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
    }
    
    private void eliminarPedido() {

    int filaSeleccionada = tablaHistorial.getSelectedRow();

    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this,
                "Selecciona un pedido del historial para eliminarlo.");
        return;
    }

    int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Estás segura de que deseas eliminar este pedido?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);

    if (confirmacion != JOptionPane.YES_OPTION) {
        return;
    }

    Long idPedido = Long.valueOf(
            modeloHistorial.getValueAt(filaSeleccionada, 0).toString()
    );

    Transaction transaccion = null;

    try (Session sesion = HibernateUtil.getSessionFactory().openSession()) {

        transaccion = sesion.beginTransaction();

        // Buscar el pedido
        Pedido pedido = sesion.get(Pedido.class, idPedido);

        if (pedido != null) {

            // Buscar todos los detalles asociados al pedido
            List<DetallePedido> detalles = sesion.createQuery(
                    "FROM DetallePedido d WHERE d.pedido.id = :id",
                    DetallePedido.class)
                    .setParameter("id", idPedido)
                    .getResultList();

            // Eliminar primero los detalles
            for (DetallePedido detalle : detalles) {
                sesion.remove(detalle);
            }

            // Después eliminar el pedido
            sesion.remove(pedido);

            transaccion.commit();

            JOptionPane.showMessageDialog(this,
                    "El pedido se eliminó correctamente.");

            cargarHistorialPedidos();

        } else {
            transaccion.rollback();

            JOptionPane.showMessageDialog(this,
                    "No se encontró el pedido seleccionado.");
        }

    } catch (Exception ex) {

        if (transaccion != null && transaccion.isActive()) {
            transaccion.rollback();
        }

        JOptionPane.showMessageDialog(this,
                "Error al eliminar el pedido: " + ex.getMessage());
    }
}
}
