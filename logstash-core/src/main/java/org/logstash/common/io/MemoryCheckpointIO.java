package org.logstash.common.io;

import org.logstash.ackedqueue.Checkpoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MemoryCheckpointIO implements CheckpointIO {

    private final String HEAD_CHECKPOINT = "checkpoint.head";
    private final String TAIL_CHECKPOINT = "checkpoint.";

    private static final Map<String, Checkpoint> sources = new HashMap<>();

    private final String dirPath;

    public static void clearSources() {
        sources.clear();
    }

    public MemoryCheckpointIO(String dirPath) {
        this.dirPath = dirPath;
    }

    @Override
    public Checkpoint writeTail(int pageNum, int firstUnackedPageNum, long firstUnackedSeqNum, long minSeqNum, int elementCount) throws IOException {
        Checkpoint checkpoint = new Checkpoint(pageNum, firstUnackedPageNum, firstUnackedSeqNum, minSeqNum, elementCount);
        this.sources.put(TAIL_CHECKPOINT + pageNum, checkpoint);
        return checkpoint;
    }

    @Override
    public Checkpoint writeHead(Checkpoint checkpoint) throws IOException {
        return this.sources.put(HEAD_CHECKPOINT, checkpoint);
    }

    @Override
    public Checkpoint readHead() throws IOException {
        return this.sources.get(HEAD_CHECKPOINT);
    }

    @Override
    public Checkpoint readTail(int pageNum) throws IOException {
        return this.sources.get(TAIL_CHECKPOINT + pageNum);
    }

    @Override
    public void purgeHead() {
        this.sources.remove(HEAD_CHECKPOINT);
    }

    @Override
    public void purgeTail(int pageNum) {
        this.sources.remove(TAIL_CHECKPOINT + pageNum);
    }
}
