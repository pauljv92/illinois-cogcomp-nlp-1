/**
 * This software is released under the University of Illinois/Research and
 *  Academic Use License. See the LICENSE file in the root folder for details.
 * Copyright (c) 2016
 *
 * Developed by:
 * The Cognitive Computation Group
 * University of Illinois at Urbana-Champaign
 * http://cogcomp.cs.illinois.edu/
 */
package edu.illinois.cs.cogcomp.core.math;

/**
 * Keeps track of maximum and the object that corresponds to the maximum.
 * <p>
 * K: type of value over which the max decision is made. T: type of the objects which generate the
 * values
 * <p>
 * In case of ties, the first object is chosen.
 * <p>
 * <b>Example:</b>
 * <p>
 * Suppose we have a list of strings that can be scored and we wish to track the maximum.
 * <p>
 * 
 * <pre>
 * ArgMax&lt;Double, String&gt; argmax = new Argmax&lt;Double, String&gt;(Double.NEGATIVE_INFINITY, &quot;&quot;);
 * 
 * for (String str : stringList) // stringList is probably an array of Strings
 * {
 *     argmax.update(str, getScore(str));
 * }
 * 
 * double maxValue = argmax.getMaxValue();
 * String argmaxString = argmax.getArgMax();
 * </pre>
 *
 * @author Vivek Srikumar
 */
public class ArgMax<T, K extends Comparable<K>> {
    private K key;
    private T object;

    public void update(T object, K value) {
        if (key.compareTo(value) < 0) {
            key = value;
            this.object = object;
        }
    }

    public K getMaxValue() {
        return key;
    }

    public T getArgmax() {
        return object;
    }

    public ArgMax(T initialObject, K initialValue) {
        key = initialValue;
        this.object = initialObject;
    }

    @Override
    public String toString() {
        return "value: " + key + ", object: " + object;
    }

}
