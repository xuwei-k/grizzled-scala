/*
  ---------------------------------------------------------------------------
  This software is released under a BSD-style license:

  Copyright (c) 2009 Brian M. Clapper. All rights reserved.

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions are
  met:

  1.  Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.

  2.  The end-user documentation included with the redistribution, if any,
      must include the following acknowlegement:

        "This product includes software developed by Brian M. Clapper
        (bmc@clapper.org, http://www.clapper.org/bmc/). That software is
        copyright (c) 2009 Brian M. Clapper."

      Alternately, this acknowlegement may appear in the software itself,
      if wherever such third-party acknowlegements normally appear.

  3.  Neither the names "clapper.org", "The Grizzled Scala Library",
      nor any of the names of the project contributors may be used to
      endorse or promote products derived from this software without prior
      written permission. For written permission, please contact
      bmc@clapper.org.

  4.  Products derived from this software may not be called "clapper.org
      Java Utility Library", nor may "clapper.org" appear in their names
      without prior written permission of Brian M. Clapper.

  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
  NO EVENT SHALL BRIAN M. CLAPPER BE LIABLE FOR ANY DIRECT, INDIRECT,
  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  ---------------------------------------------------------------------------
*/

/**
 * Some collection-related helpers.
 */
package grizzled.collection

import java.util.{Collection, Iterator => JIterator}

/**
 * Useful for converting a collection into an object suitable for use with
 * Scala's <tt>for</tt> loop.
 */
class CollectionIterator[T](val iterator: JIterator[T]) extends Iterator[T]
{

    /**
     * Alternate constructor that takes a collection.
     *
     * @param collection  the collection
     */
    def this(collection: Collection[T]) = this(collection.iterator)

    def hasNext: Boolean = iterator.hasNext
    def next: T = iterator.next
}

/**
 * An <tt>Iterator</tt> for lists.
 */
class ListIterator[+T](val list: List[T]) extends Iterator[T]
{
    private var cursor = 0

    def hasNext: Boolean = cursor < list.length
    def next: T =
    {
        val result = list(cursor)
        cursor += 1
        result
    }
}

/**
 * An iterator that iterates, serially, over the contents of multiple other
 * iterators.
 *
 * @param iterators  the iterators to wrap
 */
class MultiIterator[T](iterators: Iterator[T]*) extends Iterator[T]
{
    private var iteratorList: List[Iterator[T]] = iterators.toList

    /**
     * Determines whether the iterator is empty. A <tt>MultiIterator</tt>
     * is empty when all contained iterators have been exhausted.
     *
     * @return <tt>true</tt> if there's more to read, <tt>false</tt> if not
     */
    def hasNext: Boolean =
    {
        if (iteratorList.length == 0)
            false

        else if (iteratorList(0).hasNext)
            true

        else
        {
            iteratorList = iteratorList drop 1
            hasNext
        }
    }

    /**
     * Get the next element.
     *
     * @return the next element
     */
    def next: T =
    {
        if (! hasNext)
            throw new java.util.NoSuchElementException

        iteratorList(0).next
    }
}

object implicits
{
    implicit def javaCollectionToScalaIterator[T](c: Collection[T]) =
        new CollectionIterator[T](c)
}
