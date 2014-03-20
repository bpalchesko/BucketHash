package bucketHash;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import bucketHash.BucketHash.Bucket;

public class BucketHashTest {
	
	BucketHash<Integer, String> germanNumbers = new BucketHash<Integer, String>();
	BucketHash<Integer, String> germanNumbersCopy;
	Bucket b = germanNumbers.getBucket(0);

	@Before
	public void setUp() throws Exception {
		germanNumbers = new BucketHash<Integer, String>();
		germanNumbers.put(0, "null");
		germanNumbers.put(1, "eins");
		germanNumbers.put(40, "vierzig");
		germanNumbersCopy = new BucketHash<Integer, String>(germanNumbers);
	}

	@Test
	public void testBucketHash() {
		assertTrue(germanNumbers instanceof BucketHash);
		assertEquals(germanNumbers.size(), 3);
		assertTrue(germanNumbers.containsValue("null"));
		assertFalse(germanNumbers.containsValue("zwei"));
	}

	@Test
	public void testBucketHashMapOfKV() {
		assertTrue(germanNumbersCopy instanceof BucketHash);
		assertEquals(germanNumbersCopy.size(), 3);
		assertTrue(germanNumbersCopy.containsValue("null"));
		assertFalse(germanNumbersCopy.containsValue("zwei"));
		assertTrue(germanNumbersCopy.equals(germanNumbers));
	}
	
	@Test
	public void testHashCode() {
		int hash0 = ((Integer)0).hashCode() ^ "null".hashCode();
		int hash1 = ((Integer)1).hashCode() ^ "eins".hashCode();
		int hash40 = ((Integer)40).hashCode() ^ "vierzig".hashCode();
		int hash41 = ((Integer)41).hashCode() ^ "vierzig".hashCode();
		assertEquals(germanNumbers.hashCode(), hash0 + hash1 + hash40);
		assertNotEquals(germanNumbers.hashCode(), hash0 + hash1 + hash41);
		BucketHash<Integer, String> germanNumbersCopy = new BucketHash<Integer, String>(germanNumbers);
		assertEquals(germanNumbersCopy.hashCode(), hash0 + hash1 + hash40);
	}

	@Test
	public void testEqualsObject() {
		assertTrue(germanNumbersCopy.equals(germanNumbers));
		germanNumbersCopy.put(2, "zwei");
		assertFalse(germanNumbersCopy.equals(germanNumbers));
		germanNumbers.put(2, "zwei");
		assertTrue(germanNumbersCopy.equals(germanNumbers));
		
		BucketHash<Integer, String>germanNumbers2 = new BucketHash<Integer, String>();
		germanNumbers2.put(0, "null");
		germanNumbers2.put(1, "eins");
		germanNumbers2.put(2, "zwei");
		germanNumbers2.put(41, "einundvierzig");
		assertFalse(germanNumbersCopy.equals(germanNumbers2));
		germanNumbers2.remove(41);
		germanNumbers2.put(40, "vierzig");
		assertTrue(germanNumbersCopy.equals(germanNumbers2));
	}

	@Test
	public void testGetBucket() {
		HashSet<Integer> forty = new HashSet<Integer>();
		forty.add(40);
		b = germanNumbers.getBucket(40);
		assertTrue(b.size() == 1);
		assertEquals(b.keySet(), forty);
		b = germanNumbers.getBucket(42);
		assertTrue(b.size() == 0);
		assertNotEquals(b.keySet(), forty);
	}

	@Test
	public void testClear() {
		assertFalse(germanNumbers.isEmpty());
		assertEquals(germanNumbers.keySet().size(), 3);
		germanNumbers.clear();
		assertEquals(germanNumbers.keySet().size(), 0);
		assertTrue(germanNumbers.isEmpty());
	}

	@Test
	public void testContainsKey() {
		assertTrue(germanNumbers.containsKey(0));
		assertTrue(germanNumbers.containsKey(1));
		assertTrue(germanNumbers.containsKey(40));
		assertFalse(germanNumbers.containsKey(168));
		germanNumbers.put(168, "hundertachtundsechzig");
		assertEquals(germanNumbers.size(), 4);
		assertTrue(germanNumbers.containsKey(168));
	}

	@Test
	public void testContainsValue() {
		assertTrue(germanNumbers.containsValue("null"));
		assertTrue(germanNumbers.containsValue("eins"));
		assertTrue(germanNumbers.containsValue("vierzig"));
		assertFalse(germanNumbers.containsValue("hundertachtundsechzig"));
		germanNumbers.put(168, "hundertachtundsechzig");
		assertEquals(germanNumbers.size(), 4);
		assertTrue(germanNumbers.containsValue("hundertachtundsechzig"));
	}

	@Test (expected = UnsupportedOperationException.class)
	public void testEntrySet() {
		germanNumbers.entrySet();
	}

	@Test
	public void testGet() {
		assertEquals("null", germanNumbers.get(0));
		assertNotEquals("zero", germanNumbers.get(0));
		assertEquals("eins", germanNumbers.get(1));
		assertNotEquals("one", germanNumbers.get(1));
		assertEquals("vierzig", germanNumbers.get(40));
		assertNotEquals("forty", germanNumbers.get(40));
		assertNull(germanNumbers.get(50));
	}

	@Test
	public void testIsEmpty() {
		assertFalse(germanNumbers.isEmpty());
		assertFalse(germanNumbersCopy.isEmpty());
		germanNumbers.clear();
		assertTrue(germanNumbers.isEmpty());
		assertFalse(germanNumbersCopy.isEmpty());
		germanNumbersCopy.clear();
		assertTrue(germanNumbersCopy.isEmpty());
	}

