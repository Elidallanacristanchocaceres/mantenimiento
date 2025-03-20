package com.mantenimiento;

import java.util.Scanner;
import java.util.List;

import com.mantenimiento.application.usecase.client.ClientUseCase;
import com.mantenimiento.application.usecase.product.ProductUseCase;
import com.mantenimiento.domain.entity.Client;
import com.mantenimiento.domain.entity.Product;
import com.mantenimiento.domain.repository.ClientRespository;
import com.mantenimiento.domain.repository.ProductRepository;
import com.mantenimiento.infrastructure.database.ConnectionDb;
import com.mantenimiento.infrastructure.database.ConnectionDbImpl;
import com.mantenimiento.infrastructure.database.ConnectionFactory;
import com.mantenimiento.infrastructure.persistence.client.ClientRepositoryImpl;
import com.mantenimiento.infrastructure.persistence.product.ProductRepositoryImpl;

public class Main {
    private static ClientUseCase clienteCasoUso;
    
        public static void main(String[] args) {
            // Crear los repositorios con la conexión a la base de datos
            ConnectionDb connectionDb = new ConnectionDbImpl(ConnectionFactory.crearConexion());
            ClientRespository clienteRepositorio = new ClientRepositoryImpl(connectionDb);
            ProductRepository productoRepositorio = new ProductRepositoryImpl(connectionDb);
    
            // Crear los casos de uso con los repositorios
            ClientUseCase clienteCasoUso = new ClientUseCase(clienteRepositorio);
            ProductUseCase productoCasoUso = new ProductUseCase(productoRepositorio);
    
            // Menú principal
            try (Scanner sc = new Scanner(System.in)) {
                int opcionPrincipal;
                do {
                    System.out.println("\n--- Menú Principal ---");
                    System.out.println("1. Clientes");
                    System.out.println("2. Productos");
                    System.out.println("3. Estadísticas");
                    System.out.println("4. Salir");
                    System.out.print("Seleccione una opción: ");
                    opcionPrincipal = sc.nextInt();
                    sc.nextLine();
    
                    switch (opcionPrincipal) {
                        case 1:
                            menuClientes(sc, clienteCasoUso);
                            break;
                        case 2:
                            menuProductos(sc, productoCasoUso);
                            break;
                        case 3:
                            menuEstadisticas(sc, clienteCasoUso, productoCasoUso);
                            break;
                        case 4:
                            System.out.println("Saliendo...");
                            break;
                        default:
                            System.out.println("❌ Opción no válida. Intente de nuevo.");
                            break;
                    }
                } while (opcionPrincipal != 4);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                ConnectionFactory.cerrarConexion();
            }
        }
    
