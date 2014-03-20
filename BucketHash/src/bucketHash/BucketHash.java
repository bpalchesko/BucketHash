package bucketHash;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Defines a custom hash map data type.
 * 
 * @author Brad Palchesko
 * 
 * @param <K>
 * @param <V>
 */
public class BucketHash<K, V> implements Map<K, V> {
	private int numBuckets;
	private Object[] buckets;
    
	/**
	 * Constructs a BucketHash.
	 */
	public BucketHash() {
		numBuckets = 101;
		buckets = new Object[numBuckets];
		for (int i = 0; i < numBuckets; i++) {
			buckets[i] = new Bucket();
		}
	}
    
	/**
	 * Constructs a BucketHash using keys and values from a map.
	 * 
	 * @param m
	 */
	public BucketHash(Map<K, V> m) {
		numBuckets = 101;
		buckets = new Object[numBuckets];
		for (int i = 0; i < numBuckets; i++) {
			buckets[i] = new Bucket();
		}
		putAll(m);
	}

	/**
	 * Determines equality by attempting to match all contained keys and
	 * values.
	 */
    @Override
    public boolean equals(Object o){
    	if(!(o instanceof Map)) return false;
    	Map m = (Map)o;
		if (m.hashCode() != hashCode()) return false;
		if (m.keySet().size() != keySet().size()) return false;
		for (K key : ((Map<K, V>) m).keySet()) {
			if (!containsKey(key)) return false;
			if (!get(key).equals(((Map) m).get(key))) return false;
    	}
    	return true;
    }
        
	/**
	 * Determines this hash code.
	 * 
	 * @return hash code value
	 */
	@Override
	public int hashCode() {
		int hashCode = 0;
		for (K key : keySet()) {
			hashCode += (key.hashCode() ^ get(key).hashCode());
		}
		return hashCode;
	}

	/**
	 * Returns the corresponding bucket given a key.
	 * 
	 * @param key
	 * @return corresponding Bucket
	 */
	Bucket getBucket(K key) {
		return (Bucket) buckets[key.hashCode() % numBuckets];
	}

	/**
	 * Removes all keys and values.
	 */
	@Override
	public void clear() {
		buckets = new Object[numBuckets];
		for (int i = 0; i < numBuckets; i++) {
			buckets[i] = new Bucket();
		}
	}

	/**
	 * Returns whether the given key is contained in the appropriate bucket.
	 */
	@Override
	public boolean containsKey(Object key) {
		if (key == null) throw new NullPointerException();
		return get(key) != null;
	}

	/**
	 * Returns whether the given value is found.
	 */
	@Override
	public boolean containsValue(Object value) {
		if (value == null) throw new NullPointerException();
		for (K key : keySet()) {
			if (get(key).equals(value)) return true;
		}
		return false;
	}

	/**
	 * Throws UnsupportedOperationException.
	 */
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the value for a given key, if found. Otherwise, returns null.
	 * 
	 * @return corresponding value
	 */
	@Override
	public V get(Object key) {
		Bucket bucket = (Bucket) buckets[key.hashCode() % numBuckets];
		return bucket.get((K) key);
	}

	/**
	 * Verifies if there are no keys contained in buckets.
	 */
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns a set of all the keys contained in buckets.
	 * 
	 * @return set of keys
	 */
	@Override
	public Set<K> keySet() {
		Set<K> keySet = new HashSet<K>();
		for (Object o : buckets) {
			Bucket b = (Bucket) o;
			keySet.addAll(b.keySet());
		}
		return keySet;
	}

	/**
	 * Adds a key and value to the appropriate bucket.
	 * 
	 * @return removed value, if applicable
	 */
	@Override
	public V put(K key, V value) {
		int index = key.hashCode() % numBuckets;
		return ((Bucket) buckets[index]).put(key, value);
	}

	/**
	 * Adds all keys and values from a map.
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (K key : m.keySet()) {
			put(key, m.get(key));
		}
	}

	/**
	 * Removes the key and corresponding value from its bucket, if found.
	 * 
	 * @return removed value, if applicable
	 */
	@Override
	public V remove(Object key) {
		int index = key.hashCode() % numBuckets;
		return ((Bucket) buckets[index]).remove((K) key);
	}

