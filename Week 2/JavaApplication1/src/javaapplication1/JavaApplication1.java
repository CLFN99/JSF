/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;
import java.util.Map;
/**
 *
 * @author jsf3
 */
public class JavaApplication1 {

    public static void main (String[] args) {
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            //System.out.format("%s=%s%n",
                           //   envName,
                             // env.get(envName));
            
        }
        System.out.print("i did it");
    }
    
}
