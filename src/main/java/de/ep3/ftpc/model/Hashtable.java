/*
 * FTP Crawler - A simple file crawler for UNIX based FTP servers
 * Copyright (C) 2014 Tobias Krebs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.ep3.ftpc.model;

/**
 * Extends the "native" hashtable data structure by some convenience methods and
 * a temporary storage, which will not be serialized to files.
 *
 * @param <K>
 * @param <V>
 */
public class Hashtable<K, V> extends java.util.Hashtable<K, V>
{

    private static final long serialVersionUID = -5644490882870791978L;

    private transient Hashtable<K, V> temporaryHashtable;

    public synchronized boolean has(K key)
    {
        return containsKey(key);
    }

    public synchronized V need(K key)
    {
        V object = get(key);

        if (object == null) {
            throw new IllegalStateException("Hashtable key " + key.toString() + " required, but not found");
        }

        return object;
    }

    public synchronized V putTemporary(K key, V value)
    {
        return getTemporaryHashtable().put(key, value);
    }

    public synchronized boolean hasTemporary(K key)
    {
        return getTemporaryHashtable().has(key);
    }

    public synchronized V getTemporary(K key)
    {
        return getTemporaryHashtable().get(key);
    }

    public synchronized V needTemporary(K key)
    {
        return getTemporaryHashtable().need(key);
    }

    private Hashtable<K, V> getTemporaryHashtable()
    {
        if (temporaryHashtable == null) {
            temporaryHashtable = new Hashtable<>();
        }

        return temporaryHashtable;
    }

}