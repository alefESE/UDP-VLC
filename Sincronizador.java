/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vlcproxy;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.Semaphore;

/**
 *
 * @author aluno
 */
public class Sincronizador {
    Semaphore prod;
    Semaphore cons;
    Consumidor[] consumidores;
    Produtor produtor;
    
    public Sincronizador(){
        prod = new Semaphore(0);
        cons = new Semaphore(0);
        consumidores = new Consumidor[9];
    }
    
    public void AddProdutor(int porta, Buffer buffer) throws SocketException{
        produtor = new Produtor(porta, buffer, prod, cons);
        produtor.start();
    }
    
    public void AddConsumidor(int porta, InetAddress addr, Buffer buffer, boolean prioridade,  int id) throws SocketException{
        consumidores[id] = new Consumidor(porta, addr, buffer, prioridade, cons, prod);
        consumidores[id].start();
    }
    
    public void Kill(int id){
        consumidores[id].Suicida();
    }
    
}
