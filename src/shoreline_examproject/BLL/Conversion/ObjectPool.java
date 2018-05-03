package shoreline_examproject.BLL.Conversion;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Object pool, implemented using: https://sourcemaking.com/design_patterns/object_pool/java
 * @author sebok
 */
public abstract class ObjectPool<T>
{
    private long expirationTime;
    
    private ConcurrentHashMap<T, Long> locked, unlocked;
    
    public ObjectPool() {
        expirationTime = 30000;
        locked = new ConcurrentHashMap();
        unlocked = new ConcurrentHashMap();
    }
    
    protected abstract T create();
    
    protected abstract boolean validate(T o);
    
    public abstract void expire(T o);
    
    public synchronized T checkOut() {
       long now = System.currentTimeMillis();
       T t;
       
        if (unlocked.size() > 0) {
            Enumeration<T> e = unlocked.keys();
            
            while (e.hasMoreElements())
            {
                t = e.nextElement();
                if ((now - unlocked.get(e)) > expirationTime) {  // Object has expired
                    unlocked.remove(e);   
                    expire(t);
                    t = null;
                }
                else {
                    if (validate(t)) {
                        unlocked.remove(e);
                        locked.put(t, now);
                        return t;
                    }
                    else {
                        unlocked.remove(t);
                        expire(t);
                        t = null;
                    }
                }
            }
        }
        t = create();
        locked.put(t, now);
        return t;
    }
    
    public synchronized void checkIn(T t)
    {
        locked.remove(t);
        unlocked.put(t, System.currentTimeMillis());
    }
}
