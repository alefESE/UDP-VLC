/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vlcproxy;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

/**
 *
 * @author aluno
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException {
        // TODO code application logic here
        Semaphore sem = new Semaphore(0);
        Semaphore con = new Semaphore(0);
        Buffer buffer = new Buffer(1316, sem);
        Sincronizador sinc = new Sincronizador();
        sinc.AddProdutor(5000, buffer);
        InetAddress addr = InetAddress.getByName("127.0.0.1");
        sinc.AddConsumidor(5001, addr, buffer, true, 0);
    }
    
}
