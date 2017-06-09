/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vlcproxy;

import java.util.concurrent.Semaphore;

/**
 *
 * @author alef_
 */
public interface Sincronizador {
    
    Buffer buffer = new Buffer(1316, 1024);
    Semaphore produtor = new Semaphore(0);//bastao produtor
    Semaphore consumidor = new Semaphore(0);//bastao consumidor
}
