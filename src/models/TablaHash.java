package models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TablaHash {
    private LinkedList<String>[] tabla;
    private int capacidad;
    private FuncionHash1 funcionHash1;
    private FuncionHash2 funcionHash2;
    private int tiempoTotalInsercion1;
    private int tiempoTotalInsercion2;

    public TablaHash(int capacidad) {
        this.capacidad = capacidad;
        tabla = new LinkedList[capacidad];
        for (int i = 0; i < capacidad; i++) {
            tabla[i] = new LinkedList<>();
        }
        funcionHash1 = new FuncionHash1();
        funcionHash2 = new FuncionHash2();
        tiempoTotalInsercion1 = 0;
        tiempoTotalInsercion2 = 0;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void insertar(String clave, String valor, int numeroFuncion) {
        int indice = obtenerIndice(clave, numeroFuncion);
        String entrada = clave + ":" + valor;
        tabla[indice].add(entrada);
    }

    public List<String> obtenerDatosEnIndice(int indice) {
        List<String> datos = new ArrayList<>();
        if (indice < 0 || indice >= capacidad) {
            return datos;
        }
        for (String entrada : tabla[indice]) {
            int separadorIndice = entrada.indexOf(':');
            if (separadorIndice != -1) {
                String clave = entrada.substring(0, separadorIndice);
                String valor = entrada.substring(separadorIndice + 1);
                datos.add("Clave: " + clave + ", Valor: " + valor);
            }
        }
        return datos;
    }

    public String buscarValorPorClave(String clave, int numeroFuncion) {
        String claveBusqueda = clave + ":";
        for (int i = 0; i < capacidad; i++) {
            for (String entrada : tabla[i]) {
                if (entrada.startsWith(claveBusqueda)) {
                    return "Índice: " + i + ", Valor: " + entrada.substring(claveBusqueda.length());
                }
            }
        }
        return null;
    }

    private int obtenerIndice(String clave, int numeroFuncion) {
        if (numeroFuncion == 1) {
            return funcionHash1.hash(clave) % capacidad;
        } else {
            return funcionHash2.hash(clave, capacidad);
        }
    }

    public int medirTiempoInsercion(String clave, String valor, int numeroFuncion) {
        long tiempoInicio = System.nanoTime();
        insertar(clave, valor, numeroFuncion);
        long tiempoFin = System.nanoTime();
        int tiempoInsercion = (int) (tiempoFin - tiempoInicio);

        if (numeroFuncion == 1) {
            tiempoTotalInsercion1 += tiempoInsercion;
        } else {
            tiempoTotalInsercion2 += tiempoInsercion;
        }

        return tiempoInsercion;
    }

    public int obtenerTiempoTotalInsercion1() {
        return tiempoTotalInsercion1;
    }

    public int obtenerTiempoTotalInsercion2() {
        return tiempoTotalInsercion2;
    }
}
