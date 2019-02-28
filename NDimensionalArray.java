/*
 * MIT License
 * 
 * Copyright (c) 2019 Sebastian Gössl
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */



package ndimensionalarray;

import java.util.Iterator;
import java.util.NoSuchElementException;



/**
 * Storage class for multidimensional data.
 * All indices (element indices and dimension indices) are zero based. The
 * elements are internaly stored in a linear array in row-major order (last
 * index increments first).
 * 
 * @author Sebastian Gössl
 * @version 1.0 26.1.2019
 */
public class NDimensionalArray<E> implements Iterable<E> {
  
  /**
   * Sizes of all different dimensions.
   */
  private final int[] dimensions;
  /**
   * Dope vecotr.
   * Factors used to calculate the linear index from a multidimensional
   * notation.
   */
  private final int[] dopeVector;
  /**
   * Stored data.
   */
  private final Object[] data;
  
  
  
  /**
   * Constructs a new NDimensionalArray with the given sizes of the different
   * dimensions.
   * 
   * @param dimensions sizes of the different dimensions of the data to store
   */
  public NDimensionalArray(int... dimensions) {
    this.dimensions = new int[dimensions.length];
    System.arraycopy(dimensions, 0, this.dimensions, 0, dimensions.length);
    
    int length = 1;
    for(int i=0; i<dimensions.length; i++) {
      this.dimensions[i] = dimensions[i];
      length *= dimensions[i];
    }
    data = new Object[length];
    
    dopeVector = new int[dimensions.length];
    dopeVector[dopeVector.length - 1] = 1;
    for(int i=dopeVector.length-2; i>=0; i--) {
      dopeVector[i] = dopeVector[i + 1] * dimensions[i + 1];
    }
  }
  
  
  
  /**
   * Returns the total number of elements stored.
   * 
   * @return total number of elements stored
   */
  public int getNumberOfElements() {
    return data.length;
  }
  
  /**
   * Returns the total number of dimensions.
   * @return total number of dimensions
   */
  public int getNumberOfDimensions() {
    return dimensions.length;
  }
  
  /**
   * Returns the size of the stored data in the specified dimension.
   * 
   * @param index of the dimension
   * @return size of the specified dimension
   */
  public int getDimension(int index) {
    return dimensions[index];
  }
  
  /**
   * Returns a copy of the sizes in all dimension.
   * 
   * @return copy of the sizes in all dimension
   */
  public int[] getDimensions() {
    final int[] copy = new int[dimensions.length];
    System.arraycopy(dimensions, 0, copy, 0, dimensions.length);
    return copy;
  }
  
  
  /**
   * Returns the element at the specified position.
   * 
   * @param index linear index of the element to return
   * @return element at the specified position
   */
  public E getLinear(int index) {
    return (E)data[index];
  }
  
  /**
   * Returns the element at the specified position.
   * 
   * @param indices indices of the element to return
   * @return element at the specified position
   */
  public E get(int... indices) {
    return getLinear(indicesToIndex(indices));
  }
  
  /**
   * Replaces the element at the specified position with the specified element.
   * 
   * @param value element to be stored at the specified position
   * @param index linear index of the element to replace
   * @return element previously at the specified position
   */
  public E setLinear(E value, int index) {
    final E oldElement = (E)data[index];
    data[index] = value;
    return oldElement;
  }
   
  /**
   * Replaces the element at the specified position with the specified element.
   * 
   * @param value element to be stored at the specified position
   * @param indices indices of the element to replace
   * @return element previously at the specified position
   */
  public E set(E value, int... indices) {
    return setLinear(value, indicesToIndex(indices));
  }
  
  
  /**
   * Converts the multidimensional indices to the equivalent linear index.
   * 
   * @param indices multidimensional indices
   * @return equivalent linear index
   */
  private int indicesToIndex(int... indices) {
    int index = 0;
    for(int i=0; i<dopeVector.length; i++) {
      index += dopeVector[i] * indices[i];
    }
    
    return index;
  }
  
  
  
  /**
   * Returns an iterator which iterates over the elements linearly.
   * 
   * @return iterator which iterates over the elements linearly
   */
  @Override
  public Iterator<E> iterator() {
    return new Iterator<E>() {
      /**
       * Linear index of the next element.
       */
      private int i = 0;
      
      @Override
      public boolean hasNext() {
        return i < getNumberOfElements();
      }
      
      @Override
      public E next() {
        if(!hasNext()) {
          throw new NoSuchElementException();
        }
        
        return getLinear(i++);
      }
    };
  }
  
  
  
  
  public static void main(String[] args) {
    final NDimensionalArray<Integer> array = new NDimensionalArray<>(2, 4, 3);
    
    for(int i=0; i<array.getNumberOfElements(); i++) {
      array.setLinear(i, i);
    }
    
    for(int k=0; k<array.getDimension(0); k++) {
      for(int j=0; j<array.getDimension(1); j++) {
        for(int i=0; i<array.getDimension(2); i++) {
          System.out.println("[" + k + "][" + j + "][" + i + "]: "
                  + array.get(k, j, i));
        }
      }
    }
    
    array.forEach(System.out::println);
  }
}