	/**
	 * Determines the total number of keys contained in buckets.
	 * 
	 * @return number of keys
	 */
	@Override
	public int size() {
		return keySet().size();
	}

	/**
	 * Returns all values contained in the buckets.
	 * 
	 * @return list of values
	 */
	@Override
	public Collection<V> values() {
		Collection<V> valueList = new ArrayList<V>();
		for (Object o : buckets) {
			Bucket b = (Bucket) o;
			valueList.addAll(b.values());
		}
		return valueList;
	}

	/**
	 * Defines the bucket data type contained within the bucket hash.
	 * 
	 * @author Brad Palchesko
	 * 
	 */
	class Bucket {
		ListNode header;

		/**
		 * Constructs a Bucket.
		 */
		public Bucket() {
			header = null;
		}
        
		/**
		 * Defines the ListNode data type contained within a bucket.
		 * 
		 * @author Brad Palchesko
		 * 
		 */
		private class ListNode {
			K key;
			V value;
			ListNode next;

			/**
			 * Constructs a ListNode.
			 * 
			 * @param key
			 * @param value
			 * @param next
			 */
			ListNode(K key, V value, ListNode next) {
				this.key = key;
				this.value = value;
				this.next = next;
			}
		}
        
		/**
		 * Determines the number of keys contained in a bucket.
		 * 
		 * @return number of keys.
		 */
		public int size() {
			int size = 0;
			ListNode counter = header;
			while (counter != null) {
				size++;
				counter = counter.next;
			}
			return size;
		}
        
		/**
		 * Finds the value corresponding to a key, if the key is found.
		 * 
		 * @param key
		 * @return corresponding value
		 */
		public V get(K key) {
			if (key == null) {
				throw new NullPointerException();
			} else if (find(key) == null) {
				return null;
			} else {
				return find(key).value;
			}
		}
        
		/**
		 * Finds the ListNode corresponding to a key, if the key is found.
		 * 
		 * @param key
		 * @return corresponding ListNode
		 */
		private ListNode find(K key) {
			if (key == null) throw new NullPointerException();
			ListNode current = header;
			while (current != null) {
				if (current.key.equals(key)) return current;
				current = current.next;
			}
			return null;
		}
        
		/**
		 * Adds a key and value to the bucket if the key is not yet present, or
		 * replaces the value if the key is already there.
		 * 
		 * @param key
		 * @param value
		 * @return removed value, if applicable
		 */
		public V put(K key, V value) {
			if (key == null || value == null) {
				throw new NullPointerException();
			} else if (find(key) == null) {
				ListNode newNode = new ListNode(key, value, header);
				header = newNode;
				return null;
			} else {
				ListNode current = find(key);
				V previous = current.value;
				current.value = value;
				return previous;
			}
		}
        
        /**
         * Removes a key and corresponding value from the bucket if found.
         * 
         * @param key
         * @return removed value
         */
        public V remove(K key) {
        	if(key == null) throw new NullPointerException();
        	ListNode remove = find(key);
        	if (remove == null) return null;
        	V valueToRemove = remove.value;
			if (remove.key == header.key) {
				header = header.next;
				return valueToRemove;
			}
			ListNode current = header;
			while (current.next != remove) {
				current = current.next;
			}
			current.next = remove.next;
			return remove.value;
        }
        
		/**
		 * Provides a set of all keys contained in the bucket.
		 * 
		 * @return set of keys
		 */
		public Set<K> keySet() {
			Set<K> keySet = new HashSet<K>();
			ListNode current = header;
			while (current != null) {
				keySet.add(current.key);
				current = current.next;
			}
			return keySet;
        }
        
		/**
		 * Privates a list of all values contained in the bucket.
		 * 
		 * @return list of values
		 */
		public Collection<V> values() {
			Collection<V> valueList = new ArrayList<V>();
			ListNode current = header;
			while (current != null) {
				valueList.add(current.value);
				current = current.next;
			}
			return valueList;
		}

		/**
		 * Produces a string of all key and value pairings in the bucket.
		 * 
		 * @return string
		 */
		public String toString() {
			ListNode current = header;
			String s = "";
			while (current != null) {
				s += "k: " + current.key + " v: " + current.value + "\n";
				current = current.next;
			}
			return s;
		}
        
    }
}


	