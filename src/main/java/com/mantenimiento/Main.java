package com.mantenimiento;

import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.mantenimiento.application.usecase.client.ClientUseCase;
import com.mantenimiento.application.usecase.product.ProductUseCase;
import com.mantenimiento.domain.repository.ClientRespository;
import com.mantenimiento.domain.repository.ProductRepository;
import com.mantenimiento.infrastructure.database.ConnectionFactory;
import com.mantenimiento.domain.entity.Client;
import com.mantenimiento.domain.entity.Product;
import com.mantenimiento.infrastructure.persistence.client.ClientRepositoryImpl;
import com.mantenimiento.infrastructure.persistence.product.ProductRepositoryImpl;
import com.mantenimiento.infrastructure.util.JsonConverter;

public class Main {
    public static void main(String[] args) {
        // Crear el JsonConverter
        JsonConverter jsonConverter = new JsonConverter();
        
        // Inicializar repositorios con JsonConverter
        ClientRespository clienteRepositorio = new ClientRepositoryImpl(ConnectionFactory.crearConexion(), jsonConverter);
        ProductRepository productoRepositorio = new ProductRepositoryImpl(ConnectionFactory.crearConexion(), jsonConverter);

        // Inicializar casos de uso con JsonConverter
        ClientUseCase clienteCasoUso = new ClientUseCase(clienteRepositorio, jsonConverter);
        ProductUseCase productoCasoUso = new ProductUseCase(productoRepositorio, jsonConverter);

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
            System.out.println("6. Filtrar Clientes");
            System.out.println("7. Ver Clientes como JSON");
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
                        // Usar el nuevo método que retorna JSON
                        String clienteJson = clienteCasoUso.registrarCliente(id, nombre, email, telefono, direccion);
                        System.out.println("✅ Cliente registrado exitosamente.");
                        System.out.println("Datos del cliente en JSON: " + clienteJson);
                    } else {
                        System.out.println("El correo debe llevar un '@gmail.com' ");
                    }
                    break;

                case 2:
                    System.out.print("Ingrese ID del Cliente a buscar: ");
                    int idBuscar = sc.nextInt();
                    sc.nextLine();
                    
                    // Usar el nuevo método que retorna JSON
                    String clienteJson = clienteCasoUso.obtenerClienteJson(idBuscar);
                    System.out.println("Cliente encontrado (JSON): " + clienteJson);
                    
                    // También mostrar de forma legible
                    Client cliente = ClientUseCase.obtenerCliente(idBuscar);
                    if (cliente != null) {
                        System.out.println("Cliente encontrado: " + cliente.getName() + " - " + cliente.getEmail()
                                + " - " + cliente.getTelefono() + " - " + cliente.getDireccion());
                    }
                    break;

                case 3:
                    System.out.println("Listado de Clientes:");
                    // Usar expresión lambda para imprimir la lista
                    clienteCasoUso.listarClientes().forEach(c -> 
                        System.out.println(c.getId() + " - " + c.getName() + " - " + c.getEmail() 
                            + " - " + c.getTelefono() + " - " + c.getDireccion())
                    );
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