        private static void menuClientes(Scanner sc, ClientUseCase clienteCasoUso) {
            int opcion;
            do {
                System.out.println("\n--- Menú de Clientes ---");
                System.out.println("1. Registrar Cliente");
                System.out.println("2. Buscar Cliente por ID");
                System.out.println("3. Listar Todos los Clientes");
                System.out.println("4. Actualizar Cliente");
                System.out.println("5. Eliminar Cliente");
                System.out.println("6. Filtrar Clientes");
                System.out.println("0. Volver al Menú Principal");
                System.out.print("Seleccione una opción: ");
                opcion = sc.nextInt();
                sc.nextLine();
    
                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese ID del Cliente: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Ingrese Nombre: ");
                        String nombre = sc.nextLine();
                        System.out.print("Ingrese Email: ");
                        String email = sc.nextLine();
                        System.out.print("Ingrese Dirección: ");
                        String direccion = sc.nextLine();
                        System.out.print("Ingrese Teléfono: ");
                        int telefono = sc.nextInt();
    
                        if (email.contains("@gmail.com")) {
                            sc.nextLine();
                            clienteCasoUso.registrarCliente(id, nombre, email, telefono, direccion);
                            System.out.println("✅ Cliente registrado exitosamente.");
                        } else {
                            System.out.println("El correo debe llevar un '@gmail.com'");
                        }
                        break;
    
                    case 2:
                        System.out.print("Ingrese ID del Cliente a buscar: ");
                        int idBuscar = sc.nextInt();
                        sc.nextLine();
    
                        Client cliente = clienteCasoUso.obtenerCliente(idBuscar);
                        if (cliente != null) {
                            System.out.println("Cliente encontrado: " + cliente.getName() + " - " + cliente.getEmail()
                                    + " - " + cliente.getTelefono() + " - " + cliente.getDireccion());
                        } else {
                            System.out.println("Cliente no encontrado.");
                        }
                        break;
    
                    case 3:
                        System.out.println("Listado de Clientes:");
                        List<Client> clientes = clienteCasoUso.listarClientes();
                        clientes.forEach(c -> System.out.println(c.getId() + " - " + c.getName() + " - " + c.getEmail()
                                + " - " + c.getTelefono() + " - " + c.getDireccion()));
                        break;
    
                    case 4:
                        System.out.print("Ingrese el ID del cliente que desea actualizar: ");
                        int idNuevo = sc.nextInt();
                        sc.nextLine();
    
                        if (clienteCasoUso.obtenerCliente(idNuevo) != null) {
                            System.out.print("Ingrese el nuevo nombre para el cliente: ");
                            String NombreNuevo = sc.nextLine();
    
                            String EmailNuevo;
                            do {
                                System.out.print("Ingrese el nuevo email del cliente: ");
                                EmailNuevo = sc.nextLine();
                                if (!EmailNuevo.contains("@gmail.com")) {
                                    System.out.println("El correo debe llevar un '@gmail.com'");
                                }
                            } while (!EmailNuevo.contains("@"));
    
                            System.out.print("Ingrese el nuevo teléfono del cliente: ");
                            int TelefonoNuevo = sc.nextInt();
                            sc.nextLine();
    
                            System.out.print("Ingrese la nueva dirección del cliente: ");
                            String DireccionNueva = sc.nextLine();
    
                            clienteCasoUso.actualizarCliente(idNuevo, NombreNuevo, EmailNuevo,
                                    TelefonoNuevo, DireccionNueva);
                            System.out.println("Cliente actualizado correctamente.");
                        } else {
                            System.out.println("El cliente con ID " + idNuevo + " no existe.");
                        }
                        break;
    
                    case 5:
                        System.out.print("Ingrese ID del Cliente a eliminar: ");
                        int idEliminar = sc.nextInt();
                        sc.nextLine();
    
                        clienteCasoUso.eliminarCliente(idEliminar);
                        System.out.println("Cliente eliminado correctamente.");
                        break;
    
                    case 6:
                        System.out.print("Ingrese la dirección a filtrar: ");
                        String direccionFiltro = sc.nextLine();
    
                        List<Client> clientesFiltrados = clienteCasoUso
                                .filtrarClientes(c -> c.getDireccion().equalsIgnoreCase(direccionFiltro));
                        System.out.println("Clientes encontrados con dirección '" + direccionFiltro + "':");
                        clientesFiltrados
                                .forEach(c -> System.out.println(c.getId() + " - " + c.getName() + " - " + c.getEmail()));
                        break;
    
                    case 0:
                        System.out.println("Volviendo al Menú Principal...");
                        break;
                    default:
                        System.out.println("❌ Opción no válida. Intente de nuevo.");
                        break;
                }
            } while (opcion != 0);
        }
    
        private static void menuProductos(Scanner sc, ProductUseCase productoCasoUso) {
            int opcion;
            do {
                System.out.println("\n--- Menú de Productos ---");
                System.out.println("1. Registrar Producto");
                System.out.println("2. Buscar Producto por ID");
                System.out.println("3. Listar Todos los Productos");
                System.out.println("4. Actualizar Producto");
                System.out.println("5. Eliminar Producto");
                System.out.println("6. Filtrar Productos");
                System.out.println("0. Volver al Menú Principal");
                System.out.print("Seleccione una opción: ");
                opcion = sc.nextInt();
                sc.nextLine();
    
                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese ID del Producto: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Ingrese Tipo de Equipo: ");
                        String tipoEquipo = sc.nextLine();
                        System.out.print("Ingrese Marca: ");
                        String marca = sc.nextLine();
                        System.out.print("Ingrese Modelo: ");
                        String modelo = sc.nextLine();
                        System.out.print("Ingrese Serie: ");
                        String serie = sc.nextLine();
                        System.out.print("Ingrese Descripción: ");
                        String descripcion = sc.nextLine();
                        System.out.print("Ingrese Fecha de Ingreso (ddMMyyyy): ");
                        String fechaIngreso = sc.nextLine();
                        System.out.print("Ingrese Fecha de Entrega (ddMMyyyy): ");
                        String fechaEntrega = sc.nextLine();
    
                        productoCasoUso.registrarProducto(id, tipoEquipo, marca, modelo, serie, descripcion, fechaIngreso,
                                fechaEntrega);
                        System.out.println("✅ Producto registrado exitosamente.");
                        break;
    
                    case 2:
                        System.out.print("Ingrese ID del Producto a buscar: ");
                        int idBuscar = sc.nextInt();
                        sc.nextLine();
    
                        Product producto = productoCasoUso.obtenerProducto(idBuscar);
                        if (producto != null) {
                            System.out.println(
                                    "Producto encontrado: " + producto.getTipoEquipo() + " - " + producto.getMarca()
                                            + " - " + producto.getModelo() + " - " + producto.getSerie() + " - "
                                            + producto.getDescripcion()
                                            + " - " + producto.getFechaIngreso() + " - " + producto.getFechaEntrega());
                        } else {
                            System.out.println("Producto no encontrado.");
                        }
                        break;
    
                    case 3:
                        System.out.println("Listado de Productos:");
                        List<Product> productos = productoCasoUso.listarProductos();
                        productos.forEach(
                                p -> System.out.println(p.getId() + " - " + p.getTipoEquipo() + " - " + p.getMarca()
                                        + " - " + p.getModelo() + " - " + p.getSerie() + " - " + p.getDescripcion()
                                        + " - " + p.getFechaIngreso() + " - " + p.getFechaEntrega()));
                        break;
    
                    case 4:
                        System.out.print("Ingrese el ID del producto que desea actualizar: ");
                        int idNuevo = sc.nextInt();
                        sc.nextLine();
    
                        if (productoCasoUso.obtenerProducto(idNuevo) != null) {
                            System.out.print("Ingrese el nuevo tipo de equipo: ");
                            String tipoEquipoNuevo = sc.nextLine();
                            System.out.print("Ingrese la nueva marca: ");
                            String marcaNueva = sc.nextLine();
                            System.out.print("Ingrese el nuevo modelo: ");
                            String modeloNuevo = sc.nextLine();
                            System.out.print("Ingrese la nueva serie: ");
                            String serieNueva = sc.nextLine();
                            System.out.print("Ingrese la nueva descripción: ");
                            String descripcionNueva = sc.nextLine();
                            System.out.print("Ingrese la nueva fecha de ingreso (ddMMyyyy): ");
                            String fechaIngresoNueva = sc.nextLine();
                            System.out.print("Ingrese la nueva fecha de entrega (ddMMyyyy): ");
                            String fechaEntregaNueva = sc.nextLine();
    
                            productoCasoUso.actualizarProducto(idNuevo, tipoEquipoNuevo, marcaNueva, modeloNuevo,
                                    serieNueva, descripcionNueva, fechaIngresoNueva, fechaEntregaNueva);
                            System.out.println("Producto actualizado correctamente.");
                        } else {
                            System.out.println("El producto con ID " + idNuevo + " no existe.");
                        }
                        break;
    
                    case 5:
                        System.out.print("Ingrese ID del Producto a eliminar: ");
                        int idEliminar = sc.nextInt();
                        sc.nextLine();
    
                        productoCasoUso.eliminarProducto(idEliminar);
                        System.out.println("Producto eliminado correctamente.");
                        break;
    
                        case 6:
                        System.out.print("Ingrese la dirección a filtrar: ");
                        String direccionFiltro = sc.nextLine();
                    
                        List<Client> clientesFiltrados = clienteCasoUso.filtrarClientes(c -> 
                        c.getDireccion().equalsIgnoreCase(direccionFiltro));
                    System.out.println("Clientes encontrados con dirección '" + direccionFiltro + "':");
                    clientesFiltrados
                        .forEach(c -> System.out.println(c.getId() + " - " + c.getName() + " - " + c.getEmail()));
                    break;
            }
        } while (opcion != 0);
    }

    private static void menuEstadisticas(Scanner sc, ClientUseCase clienteCasoUso, ProductUseCase productoCasoUso) {
        int opcion;
        do {
            System.out.println("\n--- Menú de Estadísticas ---");
            System.out.println("1. Estadísticas de Clientes");
            System.out.println("2. Estadísticas de Productos");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    System.out.println("\n--- Estadísticas de Clientes ---");
                    System.out.println("Total de clientes: " + clienteCasoUso.listarClientes().size());
                    break;

                case 2:
                    System.out.println("\n--- Estadísticas de Productos ---");
                    System.out.println("Total de productos: " + productoCasoUso.listarProductos().size());
                    break;

                case 0:
                    System.out.println("Volviendo al Menú Principal...");
                    break;
                default:
                    System.out.println("❌ Opción no válida. Intente de nuevo.");
                    break;
            }
        } while (opcion != 0);
    }
}