package com.mantenimiento;

import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;
import java.text.ParseException;

import com.mantenimiento.application.usecase.client.ClientUseCase;
import com.mantenimiento.application.usecase.product.ProductUseCase;
import com.mantenimiento.domain.repository.ClientRespository;
import com.mantenimiento.domain.repository.ProductRepository;
import com.mantenimiento.infrastructure.database.ConnectionFactory;
import com.mantenimiento.domain.entity.Client;
import com.mantenimiento.domain.entity.Product;
import com.mantenimiento.infrastructure.persistence.client.ClientRepositoryImpl;
import com.mantenimiento.infrastructure.persistence.product.ProductRepositoryImpl;

public class Main {
    public static void main(String[] args) {
        ClientRespository clienteRepositorio = new ClientRepositoryImpl(ConnectionFactory.crearConexion());
        ProductRepository productoRepositorio = new ProductRepositoryImpl(ConnectionFactory.crearConexion());

        ClientUseCase clienteCasoUso = new ClientUseCase(clienteRepositorio);
        ProductUseCase productoCasoUso = new ProductUseCase(productoRepositorio);

        try (Scanner sc = new Scanner(System.in)) {
            int opcionPrincipal;
            do {
                System.out.println("\n--- Menú Principal ---");
                System.out.println("1. Clientes");
                System.out.println("2. Productos");
                System.out.println("3. Salir");
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
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("❌ Opción no válida. Intente de nuevo.");
                        break;
                }
            } while (opcionPrincipal != 3);
        } catch (Exception e) {
            System.out.println("Error al poner signos");
        }
    }

    private static boolean validarFecha(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyy");
        formato.setLenient(false);
        try {
            formato.parse(fecha);
            return true;
        } catch (ParseException e) {
            return false;
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
                    System.out.println("Ingrese Direccion: ");
                    String direccion = sc.nextLine();
                    System.out.print("Ingrese Telefono: ");
                    int telefono = sc.nextInt();

                    if (email.contains("@gmail.com")) {
                        sc.nextLine();
                        clienteCasoUso.registrarCliente(id, nombre, email, telefono, direccion);
                        System.out.println("✅ Cliente registrado exitosamente.");
                    } else {
                        System.out.println("El correo debe llevar un '@gmail.com' ");
                    }

                    break;

                case 2:
                    System.out.print("Ingrese ID del Cliente a buscar: ");
                    int idBuscar = sc.nextInt();
                    sc.nextLine();
                    Client cliente = ClientUseCase.obtenerCliente(idBuscar);
                    if (cliente != null) {
                        System.out.println("Cliente encontrado: " + cliente.getName() + " - " + cliente.getEmail()
                                + " - " + cliente.getTelefono() + " - " + cliente.getDireccion());
                    } else {
                        System.out.println("❌ Cliente no encontrado.");
                    }
                    break;

                case 3:
                    System.out.println("Listado de Clientes:");
                    clienteCasoUso.listarClientes().forEach(c -> System.out.println(c.getId() + " - " + c.getName()
                            + " - " + c.getEmail() + " - " + c.getTelefono() + " - " + c.getDireccion()));
                    break;

                case 4:
                    System.out.print("Ingrese el ID del cliente que deseas actualizar: ");
                    int idNuevo = sc.nextInt();
                    sc.nextLine(); // Consumir la nueva línea sobrante

                    if (ClientUseCase.obtenerCliente(idNuevo) != null) {
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

                        System.out.println("Ingrese el nuevo telefono del cliente: ");
                        int TelefonoNuevo = sc.nextInt();
                        sc.nextLine(); // Consumir la nueva línea sobrante

                        System.out.println("Ingrese la nueva direccion del cliente: ");
                        String DireccionNueva = sc.nextLine();

                        System.out.println("");

                        clienteCasoUso.actualizarCliente(idNuevo, NombreNuevo, EmailNuevo, TelefonoNuevo,
                                DireccionNueva);
                        System.out.println("Cliente actualizado correctamente:");
                        ClientUseCase.obtenerCliente(idNuevo);
                    } else {
                        System.out.println("El cliente con ID " + idNuevo + " no existe.");
                    }
                    break;

                case 5:
                    System.out.print("Ingrese ID del Cliente a eliminar: ");
                    int idEliminar = sc.nextInt();
                    sc.nextLine();
                    // Verificar si el cliente existe antes de eliminar
                    Client clienteAEliminar = ClientUseCase.obtenerCliente(idEliminar);
                    if (clienteAEliminar != null) {
                        clienteCasoUso.eliminarCliente(idEliminar);
                        System.out.println("✅ Cliente eliminado exitosamente.");
                    } else {
                        System.out.println("❌ Cliente no encontrado. No se puede eliminar.");
                    }
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
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese ID del Equipo: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Ingrese el tipo de equipo:");
                    String nuevoTipoEquipo = sc.nextLine();
                    System.out.println("Ingrese la marca del equipo:");
                    String nuevaMarca = sc.nextLine();
                    System.out.println("Ingrese el modelo del equipo:");
                    String Modelo = sc.nextLine();
                    System.out.println("Ingrese el numero de serie del equipo:");
                    String Serie = sc.nextLine();
                    System.out.println("Ingrese la descripcion del equipo:");
                    String Descripcion = sc.nextLine();

                    // Validar y obtener la fecha de ingreso
                    String fechaIngreso = "";
                    boolean fechaIngresoValida = false;
                    while (!fechaIngresoValida) {
                        System.out.println("Ingrese la fecha de ingreso del equipo (ddMMyyyy):");
                        fechaIngreso = sc.nextLine();
                        if (validarFecha(fechaIngreso)) {
                            fechaIngresoValida = true;
                        } else {
                            System.out.println("❌ Error: Formato de fecha inválido. Use el formato ddMMyyyy.");
                        }
                    }

                    // Validar y obtener la fecha de entrega
                    String fechaEntrega = "";
                    boolean fechaEntregaValida = false;
                    while (!fechaEntregaValida) {
                        System.out.println("Ingrese la fecha de entrega del equipo (ddMMyyyy):");
                        fechaEntrega = sc.nextLine();
                        if (validarFecha(fechaEntrega)) {
                            fechaEntregaValida = true;
                        } else {
                            System.out.println("❌ Error: Formato de fecha inválido. Use el formato ddMMyyyy.");
                        }
                    }

                    // Registrar el producto
                    productoCasoUso.registrarproducto(id, nuevoTipoEquipo, nuevaMarca, Modelo, Serie, Descripcion,
                            fechaIngreso, fechaEntrega);
                    System.out.println("✅ Producto registrado exitosamente.");
                    break;

                case 2:
                    System.out.print("Ingrese ID del Producto a buscar: ");
                    int idBuscar = sc.nextInt();
                    sc.nextLine();
                    Product producto = productoCasoUso.obtenerproducto(idBuscar);
                    if (producto != null) {
                        System.out.println("Producto encontrado: " + producto.getTipoEquipo() + ", "
                                + producto.getMarca() + ", " + producto.getModelo() + ", " + producto.getSerie() + ", "
                                + producto.getDescripcion() + ", " + producto.getFechaIngreso() + ", "
                                + producto.getFechaEntrega());
                    } else {
                        System.out.println("❌ Producto no encontrado.");
                    }
                    break;

                case 3:
                    System.out.println("Listado de Productos:");
                    productoCasoUso.listarproductos()
                            .forEach(p -> System.out.println(p.getId() + " - " + p.getTipoEquipo() + " - "
                                    + p.getMarca() + " - " + p.getModelo() + " - " + p.getSerie() + " - "
                                    + p.getDescripcion() + " - " + p.getFechaIngreso() + " - " + p.getFechaEntrega()));
                    break;

                

                case 5:
                    System.out.print("Ingrese ID del Producto a eliminar: ");
                    int idEliminar = sc.nextInt();
                    sc.nextLine();
                    // Verificar si el producto existe antes de eliminar
                    Product productoAEliminar = productoCasoUso.obtenerproducto(idEliminar);
                    if (productoAEliminar != null) {
                        productoCasoUso.eliminarproducto(idEliminar);
                        System.out.println("✅ Producto eliminado exitosamente.");
                    } else {
                        System.out.println("❌ Producto no encontrado. No se puede eliminar.");
                    }
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