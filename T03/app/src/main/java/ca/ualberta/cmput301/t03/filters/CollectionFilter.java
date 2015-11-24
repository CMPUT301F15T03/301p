package ca.ualberta.cmput301.t03.filters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by quentinlautischer on 2015-11-23.
 * http://www.javaworld.com/article/2072996/design-patterns/filter-collections.html
 */
import java.lang.reflect.*;

/**
 * <p>Title: CollectionFilter</p>
 * <p>Description: </p>
 * @author David Rappoport
 * @version 1.0
 */

public class CollectionFilter<T extends Collection> implements java.io.Serializable {

    private ArrayList allFilterCriteria = new ArrayList();

    /**
     * Adds a FilterCriteria to be used by the filter
     * @param filterCriteria
     */
    public void addFilterCriteria(FilterCriteria filterCriteria){
        allFilterCriteria.add(filterCriteria);
    }

    /**
     * Starts the filtering process. For each object in the collection,
     * all FilterCriteria are called. Only if the object passess
     * all FilterCriteria it remains in the collection. Otherwise, it is removed.
     * @param collection
     */
    public void filter(T collection){

        if(collection != null){
            Iterator iter = collection.iterator();
            while(iter.hasNext()){
                Object o = iter.next();
                if(!passesAllCriteria(o)){
                    iter.remove();
                }
            }
        }
    }

    /**
     * This method does the same as the filter method. However, a copy of
     * the original collection is created and filtered. The original collection
     * remains unchanged and the copy is returned. Only use this method
     * for collection classes that define a default constructor
     * @param inputCollection
     * @return a filtered copy of the input collection
     */
    public T filterCopy(T inputCollection){

        T outputCollection = null;

        if(inputCollection != null){

            outputCollection = (T) createObjectSameClass(inputCollection);
            outputCollection.addAll(inputCollection);

            Iterator iter = outputCollection.iterator();
            while(iter.hasNext()){
                Object o = iter.next();
                if(!passesAllCriteria(o)){
                    iter.remove();
                }
            }
        }
        return outputCollection;
    }


    /**
     * Makes sure the specified object passes all FilterCriteria's passes method.
     * @param o
     * @return
     */
    private boolean passesAllCriteria(Object o){
        for(int i = 0; i < allFilterCriteria.size(); i ++){
            FilterCriteria filterCriteria = (FilterCriteria)allFilterCriteria.get(i);
            if(!filterCriteria.passes(o)){
                return false;
            }
        }
        return true;
    }

    /**
     * Call the no arguments constructor of the object passed
     * @param object
     * @return
     */
    public Object createObjectSameClass(Object object){

        Class[] NO_ARGS = new Class[0];
        Object sameClassObject = null;
        try{
            if(object != null){
                Constructor constructor = object.getClass().getConstructor(NO_ARGS);
                sameClassObject = constructor.newInstance(NO_ARGS);
            }
        }catch(IllegalAccessException e){
            //@todo do something
        }catch(NoSuchMethodException e){
            //@todo do something
        }catch(InstantiationException e){
            //@todo do something
        }catch(Exception e){
            //@todo do something
        }
        return sameClassObject;
    }


}