	@Test
	public void testKeySet() {
		HashSet<Integer> keys = (HashSet<Integer>)germanNumbers.keySet();
		assertEquals(keys.size(), 3);
		assertTrue(keys.contains(0));
		assertTrue(keys.contains(1));
		assertTrue(keys.contains(40));
		assertFalse(keys.contains(50));
		germanNumbers.put(50, "fuenfzig");
		keys = (HashSet<Integer>)germanNumbers.keySet();
		assertEquals(keys.size(), 4);
		assertTrue(keys.contains(50));
	}

	@Test
	public void testPut() {
		assertFalse(germanNumbers.containsKey(50));
		assertFalse(germanNumbers.containsValue("fuenfzig"));		
		germanNumbers.put(50, "fuenfzig");
		assertTrue(germanNumbers.containsKey(50));
		assertTrue(germanNumbers.containsValue("fuenfzig"));
		germanNumbers.remove(50);
		assertFalse(germanNumbers.containsKey(50));
		assertFalse(germanNumbers.containsValue("fuenfzig"));
		assertEquals(germanNumbers.size(), 3);
		assertEquals(germanNumbers.put(0, "zip"), "null");
		assertEquals(germanNumbers.size(), 3);
		assertEquals(germanNumbers.get(0), "zip");
	}

	@Test
	public void testPutAll() {	
		BucketHash<Integer, String> moreGermanNumbers = new BucketHash<Integer, String>();
		moreGermanNumbers.put(80, "achtzig");
		moreGermanNumbers.put(90, "neunzig");
		assertEquals(germanNumbers.size(), 3);
		assertFalse(germanNumbers.containsKey(80));
		assertFalse(germanNumbers.containsValue("achtzig"));
		assertFalse(germanNumbers.containsKey(90));
		assertFalse(germanNumbers.containsValue("neunzig"));
		germanNumbers.putAll(moreGermanNumbers);
		assertEquals(germanNumbers.size(), 5);
		assertTrue(germanNumbers.containsKey(80));
		assertTrue(germanNumbers.containsValue("achtzig"));
		assertTrue(germanNumbers.containsKey(90));
		assertTrue(germanNumbers.containsValue("neunzig"));
	}

	@Test
	public void testRemove() {
		germanNumbers.put(50, "fuenfzig");
		assertTrue(germanNumbers.containsKey(50));
		assertTrue(germanNumbers.containsValue("fuenfzig"));
		germanNumbers.remove(50);
		assertFalse(germanNumbers.containsKey(50));
		assertFalse(germanNumbers.containsValue("fuenfzig"));
	}

	@Test
	public void testSize() {
		assertEquals(germanNumbers.size(), 3);
		germanNumbers.put(50, "fuenfzig");
		assertEquals(germanNumbers.size(), 4);
		germanNumbers.clear();
		assertEquals(germanNumbers.size(), 0);
	}

	@Test
	public void testValues() {
		ArrayList<String> valueList = (ArrayList<String>)germanNumbers.values();
		assertEquals(valueList.size(), 3);
		assertEquals(valueList.get(0), "null");
		assertEquals(valueList.get(1), "eins");
		assertEquals(valueList.get(2), "vierzig");	
	}
	
	@Test
	public void testBucket(){
		BucketHash<Integer, String> germanNumbers3 = new BucketHash<Integer, String>();
		b = germanNumbers3.getBucket(0);
    	assertNull(b.header);
    }
    
	@Test
    public void testBucketSize() {
		BucketHash<Integer, String> germanNumbers3 = new BucketHash<Integer, String>();
		b = germanNumbers3.getBucket(0);
		assertEquals(b.size(), 0);
		b = germanNumbers.getBucket(0);
		assertEquals(b.size(), 1);
    }
    
	@Test
    public void testBucketGet() {
    	b = germanNumbers.getBucket(0);
    	assertEquals(b.get(0), "null");
    	assertNull(b.get(100)); 	
    }
    
	@Test
    public void testBucketPut() {
    	b = germanNumbers.getBucket(0);
    	assertEquals(b.size(), 1);
    	b.put((Integer)7, "sieben");
    	assertEquals(b.size(), 2);
    	assertEquals(b.get(7), "sieben");    	
    }
    
	@Test
    public void testBucketRemove() {
    	b = germanNumbers.getBucket(0);
    	assertEquals(b.size(), 1);
    	b.remove(0);
    	assertEquals(b.size(), 0);
    }
    
	@Test
    public void testBucketKeySet() {
    	b = germanNumbers.getBucket(0);
    	assertEquals(b.keySet().size(), 1);
    	b.put(9, "neun");
    	assertEquals(b.keySet().size(), 2);
    	assertTrue(b.keySet().contains(0));
    	assertTrue(b.keySet().contains(9));
    	b.remove(0);
    	assertEquals(b.keySet().size(), 1);
    	b.remove(9);
    	assertEquals(b.keySet().size(), 0);
    }
    
	@Test
    public void testBucketValues() {
    	b = germanNumbers.getBucket(0);
    	assertEquals(b.values().size(), 1);
    	b.put(9, "neun");
    	assertEquals(b.values().size(), 2);
    	assertTrue(b.values().contains("null"));
    	assertTrue(b.values().contains("neun"));
    	b.remove(0);
    	assertEquals(b.values().size(), 1);
    	b.remove(9);
    	assertEquals(b.values().size(), 0);
    }
    
	@Test
    public void testBucketToString() {
    	b = germanNumbers.getBucket(0);
    	assertEquals(b.toString(), "k: 0 v: null\n");
    	b.put(9, "neun");
    	assertEquals(b.toString(), "k: 9 v: neun\nk: 0 v: null\n");
    }

}
