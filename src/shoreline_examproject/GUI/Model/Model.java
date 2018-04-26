/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_examproject.GUI.Model;

/**
 *
 * @author Bence
 */
public class Model {
    private static Model instance;
    
    
    
    public static Model getInstance(){
        if(instance == null)
        {
            instance = new Model();
        }
        return instance;
    }
}