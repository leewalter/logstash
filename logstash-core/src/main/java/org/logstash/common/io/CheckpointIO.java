package org.logstash.common.io;

import org.logstash.ackedqueue.Checkpoint;
import java.io.IOException;

public interface CheckpointIO {

    // @return Checkpoint the written checkpoint object
    Checkpoint writeTail(int pageNum, int firstUnackedPageNum, long firstUnackedSeqNum, long minSeqNum, int elementCount) throws IOException;

    Checkpoint writeHead(Checkpoint checkpoint) throws IOException;

    Checkpoint readHead() throws IOException;

    Checkpoint readTail(int pageNum) throws IOException;

    void purgeHead() throws IOException;

    void purgeTail(int pageNum) throws IOException;
}
