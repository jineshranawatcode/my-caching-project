package com.cache;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.PriorityQueue;
import java.util.Comparator;


public class DocumentCache {
    // Thread-safe map to store documents
    private final ConcurrentHashMap<String, Document> cache;
    // Queue to manage document eviction based on expiration and frequency
    private final PriorityQueue<Document> evictionQueue;
    private final int maxCacheSize;

    public DocumentCache(int maxCacheSize) {
        this.cache = new ConcurrentHashMap<>();
        // Custom comparator for eviction priority
        this.evictionQueue = new PriorityQueue<>(
            Comparator.comparingLong(Document::getExpirationTime)
                      .thenComparingInt(Document::getFrequency)
        );
        this.maxCacheSize = maxCacheSize;
    }

    // Document class representing cached items
    public static class Document implements Serializable {
        private final String documentId;
        private final String content;
        private final long ttl; // Time-To-Live in milliseconds
        private long expirationTime;
        private int frequency;

        public Document(String documentId, String content, long ttl) {
            this.documentId = documentId;
            this.content = content;
            this.ttl = ttl;
            this.expirationTime = System.currentTimeMillis() + ttl;
            this.frequency = 0;
        }

        public long getExpirationTime() { return expirationTime; }
        public int getFrequency() { return frequency; }
        
        // Update document access stats
        public void updateAccess() {
            this.expirationTime = System.currentTimeMillis() + ttl;
            this.frequency++;
        }
    }

    // Add a new document to the cache
    public void addDocument(Document document) {
        cache.put(document.documentId, document);
        evictionQueue.offer(document);
        evictIfNecessary();
    }

    // Retrieve a document from the cache
    public Document getDocument(String documentId) {
        Document document = cache.get(documentId);
        if (document != null && !isExpired(document)) {
            // Update document stats and reposition in eviction queue
            evictionQueue.remove(document);
            document.updateAccess();
            evictionQueue.offer(document);
            return document;
        } else {
            // Remove expired or non-existent document
            cache.remove(documentId);
            evictionQueue.remove(document);
            return null;
        }
    }

    // Check if a document has expired
    private boolean isExpired(Document document) {
        return System.currentTimeMillis() > document.getExpirationTime();
    }

    // Evict documents if cache is full or contains expired items
    private void evictIfNecessary() {
        while (!evictionQueue.isEmpty() && 
               (isExpired(evictionQueue.peek()) || cache.size() > maxCacheSize)) {
            Document toEvict = evictionQueue.poll();
            cache.remove(toEvict.documentId);
        }
    }

    // Manually trigger cleanup of expired documents
    public void removeExpiredDocuments() {
        long currentTime = System.currentTimeMillis();
        evictionQueue.removeIf(doc -> {
            if (currentTime > doc.getExpirationTime()) {
                cache.remove(doc.documentId);
                return true;
            }
            return false;
        });
    }
}