package edu.sjsu.cmpe.cache.repository;

import edu.sjsu.cmpe.cache.domain.Entry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by arjunshukla on 5/16/15.
 */
public class ChronicleMapCache implements CacheInterface {
    Map<Long, Entry> chronicleHashMap;



    public ChronicleMapCache(Map<Long, Entry> entry) throws IOException {
//        ChronicleMapBuilder<Long, Entry> builder=ChronicleMapBuilder.of(Long.class, Entry.class)
//                .constantValueSizeBySample(new Entry()).actualChunkSize(2000);
        chronicleHashMap=entry;
    }

    private Map<Long,Entry> createChronicleMap(Map<Long,Entry> entry ) throws IOException {
        File file = new File("logs/data.dat");
        ChronicleMapBuilder<Long,Entry> builder = ChronicleMapBuilder.of(Long.class, Entry.class);
        return builder.createPersistedTo(file);
    }

    @Override
    public Entry save(Entry newEntry) {
        checkNotNull(newEntry, "newEntry instance must not be null");
        chronicleHashMap.putIfAbsent(newEntry.getKey(), newEntry);

        return newEntry;
    }

    @Override
    public Entry get(Long key) {
        checkArgument(key > 0,
                "Key was %s but expected greater than zero value", key);
        return chronicleHashMap.get(key);
    }

    @Override
    public List<Entry> getAll() {
        return new ArrayList<Entry>(chronicleHashMap.values());
    }
}