                        // Usar el nuevo método que retorna JSON
                        String clienteActualizadoJson = clienteCasoUso.actualizarCliente(idNuevo, NombreNuevo, EmailNuevo, 
                                TelefonoNuevo, DireccionNueva);
                        System.out.println("Cliente actualizado correctamente:");
                        System.out.println("Datos actualizados (JSON): " + clienteActualizadoJson);
                    } else {
                        System.out.println("El cliente con ID " + idNuevo + " no existe.");
                    }
                    break;

                case 5:
                    System.out.print("Ingrese ID del Cliente a eliminar: ");
                    int idEliminar = sc.nextInt();
                    sc.nextLine();
                    
                    // Usar el nuevo método que retorna JSON
                    String resultadoEliminacion = clienteCasoUso.eliminarCliente(idEliminar);
                    System.out.println("Resultado de la eliminación: " + resultadoEliminacion);
                    break;

                case 6:
                    menuFiltrarClientes(sc, clienteCasoUso);
                    break;
                    
                case 7:
                    // Mostrar todos los clientes en formato JSON
                    String todosClientesJson = clienteCasoUso.listarClientesJson();
                    System.out.println("Todos los clientes (JSON):");
                    System.out.println(todosClientesJson);
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
    
    private static void menuFiltrarClientes(Scanner sc, ClientUseCase clienteCasoUso) {
        int opcion;
        do {
            System.out.println("\n--- Filtrar Clientes ---");
            System.out.println("1. Filtrar por dirección");
            System.out.println("2. Filtrar por nombre que contenga");
            System.out.println("3. Ver clientes agrupados por dirección");
            System.out.println("4. Ver mapa de clientes por ID");
            System.out.println("5. Ver mapa de clientes por email");
            System.out.println("0. Volver al menú de clientes");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese la dirección a filtrar: ");
                    String direccion = sc.nextLine();
                    
                    // Usar predicado con lambda para filtrar
                    Predicate<Client> filtroDireccion = cliente -> 
                        cliente.getDireccion().equalsIgnoreCase(direccion);
                    
                    List<Client> clientesFiltrados = clienteCasoUso.filtrarClientes(filtroDireccion);
                    System.out.println("Clientes encontrados con dirección '" + direccion + "':");
                    clientesFiltrados.forEach(c -> 
                        System.out.println(c.getId() + " - " + c.getName() + " - " + c.getEmail())
                    );
                    break;
                    
                case 2:
                    System.out.print("Ingrese parte del nombre a buscar: ");
                    String nombreBuscar = sc.nextLine().toLowerCase();
                    
                    // Usar predicado con lambda para filtrar por nombre
                    Predicate<Client> filtroNombre = cliente -> 
                        cliente.getName().toLowerCase().contains(nombreBuscar);
                    
                    List<Client> clientesPorNombre = clienteCasoUso.filtrarClientes(filtroNombre);
                    System.out.println("Clientes cuyo nombre contiene '" + nombreBuscar + "':");
                    clientesPorNombre.forEach(c -> 
                        System.out.println(c.getId() + " - " + c.getName() + " - " + c.getEmail())
                    );
                    break;
                    
                case 3:
                    // Usar el método para agrupar clientes por dirección
                    Map<String, List<Client>> clientesPorDireccion = clienteCasoUso.agruparPorDireccion();
                    System.out.println("Clientes agrupados por dirección:");
                    
                    clientesPorDireccion.forEach((dir, clientes) -> {
                        System.out.println("\nDirección: " + dir);
                        System.out.println("Cantidad de clientes: " + clientes.size());
                        clientes.forEach(c -> 
                            System.out.println("  - " + c.getId() + ": " + c.getName())
                        );
                    });
                    break;
                    
                case 4:
                    // Obtener mapa de clientes por ID
                    Map<Integer, Client> clientesPorId = clienteCasoUso.obtenerMapaPorId();
                    System.out.println("Mapa de clientes por ID:");
                    clientesPorId.forEach((id, c) -> 
                        System.out.println("ID " + id + ": " + c.getName() + " - " + c.getEmail())
                    );
                    break;
                    
                case 5:
                    // Obtener mapa de clientes por email
                    Map<String, Client> clientesPorEmail = clienteCasoUso.obtenerMapaPorEmail();
                    System.out.println("Mapa de clientes por email:");
                    clientesPorEmail.forEach((email, c) -> 
                        System.out.println(email + ": " + c.getName() + " (ID: " + c.getId() + ")")
                    );
                    break;
                    
                case 0:
                    System.out.println("Volviendo al menú de clientes...");
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
            System.out.println("7. Ver Productos como JSON");
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

                    // Registrar el producto y mostrar JSON
                    String productoJson = productoCasoUso.registrarproducto(id, nuevoTipoEquipo, nuevaMarca, Modelo, 
                            Serie, Descripcion, fechaIngreso, fechaEntrega);
                    System.out.println("✅ Producto registrado exitosamente.");
                    System.out.println("Datos del producto en JSON: " + productoJson);
                    break;

                case 2:
                    System.out.print("Ingrese ID del Producto a buscar: ");
                    int idBuscar = sc.nextInt();
                    sc.nextLine();
                    
                    // Usar el nuevo método que retorna JSON
                    String productoJsonBuscado = productoCasoUso.obtenerProductoJson(idBuscar);
                    System.out.println("Producto encontrado (JSON): " + productoJsonBuscado);
                    
                    // También mostrar de forma legible
                    Product producto = productoCasoUso.obtenerproducto(idBuscar);
                    if (producto != null) {
                        System.out.println("Producto encontrado: " + producto.getTipoEquipo() + ", "
                                + producto.getMarca() + ", " + producto.getModelo() + ", " + producto.getSerie() + ", "
                                + producto.getDescripcion() + ", " + producto.getFechaIngreso() + ", "
                                + producto.getFechaEntrega());
                    }
                    break;

                case 3:
                    System.out.println("Listado de Productos:");
                    // Usar expresión lambda para imprimir la lista
                    productoCasoUso.listarproductos().forEach(p -> 
                        System.out.println(p.getId() + " - " + p.getTipoEquipo() + " - " + p.getMarca() 
                            + " - " + p.getModelo() + " - " + p.getSerie() + " - " + p.getDescripcion() 
                            + " - " + p.getFechaIngreso() + " - " + p.getFechaEntrega())
                    );
                    break;

                case 4:
                    System.out.print("Ingrese ID del Producto a actualizar: ");
                    int idActualizar = sc.nextInt();
                    sc.nextLine();
                    
                    // Verificar si el producto existe
                    if (productoCasoUso.obtenerproducto(idActualizar) != null) {
                        System.out.println("Ingrese el tipo de equipo:");
                        String tipoEquipo = sc.nextLine();
                        System.out.println("Ingrese la marca del equipo:");
                        String marca = sc.nextLine();
                        System.out.println("Ingrese el modelo del equipo:");
                        String modelo = sc.nextLine();
                        System.out.println("Ingrese el numero de serie del equipo:");
                        String serie = sc.nextLine();
                        System.out.println("Ingrese la descripcion del equipo:");
                        String descripcion = sc.nextLine();
                        
                        // Validar y obtener la fecha de ingreso
                        String fechaIngresoAct = "";
                        boolean fechaIngresoValidaAct = false;
                        while (!fechaIngresoValidaAct) {
                            System.out.println("Ingrese la fecha de ingreso del equipo (ddMMyyyy):");
                            fechaIngresoAct = sc.nextLine();
                            if (validarFecha(fechaIngresoAct)) {
                                fechaIngresoValidaAct = true;
                            } else {
                                System.out.println("❌ Error: Formato de fecha inválido. Use el formato ddMMyyyy.");
                            }
                        }
                        
                        // Validar y obtener la fecha de entrega
                        String fechaEntregaAct = "";
                        boolean fechaEntregaValidaAct = false;
                        while (!fechaEntregaValidaAct) {
                            System.out.println("Ingrese la fecha de entrega del equipo (ddMMyyyy):");
                            fechaEntregaAct = sc.nextLine();
                            if (validarFecha(fechaEntregaAct)) {
                                fechaEntregaValidaAct = true;
                            } else {
                                System.out.println("❌ Error: Formato de fecha inválido. Use el formato ddMMyyyy.");
                            }
                        }
                        
                        // Actualizar el producto y mostrar JSON
                        String productoActualizadoJson = productoCasoUso.actualizarproducto(idActualizar, tipoEquipo, marca, 
                                modelo, serie, descripcion, fechaIngresoAct, fechaEntregaAct);
                        System.out.println("✅ Producto actualizado exitosamente.");
                        System.out.println("Datos actualizados (JSON): " + productoActualizadoJson);
                    } else {
                        System.out.println("❌ Producto no encontrado. No se puede actualizar.");
                    }
                    break;

                case 5:
                    System.out.print("Ingrese ID del Producto a eliminar: ");
                    int idEliminar = sc.nextInt();
                    sc.nextLine();
                    
                    // Usar el nuevo método que retorna JSON
                    String resultadoEliminacion = productoCasoUso.eliminarproducto(idEliminar);
                    System.out.println("Resultado de la eliminación: " + resultadoEliminacion);
                    break;
                    
                case 6:
                    menuFiltrarProductos(sc, productoCasoUso);
                    break;
                    
                case 7:
                    // Mostrar todos los productos en formato JSON
                    String todosProductosJson = productoCasoUso.listarProductosJson();
                    System.out.println("Todos los productos (JSON):");
                    System.out.println(todosProductosJson);
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
    
    private static void menuFiltrarProductos(Scanner sc, ProductUseCase productoCasoUso) {
        int opcion;
        do {
            System.out.println("\n--- Filtrar Productos ---");
            System.out.println("1. Filtrar por marca");
            System.out.println("2. Filtrar por tipo de equipo");
            System.out.println("3. Ver productos agrupados por marca");
            System.out.println("4. Ver mapa de productos por ID");
            System.out.println("5. Ver conteo de productos por tipo");
            System.out.println("0. Volver al menú de productos");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese la marca a filtrar: ");
                    String marca = sc.nextLine();
                    
                    // Usar el método para filtrar por marca
                    List<Product> productosPorMarca = productoCasoUso.filtrarPorMarca(marca);
                    System.out.println("Productos encontrados de la marca '" + marca + "':");
                    productosPorMarca.forEach(p -> 
                        System.out.println(p.getId() + " - " + p.getTipoEquipo() + " - " + p.getModelo())
                    );
                    break;
                    
                case 2:
                    System.out.print("Ingrese el tipo de equipo a filtrar: ");
                    String tipo = sc.nextLine();
                    
                    // Usar predicado con lambda para filtrar por tipo
                    Predicate<Product> filtroTipo = producto -> 
                        producto.getTipoEquipo().equalsIgnoreCase(tipo);
                    
                    List<Product> productosPorTipo = productoCasoUso.filtrarProductos(filtroTipo);
                    System.out.println("Productos del tipo '" + tipo + "':");
                    productosPorTipo.forEach(p -> 
                        System.out.println(p.getId() + " - " + p.getMarca() + " - " + p.getModelo())
                    );
                    break;
                    
                case 3:
                    // Usar el método para agrupar productos por marca
                    Map<String, List<Product>> productosPorMarcaAgrupados = productoCasoUso.agruparPorMarca();
                    System.out.println("Productos agrupados por marca:");
                    
                    productosPorMarcaAgrupados.forEach((m, productos) -> {
                        System.out.println("\nMarca: " + m);
                        System.out.println("Cantidad de productos: " + productos.size());
                        productos.forEach(p -> 
                            System.out.println("  - " + p.getId() + ": " + p.getTipoEquipo() + " " + p.getModelo())
                        );
                    });
                    break;
                    
                case 4:
                    // Obtener mapa de productos por ID
                    Map<Integer, Product> productosPorId = productoCasoUso.obtenerMapaPorId();
                    System.out.println("Mapa de productos por ID:");
                    productosPorId.forEach((id, p) -> 
                        System.out.println("ID " + id + ": " + p.getTipoEquipo() + " " + p.getMarca() + " " + p.getModelo())
                    );
                    break;
                    
                case 5:
                    // Obtener conteo de productos por tipo
                    Map<String, Long> conteoPorTipo = productoCasoUso.contarPorTipoEquipo();
                    System.out.println("Conteo de productos por tipo:");
                    conteoPorTipo.forEach((t, cantidad) -> 
                        System.out.println(t + ": " + cantidad + " productos")
                    );
                    break;
                    
                case 0:
                    System.out.println("Volviendo al menú de productos...");
                    break;
                default:
                    System.out.println("❌ Opción no válida. Intente de nuevo.");
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
                    // Obtener estadísticas de clientes
                    Map<String, Object> estadisticasClientes = clienteCasoUso.obtenerEstadisticas();
                    System.out.println("\n--- Estadísticas de Clientes ---");
                    System.out.println("Total de clientes: " + estadisticasClientes.get("totalClientes"));
                    System.out.println("Promedio de teléfono: " + estadisticasClientes.get("promedioTelefono"));
                    
                    System.out.println("\nClientes por dirección:");
                    @SuppressWarnings("unchecked")
                    Map<String, Long> clientesPorDireccion = 
                        (Map<String, Long>) estadisticasClientes.get("clientesPorDireccion");
                    
                    clientesPorDireccion.forEach((direccion, cantidad) -> 
                        System.out.println(direccion + ": " + cantidad + " clientes")
                    );
                    break;
                    
                case 2:
                    // Obtener estadísticas de productos
                    Map<String, Object> estadisticasProductos = productoCasoUso.obtenerEstadisticas();
                    System.out.println("\n--- Estadísticas de Productos ---");
                    System.out.println("Total de productos: " + estadisticasProductos.get("totalProductos"));
                    
                    System.out.println("\nProductos por marca:");
                    @SuppressWarnings("unchecked")
                    Map<String, Long> productosPorMarca = 
                        (Map<String, Long>) estadisticasProductos.get("productosPorMarca");
                    
                    productosPorMarca.forEach((marca, cantidad) -> 
                        System.out.println(marca + ": " + cantidad + " productos")
                    );
                    
                    System.out.println("\nModelos por marca:");
                    @SuppressWarnings("unchecked")
                    Map<String, List<String>> modelosPorMarca = 
                        (Map<String, List<String>>) estadisticasProductos.get("modelosPorMarca");
                    
                    modelosPorMarca.forEach((marca, modelos) -> {
                        System.out.println(marca + ": " + String.join(", ", modelos));
                    });
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